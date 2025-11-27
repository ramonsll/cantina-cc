public class ItemEstoque {
    private String nome;
    private double preco;
    private int quantidadeTotal;
    private int quantidadeReservada;

    public ItemEstoque(String nome, double preco, int quantidadeTotal) {
        this.nome = nome;
        this.preco = preco;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeReservada = 0;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    // Quantidade realmente disponível para reserva/venda
    public int getQuantidadeDisponivel() {
        return quantidadeTotal - quantidadeReservada;
    }

    public void adicionarQuantidade(int qnt) {
        quantidadeTotal += qnt;
    }

    public void removerQuantidade(int qnt) {
        if (qnt <= getQuantidadeDisponivel()) {
            quantidadeTotal -= qnt;
        }
    }

    public void reservar(int qnt) {
        quantidadeReservada += qnt;
    }

    public void cancelarReserva(int qnt) {
        quantidadeReservada -= qnt;
        if (quantidadeReservada < 0) quantidadeReservada = 0;
    }
}
