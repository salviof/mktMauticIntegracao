/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

import com.super_bits.Super_Bits.mktMauticIntegracao.configAppp.ConfiguradorCoremktMauticIntegracao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import org.junit.Test;
import testes.testesSupers.TestesApiRest;

/**
 *
 * @author sfurbino
 */
public class FabMauticContatoRestTest extends TestesApiRest {

    public FabMauticContatoRestTest() {
    }

    /**
     * Test of values method, of class FabMauticContatoRest.
     */
    @Test
    public void testValues() {
        try {
            SBCore.configurar(new ConfiguradorCoremktMauticIntegracao(), SBCore.ESTADO_APP.DESENVOLVIMENTO);

            gerarCodigos(FabMauticContatoRest.class);
        } catch (Throwable t) {
            System.out.println("Deu merda");
        }

    }

}
