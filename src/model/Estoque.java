package model;

import java.util.ArrayList; // importando a classe ArrayList
import java.util.List; // importando a interface List

public class Estoque{
    private List<ItemEstoque> itens = new ArrayList<>(); // lista para armazenar os itens do estoque

    public void listarProdutosEstoque(){
        for(ItemEstoque item : itens){ // qro entender melhor como se le esse laço dps
            System.out.println("Produto: " + item.getProduto().getNome() + ", Quantidade: " + item.getProduto().getQuantidade() + ", Preco: R$ " + item.getProduto().getPrecoCusto());
        }
    }

    public void adicionarItem(ItemEstoque item){
        itens.add(item); // adicionando o item à lista
    }

    public double calcularTotal(){
        double total = 0.0;
        for(ItemEstoque item : itens){
            total += item.getPrecoCusto() * item.getQuantidade();
        }
        return total; 
    }
}