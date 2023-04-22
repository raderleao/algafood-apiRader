package com.rader.algafood.api.assembler;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.controller.GrupoController;
import com.rader.algafood.api.model.GrupoModel;
import com.rader.algafood.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrupoModelAssembler
        extends RepresentationModelAssemblerSupport<Grupo, GrupoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public GrupoModelAssembler() {
        super(GrupoController.class, GrupoModel.class);
    }

    @Override
    public GrupoModel toModel(Grupo grupo) {
        GrupoModel grupoModel = createModelWithId(grupo.getId(), grupo);
        modelMapper.map(grupo, grupoModel);

        grupoModel.add(algaLinks.linkToGrupos("grupos"));

        grupoModel.add(algaLinks.linkToGrupoPermissoes(grupo.getId(), "permissoes"));

        return grupoModel;
    }

    @Override
    public CollectionModel<GrupoModel> toCollectionModel(Iterable<? extends Grupo> entities) {
        return super.toCollectionModel(entities)
                .add(algaLinks.linkToGrupos());
    }
}
