package com.rader.algafoodapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class EntidadeEmUsoException extends NegocioException {
    public EntidadeEmUsoException(String mensagem){

        super(mensagem);
    }
}
