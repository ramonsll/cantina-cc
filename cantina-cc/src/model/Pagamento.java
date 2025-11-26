package model;

public class Pagamento {
    private FormaPagamento formaPagamento;
    private boolean realizado;

    public boolean realizarPagamento(FormaPagamento forma) {
        this.formaPagamento = forma;
        this.realizado = true;
        return true;
    }

    public boolean isRealizado() { return realizado; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
}