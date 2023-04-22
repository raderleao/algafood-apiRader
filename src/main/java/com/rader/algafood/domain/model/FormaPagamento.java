package com.rader.algafood.domain.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FormaPagamento {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String descricao;

    @UpdateTimestamp
    private OffsetDateTime dataAtualizacao;
}
