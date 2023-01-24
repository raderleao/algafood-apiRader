package com.rader.algafoodapi.domain.service;

import com.rader.algafoodapi.domain.exception.PermissaoNaoEncontradaException;
import com.rader.algafoodapi.domain.model.Permissao;
import com.rader.algafoodapi.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroPermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

    public Permissao buscarOuFalhar(Long permissaoId) {
        return permissaoRepository.findById(permissaoId)
                .orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
    }
}
