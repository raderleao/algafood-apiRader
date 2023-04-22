package com.rader.algafood.api.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProdutoInput {

    //@ApiModelProperty(example = "Espetinho de Cupim", required = true)
    @NotBlank
    private String nome;

    //@ApiModelProperty(example = "Acompanha farinha, mandioca e vinagrete", required = true)
    @NotBlank
    private String descricao;

    //@ApiModelProperty(example = "12.50", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal preco;

    //@ApiModelProperty(example = "true", required = true)
    @NotNull
    private Boolean ativo;

}