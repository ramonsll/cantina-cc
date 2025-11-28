package model;

import java.util.HashMap;
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

    // Estoque centralizado (utiliza a sua classe Estoque existente)
    public Estoque estoque = new Estoque();

    // -------------------------
    // PRODUTOS (CRUD mínimo)
    // -------------------------
    public void addProduto(Produto produto) {
        if (produto == null) throw new IllegalArgumentException("Produto nulo");
        produtos.put(produto.getId(), produto);
    }

    public Produto localizarProduto(int codigo) {
        return produtos.get(codigo);
    }

    // -------------------------
    // ESTOQUE (incremento/decremento)
    // -------------------------
    /**
     * Repor estoque: incrementa a quantidade do produto.
     * Usa a classe Estoque (que internamente faz get/put).
     */
    public void reporEstoque(int codigoProduto, int quantidade) {
        Produto p = localizarProduto(codigoProduto);
        if (p == null) throw new RuntimeException("Produto não encontrado: " + codigoProduto);
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        estoque.addProduto(p, quantidade); // responsável por somar à quantidade atual
    }

    /**
     * Retorna a quantidade atual do produto no estoque.
     */
    public int consultarQuantidadeEstoque(int codigoProduto) {
        Produto p = localizarProduto(codigoProduto);
        if (p == null) return 0;
        return estoque.getQuantidade(p);
    }

    // -------------------------
    // VENDAS
    // -------------------------
    /**
     * Cria uma venda e registra no mapa.
     * Você controla o id externamente (não gera aqui).
     */
    public Venda criarVenda(int id, FormaPagamento forma) {
        if (vendas.containsKey(id)) throw new RuntimeException("Venda já existe com id: " + id);
        Venda v = new Venda(id, forma);
        vendas.put(id, v);
        return v;
    }

    public Venda localizarVenda(int id) {
        return vendas.get(id);
    }

    /**
     * Adiciona um item à venda VALIDANDO estoque antes.
     * Se houver estoque suficiente, decrementa automaticamente.
     */
    public void adicionarItemAVenda(int idVenda, int codigoProduto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");

        Venda venda = localizarVenda(idVenda);
        if (venda == null) throw new RuntimeException("Venda não encontrada: " + idVenda);

        Produto produto = localizarProduto(codigoProduto);
        if (produto == null) throw new RuntimeException("Produto não encontrado: " + codigoProduto);

        int disponivel = estoque.getQuantidade(produto);
        if (disponivel < quantidade) {
            throw new RuntimeException("Estoque insuficiente para produto " + codigoProduto +
                                       ". Disponível: " + disponivel + ", pedido: " + quantidade);
        }

        // cria o ItemVenda (usa seu construtor existente) e adiciona na venda
        ItemVenda item = new ItemVenda(produto, quantidade);
        venda.adicionarItem(item);

        // decrementa o estoque
        estoque.removerProduto(produto, quantidade);
    }

    // parte das R3ESERVAS 

    private List<Reserva> reservas = new ArrayList<>();
    private int limiteReservasPorCliente = 3; 

    // ---------------------------------------------------
// MÉTODO PRINCIPAL DE NEGÓCIO: RESERVAR PRODUTOS
// ---------------------------------------------------
public boolean reservarProduto(Cliente cliente, int idProduto, int quantidade) {

    ItemEstoque item = estoque.getItem(idProduto);
    if (item == null) return false;

    if (quantidade <= 0 || quantidade > item.getQuantidade()) {
        return false;
    }

    // Limite de reservas por cliente
    long qtdReservasAtivas = reservas.stream()
        .filter(r -> r.getCliente().getId() == cliente.getId() && r.estaAtiva())
        .count();

    if (qtdReservasAtivas >= limiteReservasPorCliente) {
        return false;
    }

    // Cria a reserva
    Reserva reserva = new Reserva(cliente, item, quantidade);
    reservas.add(reserva);

    // Diminui do estoque como "bloqueado"
    item.setQuantidade(item.getQuantidade() - quantidade);

    return true;
}

    public boolean cancelarReserva(int idReserva) {
    for (Reserva r : reservas) {
        if (r.getId() == idReserva && r.estaAtiva()) {

            r.cancelar();

            // devolve ao estoque
            ItemEstoque item = r.getItem();
            item.setQuantidade(item.getQuantidade() + r.getQuantidade());

            return true;
        }
    }
    return false;
}

    public boolean confirmarRetirada(int idReserva) {
    for (Reserva r : reservas) {
        if (r.getId() == idReserva && r.estaAtiva()) {

            r.confirmar();
            // nada é devolvido ao estoque → o cliente já pegou

            return true;
        }
    }
    return false;
}

    public List<Reserva> listarReservasCliente(int idCliente) {
    return reservas.stream()
        .filter(r -> r.getCliente().getId() == idCliente)
        .toList();
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

