package com.rader.algafood.domain.service;

import com.rader.algafood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {
    byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
