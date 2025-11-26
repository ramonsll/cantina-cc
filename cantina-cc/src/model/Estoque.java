package model;

public class Estoque {

    private Map<Integer, ItemEstoque> estoque = new HashMap<>();

    public void addProduto(Produto produto, int quantidade) {
        ItemEstoque item = estoque.get(produto.getId());
        if (item == null) {
            item = new ItemEstoque(produto, quantidade);
        } else {
            item.setQuantidade(item.getQuantidade() + quantidade);
        }
        estoque.put(produto.getId(), item);
    }

    public void removerProduto(Produto produto, int quantidade) {
        ItemEstoque item = estoque.get(produto.getId());
        if (item == null) throw new RuntimeException("Produto não existe no estoque");

        if (item.getQuantidade() < quantidade)
            throw new RuntimeException("Quantidade insuficiente");

        item.setQuantidade(item.getQuantidade() - quantidade);
    }

    public int getQuantidade(Produto produto) {
        ItemEstoque item = estoque.get(produto.getId());
        return (item != null) ? item.getQuantidade() : 0;
    }

    public ItemEstoque getItem(int idProduto) {
        return estoque.get(idProduto);
    }

    public Map<Integer, ItemEstoque> listarItens() {
        return Collections.unmodifiableMap(estoque);
    }
}