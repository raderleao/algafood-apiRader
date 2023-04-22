package com.rader.algafood.api.openapi.model;

import com.rader.algafood.api.model.FormaPagamentoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

//@ApiModel("FormasPagamentoModel")
@Data
public class FormasPagamentoModelOpenApi {

    private FormasPagamentoEmbeddedModelOpenApi _embedded;
    private Links _links;

    //@ApiModel("FormasPagamentoEmbeddedModel")
    @Data
    public class FormasPagamentoEmbeddedModelOpenApi {

        private List<FormaPagamentoModel> formasPagamento;

    }
}
