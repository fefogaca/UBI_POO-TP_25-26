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


    // Adicionar cliente Nome/NIF/Morada
    public void adicionarCliente(String nome, String nif, Morada morada) {
        if (acharClienteNif(nif) != null) {
            System.out.println("Erro: Já existe um cliente com o NIF " + nif);
            return;
        }
        Cliente novoCliente = new Cliente(nome, nif, morada);
        this.clientes.add(novoCliente);
        System.out.println("Cliente adicionado com sucesso!");
    }

    // Achar cliente por NIF
    public Cliente acharClienteNif(String nif) {
        for (Cliente cliente : clientes) {
            if (cliente.getNif().equals(nif)) {
                return cliente;
            }
        }
        return null;
    }

    // Listar clientes
    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente registado.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente.toString());
        }
    }


    // Eliminar clientes
    public void eliminarCliente(String nif) {
        Cliente clienteEliminar = acharClienteNif(nif);
        if (clienteEliminar != null) {
            clientes.remove(clienteEliminar);
            System.out.println("Cliente eliminado com sucesso!");
        } else {
            System.out.println("Cliente " + nif + " não encontrado.");
        }
    }

    public void editarcliente(String nif, String NovoNome, String NovaRua, String NovaCidade, String NovocodPostal) {
        Cliente clienteparaeditar = acharClienteNif(nif);
        if (clienteparaeditar == null) {
            System.out.println("Cliente nao encontrado.");
            return;
        }

        // atualiza o nome
        if (NovoNome != null && !NovoNome.isEmpty()) {
            clienteparaeditar.setNome(NovoNome);
        }

        //atualiza a morada
        Morada moradaparaEditar = clienteparaeditar.getMorada();

        if (NovaRua != null && !NovaRua.isEmpty()) {
            moradaparaEditar.setRua(NovaRua);
        }

        // atualiza a cidade
        if (NovaCidade != null && !NovaCidade.isEmpty()) {
            moradaparaEditar.setCidade(NovaCidade);
        }
        if (NovocodPostal != null && !NovocodPostal.isEmpty()) {
            moradaparaEditar.setCodPostal(NovocodPostal);
        }
        System.out.println("cliente (NIF:"+ nif+ ") atualizado com exito!");
    }



    // Achar produtos por ID
    public Produto acharProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    // Adicionar produto
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

    // Eliminar os produtos por ID
    public void eliminarProduto(int id) {
        Produto produtoEliminar = acharProdutoPorId(id); // Usa o novo helper

        if (produtoEliminar != null) {
            produtos.remove(produtoEliminar);
            System.out.println("Produto (ID: " + id + ") eliminado com sucesso!");
        } else {
            System.out.println("Produto com ID " + id + " não encontrado.");
        }
    }

    // Listar os produtos por ID
    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto registado.");
            return;
        }
        for (Produto produto : produtos) {
            System.out.println(produto.toString());
        }
    }

    // Verificação se há clientes
    public boolean temClientes() {
        return !clientes.isEmpty();
    }

    // Verificação se há produtos
    public boolean temProdutos() {
        return !produtos.isEmpty();
    }
}