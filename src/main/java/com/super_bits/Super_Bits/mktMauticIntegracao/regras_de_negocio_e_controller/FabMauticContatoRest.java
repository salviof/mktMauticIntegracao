/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.ItfFabricaIntegracaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.FabTipoConexaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.InfoConsumoRestService;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.ItfApiServicoTokenCliente;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.servicoRegistrado.FabTipoAutenticacaoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.servicoRegistrado.InfoConfigRestClientIntegracao;

import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.importacao.FabTipoArquivoImportacao;

/**
 *
 * @author SalvioF
 */
@InfoConfigRestClientIntegracao(configuracao = FabConfigModuloMautic.class,
        enderecosDocumentacao = "https://developer.mautic.org/#rest-api",
        nomeIntegracao = "mautic",
        tipoAutenticacao = FabTipoAutenticacaoRest.OAUTHV2
)
public enum FabMauticContatoRest implements ItfFabricaIntegracaoRest {

    @InfoConsumoRestService(getPachServico = "/api/contacts?search={1}", tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            adicionarAutenticacaoBearer = true)
    CONTATO_LISTAR_COM_FILTRO,
    /**
     * Encontra uma companha através de um campo, exemplo pelo site.
     */
    @InfoConsumoRestService(getPachServico = "/api/companies?search={1}", tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            parametrosGet = {"search"}, adicionarAutenticacaoBearer = true)
    LISTAREMPRESA_COM_FILTRO,
    CONTATO_CTR_SALVAR_NOVO_CONTATO,
    @InfoConsumoRestService(getPachServico = "/api/contacts/{1}/edit",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            tipoConexao = FabTipoConexaoRest.PUT,
            adicionarAutenticacaoBearer = true,
            parametrosPost = {"companyname", "companyemail", "companywebsite", "companyphone", "companydescription"}
    )
    CONTATO_CTR_SALVAR_EDITAR_CONTATO,
    /**
     *
     */
    @InfoConsumoRestService(getPachServico = "/api/companies/new",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            tipoConexao = FabTipoConexaoRest.POST,
            adicionarAutenticacaoBearer = true,
            parametrosPost = {"companyname", "companyemail", "companywebsite", "companyphone", "companydescription"}
    )
    EMPRESA_CTR_SALVAR_NOVA_EMPRESA,
    @InfoConsumoRestService(getPachServico = "/api/companies/{1}/edit",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            tipoConexao = FabTipoConexaoRest.PUT,
            adicionarAutenticacaoBearer = true,
            parametrosGet = {"id"},
            parametrosPost = {"companyname", "companyemail", "companywebsite", "companyphone", "companydescription"}
    )
    EMPRESA_CTR_SALVAR_EDITAR_EMPRESA,
    @InfoConsumoRestService(getPachServico = "/api/companies/{1}/contact/{2}/add",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            adicionarAutenticacaoBearer = true,
            tipoConexao = FabTipoConexaoRest.POST
    )
    EMPRESA_CTR_SALVAR_ADICIONAR_CONTATO;

    @Override
    public ItfApiServicoTokenCliente getApiTokenAcesso() {
        throw new UnsupportedOperationException("O METODO AINDA N\u00c3O FOI IMPLEMENTADO.");
    }

}
