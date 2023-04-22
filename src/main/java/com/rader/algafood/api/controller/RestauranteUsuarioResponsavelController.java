package com.rader.algafood.api.controller;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.model.UsuarioModel;
import com.rader.algafood.api.assembler.UsuarioModelAssembler;
import com.rader.algafood.api.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.rader.algafood.domain.model.Restaurante;
import com.rader.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController implements RestauranteUsuarioResponsavelControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        CollectionModel<UsuarioModel> usuariosModel = usuarioModelAssembler
                .toCollectionModel(restaurante.getResponsaveis())
                .removeLinks()
                .add(algaLinks.linkToRestauranteResponsaveis(restauranteId))
                .add(algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));

        usuariosModel.getContent().stream().forEach(usuarioModel -> {
            usuarioModel.add(algaLinks.linkToRestauranteResponsavelDesassociacao(
                    restauranteId, usuarioModel.getId(), "desassociar"));
        });

        return usuariosModel;
    }


    @Override
    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        cadastroRestaurante.desassociarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        cadastroRestaurante.associarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
