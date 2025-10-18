package model;

public class ItemEstoque{
    private Produto produto;
    private int quantidade;
    private double precoCusto;

    public ItemEstoque(Produto produto, int quantidade, double precoCusto){
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
    }

    public Produto getProduto(){
        return produto;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public double getPrecoCusto(){
        return precoCusto;
    }
}