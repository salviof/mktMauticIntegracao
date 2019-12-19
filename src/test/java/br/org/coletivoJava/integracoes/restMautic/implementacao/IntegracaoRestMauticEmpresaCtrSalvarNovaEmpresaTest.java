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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sfurbino
 */
public class IntegracaoRestMauticEmpresaCtrSalvarNovaEmpresaTest {

    public IntegracaoRestMauticEmpresaCtrSalvarNovaEmpresaTest() {
    }

    /**
     * Test of gerarCorpoRequisicao method, of class
     * IntegracaoRestMauticEmpresaCtrSalvarNovaEmpresa.
     */
    @Test
    public void testGerarCorpoRequisicao() {
        SBCore.configurar(new ConfiguradorCoremktMauticIntegracao(), SBCore.ESTADO_APP.DESENVOLVIMENTO);

        RespostaWebServiceSimples resp = FabMauticContatoRest.EMPRESA_CTR_SALVAR_NOVA_EMPRESA.getAcao(SBCore.getUsuarioLogado(), "EmpresaTeste", "salviof@gmail.com", "www.google.com.br", "3132240677", "apenas um teste").getResposta();
        System.out.println(resp.isSucesso());
        System.out.println(resp.getRespostaTexto());
        System.out.println(resp.getRespostaErro());
    }

}
