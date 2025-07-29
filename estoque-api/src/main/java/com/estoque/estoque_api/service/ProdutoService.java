package com.estoque.estoque_api.service;

import com.estoque.estoque_api.model.Produto;
import com.estoque.estoque_api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository repository;
    public Produto salvar(Produto produto){
        Optional<Produto> existente = repository.findByNomeAndDescricao(produto.getNome(), produto.getDescricao());

        if (existente.isPresent()){
            Produto produtoExistente = existente.get();
            produtoExistente.incrementarQuantidade(produto.getQuantidade());
            return repository.save(produtoExistente);
        } else {
            return repository.save(produto);
        }
    }

    public List<Produto> listarTodos(){
        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id){
        return repository.findById(id);
    }

    public List<Produto> buscarPorFiltros(String nome, String descricao) {
        if (nome != null){
            nome = nome.toLowerCase();
        }
        if (descricao != null){
            descricao = descricao.toLowerCase();
        }
        return repository.buscarPorFiltros(nome, descricao);
    }

    public Optional<Produto> atualizar(Long id, Produto produtoAtualizado){
        return repository.findById(id).map(produtoExistente -> {
            produtoExistente.setNome(produtoAtualizado.getNome());
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
            produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
            produtoExistente.setPreco(produtoAtualizado.getPreco());
            return repository.save(produtoExistente);
        });
    }

    public void deletar(Long id){
        repository.deleteById(id);
    }

    public Produto atualizarQuantidade(Produto produto) {
        return repository.save(produto);
    }

}
