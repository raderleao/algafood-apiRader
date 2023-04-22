package com.rader.algafood.api.assembler;

import com.rader.algafood.api.model.input.GrupoInput;
import com.rader.algafood.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class GrupoInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Grupo toDomainObject(GrupoInput grupoInput) {
        return modelMapper.map(grupoInput, Grupo.class);
    }

    public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo) {
        modelMapper.map(grupoInput, grupo);
    }
}
