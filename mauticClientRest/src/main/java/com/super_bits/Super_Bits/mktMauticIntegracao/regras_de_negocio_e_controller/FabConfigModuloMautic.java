/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.ItfFabConfigModulo;

/**
 *
 * @author SalvioF
 */
public enum FabConfigModuloMautic implements ItfFabConfigModulo {

    URL_SERVIDOR_MAUTIC,
    URL_SERIVIDOR_CLIENTE,
    CHAVE_API_PUBLICA,
    CHAVE_API_PRIVADA;

    @Override
    public String getValorPadrao() {
        return "Configure o arquivo de propriedades na pasta resources do seu projeto";
    }

}
