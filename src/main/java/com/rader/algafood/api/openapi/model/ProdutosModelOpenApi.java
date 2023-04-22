package com.rader.algafood.api.openapi.model;

import com.rader.algafood.api.model.ProdutoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

//@ApiModel("ProdutosModel")
@Data
public class ProdutosModelOpenApi {

    private ProdutosEmbeddedModelOpenApi _embedded;
    private Links _links;

    //@ApiModel("ProdutosEmbeddedModel")
    @Data
    public class ProdutosEmbeddedModelOpenApi {

        private List<ProdutoModel> produtos;

    }
}
