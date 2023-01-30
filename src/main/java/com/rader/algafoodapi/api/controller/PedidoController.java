package com.rader.algafoodapi.api.controller;

import com.rader.algafoodapi.api.assembler.PedidoInputDisassembler;
import com.rader.algafoodapi.api.assembler.PedidoModelAssembler;
import com.rader.algafoodapi.api.assembler.PedidoResumoModelAssembler;
import com.rader.algafoodapi.api.model.PedidoModel;
import com.rader.algafoodapi.api.model.PedidoResumoModel;
import com.rader.algafoodapi.api.model.input.PedidoInput;
import com.rader.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.rader.algafoodapi.domain.exception.NegocioException;
import com.rader.algafoodapi.domain.model.Pedido;
import com.rader.algafoodapi.domain.model.Usuario;
import com.rader.algafoodapi.domain.repository.PedidoRepository;
import com.rader.algafoodapi.domain.repository.filter.PedidoFilter;
import com.rader.algafoodapi.domain.service.EmissaoPedidoService;
import com.rader.algafoodapi.infrastructure.repository.spec.PedidoSpecs;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EmissaoPedidoService emissaoPedido;

    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;

    @GetMapping
    public List<PedidoResumoModel> pesquisar(PedidoFilter filtro) {
        List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));

        return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
    }

    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);

        return pedidoModelAssembler.toModel(pedido);
    }

    @PostMapping
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

}

