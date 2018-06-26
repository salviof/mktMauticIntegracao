/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

import com.google.common.net.HttpHeaders;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
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

    @Override
    public default Oath2Conexao getOauthConexao() {
        if (MapaInfoOauthEmAndamento.getAutenticadorSistemaAtual(this) == null) {
            MapaInfoOauthEmAndamento.registrarAutenticador(new Oath2Conexao(FabConfigModuloMautic.class, FabTipoClienteOauth.SISTEMA, this.getClass()), this);
        }
        return MapaInfoOauthEmAndamento.getAutenticadorSistemaAtual(this);
    }

    @Override
    public default String getUrlServidor() {
        return getOauthConexao().getSiteServidor();
    }

    @Override
    public default InfoTokenOauth2 gerarNovoToken(String solicitacao) {
        if (solicitacao == null) {
            System.out.println("Impossível gerar token com chave de solicitação nula");
            return null;
        }
        System.out.println("Gerando token com solicitação" + solicitacao);

        try {
            String respostaStr = "";

            URL url = new URL(getUrlToken());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            String texto = "client_id=" + getOauthConexao().getChavePublica()
                    + "&client_secret=" + getOauthConexao().getChavePrivada() + ""
                    + "&code=" + solicitacao
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

            Date dataHora = UtilSBCoreDataHora.incrementaSegundos(new Date(), Integer.parseInt(respostaJson.get("expires_in").toString()));

            respostaJson.put("dataHoraExpirarToken", String.valueOf(dataHora.getTime()));
            getOauthConexao().getConfiModulo().getRepositorioDeArquivosExternos().putConteudoRecursoExterno(SBCore.getGrupoProjeto(), respostaJson.toJSONString());

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
            JSONObject infoTokentArmazenado = getOauthConexao().getConfiModulo().getRepositorioDeArquivosExternos().getJsonObjeto(SBCore.getGrupoProjeto());
            if (infoTokentArmazenado == null) {
                return null;
            }
            InfoTokenOauth2 token = new InfoTokenOauth2((String) infoTokentArmazenado.get("access_token"));
            token.setTokenRefresh((String) infoTokentArmazenado.get("refresh_token"));
            String expiraStr = String.valueOf(infoTokentArmazenado.get("dataHoraExpirarToken"));
            token.setDataHoraExpirarToken(new Date(Long.valueOf(expiraStr)));

            return token;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "ouve um erro tratando o token armazenado." + t.getMessage(), t);
            return null;
        }

    }

    public default String getUrlToken() {
        return getOauthConexao().getSiteServidor() + "/oauth/v2/token";
    }

    @Override
    public default String getUrlSolicitacaoAutenticacao() {
        try {

            String paginaRetornoCodificada = getUrlRetornoSucesso();
            paginaRetornoCodificada = new URLCodec().encode(paginaRetornoCodificada);
            String url = getOauthConexao().getSiteServidor() + "/oauth/v2/authorize?response_type=code&client_id=" + getOauthConexao().getChavePublica() + "&redirect_uri=" + paginaRetornoCodificada;
            return url;
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public default String getUrlRetornoSucesso() {
        try {
            String url = getOauthConexao().getSiteCliente() + "/solicitacaoAuth2Recept";
            return url;
        } catch (Throwable t) {
            return null;
        }

    }

    @Override
    public default InfoTokenOauth2 getTokenArmazenadoUsuario() {
        return getTokenArmazenadoSistema();
    }

}
