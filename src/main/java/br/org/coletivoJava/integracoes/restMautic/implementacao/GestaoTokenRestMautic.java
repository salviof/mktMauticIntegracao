package br.org.coletivoJava.integracoes.restMautic.implementacao;

import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreDataHora;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.FabTipoConexaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.RespostaWebServiceSimples;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.oauth.InfoTokenOauth2;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.gestaoToken.GestaoTokenOath2;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.FabTipoAgenteClienteRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.UtilSBApiRestClient;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfUsuario;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GestaoTokenRestMautic extends GestaoTokenOath2 {

    public GestaoTokenRestMautic(final FabTipoAgenteClienteRest pTipoAgente,
            final ItfUsuario pUsuario) {
        super(FabMauticContatoRest.class, pTipoAgente, pUsuario);

    }

    // ------------------------ 1---------------------------------------//
    @Override
    public String gerarUrlTokenObterChaveAcesso() {

        return urlServidorApiRest + "/oauth/v2/token";
    }

    @Override
    public String extrairNovoCodigoSolicitacao(HttpServletRequest pRespostaServidorAutenticador) {
        return super.extrairNovoCodigoSolicitacao(pRespostaServidorAutenticador);

    }

    @Override
    public String gerarUrlRetornoSucessoGeracaoTokenDeAcesso() {
        return "https://casanovadigital.com.br";

    }

    @Override
    public String gerarUrlTokenObterCodigoSolicitacao() {
        return urlServidorApiRest
                + "/oauth/v2/authorize"
                + "?response_type=code&client_id=" + chavePublica
                + "&redirect_uri=" + urlRetornoReceberCodigoSolicitacao;

    }

    @Override
    public String gerarUrlRetornoReceberCodigoSolicitacao() {
        return super.gerarUrlRetornoReceberCodigoSolicitacao();

    }

    @Override
    public String gerarNovoToken() {
        if (codigoSolicitacao == null) {
            System.out.println("Impossível gerar token com chave de solicitação nula");
            return null;
        }
        System.out.println("Gerando token com solicitação" + codigoSolicitacao);

        try {

            gerarUrlRetornoSucessoGeracaoTokenDeAcesso();
            String texto = "client_id=" + chavePublica
                    + "&client_secret=" + chavePrivada
                    + "&grant_type=authorization_code"
                    + "&code=" + codigoSolicitacao
                    + "&redirect_uri=" + urlRetornoReceberCodigoSolicitacao;

            RespostaWebServiceSimples resp = UtilSBApiRestClient.getRespostaRest(urlSolictacaoToken, FabTipoConexaoRest.POST, true, new HashMap(), texto);

            if (resp.isSucesso()) {
                JSONObject respostaJson = resp.getRespostaComoObjetoJson();
                String tokenGerado = respostaJson.get("access_token").toString();

                armazenarRespostaToken(respostaJson.toJSONString());
                loadTokenArmazenado();
                return tokenGerado;
            }

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro " + t.getMessage(), t);
        }
        return null;

    }

    @Override
    public String extrairToken(JSONObject pJson) {
        return (String) pJson.get("access_token");
    }

    // --------------------------------------3----------------------------------------//
    @Override
    public String loadTokenArmazenado() {
        try {
            JSONObject infoTokentArmazenado = super.loadTokenArmazenadoComoJsonObject();
            if (infoTokentArmazenado == null) {
                return null;
            }
            String tk = extrairToken(infoTokentArmazenado);
            InfoTokenOauth2 tokenGerado = new InfoTokenOauth2(tk);
            tokenGerado.setTokenRefresh((String) infoTokentArmazenado.get("refresh_token"));
            String expiraStr = String.valueOf(infoTokentArmazenado.get("dataHoraExpirarToken"));
            tokenGerado.setDataHoraExpirarToken(new Date(Long.valueOf(expiraStr)));
            tokenDeAcesso = tokenGerado;
            return tokenGerado.getTokenValido();
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "ouve um erro tratando o token armazenado." + t.getMessage(), t);
            return null;
        }
    }

    @Override
    public boolean armazenarRespostaToken(String pJson) {
        JSONParser parser = new JSONParser();
        JSONObject respostaJson;
        try {
            respostaJson = (JSONObject) parser.parse(pJson);
            Date dataHora = UtilSBCoreDataHora.incrementaSegundos(new Date(), Integer.parseInt(respostaJson.get("expires_in").toString()));
            respostaJson.put("dataHoraExpirarToken", String.valueOf(dataHora.getTime()));

            return super.armazenarRespostaToken(respostaJson.toJSONString()); //chamada super do metodo (implementação classe pai)
        } catch (ParseException ex) {
            Logger.getLogger(GestaoTokenRestMautic.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public boolean validarToken() {
        return tokenDeAcesso.getTokenValido() != null;
    }

}
