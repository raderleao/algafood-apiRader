package com.rader.algafood.api.assembler;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.controller.CozinhaController;
import com.rader.algafood.api.model.CozinhaModel;
import com.rader.algafood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class CozinhaModelAssembler
        extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public CozinhaModelAssembler(){
        super(CozinhaController.class, CozinhaModel.class);
    }

    @Override
    public CozinhaModel toModel(Cozinha cozinha) {
        CozinhaModel cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
        modelMapper.map(cozinha, cozinhaModel);

        cozinhaModel.add(algaLinks.linkToCozinhas("cozinhas"));

        return cozinhaModel;
    }

}
