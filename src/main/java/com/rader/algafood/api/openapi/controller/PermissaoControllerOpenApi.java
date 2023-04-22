package com.rader.algafood.api.openapi.controller;

import com.rader.algafood.api.model.PermissaoModel;
import org.springframework.hateoas.CollectionModel;

//@Api(tags = "Permissões")
public interface PermissaoControllerOpenApi {
    //@ApiOperation("Lista as permissões")
    CollectionModel<PermissaoModel> listar();

}
