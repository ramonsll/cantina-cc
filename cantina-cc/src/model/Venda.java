package model;

public class Venda {

    private int id;
    private List<ItemVenda> itens = new ArrayList<>();
    private FormaPagamento formaPagamento;

    public Int getId(){ return id;}
    public void setId(int id){ this.id = id;} 

    public FormaPagamento getFormaPagamento(){ return formaPagamento;} 
    public void setFormaPagamento(FormaPagamento formaPagamento){ this.formaPagamento = formaPagamento;}

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemVenda item : itens) {
            total += item.getPreco() * item.getQuantidade();
        }
        return total;
    }

    public Venda(int id, FormaPagamento forma) {
        this.id = id;
        this.formaPagamento = forma;
    }
}