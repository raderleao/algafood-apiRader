package com.rader.algafood.api.openapi.model;

import com.rader.algafood.api.model.CidadeModel;

import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

//@ApiModel("CidadesModel")
@Data
public class CidadesModelOpenApi {
    private CidadeEmbeddedModelOpenApi _embedded;
    private Links _links;

    //@ApiModel("CidadesEmbeddedModel")
    @Data
    public class CidadeEmbeddedModelOpenApi {
        private List<CidadeModel> cidades;
    }
}
