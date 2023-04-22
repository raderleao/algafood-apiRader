package com.rader.algafood.api.controller;

import com.rader.algafood.api.model.UsuarioModel;
import com.rader.algafood.api.model.input.UsuarioInput;
import com.rader.algafood.api.openapi.controller.UsuarioControllerOpenApi;
import com.rader.algafood.domain.repository.UsuarioRepository;
import com.rader.algafood.api.assembler.UsuarioInputDisassembler;
import com.rader.algafood.api.assembler.UsuarioModelAssembler;
import com.rader.algafood.api.model.input.SenhaInput;
import com.rader.algafood.api.model.input.UsuarioComSenhaInput;
import com.rader.algafood.domain.model.Usuario;
import com.rader.algafood.domain.service.CadastroUsuarioService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController implements UsuarioControllerOpenApi {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CadastroUsuarioService cadastroUsuario;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UsuarioModel> listar() {
        List<Usuario> todasUsuarios = usuarioRepository.findAll();

        return usuarioModelAssembler.toCollectionModel(todasUsuarios);
    }

    @GetMapping(value = "/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsuarioModel buscar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

        return usuarioModelAssembler.toModel(usuario);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioInput) {
        Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
        usuario = cadastroUsuario.salvar(usuario);

        return usuarioModelAssembler.toModel(usuario);
    }

    @PutMapping(value = "/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsuarioModel atualizar(@PathVariable Long usuarioId,
                                  @RequestBody @Valid UsuarioInput usuarioInput) {
        Usuario usuarioAtual = cadastroUsuario.buscarOuFalhar(usuarioId);
        usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
        usuarioAtual = cadastroUsuario.salvar(usuarioAtual);

        return usuarioModelAssembler.toModel(usuarioAtual);
    }

    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senha) {
        cadastroUsuario.alterarSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
    }
}
