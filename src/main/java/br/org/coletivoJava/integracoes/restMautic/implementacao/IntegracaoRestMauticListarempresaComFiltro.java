package br.org.coletivoJava.integracoes.restMautic.implementacao;

import br.org.coletivoJava.integracoes.restMautic.api.InfoIntegracaoRestMautic;
import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.ConsumoWSExecucao;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.AcaoApiIntegracaoComOauthAbstrato;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.FabTipoAgenteClienteRest;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfUsuario;
import java.util.Map;

@InfoIntegracaoRestMautic(tipo = FabMauticContatoRest.LISTAREMPRESA_COM_FILTRO)
public class IntegracaoRestMauticListarempresaComFiltro
        extends
        AcaoApiIntegracaoComOauthAbstrato {

    public IntegracaoRestMauticListarempresaComFiltro(
            final FabTipoAgenteClienteRest pTipoAgente,
            final ItfUsuario pUsuario, final java.lang.Object... pParametro) {
        super(FabMauticContatoRest.LISTAREMPRESA_COM_FILTRO, pTipoAgente,
                pUsuario, pParametro);
    }

    @Override
    public String gerarUrlRequisicao() {
        return super.gerarUrlRequisicao(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> gerarCabecalho() {
        return super.gerarCabecalho(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gerarResposta(ConsumoWSExecucao pConsumoRest) {
        super.gerarResposta(pConsumoRest); //To change body of generated methods, choose Tools | Templates.
    }

}
