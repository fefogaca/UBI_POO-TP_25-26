package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Representa uma encomenda feita por um Cliente.
 * <p>
 * Uma encomenda associa um cliente a um conjunto de produtos com quantidades específicas,
 * mantém um estado (processamento, pago, enviado, entregue) e calcula o preço total.
 * Armazena também a data e hora de criação para permitir análises temporais e estatísticas.
 * Demonstra o uso de Enums, sobrecarga de construtores e composição.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class Encomenda implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Enum que representa os possíveis estados de uma encomenda.
     */
    public enum EstadoEncomenda {
        /** Encomenda em processamento. */
        EM_PROCESSAMENTO("Em processamento"),
        /** Encomenda paga. */
        PAGO("Pago"),
        /** Encomenda enviada. */
        ENVIADO("Enviado"),
        /** Encomenda entregue. */
        ENTREGUE("Entregue");

        private final String descricao;

        /**
         * Construtor do enum EstadoEncomenda.
         *
         * @param descricao A descrição textual do estado.
         */
        EstadoEncomenda(String descricao) { this.descricao = descricao; }

        /**
         * Obtém a descrição textual do estado.
         *
         * @return A descrição do estado.
         */
        public String getDescricao() { return descricao; }
    }

    private static int proximoId = 1;
    private int id;
    private Cliente cliente;
    private Map<Produto, Integer> produtos;
    private double precoTotal;
    private EstadoEncomenda estado;
    private LocalDateTime dataHora;

    /**
     * Construtor principal para criar uma nova encomenda.
     * A data e hora são automaticamente definidas como o momento atual.
     * @param cliente O cliente que fez a encomenda.
     * @param produtos O mapa de produtos e respetivas quantidades.
     */
    public Encomenda(Cliente cliente, Map<Produto, Integer> produtos) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.produtos = produtos;
        this.estado = EstadoEncomenda.EM_PROCESSAMENTO;
        this.precoTotal = calcularPrecoTotal();
        this.dataHora = LocalDateTime.now();
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

    /**
     * Calcula o preço total da encomenda com base nos produtos e quantidades.
     *
     * @return O preço total calculado.
     */
    private double calcularPrecoTotal() {
        double total = 0;
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            total += entry.getKey().getPreco() * entry.getValue();
        }
        return total;
    }

    /**
     * Obtém o ID único da encomenda.
     *
     * @return O ID da encomenda.
     */
    public int getId() { return id; }

    /**
     * Obtém o cliente que fez a encomenda.
     *
     * @return O cliente associado à encomenda.
     */
    public Cliente getCliente() { return cliente; }

    /**
     * Obtém o mapa de produtos e quantidades da encomenda.
     *
     * @return Mapa onde a chave é o produto e o valor é a quantidade.
     */
    public Map<Produto, Integer> getProdutos() { return produtos; }

    /**
     * Obtém o preço total da encomenda.
     *
     * @return O preço total calculado.
     */
    public double getPrecoTotal() { return precoTotal; }

    /**
     * Obtém o estado atual da encomenda.
     *
     * @return O estado da encomenda.
     */
    public EstadoEncomenda getEstado() { return estado; }

    /**
     * Obtém a quantidade total de itens incluídos na encomenda.
     *
     * @return soma das quantidades de todos os produtos associados
     */
    public int getQuantidadeTotal() {
        return produtos.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Obtém a data e hora em que a encomenda foi criada.
     *
     * @return A data e hora da encomenda.
     */
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /**
     * Define a data e hora da encomenda.
     * Útil ao carregar dados persistidos.
     *
     * @param dataHora A nova data e hora.
     */
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * Define o estado da encomenda.
     *
     * @param estado O novo estado da encomenda.
     */
    public void setEstado(EstadoEncomenda estado) { this.estado = estado; }

    /**
     * Define o preço total da encomenda.
     *
     * @param precoTotal O novo preço total.
     */
    public void setPrecoTotal(double precoTotal) { this.precoTotal = precoTotal; }

    /**
     * Define o próximo ID a ser usado para uma nova encomenda.
     * Útil ao carregar dados de um ficheiro.
     *
     * @param id O próximo ID.
     */
    public static void setProximoId(int id) {
        proximoId = id;
    }

    /**
     * Retorna uma representação textual da encomenda.
     *
     * @return String formatada com ID, nome do cliente e estado.
     */
    @Override
    public String toString() {
        return "ID: " + id + " | Cliente: " + cliente.getNome() + " | Estado: " + estado.getDescricao();
    }
}