/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.restMautic.implementacao;

import com.super_bits.Super_Bits.mktMauticIntegracao.configAppp.ConfiguradorCoremktMauticIntegracao;
import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.RespostaWebServiceSimples;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.token.ItfTokenGestao;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sfurbino
 */
public class IntegracaoRestMauticContatoCtrSalvarNovoContatoTest {

    public IntegracaoRestMauticContatoCtrSalvarNovoContatoTest() {
    }

    @Test
    public void testSomeMethod() {
        SBCore.configurar(new ConfiguradorCoremktMauticIntegracao(), SBCore.ESTADO_APP.DESENVOLVIMENTO);

        RespostaWebServiceSimples resp = FabMauticContatoRest.CONTATO_CTR_SALVAR_NOVO_CONTATO.getAcao(SBCore.getUsuarioLogado(), "EmpresaTeste", "salviof@gmail.com", "www.google.com.br", "3132240677", "apenas um teste").getResposta();
        Assert.assertTrue("Falha  cadastrando contato via rest", resp.isSucesso());

    }

}
