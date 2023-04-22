package com.rader.algafood.api.openapi.controller;

import org.springframework.data.domain.Pageable;

import com.rader.algafood.api.model.PedidoModel;
import com.rader.algafood.api.model.PedidoResumoModel;
import com.rader.algafood.api.model.input.PedidoInput;
import com.rader.algafood.domain.filter.PedidoFilter;

import org.springframework.hateoas.PagedModel;

//@Api(tags = "Pedidos")
public interface PedidoControllerOpenApi {

   /* @ApiOperation("Pesquisa os pedidos")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string")
    })*/
    PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable);

   /* @ApiOperation("Registra um pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido registrado"),
    })*/
    PedidoModel adicionar(
            //@ApiParam(name = "corpo", value = "Representação de um novo pedido", required = true)
            PedidoInput pedidoInput);

    /*@ApiOperation("Busca um pedido por código")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string")
    })*/
    PedidoModel buscar(
            /*//@ApiParam(value = "Código de um pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55",
                    required = true)*/
            String codigoPedido);

}
