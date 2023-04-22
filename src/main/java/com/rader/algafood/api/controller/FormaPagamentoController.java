package com.rader.algafood.api.controller;

import com.rader.algafood.api.AlgaLinks;
import com.rader.algafood.api.assembler.FormaPagamentoInputDisassembler;
import com.rader.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.rader.algafood.api.model.FormaPagamentoModel;
import com.rader.algafood.api.model.input.FormaPagamentoInput;
import com.rader.algafood.api.openapi.controller.FormaPagamentoControllerOpenApi;
import com.rader.algafood.domain.model.FormaPagamento;
import com.rader.algafood.domain.repository.FormaPagamentoRepository;
import com.rader.algafood.domain.service.CadastroFormaPagamentoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamento;

    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @Autowired
    private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";
        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();

        if (dataUltimaAtualizacao != null ){
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }

        if (request.checkNotModified(eTag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic())
                    .eTag(eTag)
                    .build();
        }

        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();

        CollectionModel<FormaPagamentoModel> formasPagamentosModel =
                formaPagamentoModelAssembler.toCollectionModel(todasFormasPagamentos);

       return ResponseEntity.ok()
               .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
               .eTag(eTag)
               .body(formasPagamentosModel);
    }

    @Override
    @GetMapping(value = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPagamentoId,
                                                      ServletWebRequest request) {

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataAtualizacao = formaPagamentoRepository
                .getDataAtualizacaoById(formaPagamentoId);

        if (dataAtualizacao != null) { eTag = String.valueOf(dataAtualizacao.toEpochSecond());}

        if (request.checkNotModified(eTag)) return null;

        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);

        FormaPagamentoModel formaPagamentoModel = formaPagamentoModelAssembler.toModel(formaPagamento);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .eTag(eTag)
                .body(formaPagamentoModel);
    }

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);

        formaPagamento = cadastroFormaPagamento.salvar(formaPagamento);

        return formaPagamentoModelAssembler.toModel(formaPagamento);
    }

    @PutMapping(value = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,
                                         @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);

        formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);

        formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);

        return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);
    }

    @Override
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId) {
        cadastroFormaPagamento.excluir(formaPagamentoId);
    }
}
