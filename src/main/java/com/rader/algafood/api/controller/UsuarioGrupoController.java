package com.rader.algafood.api.controller;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.assembler.GrupoModelAssembler;
import com.rader.algafood.api.model.GrupoModel;
import com.rader.algafood.api.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.rader.algafood.domain.model.Usuario;
import com.rader.algafood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

    @Autowired
    private CadastroUsuarioService cadastroUsuario;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

        CollectionModel<GrupoModel> gruposModel = grupoModelAssembler.toCollectionModel(usuario.getGrupos())
                .removeLinks()
                .add(algaLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));

        gruposModel.getContent().forEach(grupoModel -> {
            grupoModel.add(algaLinks.linkToUsuarioGrupoDesassociacao(
                    usuarioId, grupoModel.getId(), "desassociar"));
        });

        return gruposModel;
    }

    @Override
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        cadastroUsuario.desassociarGrupo(usuarioId, grupoId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        cadastroUsuario.associarGrupo(usuarioId, grupoId);

        return ResponseEntity.noContent().build();
    }
}
