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
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.conexaoWebServiceClient.ConexaoClienteWebService;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.FabTipoClienteOauth;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.InfoTokenOauth2;
import com.super_bits.modulosSB.SBCore.modulos.Controller.WS.oauth.Oath2Conexao;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.FabErro;
import com.super_bits.modulosSB.SBCore.testesFW.TesteJunit;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.junit.Test;

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

            ConexaoClienteWebService conexaoTentativa1 = FabMauticContatoRest.CONTATO_LISTAR.getConexao();

            if (conexaoTentativa1 == null) {
                Scanner in = new Scanner(System.in);

                String code = in.nextLine();

                InfoTokenOauth2 conecao = FabMauticContatoRest.CONTATO_LISTAR.gerarNovoToken(code);
                ConexaoClienteWebService conexaoTentativa2 = FabMauticContatoRest.CONTATO_LISTAR.getConexao();

            } else {

                System.out.println(conexaoTentativa1.getRespostaTexto());

                JSONParser parser = new JSONParser();
                Object valor = parser.parse(conexaoTentativa1.getRespostaTexto());
                System.out.println(valor);

                ConexaoClienteWebService registroUnico = FabMauticContatoRest.LISTAREMPRESA_COM_FILTRO.getConexao("contato@casanovadigital.com.br");

                String valorTXT = registroUnico.getRespostaTexto();
                JSONObject empresas = (JSONObject) ((JSONObject) parser.parse(valorTXT)).get("companies");
                for (Object codEmpresa : empresas.keySet()) {
                    String codigo = codEmpresa.toString();
                    System.out.println(codigo);
                    ConexaoClienteWebService registro = FabMauticContatoRest.EMPRESA_CTR_SALVAR_EDITAR_EMPRESA.getConexao(codigo, "Casa nova digital", "contato@casanovadigital.com.br", "casanovadigital.com.br", "32240677", "Teste");
                    String reg = registro.getRespostaTexto();
                    System.out.println("REtorno edição " + reg);
                }
                System.out.println(valorTXT);

                ConexaoClienteWebService registro = FabMauticContatoRest.EMPRESA_CTR_SALVAR_NOVA_EMPRESA.getConexao("ClubeMM", "kleber@clubemm.com.br", "www.clublemm.com.br", "32240677", "");
                String texto = registro.getRespostaTexto();
                System.out.println(texto);
            }

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
