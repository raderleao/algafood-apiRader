package com.rader.algafood.domain.repository;


import com.rader.algafood.domain.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepositoryQueries {

    List<Restaurante> find(String nome,
                           BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

    List<Restaurante> findComFreteGratis(String nome);
}
