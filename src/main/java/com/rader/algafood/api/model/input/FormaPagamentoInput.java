package com.rader.algafood.api.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormaPagamentoInput {

    //@ApiModelProperty(example = "Cartão de crédito", required = true)
    @NotBlank
    private String descricao;

}
