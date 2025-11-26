package model; // declarando o pacote 

public class Produto {

    private String nome;
    private double preco;
    private int id;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço inválido.");
        }
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto(String nome, int id, double preco) {
        this.setNome(nome);
        this.setPreco(preco);
        this.setId(id);
    }

}