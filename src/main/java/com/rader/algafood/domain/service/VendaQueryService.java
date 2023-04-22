package com.rader.algafood.domain.service;

import com.rader.algafood.domain.filter.VendaDiariaFilter;
import com.rader.algafood.domain.model.dto.VendaDiaria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
