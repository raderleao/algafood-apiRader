package com.rader.algafood.api.controller;

import com.rader.algafood.api.model.input.GrupoInput;
import com.rader.algafood.api.assembler.GrupoInputDisassembler;
import com.rader.algafood.api.assembler.GrupoModelAssembler;
import com.rader.algafood.api.model.GrupoModel;
import com.rader.algafood.api.openapi.controller.GrupoControllerOpenApi;
import com.rader.algafood.domain.model.Grupo;
import com.rader.algafood.domain.repository.GrupoRepository;
import com.rader.algafood.domain.service.CadastroGrupoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupo;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<GrupoModel> listar() {
        List<Grupo> todosGrupos = grupoRepository.findAll();

        return grupoModelAssembler.toCollectionModel(todosGrupos);
    }

    @Override
    @GetMapping(path = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GrupoModel buscar(@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        return grupoModelAssembler.toModel(grupo);
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);

        grupo = cadastroGrupo.salvar(grupo);

        return grupoModelAssembler.toModel(grupo);
    }

    @Override
    @PutMapping(path = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GrupoModel atualizar(@PathVariable Long grupoId,
                                @RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);

        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);

        grupoAtual = cadastroGrupo.salvar(grupoAtual);

        return grupoModelAssembler.toModel(grupoAtual);
    }

    @Override
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        cadastroGrupo.excluir(grupoId);
    }
}
