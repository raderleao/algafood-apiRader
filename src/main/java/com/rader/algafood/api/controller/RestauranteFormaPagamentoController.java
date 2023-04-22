package com.rader.algafood.api.controller;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.rader.algafood.api.model.FormaPagamentoModel;
import com.rader.algafood.api.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.rader.algafood.domain.model.Restaurante;
import com.rader.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

    @Override
    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        CollectionModel<FormaPagamentoModel> formasPagamentoModel
                = formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento())
                .removeLinks()
                .add(algaLinks.linkToRestauranteFormasPagamento(restauranteId))
                .add(algaLinks.linkToRestauranteFormaPagamentoAsassociacao(restauranteId, "associar"));


        formasPagamentoModel.getContent().forEach(formaPagamentoModel -> {
            formaPagamentoModel.add(algaLinks.linkToRestauranteFormaPagamentoDesassociacao(
                    restauranteId, formaPagamentoModel.getId(), "desassociar"));
                });

        return formasPagamentoModel;
    }

    @Override
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId,
                                            @PathVariable Long formaPagamentoId){
        cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){
        cadastroRestaurante.asassociarFormaPagamento(restauranteId, formaPagamentoId);
        return ResponseEntity.noContent().build();
    }

}

