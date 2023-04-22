package com.rader.algafood.domain.repository;


import com.rader.algafood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

    FotoProduto save(FotoProduto foto);

    void delete (FotoProduto foto);
}
