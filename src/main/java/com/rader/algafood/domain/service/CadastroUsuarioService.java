package com.rader.algafood.domain.service;

import com.rader.algafood.domain.exception.NegocioException;
import com.rader.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.rader.algafood.domain.repository.UsuarioRepository;
import com.rader.algafood.domain.model.Grupo;
import com.rader.algafood.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupo;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        usuarioRepository.detach(usuario);

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)){
            throw new NegocioException(
                    String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarOuFalhar(usuarioId);

        if (usuario.senhaNaoCoincideCom(senhaAtual)) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }

        usuario.setSenha(novaSenha);
    }
    @Transactional
    public void desassociarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        usuario.removerGrupo(grupo);
    }

    @Transactional
    public void associarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        usuario.adicionarGrupo(grupo);
    }

    public Usuario buscarOuFalhar(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }
}

