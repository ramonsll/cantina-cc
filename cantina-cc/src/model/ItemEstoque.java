package model;

public class ItemEstoque {

    private Produto produto;
    private int quantidade;

    public ItemEstoque(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;

    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(quantidade) {
        this.quantidade = quantidade;
    }

}
