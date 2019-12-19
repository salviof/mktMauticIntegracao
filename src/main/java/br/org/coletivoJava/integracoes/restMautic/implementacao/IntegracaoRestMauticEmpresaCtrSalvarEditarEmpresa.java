package br.org.coletivoJava.integracoes.restMautic.implementacao;

import br.org.coletivoJava.integracoes.restMautic.api.InfoIntegracaoRestMautic;
import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.AcaoApiIntegracaoComOauthAbstrato;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.FabTipoAgenteClienteRest;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfUsuario;

@InfoIntegracaoRestMautic(tipo = FabMauticContatoRest.EMPRESA_CTR_SALVAR_EDITAR_EMPRESA)
public class IntegracaoRestMauticEmpresaCtrSalvarEditarEmpresa
        extends
        AcaoApiIntegracaoComOauthAbstrato {

    public IntegracaoRestMauticEmpresaCtrSalvarEditarEmpresa(
            final FabTipoAgenteClienteRest pTipoAgente,
            final ItfUsuario pUsuario, final java.lang.Object... pParametro) {
        super(FabMauticContatoRest.EMPRESA_CTR_SALVAR_EDITAR_EMPRESA,
                pTipoAgente, pUsuario, pParametro);
    }

    @Override
    public String gerarCorpoRequisicao() {
        return "{ \"companyname\":\"" + parametros[0] + "\",\"companyemail\":\"" + parametros[1] + "\", \"companywebsite\": \"" + parametros[2] + "\", \"companyphone\": \"" + parametros[3] + "\",\"companydescription\":\"" + parametros[4] + "\"}";
    }

}
