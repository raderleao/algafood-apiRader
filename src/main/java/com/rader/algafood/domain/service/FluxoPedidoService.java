package com.rader.algafood.domain.service;

import com.rader.algafood.domain.repository.PedidoRepository;
import com.rader.algafood.domain.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    @Autowired
    private EmissaoPedidoService emissaoPedido;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public void confirmar (String codigoPedido){
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.confirmar();

        pedidoRepository.save(pedido);
    }

    @Transactional
    public void entregar (String codigoPedido){
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.entregar();
    }

    @Transactional
    public void cancelar (String codigoPedido){
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.cancelar();

        pedidoRepository.save(pedido);
    }

}
