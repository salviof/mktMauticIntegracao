package br.org.coletivoJava.integracoes.restMautic.implementacao;

import br.org.coletivoJava.integracoes.restMautic.api.InfoIntegracaoRestMautic;
import com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller.FabMauticContatoRest;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.AcaoApiIntegracaoComOauthAbstrato;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.FabTipoAgenteClienteRest;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfUsuario;

@InfoIntegracaoRestMautic(tipo = FabMauticContatoRest.CONTATO_CTR_SALVAR_EDITAR_CONTATO)
public class IntegracaoRestMauticContatoCtrSalvarEditarContato
		extends
			AcaoApiIntegracaoComOauthAbstrato {

	public IntegracaoRestMauticContatoCtrSalvarEditarContato(
			final FabTipoAgenteClienteRest pTipoAgente,
			final ItfUsuario pUsuario, final java.lang.Object... pParametro) {
		super(FabMauticContatoRest.CONTATO_CTR_SALVAR_EDITAR_CONTATO,
				pTipoAgente, pUsuario, pParametro);
	}
}