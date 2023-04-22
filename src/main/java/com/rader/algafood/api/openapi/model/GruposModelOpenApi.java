package com.rader.algafood.api.openapi.model;

import com.rader.algafood.api.model.GrupoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

//@ApiModel("GruposModel")
@Data
public class GruposModelOpenApi {

    private GruposEmbeddedModelOpenApi _embedded;
    private Links _links;

    //@ApiModel("GruposEmbeddedModel")
    @Data
    public class GruposEmbeddedModelOpenApi {

        private List<GrupoModel> grupos;

    }
}
