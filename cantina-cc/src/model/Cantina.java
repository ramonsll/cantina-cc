package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cantina {

    private static Cantina instancia;

    private Cantina() { }

    public static Cantina getInstancia() {
        if (instancia == null) {
            instancia = new Cantina();
        }
        return instancia;
    }

    // "Banco de dados" em memória
    public Map<Integer, ClientePresencial> clientesPresenciais = new HashMap<>();
    public Map<Integer, ClienteOnline> clientesOnline = new HashMap<>();
    public Map<Integer, Fornecedor> fornecedores = new HashMap<>();
    public Map<Integer, Produto> produtos = new HashMap<>();
    public Map<Integer, Venda> vendas = new HashMap<>();

    // Estoque centralizado
    public Estoque estoque = new Estoque();

    // Reservas
    private List<Reserva> reservas = new ArrayList<>();
    private int limiteReservasPorCliente = 3;

    // -------------------------
    // PRODUTOS (CRUD mínimo)
    // -------------------------
    public void addProduto(Produto produto, int quantidadeInicial) {
        if (produto == null) throw new IllegalArgumentException("Produto nulo");
        produtos.put(produto.getId(), produto);
        if (quantidadeInicial > 0) {
            estoque.addProduto(produto, quantidadeInicial);
        }
    }

    public Produto localizarProduto(int codigo) {
        return produtos.get(codigo);
    }

    // -------------------------
    // ESTOQUE (incremento/decremento)
    // -------------------------
    public void reporEstoque(int codigoProduto, int quantidade) {
        Produto p = localizarProduto(codigoProduto);
        if (p == null) throw new RuntimeException("Produto não encontrado: " + codigoProduto);
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        estoque.addProduto(p, quantidade);
    }

    public int consultarQuantidadeEstoque(int codigoProduto) {
        return estoque.getQuantidadeDisponivel(codigoProduto);
    }

    // -------------------------
    // VENDAS
    // -------------------------
    public Venda criarVenda(int id, FormaPagamento forma) {
        if (vendas.containsKey(id)) throw new RuntimeException("Venda já existe com id: " + id);
        Venda v = new Venda(id, forma);
        vendas.put(id, v);
        return v;
    }

    public Venda localizarVenda(int id) {
        return vendas.get(id);
    }

    public void adicionarItemAVenda(int idVenda, int codigoProduto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");

        Venda venda = localizarVenda(idVenda);
        if (venda == null) throw new RuntimeException("Venda não encontrada: " + idVenda);

        Produto produto = localizarProduto(codigoProduto);
        if (produto == null) throw new RuntimeException("Produto não encontrado: " + codigoProduto);

        int disponivel = estoque.getQuantidadeDisponivel(codigoProduto);
        if (disponivel < quantidade) {
            throw new RuntimeException("Estoque insuficiente para produto " + codigoProduto +
                    ". Disponível: " + disponivel + ", pedido: " + quantidade);
        }

        ItemVenda item = new ItemVenda(produto, quantidade);
        venda.adicionarItem(item);

        // decrementa do estoque disponível (venda efetiva)
        estoque.removerProduto(produto, quantidade);
    }

    // -------------------------
    // RESERVAS
    // -------------------------
    public boolean reservarProduto(Cliente cliente, int idProduto, int quantidade) {
        if (cliente == null) throw new IllegalArgumentException("Cliente nulo");

        ItemEstoque item = estoque.getItem(idProduto);
        if (item == null) return false;

        if (quantidade <= 0 || quantidade > item.getQuantidadeDisponivel()) {
            return false;
        }

        long qtdReservasAtivas = reservas.stream()
                .filter(r -> r.getCliente().getId() == cliente.getId() && r.estaAtiva())
                .count();

        if (qtdReservasAtivas >= limiteReservasPorCliente) {
            return false;
        }

        // bloqueia no estoque (reserva)
        boolean reservado = estoque.reservarQuantidade(idProduto, quantidade);
        if (!reservado) return false;

        // cria reserva e adiciona à lista
        Reserva reserva = new Reserva(cliente, item, quantidade);
        reservas.add(reserva);

        return true;
    }

    public boolean cancelarReserva(int idReserva) {
        for (Reserva r : reservas) {
            if (r.getId() == idReserva && r.estaAtiva()) {
                r.cancelar();
                // libera estoque reservado
                estoque.liberarReserva(r.getItem().getProduto().getId(), r.getQuantidade());
                return true;
            }
        }
        return false;
    }

    public boolean confirmarRetirada(int idReserva) {
        for (Reserva r : reservas) {
            if (r.getId() == idReserva && r.estaAtiva()) {
                r.confirmar();
                // consome a reserva: diminui reservado e o total em estoque
                estoque.consumirReserva(r.getItem().getProduto().getId(), r.getQuantidade());
                return true;
            }
        }
        return false;
    }

    public List<Reserva> listarReservasCliente(int idCliente) {
        List<Reserva> out = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getCliente().getId() == idCliente) out.add(r);
        }
        return out;
    }

    public long reservasAtivasCliente(int idCliente) {
        return reservas.stream()
                .filter(r -> r.getCliente().getId() == idCliente && r.estaAtiva())
                .count();
    }

    // -------------------------
    // CLIENTES / FORNECEDORES (básico)
    // -------------------------
    public void addClientePresencial(ClientePresencial c) {
        if (c == null) throw new IllegalArgumentException("Cliente nulo");
        clientesPresenciais.put(c.getId(), c);
    }

    public ClientePresencial localizarClientePresencial(int id) {
        return clientesPresenciais.get(id);
    }

    public void addClienteOnline(ClienteOnline c) {
        if (c == null) throw new IllegalArgumentException("Cliente nulo");
        clientesOnline.put(c.getId(), c);
    }

    public ClienteOnline localizarClienteOnline(int id) {
        return clientesOnline.get(id);
    }

    public void addFornecedor(Fornecedor f) {
        if (f == null) throw new IllegalArgumentException("Fornecedor nulo");
        fornecedores.put(f.getId(), f);
    }

    public Fornecedor localizarFornecedor(int id) {
        return fornecedores.get(id);
    }
}
