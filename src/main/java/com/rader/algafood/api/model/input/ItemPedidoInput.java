package com.rader.algafood.api.model.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemPedidoInput {

    //@ApiModelProperty(example = "1", required = true)
    @NotNull
    private Long produtoId;

    //@ApiModelProperty(example = "2", required = true)
    @NotNull
    @PositiveOrZero
    private Integer quantidade;

    //@ApiModelProperty(example = "Menos picante, por favor")
    private String observacao;
}
