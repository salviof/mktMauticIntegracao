/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.testes;

import com.super_bits.Super_Bits.mktMauticIntegracao.configAppp.ConfiguradorCoremktMauticIntegracao;
import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.token.ItfTokenGestao;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.gestaoToken.MapaTokensGerenciados;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author sfurbino
 * @since 17/12/2019
 * @version 1.0
 */
public class TesteUnitarioOauth {

    @Test
    public void testeMauticOauth() {
        SBCore.configurar(new ConfiguradorCoremktMauticIntegracao(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        ItfTokenGestao oauth = FabMauticContatoRest.CONTATO_CTR_SALVAR_EDITAR_CONTATO.getGestaoToken(SBCore.getUsuarioLogado());
        String url = oauth.getComoGestaoOauth().getUrlObterCodigoSolicitacao();
        String urlRetorno = oauth.getComoGestaoOauth().getUrlRetornoReceberCodigoSolicitacao();

        try {
            Runtime.getRuntime().exec(new String[]{"chromium-browser", url});
        } catch (IOException ex) {
            Logger.getLogger(TesteUnitarioOauth.class.getName()).log(Level.SEVERE, null, ex);
        }

        ServidorOauthRecepcaoSpark servidor = new ServidorOauthRecepcaoSpark("8080");
        servidor.start();

        int segundos = 30;
        while (segundos >= 0) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TesteUnitarioOauth.class.getName()).log(Level.SEVERE, null, ex);
            }
            segundos--;
        }

    }

}
