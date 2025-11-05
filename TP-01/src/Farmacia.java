import java.util.ArrayList;
import java.util.List;

public class Farmacia {
    private List<Produto> produtos;
    private List<Cliente> clientes;
    private Entregas entregas;

    public Farmacia() {
        this.produtos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.entregas = new Entregas();
    }

    public Entregas getEntregas() {
        return entregas;
    }

    public void adicionarCliente(String nome, String nif, Morada morada) {
        if (acharClienteNif(nif) != null) {
            System.out.println("Erro: Já existe um cliente com o NIF " + nif);
            return;
        }
        Cliente novoCliente = new Cliente(nome, nif, morada);
        this.clientes.add(novoCliente);
        System.out.println("Cliente adicionado com sucesso!");
    }

    public Cliente acharClienteNif(String nif) {
        for (Cliente cliente : clientes) {
            if (cliente.getNif().equals(nif)) {
                return cliente;
            }
        }
        return null;
    }

    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente registado.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente.toString());
        }
    }

    public void eliminarCliente(String nif) {
        Cliente clienteEliminar = acharClienteNif(nif);
        if (clienteEliminar != null) {
            clientes.remove(clienteEliminar);
            System.out.println("Cliente eliminado com sucesso!");
        } else {
            System.out.println("Cliente " + nif + " não encontrado.");
        }
    }

    public Produto acharProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    public void adicionarProduto(String nome, double preco, int stockInicial) {
        if (preco < 0) {
            System.out.println("Erro: O preço não pode ser negativo!");
            return;
        }
        if (stockInicial < 0) {
            System.out.println("Erro: O stock não pode ser negativo!");
            return;
        }

        Produto novoProduto = new Produto(nome, preco, stockInicial);
        this.produtos.add(novoProduto);
        System.out.println("Produto (ID: " + novoProduto.getId() + ") adicionado com sucesso!");
    }

    public void eliminarProduto(int id) {
        Produto produtoEliminar = acharProdutoPorId(id);

        if (produtoEliminar != null) {
            produtos.remove(produtoEliminar);
            System.out.println("Produto (ID: " + id + ") eliminado com sucesso!");
        } else {
            System.out.print("Produto com ID " + id + " não encontrado.\n");
        }
    }

    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto registado.");
            return;
        }
        for (Produto produto : produtos) {
            System.out.println(produto.toString());
        }
    }

    public boolean temClientes() {
        return !clientes.isEmpty();
    }
    public boolean temProdutos() {
        return !produtos.isEmpty();
    }
}