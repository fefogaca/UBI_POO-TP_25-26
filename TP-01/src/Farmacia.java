import java.util.ArrayList;
import java.util.List;

public class Farmacia {
    // Listas de Objetos
    private List<Produto> produtos;
    private List<Cliente> clientes;

    public Farmacia() {
        this.produtos = new ArrayList<>();
        this.clientes = new ArrayList<>();
    }

    // --- Métodos de Gestão de Clientes (Sem alterações) ---
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

    public void listarClientesNN() {
        if(clientes.isEmpty()){
            System.out.println("Nenhum cliente registado.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println("Cliente: " + cliente.getNome() + " | NIF: " + cliente.getNif());
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

    // --- Métodos de Gestão de Produtos ---

    // Achar produto pelo nome dele
    public Produto acharProdutoNome(String nome) {
        for (Produto produto : produtos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                return produto;
            }
        }
        return null;
    }

    // --- MUDANÇA 1: Novo método para achar produto pelo ID ---
    public Produto acharProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null; // Não encontrou
    }

    // Achar produto pelo índice na lista
    public Produto acharProdutoPorIndice(int indice) {
        if (indice >= 0 && indice < produtos.size()) {
            return produtos.get(indice);
        }
        return null;
    }


    public void adicionarProduto(String nome, double preco, int stockInicial) {
        // Validações
        if (preco < 0) {
            System.out.println("Erro: O preço não pode ser negativo!");
            return;
        }
        if (stockInicial < 0) {
            System.out.println("Erro: O stock não pode ser negativo!");
            return;
        }

        // Esta linha agora funciona, pois o construtor de Produto
        // só pede 3 argumentos e trata do ID automaticamente.
        Produto novoProduto = new Produto(nome, preco, stockInicial);
        this.produtos.add(novoProduto);
        System.out.println("Produto (ID: " + novoProduto.getId() + ") adicionado com sucesso!");
    }

    // Metodo para eliminar produtos pelo nome (existente)
    public void eliminarProduto(String nome) {
        Produto produtoEliminar = acharProdutoNome(nome);

        if (produtoEliminar != null) {
            produtos.remove(produtoEliminar);
            System.out.println("Produto eliminado com sucesso!");
        } else {
            System.out.println("Produto " + nome + " não encontrado.");
        }
    }

    // --- MUDANÇA 2: Novo método para eliminar produtos pelo ID ---
    // Isto é "sobrecarga de método" (method overloading),
    // pois temos dois métodos 'eliminarProduto' mas com argumentos diferentes.
    public void eliminarProduto(int id) {
        Produto produtoEliminar = acharProdutoPorId(id); // Usa o novo helper

        if (produtoEliminar != null) {
            produtos.remove(produtoEliminar);
            System.out.println("Produto (ID: " + id + ") eliminado com sucesso!");
        } else {
            System.out.println("Produto com ID " + id + " não encontrado.");
        }
    }

    // --- MUDANÇA 3: Listar produtos agora mostra o ID ---
    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto registado.");
            return;
        }

        // Removemos o 'indice' manual, pois o toString() do Produto
        // agora inclui o ID real do produto.
        for (Produto produto : produtos) {
            System.out.println(produto.toString());
        }
    }
}