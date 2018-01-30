/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.ItfFabConfigModulo;
import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.tipoModulos.integracaoOauth.FabPropriedadeModuloIntegracaoOauth;
import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.tipoModulos.integracaoOauth.InfoPropriedadeConfigOauth;

/**
 *
 * @author SalvioF
 */
public enum FabConfigModuloMautic implements ItfFabConfigModulo {

    @InfoPropriedadeConfigOauth(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.SITE_SERVIDOR)
    URL_SERVIDOR_MAUTIC,
    @InfoPropriedadeConfigOauth(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.SITE_CLIENTE)
    URL_SERIVIDOR_CLIENTE,
    @InfoPropriedadeConfigOauth(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.CHAVE_PUBLICA)
    CHAVE_API_PUBLICA,
    @InfoPropriedadeConfigOauth(tipoPropriedade = FabPropriedadeModuloIntegracaoOauth.CHAVE_PRIVADA)
    CHAVE_API_PRIVADA;

    @Override
    public String getValorPadrao() {
        return "Configure o arquivo de propriedades na pasta resources do seu projeto";
    }

}
