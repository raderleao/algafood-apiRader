package com.rader.algafood.api.controller;

import com.rader.algafood.api.model.CidadeModel;
import com.rader.algafood.api.model.input.CidadeInput;
import com.rader.algafood.api.openapi.controller.CidadeControllerOpenApi;
import com.rader.algafood.domain.exception.EstadoNaoEncontradoException;
import com.rader.algafood.domain.exception.NegocioException;
import com.rader.algafood.api.ResourceUriHelper;
import com.rader.algafood.api.assembler.CidadeInputDisassembler;
import com.rader.algafood.api.assembler.CidadeModelAssembler;
import com.rader.algafood.domain.model.Cidade;
import com.rader.algafood.domain.repository.CidadeRepository;
import com.rader.algafood.domain.service.CadastroCidadeService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/cidades")
public class CidadeController implements CidadeControllerOpenApi {
        @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDisassembler cidadeInputDisassembler;
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<CidadeModel> listar() {
        List<Cidade> todasCidades = cidadeRepository.findAll();

        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @Override
    @GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CidadeModel buscar(@PathVariable Long cidadeId) {
        Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

        return cidadeModelAssembler.toModel(cidade);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            cidade = cadastroCidade.salvar(cidade);

            CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());

            return cidadeModel;
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @Override
    @PutMapping(path= "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CidadeModel atualizar(@PathVariable Long cidadeId,
                                 @RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            cidadeAtual = cadastroCidade.salvar(cidadeAtual);

            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @Override
    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidade.excluir(cidadeId);
    }

}