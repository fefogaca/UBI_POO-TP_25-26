package modelo;

import java.io.Serializable;
import java.util.Map;

/**
 * Representa uma encomenda feita por um Cliente.
 */
public class Encomenda implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum EstadoEncomenda {
        EM_PROCESSAMENTO("Em processamento"),
        PAGO("Pago"),
        ENVIADO("Enviado"),
        ENTREGUE("Entregue");

        private final String descricao;
        EstadoEncomenda(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }
    }

    private static int proximoId = 1;
    private int id;
    private Cliente cliente;
    private Map<Produto, Integer> produtos;
    private double precoTotal;
    private EstadoEncomenda estado;

    /**
     * Construtor principal para criar uma nova encomenda.
     * @param cliente O cliente que fez a encomenda.
     * @param produtos O mapa de produtos e respetivas quantidades.
     */
    public Encomenda(Cliente cliente, Map<Produto, Integer> produtos) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.produtos = produtos;
        this.estado = EstadoEncomenda.EM_PROCESSAMENTO;
        this.precoTotal = calcularPrecoTotal();
    }

    /**
     * Construtor sobrecarregado (Overloading) que permite definir um estado inicial para a encomenda.
     * @param cliente O cliente que fez a encomenda.
     * @param produtos O mapa de produtos e respetivas quantidades.
     * @param estadoInicial O estado inicial da encomenda.
     */
    public Encomenda(Cliente cliente, Map<Produto, Integer> produtos, EstadoEncomenda estadoInicial) {
        this(cliente, produtos); // Chama o construtor principal
        this.estado = estadoInicial;
    }

    private double calcularPrecoTotal() {
        double total = 0;
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            total += entry.getKey().getPreco() * entry.getValue();
        }
        return total;
    }

    // Getters
    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Map<Produto, Integer> getProdutos() { return produtos; }
    public double getPrecoTotal() { return precoTotal; }
    public EstadoEncomenda getEstado() { return estado; }

    // Setters
    public void setEstado(EstadoEncomenda estado) { this.estado = estado; }
    public void setPrecoTotal(double precoTotal) { this.precoTotal = precoTotal; }

    public static void setProximoId(int id) {
        proximoId = id;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Cliente: " + cliente.getNome() + " | Estado: " + estado.getDescricao();
    }
}