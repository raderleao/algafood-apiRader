package com.rader.algafood.api.openapi.model;

import com.rader.algafood.api.model.PermissaoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

//@ApiModel("PermissoesModel")
@Data
public class PermissoesModelOpenApi {

    private PermissoesEmbeddedModelOpenApi _embedded;
    private Links _links;

    //@ApiModel("PermissoesEmbeddedModel")
    @Data
    public class PermissoesEmbeddedModelOpenApi {

        private List<PermissaoModel> permissoes;

    }
}
