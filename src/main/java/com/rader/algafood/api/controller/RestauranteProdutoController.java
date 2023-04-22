package com.rader.algafood.api.controller;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.model.ProdutoModel;
import com.rader.algafood.api.assembler.ProdutoInputDisassembler;
import com.rader.algafood.api.assembler.ProdutoModelAssembler;
import com.rader.algafood.api.model.input.ProdutoInput;
import com.rader.algafood.api.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.rader.algafood.domain.model.Produto;
import com.rader.algafood.domain.model.Restaurante;
import com.rader.algafood.domain.repository.ProdutoRepository;
import com.rader.algafood.domain.service.CadastroProdutoService;
import com.rader.algafood.domain.service.CadastroRestauranteService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CadastroProdutoService cadastroProduto;

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private ProdutoModelAssembler produtoModelAssembler;

    @Autowired
    private ProdutoInputDisassembler produtoInputDisassembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId,
                                                @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        List<Produto> todosProdutos = null;

        if (incluirInativos) {
            todosProdutos = produtoRepository.findTodosByRestaurante(restaurante);
        } else {
            todosProdutos = produtoRepository.findAtivosByRestaurante(restaurante);
        }

        return produtoModelAssembler.toCollectionModel(todosProdutos)
                .add(algaLinks.linkToProdutos(restauranteId));
    }

    @Override
    @GetMapping(value = "/{produtoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

        return produtoModelAssembler.toModel(produto);
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long restauranteId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);

        produto = cadastroProduto.salvar(produto);

        return produtoModelAssembler.toModel(produto);
    }

    @Override
    @PutMapping(value = "/{produtoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {
        Produto produtoAtual = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);

        produtoAtual = cadastroProduto.salvar(produtoAtual);

        return produtoModelAssembler.toModel(produtoAtual);
    }
}
