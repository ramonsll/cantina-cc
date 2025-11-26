public class ItemVenda {
    private Produto produto;
    private int quantidade;
    private double preco;

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = produto.getPreco(); // preco unitário
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPreco() { return preco; }

    public double calcularSubtotal() {
        return preco * quantidade;
    }
}