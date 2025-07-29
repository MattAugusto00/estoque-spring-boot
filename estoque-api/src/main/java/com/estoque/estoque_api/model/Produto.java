package com.estoque.estoque_api.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal preco;

    public void incrementarQuantidade(int quantidadeASomar) {
        if(quantidadeASomar > 0){
            this.quantidade = this.quantidade + quantidadeASomar;
        }
    }
}
