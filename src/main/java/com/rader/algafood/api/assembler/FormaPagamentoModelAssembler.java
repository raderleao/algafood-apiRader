package com.rader.algafood.api.assembler;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.controller.FormaPagamentoController;
import com.rader.algafood.domain.model.FormaPagamento;
import com.rader.algafood.api.model.FormaPagamentoModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FormaPagamentoModelAssembler
        extends RepresentationModelAssemblerSupport<FormaPagamento, FormaPagamentoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public FormaPagamentoModelAssembler() {
        super(FormaPagamentoController.class, FormaPagamentoModel.class);
    }

    @Override
    public FormaPagamentoModel toModel(FormaPagamento formaPagamento) {
        FormaPagamentoModel formaPagamentoModel =
                createModelWithId(formaPagamento.getId(), formaPagamento);

        modelMapper.map(formaPagamento, formaPagamentoModel);

        formaPagamentoModel.add(algaLinks.linkToFormasPagamento("formasPagamento"));

        return formaPagamentoModel;
    }

    @Override
    public CollectionModel<FormaPagamentoModel> toCollectionModel(Iterable<? extends FormaPagamento> entities) {
        return super.toCollectionModel(entities)
                .add(algaLinks.linkToFormasPagamento());
    }

}
