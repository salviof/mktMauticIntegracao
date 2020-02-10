/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.Super_Bits.mktMauticIntegracao.regras_de_negocio_e_controller;

/**
 *
 * @author sfurbino
 */
public enum FabTwiloWhatapp {

    /**
     * curl
     * 'https://api.twilio.com/2010-04-01/Accounts/AC52493c2c7315095c89f35f7f39e6ef50/Messages.json'
     * -X POST \ --data-urlencode 'To=whatsapp:+553171125577' \ --data-urlencode
     * 'From=whatsapp:+14155238886' \ --data-urlencode 'Body=Hello! This is an
     * editable text message. You are free to change it and write whatever you
     * like.' \ -u AC52493c2c7315095c89f35f7f39e6ef50:[AuthToken]
     *
     */
    ENVIAR_MENSAGEM;
}
