package com.rader.algafood.api.openapi.model;

import com.rader.algafood.api.model.EstadoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

//@ApiModel("EstadosModel")
@Data
public class EstadosModelOpenApi {

    private EstadosEmbeddedModelOpenApi _embedded;
    private Links _links;

    //@ApiModel("EstadosEmbeddedModel")
    @Data
    public class EstadosEmbeddedModelOpenApi {

        private List<EstadoModel> estados;

    }
}
