/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.integracoes.testes;

import com.super_bits.modulosSB.SBCore.integracao.libRestClient.implementacao.UtilSBApiRestClient;
import spark.Spark;

/**
 *
 * @author sfurbino
 * @since 17/12/2019
 * @version 1.0
 */
public class ServidorOauthRecepcaoSpark extends Thread {

    private String porta;
    private String caminho;

    public ServidorOauthRecepcaoSpark(String porta) {
        this.porta = porta;

    }

    @Override
    public void run() {
        String porta = this.porta;
        Spark.port(Integer.valueOf(porta));

        Spark.get("/hello", (req, res) -> {
            try {

                UtilSBApiRestClient.receberCodigoSolicitacaoOauth(req.raw());
                return "Olá Cidadão";
            } catch (Throwable t) {
                return "Erro Maluco" + t.getMessage();
            }

        });

    }

}
