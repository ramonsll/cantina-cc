import java.time.LocalDateTime;

public class Reserva {
    private static int contador = 1;

    private int id;
    private Cliente cliente;
    private ItemEstoque item;
    private int quantidade;
    private LocalDateTime dataCriacao;
    private StatusReserva status;

    public Reserva(Cliente cliente, ItemEstoque item, int quantidade) {
        this.id = contador++;
        this.cliente = cliente;
        this.item = item;
        this.quantidade = quantidade;
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusReserva.ATIVA;
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public ItemEstoque getItem() { return item; }
    public int getQuantidade() { return quantidade; }
    public StatusReserva getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public boolean estaAtiva() {
        return status == StatusReserva.ATIVA;
    }

    public void cancelar() {
        if (estaAtiva()) status = StatusReserva.CANCELADA;
    }

    public void confirmar() {
        if (estaAtiva()) status = StatusReserva.CONFIRMADA;
    }
}

public enum StatusReserva {
    ATIVA,
    CANCELADA,
    CONFIRMADA
}
