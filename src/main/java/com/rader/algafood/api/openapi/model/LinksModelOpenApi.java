package com.rader.algafood.api.openapi.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@ApiModel("Links")
public class LinksModelOpenApi {

    private LinkModel rel;

    @Setter
    @Getter
    //@ApiModel("Link")
    private class LinkModel {

        private String href;
        private boolean templated;

    }

}
