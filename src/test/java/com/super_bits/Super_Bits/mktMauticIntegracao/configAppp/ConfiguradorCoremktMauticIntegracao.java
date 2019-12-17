/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.configAppp;

import com.super_bits.modulosSB.SBCore.ConfigGeral.ConfiguradorCoreDeProjetoJarAbstrato;
import com.super_bits.modulosSB.SBCore.ConfigGeral.ConfiguradorCoreModelSemPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.ItfConfiguracaoCoreCustomizavel;

/**
 *
 * Porque o configurador core do model fica no pacote teste?
 *
 * porque o é um pacote feito para ser usado por outros projetos, em uma
 * situação normal o único momento que estas classes são executadas é em testes
 * do Junit, ou em outros projetos com seus proprios configuradores de core
 *
 * Com o configurador model no pacote de testes você pode viver feliz temdo a
 * certeza que nenhum estagiario irá executar a configuração do model em outros
 * projetos
 *
 * @author salvioF
 */
public class ConfiguradorCoremktMauticIntegracao extends ConfiguradorCoreModelSemPersistencia {

    public ConfiguradorCoremktMauticIntegracao() {
        setIgnorarConfiguracaoPermissoes(true);
        setIgnorarConfiguracaoAcoesDoSistema(true);
    }

    /**
     *
     * O Core do sistema precisa ser configurado para execução da maioria das
     * tarefas, no core classes importante de ambiente de execução são
     * configuradas como:
     *
     * ClasseDeErro CentralDeMensagens CentralVisualizacao CentralEventos
     * ConfigPermissao ControleDeSessao e Fabricas de Ações do sistema
     *
     * Alterando a implementação destas classes o comportamento ao executar um
     * metodo como enviar mensagem ao usuario pode ser alterado, tortando seu
     * código portatil a diversos ambientes, Web, Mobile, Desktop ou IOC
     *
     * As informações cadastrais do sistemas ficam no arquivo SBProjeto.prop na
     * pasta resource
     *
     * @param pConfig Objeto Com informações de configuração do sistema
     */
    @Override
    public void defineFabricasDeACao(ItfConfiguracaoCoreCustomizavel pConfig) {
        setIgnorarConfiguracaoPermissoes(true);
        setIgnorarConfiguracaoAcoesDoSistema(true);
    }

}
