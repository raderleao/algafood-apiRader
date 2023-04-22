package com.rader.algafood.domain.listener;

import com.rader.algafood.domain.service.EnvioEmailService;
import com.rader.algafood.domain.event.PedidoCanceladoEvent;
import com.rader.algafood.domain.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoCanceladoListener {

    @Autowired
    private EnvioEmailService envioEmail;

    @TransactionalEventListener
    public void aoConfirmarPedido(PedidoCanceladoEvent event) {
        Pedido pedido = event.getPedido();

        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - Pedido cancelado")
                .corpo("pedido-cancelado.html")
                .variavel("pedido", pedido)
                .destinatario(pedido.getCliente().getEmail())
                .build();

        envioEmail.enviar(mensagem);
    }

}