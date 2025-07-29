package com.estoque.estoque_api.controller;

import com.estoque.estoque_api.model.Produto;
import com.estoque.estoque_api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService service;

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
        Produto salvo = service.salvar(produto);
        URI location = URI.create("/produtos/" + salvo.getId());
        return ResponseEntity
                .created(location)
                .body(salvo);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Produto>> buscarPorFiltros(@RequestParam(required = false) String nome,
                                                          @RequestParam(required = false) String descricao){
        List<Produto> resultados;

        if (nome == null && descricao == null) {
            resultados = service.listarTodos();
        } else {
            resultados = service.buscarPorFiltros(nome, descricao);
        }

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resultados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto){
        return service.atualizar(id, produto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/remover-quantidade")
    public ResponseEntity<?> removerQuantidade(
            @PathVariable Long id,
            @RequestParam int quantidadeParaRemover) {

        Optional<Produto> produtoOpt = service.buscarPorId(id);
        if (produtoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (quantidadeParaRemover <= 0) {
            return ResponseEntity.badRequest().body("A quantidade deve ser maior que zero.");
        }

        Produto produto = produtoOpt.get();
        int quantidadeAtual = produto.getQuantidade();

        if (quantidadeParaRemover > quantidadeAtual) {
            return ResponseEntity.badRequest().body("Estoque insuficiente para remover a quantidade desejada.");
        }

        int novaQuantidade = quantidadeAtual - quantidadeParaRemover;

        if (novaQuantidade == 0) {
            service.deletar(id);
            return ResponseEntity.ok("Produto removido pois a quantidade chegou a zero.");
        } else {
            produto.setQuantidade(novaQuantidade);
            Produto atualizado = service.atualizarQuantidade(produto);
            return ResponseEntity.ok(atualizado);
        }
    }
}