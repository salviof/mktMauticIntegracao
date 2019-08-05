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

    @InfoConsumoRestService(getCaminho = "/api/contacts?search={1}", tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            adicionarAutenticacaoBearer = true)
    CONTATO_LISTAR_COM_FILTRO,
    /**
     * Encontra uma companha através de um campo, exemplo pelo site.
     */
    @InfoConsumoRestService(getCaminho = "/api/companies?search={1}", tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            parametrosGet = {"search"}, adicionarAutenticacaoBearer = true)
    LISTAREMPRESA_COM_FILTRO,
    CONTATO_CTR_SALVAR_NOVO_CONTATO,
    @InfoConsumoRestService(getCaminho = "/api/contacts/{1}/edit",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            tipoConexao = FabTipoConexaoRest.PUT,
            adicionarAutenticacaoBearer = true,
            parametrosPost = {"companyname", "companyemail", "companywebsite", "companyphone", "companydescription"}
    )
    CONTATO_CTR_SALVAR_EDITAR_CONTATO,
    /**
     *
     */
    @InfoConsumoRestService(getCaminho = "/api/companies/new",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            tipoConexao = FabTipoConexaoRest.POST,
            adicionarAutenticacaoBearer = true,
            parametrosPost = {"companyname", "companyemail", "companywebsite", "companyphone", "companydescription"}
    )
    EMPRESA_CTR_SALVAR_NOVA_EMPRESA,
    @InfoConsumoRestService(getCaminho = "/api/companies/{1}/edit",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            tipoConexao = FabTipoConexaoRest.PUT,
            adicionarAutenticacaoBearer = true,
            parametrosGet = {"id"},
            parametrosPost = {"companyname", "companyemail", "companywebsite", "companyphone", "companydescription"}
    )
    EMPRESA_CTR_SALVAR_EDITAR_EMPRESA,
    @InfoConsumoRestService(getCaminho = "/api/companies/{1}/contact/{2}/add",
            tipoInformacaoRecebida = FabTipoArquivoImportacao.JSON,
            adicionarAutenticacaoBearer = true,
            tipoConexao = FabTipoConexaoRest.POST
    )
    EMPRESA_CTR_SALVAR_ADICIONAR_CONTATO;

    @Override
    public String getCorpoRequisicao(String... parametros) {
        String conteudo = "";
        switch (this) {
            case CONTATO_CTR_SALVAR_EDITAR_CONTATO:
                conteudo = "{\"id\":\"{0}\",\"email\":\"{1}\",\"firstname\":\"{2}\",\"lastname\":\"{3}\",\"mobile\":\"{4}\"}";
                break;

            case CONTATO_CTR_SALVAR_NOVO_CONTATO:

                break;

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
            System.out.println("O Sistema não possui um token válido, acesse \n" + getUrlSolicitacaoAutenticacao() + " \n para Obter uma no");
            return null;
        }

    }

    @Override
    public String getCaminhoServico(String... parametros) {
        Oath2Conexao conexao = getOauthConexao();

        getInformacoesConsumo().getCaminho();

        if (conexao.isPossuiTokenValido()) {
            String token = MapaInfoOauthEmAndamento.getAutenticadorSistemaAtual(this).getTokenDeAcesso().getTokenValido();

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
