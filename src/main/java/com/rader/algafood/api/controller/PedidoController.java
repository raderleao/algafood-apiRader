package com.rader.algafood.api.controller;

import com.rader.algafood.api.model.PedidoModel;
import com.rader.algafood.api.model.input.PedidoInput;
import com.rader.algafood.api.openapi.controller.PedidoControllerOpenApi;
import com.rader.algafood.core.data.PageWrapper;
import com.rader.algafood.core.data.PageableTranslator;
import com.rader.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.rader.algafood.domain.exception.NegocioException;
import com.rader.algafood.domain.filter.PedidoFilter;
import com.rader.algafood.domain.repository.PedidoRepository;
import com.rader.algafood.domain.service.EmissaoPedidoService;
import com.rader.algafood.infrastructure.repository.spec.PedidoSpecs;
import com.rader.algafood.api.assembler.PedidoInputDisassembler;
import com.rader.algafood.api.assembler.PedidoModelAssembler;
import com.rader.algafood.api.assembler.PedidoResumoModelAssembler;
import com.rader.algafood.api.model.PedidoResumoModel;
import com.rader.algafood.domain.model.Pedido;
import com.rader.algafood.domain.model.Usuario;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/pedidos")
public class PedidoController implements PedidoControllerOpenApi {

    private PedidoRepository pedidoRepository;

    private EmissaoPedidoService emissaoPedido;

    private PedidoModelAssembler pedidoModelAssembler;

    private PedidoResumoModelAssembler pedidoResumoModelAssembler;

    private PedidoInputDisassembler pedidoInputDisassembler;

    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

    public PedidoController() {}

    public PedidoController(
            final PedidoRepository pedidoRepository,
            final EmissaoPedidoService emissaoPedido,
            final PedidoModelAssembler pedidoModelAssembler,
            final PedidoResumoModelAssembler pedidoResumoModelAssembler,
            final PedidoInputDisassembler pedidoInputDisassembler,
            final PagedResourcesAssembler<Pedido> pagedResourcesAssembler
    ){
        this.pedidoRepository = pedidoRepository;
        this.emissaoPedido = emissaoPedido;
        this.pedidoModelAssembler = pedidoModelAssembler;
        this.pedidoResumoModelAssembler = pedidoResumoModelAssembler;
        this.pedidoInputDisassembler = pedidoInputDisassembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro,
                                                   @PageableDefault(size = 10) Pageable pageable) {
        Pageable pageableTraduzido = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = pedidoRepository.findAll(
                PedidoSpecs.usandoFiltro(filtro), pageableTraduzido);

        pedidosPage = new PageWrapper<>(pedidosPage, pageable);

        return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
    }

    @Override
    @GetMapping(value = "/{codigoPedido}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);

        return pedidoModelAssembler.toModel(pedido);
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO: pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    private Pageable traduzirPageable(Pageable apiPageable) {
        var mapeamento = Map.of(
                "codigo", "codigo",
                "subtotal", "subtotal",
                "taxaFrete", "taxaFrete",
                "valorTotal", "valorTotal",
                "dataCriacao", "dataCriacao",
                "restaurante.nome", "restaurante.nome",
                "restaurante.id", "restaurante.id",
                "cliente.id", "cliente.id",
                "cliente.nome", "cliente.nome"
        );

        return PageableTranslator.translate(apiPageable, mapeamento);
    }

}

