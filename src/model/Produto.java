package model; // declarando o pacote 

public class Produto{ // criando a classe
    private String nome; 
    private double preco;

    public Produto(String nome, double preco){ // construtor da classe
        this.nome = nome; // inicializando o atributo nome
        this.preco = preco; // inicializando o atributo preco
    }

    public String getNome(){
        return nome;
    }

    public double getPreco(){
        return preco;
    }

    public void atualizarPreco(double novoPreco){
        this.preco = novoPreco;
    }

    @Override
    public String toString(){ // sobrescrevendo o método toString
        return "Produto: " + nome + ", Preco: R$ " + preco;
    }
}