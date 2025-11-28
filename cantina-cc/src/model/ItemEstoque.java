package model;

public class ItemEstoque {
    private Produto produto;
    private int quantidadeTotal;       // total físico no estoque
    private int quantidadeReservada;   // quantidade bloqueada por reservas

    public ItemEstoque(Produto produto, int quantidadeInicial) {
        if (produto == null) throw new IllegalArgumentException("Produto nulo");
        if (quantidadeInicial < 0) throw new IllegalArgumentException("Quantidade negativa");
        this.produto = produto;
        this.quantidadeTotal = quantidadeInicial;
        this.quantidadeReservada = 0;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public int getQuantidadeReservada() {
        return quantidadeReservada;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeTotal - quantidadeReservada;
    }

    // incrementa o total
    public void adicionarQuantidade(int q) {
        if (q <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        this.quantidadeTotal += q;
    }

    // remove do total (usado quando a mercadoria é retirada / vendida de fato)
    public void removerQuantidadeTotal(int q) {
        if (q <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        if (q > getQuantidadeDisponivel()) {
            // permitir remover somente aquilo que não está reservado
            throw new IllegalStateException("Quantidade insuficiente disponível para remoção");
        }
        this.quantidadeTotal -= q;
    }

    // marca quantidade como reservada (bloqueia). retorna true se ok
    public boolean reservar(int q) {
        if (q <= 0) return false;
        if (q > getQuantidadeDisponivel()) return false;
        this.quantidadeReservada += q;
        return true;
    }

    // libera quantidade reservada (cancelamento)
    public void liberarReserva(int q) {
        if (q <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        if (q > quantidadeReservada) throw new IllegalStateException("Tentativa de liberar mais do que reservado");
        this.quantidadeReservada -= q;
    }

    // confirma retirada: diminui total e diminui reservado (reserva consumida)
    public void consumirReserva(int q) {
        if (q <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        if (q > quantidadeReservada) throw new IllegalStateException("Quantidade maior que a reservada");
        // agora diminui reservado e total
        this.quantidadeReservada -= q;
        this.quantidadeTotal -= q;
    }

    // usado para debugging/visão
    @Override
    public String toString() {
        return String.format("ItemEstoque[produtoId=%d, total=%d, reservada=%d, disponivel=%d]",
                produto.getId(), quantidadeTotal, quantidadeReservada, getQuantidadeDisponivel());
    }
}
