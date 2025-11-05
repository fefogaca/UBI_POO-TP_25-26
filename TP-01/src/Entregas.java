import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Entregas {
    private List<Encomenda> encomendas;

    public Entregas() {
        this.encomendas = new ArrayList<>();
    }

    public void registarEncomenda(Cliente cliente, Map<Produto, Integer> produtos) {
        // Se houver stock para todos, então cria a encomenda e baixa o stock
        Encomenda novaEncomenda = new Encomenda(cliente, produtos);
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
