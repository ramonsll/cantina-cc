package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Estoque {

    // chave = id do produto
    private Map<Integer, ItemEstoque> itens = new HashMap<>();

    // adiciona produto ao estoque (soma se já existir)
    public void addProduto(Produto produto, int quantidade) {
        if (produto == null) throw new IllegalArgumentException("Produto nulo");
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");

        ItemEstoque item = itens.get(produto.getId());
        if (item == null) {
            item = new ItemEstoque(produto, quantidade);
            itens.put(produto.getId(), item);
        } else {
            item.adicionarQuantidade(quantidade);
        }
    }

    // remover do total disponível (venda efetivada). Só remove do disponível, não do reservado.
    public void removerProduto(Produto produto, int quantidade) {
        if (produto == null) throw new IllegalArgumentException("Produto nulo");
        ItemEstoque item = itens.get(produto.getId());
        if (item == null) throw new IllegalStateException("Produto não existe no estoque");
        item.removerQuantidadeTotal(quantidade);
    }

    // reserva: bloqueia quantidade (retorna true se ok)
    public boolean reservarQuantidade(int idProduto, int quantidade) {
        ItemEstoque item = itens.get(idProduto);
        if (item == null) return false;
        return item.reservar(quantidade);
    }

    // libera uma reserva (usado ao cancelar)
    public void liberarReserva(int idProduto, int quantidade) {
        ItemEstoque item = itens.get(idProduto);
        if (item == null) throw new IllegalStateException("Produto não existe no estoque");
        item.liberarReserva(quantidade);
    }

    // confirmar retirada (consumir reserva: diminui reserved e total)
    public void consumirReserva(int idProduto, int quantidade) {
        ItemEstoque item = itens.get(idProduto);
        if (item == null) throw new IllegalStateException("Produto não existe no estoque");
        item.consumirReserva(quantidade);
    }

    // obtém quantidade disponível (total - reservado)
    public int getQuantidadeDisponivel(int idProduto) {
        ItemEstoque item = itens.get(idProduto);
        if (item == null) return 0;
        return item.getQuantidadeDisponivel();
    }

    public ItemEstoque getItem(int idProduto) {
        return itens.get(idProduto);
    }

    public Map<Integer, ItemEstoque> listarItens() {
        return Collections.unmodifiableMap(itens);
    }
}
