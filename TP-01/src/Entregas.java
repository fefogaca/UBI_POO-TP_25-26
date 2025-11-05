import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Entregas {
    private List<Encomenda> encomendas;

    public Entregas() {
        this.encomendas = new ArrayList<>();
    }

    public void registarEncomenda(Cliente cliente, Map<Produto, Integer> produtos) {
        // Verificar stock antes de criar a encomenda
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            Produto p = entry.getKey();
            int quantidade = entry.getValue();
            if (p.getStock() < quantidade) {
                System.out.println("Erro: Stock insuficiente para o produto '" + p.getNome() + "'. Stock disponível: " + p.getStock() + ", Quantidade pedida: " + quantidade);
                return; // Cancela a encomenda
            }
        }

        // Se houver stock para todos, então cria a encomenda e baixa o stock
        Encomenda novaEncomenda = new Encomenda(cliente, produtos);
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            entry.getKey().removerStock(entry.getValue());
        }
        this.encomendas.add(novaEncomenda);
        System.out.println("Encomenda registada com sucesso! ID: " + novaEncomenda.getId());
    }

    public Encomenda acharEncomendaPorId(int id) {
        for (Encomenda e : encomendas) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public void atualizarEstado(int idEncomenda, Encomenda.EstadoEncomenda novoEstado) {
        Encomenda e = acharEncomendaPorId(idEncomenda);
        if (e != null) {
            e.setEstado(novoEstado);
            System.out.println("Estado da encomenda " + idEncomenda + " atualizado para: " + novoEstado.getDescricao());
        } else {
            System.out.println("Erro: Encomenda com ID " + idEncomenda + " não encontrada.");
        }
    }

    public void listarEncomendas() {
        if (encomendas.isEmpty()) {
            System.out.println("Nenhuma encomenda registada.");
            return;
        }
        for (Encomenda e : encomendas) {
            System.out.println(e.toString());
        }
    }
}
