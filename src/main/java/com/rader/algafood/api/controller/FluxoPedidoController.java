package com.rader.algafood.api.controller;

import com.rader.algafood.api.openapi.controller.FluxoPedidoControllerOpenApi;
import com.rader.algafood.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/pedidos/{codigoPedido}")
public class FluxoPedidoController implements FluxoPedidoControllerOpenApi {
    @Autowired
    private FluxoPedidoService fluxoPedido;

    @Override
    @PutMapping(path ="/confirmacao", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> confirmar(@PathVariable String codigoPedido){
        fluxoPedido.confirmar(codigoPedido);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(path = "/cancelamento", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> cancelar(@PathVariable String codigoPedido){
        fluxoPedido.cancelar(codigoPedido);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(path="/entrega", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> entregar(@PathVariable String codigoPedido){
        fluxoPedido.entregar(codigoPedido);
        return ResponseEntity.noContent().build();
    }
}
