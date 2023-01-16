package com.rader.algafoodapi.domain.service;

import com.rader.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.rader.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.rader.algafoodapi.domain.exception.RestauranteNaoEncontradoException;
import com.rader.algafoodapi.domain.model.Cozinha;
import com.rader.algafoodapi.domain.model.Restaurante;
import com.rader.algafoodapi.domain.repository.CozinhaRepository;
import com.rader.algafoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


@Service
public class CadastroRestauranteService {
    private static final String MSG_RESTAURANTE_EM_USO  =
            "Restaurante de código %d não pode ser removido, pois está em uso";

    private static final String MSG_RESTAURANTE_NAO_ENCONTRADO =
            "Não existe um cadastro de restaurante com código %d";

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();

        Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);

        restaurante.setCozinha(cozinha);

        return restauranteRepository.save(restaurante);
    }

    public Restaurante buscarOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
    }
}
