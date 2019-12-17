package br.org.coletivoJava.integracoes.restMautic.implementacao;

import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.AcaoApiIntegracaoHeaderBuilder;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.transmissao_recepcao_rest_client.ItfAcaoApiRest;

public class IntegracaoRestMautic_HeaderPadrao
		extends
			AcaoApiIntegracaoHeaderBuilder {

	public IntegracaoRestMautic_HeaderPadrao(final ItfAcaoApiRest pAcao) {
		super(pAcao);
	}
}