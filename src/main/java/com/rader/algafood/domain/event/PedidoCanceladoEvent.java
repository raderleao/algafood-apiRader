package com.rader.algafood.domain.event;

import com.rader.algafood.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {
    private Pedido pedido;
}
