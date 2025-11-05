import java.util.List;
import java.util.Map;

public class Encomenda {

    public enum EstadoEncomenda {
        EM_PROCESSAMENTO("Em processamento"),
        PAGO("Pago"),
        ENVIADO("Enviado"),
        ENTREGUE("Entregue");

        private final String descricao;

        EstadoEncomenda(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    private static int proximoId = 1;
    private int id;
    private Cliente cliente;
    private Map<Produto, Integer> produtos;
    private double precoTotal;
    private EstadoEncomenda estado;

    public Encomenda(Cliente cliente, Map<Produto, Integer> produtos) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.produtos = produtos;
        this.estado = EstadoEncomenda.EM_PROCESSAMENTO;
        this.precoTotal = calcularPrecoTotal();
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

    // Setter para o estado
    public void setEstado(EstadoEncomenda estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Encomenda ID: ").append(id)
          .append(" | Cliente: ").append(cliente.getNome())
          .append(" | Estado: ").append(estado.getDescricao())
          .append(" | Preço Total: ").append(String.format("%.2f", precoTotal)).append("€\n");
        sb.append("  Produtos:\n");
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            sb.append("    - ").append(entry.getKey().getNome())
              .append(" | Quantidade: ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
