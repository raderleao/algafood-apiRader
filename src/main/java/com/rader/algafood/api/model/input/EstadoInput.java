package com.rader.algafood.api.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoInput {

    //@ApiModelProperty(example = "Minas Gerais", required = true)
    @NotBlank
    private String nome;
}