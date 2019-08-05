/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.configAppp;

import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabConfigModuloMautic;
import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.ConfigGeral.arquivosConfiguracao.ConfigModuloBean;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreJson;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringsExtrator;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreValidacao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.conexaoWebServiceClient.ConexaoClienteWebService;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.InfoTokenOauth2;

import java.util.Scanner;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.junit.Test;
import testesFW.TesteJunit;

/**
 *
 * É recomendável que seu projeto tenha uma classse basica abstrata de testes,
 * onde os testes genericos posssam ser criados, e o setup do teste possa ser
 * universalizado, utilize este exemplo para aprimorar a classe de testes do
 * projeto
 *
 * Estas informações podem te ajudar nesta tarefa:
 *
 * O Sistema oferece 3 classes abstratas nativas para testes com métodos para
 * axiliar nesta tarefa.
 *
 * TesteJunit,
 *
 * que obriga criar um método para configurar o ambiente,(SBCore.configurar)
 * possui o metodo lancar exececao, para exibição de relatório de erro mantendo
 * a compatibilidade com o Junit, e importa todos os Asserts do JUNIT
 *
 * TesteAcoes,
 *
 * para testar ações do Sistema (Importante certificar que as ações estejam
 * corretamente configuradas antes iniciar um projeto)
 *
 *
 * TesteJunitPercistencia
 *
 * possui um entityManager principal do projeto no padrão singleton (Singleton
 * no sentido literal signfica coisa única), e é um padrão de desenvolvimento
 * que checa se a coisa é nula e se não for instancia, no modo estático.
 *
 *
 * Este exemplo foi criado pensando no seu aprendizado, divirta-se!
 *
 * @author sfurbino
 */
public class TesteProjetoExemplo extends TesteJunit {

    @Override
    protected void configAmbienteDesevolvimento() {
        //Nas classes de ambiente padrão do sistema  modo desenvolvimento significa execuções via JUNIT, HOmologação Jetty na sua maquina, e Produção na Web
        SBCore.configurar(new ConfiguradorCoremktMauticIntegracao(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        //Configuração do arquivo de persistencia

    }

    public static void main(String... pParametros) {
        SBCore.configurar(new ConfiguradorCoremktMauticIntegracao(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        ConfigModuloBean configMod = new ConfigModuloBean(SBCore.getConfigModulo(FabConfigModuloMautic.class));
        System.out.println(configMod.getPropriedadeCampo(configMod.getCampos().get(0)).getValor());
        try {

            JSONParser parser = new JSONParser();

            ConexaoClienteWebService restEmpresasComFiltro = FabMauticContatoRest.LISTAREMPRESA_COM_FILTRO.getConexao("salviof@gmail.com");
            JSONObject jsonEmpresas = restEmpresasComFiltro.getRespostaComoObjetoJson();
            ConexaoClienteWebService contatos = FabMauticContatoRest.CONTATO_LISTAR_COM_FILTRO.getConexao("emailnaoinformado@drDecio.com.br");
            System.out.println(contatos.getRespostaTexto());
            JSONObject jsonContatos = contatos.getRespostaComoObjetoJson();
            String idContato = UtilSBCoreJson.getValorApartirDoCaminho("contacts[0].id", jsonContatos);

            String idEmpresa = UtilSBCoreJson.getValorApartirDoCaminho("companies[0].id", jsonEmpresas);

            String contatoPrimeiroNome = UtilSBCoreJson.getValorApartirDoCaminho("contacts[0].fields[0].firstname", jsonContatos);
            String contatoEmail = UtilSBCoreJson.getValorApartirDoCaminho("contacts[0].fields[0].email", jsonContatos);
            String contatolastname = UtilSBCoreJson.getValorApartirDoCaminho("contacts[0].fields[0].lastname", jsonContatos);
            String contatomobile = UtilSBCoreJson.getValorApartirDoCaminho("contacts[0].fields[0].mobile", jsonContatos);
            if (contatomobile == null) {
                contatomobile = "";
            }
            ConexaoClienteWebService contatoAdicionado = FabMauticContatoRest.EMPRESA_CTR_SALVAR_ADICIONAR_CONTATO.getConexao(idEmpresa, idContato);
            System.out.println(contatoAdicionado.getRespostaComoObjetoJson());

            ConexaoClienteWebService conexaoEditar = FabMauticContatoRest.CONTATO_CTR_SALVAR_EDITAR_CONTATO.getConexao(idContato, contatoEmail, contatoPrimeiroNome,
                    contatolastname, contatomobile);

            System.out.println(conexaoEditar.getRespostaTexto());
            JSONObject jsonContatoAtualizado = conexaoEditar.getRespostaComoObjetoJson();
            String idContatoAtualizado = UtilSBCoreJson.getValorApartirDoCaminho("contact.id", jsonContatoAtualizado);
            String valorTXT = restEmpresasComFiltro.getRespostaTexto();
            JSONObject empresas = (JSONObject) ((JSONObject) parser.parse(valorTXT)).get("companies");
            for (Object codEmpresa : empresas.keySet()) {
                String codigo = codEmpresa.toString();
                System.out.println(codigo);
                ConexaoClienteWebService registro = FabMauticContatoRest.EMPRESA_CTR_SALVAR_EDITAR_EMPRESA.getConexao(codigo, "Casa nova digital", "contato@casanovadigital.com.br", "casanovadigital.com.br", "32240677", "Teste");
                String reg = registro.getRespostaTexto();
                JSONObject jsonEmpresaAtualizada = registro.getRespostaComoObjetoJson();
                String idEmpresaAtualizado = UtilSBCoreJson.getValorApartirDoCaminho("company.id", jsonEmpresaAtualizada);
                System.out.println("REtorno edição " + reg);
            }
            System.out.println(valorTXT);

            ConexaoClienteWebService registro = FabMauticContatoRest.EMPRESA_CTR_SALVAR_NOVA_EMPRESA.getConexao("ClubeMM", "kleber@clubemm.com.br", "www.clublemm.com.br", "32240677", "");
            String texto = registro.getRespostaTexto();
            System.out.println(texto);

            //  FabMauticContatoRest.CONTATO_LISTAR.autenticarSistema();
            //System.out.println(configuracao.getPropriedade(FabConfigModuloMautic.CHAVE_API_PRIVADA));
            //String textp = FabMauticContatoRest.CONTATO_LISTAR.getRespostaEmTexto();
        } catch (Throwable t) {
            //  lancarErroJUnit(t);
            SBCore.RelatarErro(FabErro.LANCAR_EXCECÃO, t.getMessage(), t);
        }
    }

    @Test
    public void teste() {

    }

}
