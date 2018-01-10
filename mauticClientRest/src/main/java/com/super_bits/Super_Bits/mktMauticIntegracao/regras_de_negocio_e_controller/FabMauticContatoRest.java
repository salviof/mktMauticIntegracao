/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

import com.super_bits.Super_Bits.mktMauticIntegracao.model.ItfContato;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.conexaoWebServiceClient.ConexaoClienteWebService;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.conexaoWebServiceClient.FabTipoConexaoRest;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.conexaoWebServiceClient.InfoConsumoRestService;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.MapaInfoOauthEmAndamento;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.Oath2Conexao;
import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.importacao.FabTipoArquivoImportacao;

/**
 *
 * @author SalvioF
 */
public enum FabMauticContatoRest implements ItfFabRestMauticGenerico<ItfContato> {

    @InfoConsumoRestService(getCaminho = "/api/contacts?access_token={0}", tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON)
    CONTATO_LISTAR,
    /**
     * Encontra uma companha através de um campo, exemplo pelo site.
     */
    @InfoConsumoRestService(getCaminho = "/api/companies?access_token={0}&search={1}", tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            parametrosGet = {"search"})
    LISTAREMPRESA_COM_FILTRO,
    CONTATO_CTR_SALVAR_NOVO_CONTATO,
    /**
     *
     */
    @InfoConsumoRestService(getCaminho = "/api/companies/new?access_token={0}",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            parametrosPost = {""}
    )
    EMPRESA_CTR_SALVAR_NOVA_EMPRESA,
    @InfoConsumoRestService(getCaminho = "/api/companies/{1}/edit?access_token={0}",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            tipoConexao = FabTipoConexaoRest.PUT,
            parametrosGet = {"id"},
            parametrosPost = {"companyname", "companyemail", "companywebsite", "companyphone", "companydescription"}
    )
    EMPRESA_CTR_SALVAR_EDITAR_EMPRESA;

    @Override
    public String getCorpoRequisicao(String... parametros) {
        String conteudo = "";
        switch (this) {
            case EMPRESA_CTR_SALVAR_EDITAR_EMPRESA:

                conteudo = "{ \"companyname\":\"{1}\",\"companyemail\":\"{2}\", \"companywebsite\": \"{3}\", \"companyphone\": \"{4}\",\"companydescription\":\"{5}\"}";
                break;
            case EMPRESA_CTR_SALVAR_NOVA_EMPRESA:
                conteudo = "companyname={0}&companyemail={1}&companywebsite={2}&companyphone={3}&companydescription={4}";

                break;

            default:
                return null;

        }
        int i = 0;
        for (String parametro : parametros) {
            conteudo = conteudo.replace("{" + i + "}", parametro);
            i++;
        }
        return conteudo;

    }

    @Override
    public String getUrlRetornoFalha() {
        return null;
    }

    @Override
    public ConexaoClienteWebService getConexao(String... pParametros) {
        Oath2Conexao conexao = getOauthConexao();
        if (conexao.isPossuiTokenValido()) {
            return ItfFabRestMauticGenerico.super.getConexao(pParametros); //To change body of generated methods, choose Tools | Templates.
        } else {
            conexao.gerarNovoToken();
            return null;
        }

    }

    @Override
    public String getCaminhoServico(String... parametros) {
        Oath2Conexao conexao = getOauthConexao();

        getInformacoesConsumo().getCaminho();

        if (conexao.isPossuiTokenValido()) {
            String token = MapaInfoOauthEmAndamento.getTokenSistema();

            String caminhoServico = getInformacoesConsumo().getCaminho().replace("{0}", token);
            int i = 1;
            for (String parametro : parametros) {

                try {
                    caminhoServico = caminhoServico.replace("{" + i + "}", parametros[i - 1]);
                    i++;
                } catch (Throwable t) {
                    throw new UnsupportedOperationException("Erro configurando parametro" + parametro);
                }

            }
            return caminhoServico;

        }
        return null;
    }

}
