/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

import com.google.common.net.HttpHeaders;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.ConfigModulo;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreDataHora;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.ItfFabricaIntegracaoRestOAuth;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.FabTipoClienteOauth;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.InfoTokenOauth2;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.MapaInfoOauthEmAndamento;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.Oath2Conexao;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.FabErro;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.apache.commons.codec.net.URLCodec;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author SalvioF
 */
public interface ItfFabRestMauticGenerico<T> extends ItfFabricaIntegracaoRestOAuth<T> {

    /**
     *
     */
    public static final ConfigModulo CONFIGURACAO_MAUTIC = SBCore.getConfigModulo(FabConfigModuloMautic.class);

    @Override
    public default Oath2Conexao getOauthConexao() {
        return new Oath2Conexao(this, FabTipoClienteOauth.SISTEMA);
    }

    @Override
    public default String getChavePublicaUsuario() {
        return getChavePublicaSistema();
    }

    @Override
    public default String getChavePublicaSistema() {
        String chave = ItfFabricaIntegracaoRestOAuth.super.getChavePublicaSistema();
        if (chave == null) {
            chave = CONFIGURACAO_MAUTIC.getPropriedade(FabConfigModuloMautic.CHAVE_API_PUBLICA);
            MapaInfoOauthEmAndamento.registrarChavePublica(SBCore.getGrupoProjeto(), chave);
            return chave;
        } else {
            return chave;
        }
    }

    @Override
    public default InfoTokenOauth2 gerarNovoToken(String solicitacao) {

        try {
            String respostaStr = "";

            URL url = new URL(getUrlToken());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            String texto = "client_id=" + CONFIGURACAO_MAUTIC.getPropriedade(FabConfigModuloMautic.CHAVE_API_PUBLICA)
                    + "&client_secret=" + CONFIGURACAO_MAUTIC.getPropriedade(FabConfigModuloMautic.CHAVE_API_PRIVADA) + ""
                    + "&code=" + MapaInfoOauthEmAndamento.getSolicitacaoSistema()
                    + "&redirect_uri=" + new URLCodec().encode(getUrlRetornoSucesso()) + "&grant_type=authorization_code";
            conn.setRequestMethod("POST");
            conn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, String.valueOf(texto.length()));
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //System.out.println(texto);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(texto);
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                respostaStr += output;
            }
            JSONParser parser = new JSONParser();
            System.out.println(respostaStr);
            JSONObject respostaJson = (JSONObject) parser.parse(respostaStr);

            MapaInfoOauthEmAndamento.registrarTokenSistema((String) respostaJson.get("access_token"));
            Date dataHora = UtilSBCoreDataHora.incrementaSegundos(new Date(), Integer.parseInt(respostaJson.get("expires_in").toString()));

            respostaJson.put("dataHoraExpirarToken", String.valueOf(dataHora.getTime()));
            CONFIGURACAO_MAUTIC.getRepositorioDeArquivosExternos().putConteudoRecursoExterno(SBCore.getGrupoProjeto(), respostaJson.toJSONString());

            conn.disconnect();
            return getTokenArmazenadoSistema();

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro " + t.getMessage(), t);
        }
        return null;
    }

    @Override
    public default InfoTokenOauth2 getTokenArmazenadoSistema() {
        try {
            JSONObject infoTokentArmazenado = CONFIGURACAO_MAUTIC.getRepositorioDeArquivosExternos().getJsonObjeto(SBCore.getGrupoProjeto());
            if (infoTokentArmazenado == null) {
                return null;
            }
            InfoTokenOauth2 token = new InfoTokenOauth2((String) infoTokentArmazenado.get("access_token"));
            token.setTokenRefresh((String) infoTokentArmazenado.get("refresh_token"));
            String expiraStr = String.valueOf(infoTokentArmazenado.get("dataHoraExpirarToken"));
            token.setDataHoraExpirarToken(new Date(Long.valueOf(expiraStr)));

            MapaInfoOauthEmAndamento.registrarTokenSistema(token);
            return token;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "ouve um erro tratando o token armazenado." + t.getMessage(), t);
            return null;
        }

    }

    public default String getUrlToken() {
        return getUrlServidor() + "/oauth/v2/token";
    }

    @Override
    public default String getUrlSolicitacaoAutenticacao() {
        try {

            String paginaRetornoCodificada = getUrlRetornoSucesso();
            paginaRetornoCodificada = new URLCodec().encode(paginaRetornoCodificada);
            String url = getUrlServidor() + "/oauth/v2/authorize?response_type=code&client_id=" + CONFIGURACAO_MAUTIC.getPropriedade(FabConfigModuloMautic.CHAVE_API_PUBLICA) + "&redirect_uri=" + paginaRetornoCodificada;
            return url;
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public default String getUrlRetornoSucesso() {
        try {
            String url = CONFIGURACAO_MAUTIC.getPropriedade(FabConfigModuloMautic.URL_SERIVIDOR_CLIENTE) + "/solicitacaoAuth2Recept";
            return url;
        } catch (Throwable t) {
            return null;
        }

    }

    @Override
    public default InfoTokenOauth2 getTokenArmazenadoUsuario() {
        return getTokenArmazenadoSistema();
    }

    @Override
    public default String getUrlServidor() {
        return "https://email.casanovadigital.com.br";
    }

}
