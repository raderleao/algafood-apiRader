package com.rader.algafood.api.openapi.model;
import com.rader.algafood.api.model.CozinhaModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

//@ApiModel("CozinhasModel")
@Setter
@Getter
public class CozinhasModelOpenApi {

    private CozinhasEmbeddedModelOpenApi _embedded;
    private Links _links;
    private PageModelOpenApi page;

    //@ApiModel("CozinhasEmbeddedModel")
    @Data
    public class CozinhasEmbeddedModelOpenApi {

        private List<CozinhaModel> cozinhas;

    }

}
