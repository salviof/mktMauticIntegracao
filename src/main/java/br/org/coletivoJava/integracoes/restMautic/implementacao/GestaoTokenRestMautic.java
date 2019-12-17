package br.org.coletivoJava.integracoes.restMautic.implementacao;

import br.org.coletivoJava.integracoes.restMautic.api.InfoIntegracaoRestMautic;
import com.google.common.net.HttpHeaders;
import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreClienteRest;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreDataHora;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.FabTipoConexaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.RespostaWebServiceSimples;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.oauth.InfoTokenOauth2;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.gestaoToken.GestaoTokenOath2;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.FabTipoAgenteClienteRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.token.ItfTokenGestao;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.UtilSBApiRestClient;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfUsuario;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.net.URLCodec;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@InfoIntegracaoRestMautic(tipo = FabMauticContatoRest.CONTATO_LISTAR_COM_FILTRO)
public class GestaoTokenRestMautic extends GestaoTokenOath2 {

    public GestaoTokenRestMautic(final FabTipoAgenteClienteRest pTipoAgente,
            final ItfUsuario pUsuario) {
        super(FabMauticContatoRest.class, pTipoAgente, pUsuario);

    }

    // ------------------------ 1---------------------------------------//
    @Override
    public String gerarUrlTokenObterChaveAcesso() {
        return urlServidorApiRest + "/oauth/v2/token/"
                + "/oauth/v2/authorize"
                + "?response_type=code&client_id=" + chavePublica
                + "&redirect_uri=" + urlRetornoSolicitacaoSucesso;

    }

    @Override
    public String gerarNovoCodigoSolicitacao(String presposta) {

        codigoSolicitacao = null;
    }

    @Override
    public String gerarUrlInformadaRetornoSolictacaoSucesso() {
        String url = urlRetornoSolicitacaoSucesso + "/solicitacaoAuth2Recept/code/Sistema/FabMauticContatoRest/";
        return url;
    }

    @Override
    public String gerarNovoToken(String pCodigoSolicitacao) {
        if (codigoSolicitacao == null) {
            System.out.println("Impossível gerar token com chave de solicitação nula");
            return null;
        }
        System.out.println("Gerando token com solicitação" + codigoSolicitacao);

        try {
            String respostaStr = "";

            URL url = new URL(urlSolictacaoToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            String texto = "client_id=" + chavePublica
                    + "&client_secret=" + chavePrivada + ""
                    + "&code=" + pCodigoSolicitacao
                    + "&redirect_uri=" + new URLCodec().encode(urlRetornoSolicitacaoSucesso) + "&grant_type=authorization_code";
            conn.setRequestMethod("POST");
            conn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, String.valueOf(texto.length()));
            conn.setDoOutput(true);
            conn.setDoInput(true);

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
            String tokenGerado = respostaJson.get("access_token").toString();

            armazenarRespostaToken(respostaStr);

            conn.disconnect();
            return tokenGerado;

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro " + t.getMessage(), t);
        }
        return null;

    }

    // --------------------------------------3----------------------------------------//
    @Override
    public String gerarNovoToken() {

        return gerarNovoToken(codigoSolicitacao);

    }

    @Override
    public String loadTokenArmazenado() {
        try {
            JSONObject infoTokentArmazenado = getConfig().getRepositorioDeArquivosExternos().getJsonObjeto(SBCore.getGrupoProjeto());
            if (infoTokentArmazenado == null) {
                return null;
            }
            InfoTokenOauth2 tokenGerado = new InfoTokenOauth2((String) infoTokentArmazenado.get("access_token"));
            tokenGerado.setTokenRefresh((String) infoTokentArmazenado.get("refresh_token"));
            String expiraStr = String.valueOf(infoTokentArmazenado.get("dataHoraExpirarToken"));
            tokenGerado.setDataHoraExpirarToken(new Date(Long.valueOf(expiraStr)));

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
            getConfig().getRepositorioDeArquivosExternos().putConteudoRecursoExterno(SBCore.getGrupoProjeto(), respostaJson.toJSONString());
            return super.armazenarRespostaToken(respostaJson); //chamada super do metodo (implementação classe pai)
        } catch (ParseException ex) {
            Logger.getLogger(GestaoTokenRestMautic.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    @Override
    public boolean validarToken() {
        return false;
    }

}
