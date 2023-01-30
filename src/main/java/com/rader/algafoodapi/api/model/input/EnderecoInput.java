package com.rader.algafoodapi.api.model.input;

import com.rader.algafoodapi.api.model.CidadeResumoModel;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.websocket.OnOpen;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnderecoInput {

    @NotBlank
    private String cep;
    @NotBlank
    private String logradouro;
    @NotBlank
    private String numero;
    private String complemento;
    @NotBlank
    private String bairro;
    @Valid
    @NotNull
    private CidadeIdInput cidade;
}
