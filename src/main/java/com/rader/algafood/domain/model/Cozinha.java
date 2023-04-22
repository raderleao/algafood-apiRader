package com.rader.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.rader.algafood.core.validation.Groups;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@JsonRootName("cozinha")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {
    @NotNull(groups = Groups.CozinhaId.class)
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "cozinha")
    private List<Restaurante> restaurantes = new ArrayList<>();
}
