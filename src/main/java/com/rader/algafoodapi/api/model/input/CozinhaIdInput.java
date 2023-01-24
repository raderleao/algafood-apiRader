package com.rader.algafoodapi.api.model.input;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CozinhaIdInput {

    @NotNull
    private Long id;
}
