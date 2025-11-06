
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Farmacia {
    private List<Produto> produtos;
    private List<Cliente> clientes;
    private List<Encomenda> encomendas;

    public Farmacia() {
        this.produtos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.encomendas = new ArrayList<>();
    }

    // Adiciona um novo cliente à nossa lista de clientes.
    public void adicionarCliente(String nome, String nif, Morada morada) {
        if (acharClienteNif(nif) != null) {
            System.out.println("Erro: Já existe um cliente com o NIF " + nif);
            return;
        }
        Cliente novoCliente = new Cliente(nome, nif, morada);
        this.clientes.add(novoCliente);
        System.out.println("Cliente adicionado com sucesso!");
    }

    // Procura por um cliente na nossa lista através do seu NIF.
    public Cliente acharClienteNif(String nif) {
        for (Cliente cliente : clientes) {
            if (cliente.getNif().equals(nif)) {
                return cliente;
            }
        }
        return null;
    }

    // Mostra todos os clientes que temos registados.
    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente registado.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente.toString());
        }
    }

    // Remove um cliente da nossa lista através do seu NIF.
    public void eliminarCliente(String nif) {
        Cliente clienteEliminar = acharClienteNif(nif);
        if (clienteEliminar != null) {
            clientes.remove(clienteEliminar);
            System.out.println("Cliente eliminado com sucesso!");
        } else {
            System.out.println("Cliente " + nif + " não encontrado.");
        }
    }


    // Procura por um produto na nossa lista através do seu ID.
    public Produto acharProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    // Adiciona um novo produto à nossa lista de produtos.
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

    // Remove um produto da nossa lista através do seu ID.
    public void eliminarProduto(int id) {
        Produto produtoEliminar = acharProdutoPorId(id);

        if (produtoEliminar != null) {
            produtos.remove(produtoEliminar);
            System.out.println("Produto (ID: " + id + ") eliminado com sucesso!");
        } else {
            System.out.print("Produto com ID " + id + " não encontrado.\n");
        }
    }

    // Mostra todos os produtos que temos registados.
    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto registado.");
            return;
        }
        for (Produto produto : produtos) {
            System.out.println(produto.toString());
        }
    }

    // Permite editar a informação de um produto.
    public void editarProduto(Scanner scanner) {
        if (!temProdutos()) {
            System.out.println("Não há nenhum produto registado.");
            return;
        }

        listarProdutos();
        System.out.print("\nID do produto a editar: ");
        int idProduto = scanner.nextInt();
        scanner.nextLine();
        Produto produto = acharProdutoPorId(idProduto);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int opcaoEditar;
        do {
            System.out.println("\n--- O que deseja editar? ---");
            System.out.println("1. Editar Nome");
            System.out.println("2. Editar Preço");
            System.out.println("3. Adicionar Stock");
            System.out.println("4. Remover Stock");
            System.out.println("0. Voltar ao Menu de Produtos\n");
            System.out.print("Escolha uma opção: ");

            opcaoEditar = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoEditar) {
                case 1:
                    System.out.println();
                    System.out.print("Insira o novo nome (" + produto.getNome() + "): ");
                    String novoNome = scanner.nextLine();
                    produto.setNome(novoNome);
                    System.out.println("Nome atualizado com sucesso!");
                    break;
                case 2:
                    System.out.println();
                    System.out.print("Novo preço (" + produto.getPreco() + "€): ");
                    double novoPreco = scanner.nextDouble();
                    scanner.nextLine();
                    produto.setPreco(novoPreco);
                    System.out.println("Preço atualizado com sucesso!");
                    break;
                case 3:
                    System.out.println();
                    System.out.print("Quantidade a adicionar (Stock atual: " + produto.getStock() + "): ");
                    int quantidadeAdicionar = scanner.nextInt();
                    scanner.nextLine();
                    produto.adicionarStock(quantidadeAdicionar);
                    System.out.println("Stock atualizado com sucesso! Novo stock: " + produto.getStock());
                    break;
                case 4:
                    System.out.println();
                    System.out.print("Quantidade a remover (Stock atual: " + produto.getStock() + "): ");
                    int quantidadeRemover = scanner.nextInt();
                    scanner.nextLine();
                    produto.removerStock(quantidadeRemover);
                    System.out.println("Stock atualizado com sucesso! Novo stock: " + produto.getStock());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcaoEditar != 0);
    }

    // Cria uma nova encomenda.
    public void novaEncomenda(Scanner scanner) {
        if (!temClientes()) {
            System.out.println("Não existem clientes registados. Por favor, adicione um cliente primeiro.");
            return;
        }

        if (!temProdutos()) {
            System.out.println("Não existem produtos registados. Por favor, adicione um produto primeiro.");
            return;
        }

        System.out.print("\nNIF do cliente: ");
        String nif = scanner.nextLine();
        System.out.println();
        Cliente cliente = acharClienteNif(nif);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        Map<Produto, Integer> produtosEncomenda = new HashMap<>();
        int idProduto;

        while (true) {
            listarProdutos();
            System.out.print("\nID do produto a adicionar (ou 0 para terminar): ");
            idProduto = scanner.nextInt();
            scanner.nextLine();

            if (idProduto == 0) {
                break;
            }

            Produto produto = acharProdutoPorId(idProduto);
            if (produto != null) {
                System.out.print("Quantidade: ");
                int quantidade = scanner.nextInt();
                scanner.nextLine();

                if (quantidade <= 0) {
                    System.out.println("Quantidade inválida.");
                    continue;
                }

                if (produto.getStock() >= quantidade) {
                    produtosEncomenda.put(produto, produtosEncomenda.getOrDefault(produto, 0) + quantidade);
                    produto.removerStock(quantidade);
                    System.out.println("Produto adicionado à encomenda.\n");
                } else {
                    System.out.println("Stock insuficiente. Stock disponível: " + produto.getStock());
                }
            } else {
                System.out.println("Produto não encontrado.");
            }
        }

        if (!produtosEncomenda.isEmpty()) {
            registarEncomenda(cliente, produtosEncomenda);
        } else {
            System.out.println("Nenhum produto adicionado. Encomenda cancelada.");
        }
    }

    // Regista uma nova encomenda na nossa lista de encomendas.
    public void registarEncomenda(Cliente cliente, Map<Produto, Integer> produtos) {
        Encomenda novaEncomenda = new Encomenda(cliente, produtos);
        this.encomendas.add(novaEncomenda);
        System.out.println("Encomenda registada com sucesso! ID: " + novaEncomenda.getId());
    }

    // Procura por uma encomenda na nossa lista através do seu ID.
    public Encomenda acharEncomendaPorId(int id) {
        for (Encomenda e : encomendas) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    // Atualiza o estado de uma encomenda.
    public void atualizarEstado(int idEncomenda, Encomenda.EstadoEncomenda novoEstado) {
        Encomenda e = acharEncomendaPorId(idEncomenda);
        if (e != null) {
            e.setEstado(novoEstado);
            System.out.println("Estado da encomenda " + idEncomenda + " atualizado para: " + novoEstado.getDescricao());
        } else {
            System.out.println("Erro: Encomenda com ID " + idEncomenda + " não encontrada.");
        }
    }

    // Mostra todas as encomendas que temos registadas.
    public void listarEncomendas() {
        if (encomendas.isEmpty()) {
            System.out.println();
            System.out.println("Nenhuma encomenda registada.");
            return;
        }
        for (Encomenda e : encomendas) {
            System.out.print(e.toString());
        }
    }

    // Permite editar a informação de um cliente.
    public void editarCliente(Scanner scanner) {
        if (!temClientes()) {
            System.out.println("Não há nenhum cliente registado");
            return;
        }

        System.out.print("Nif do cliente a editar: ");
        String nif = scanner.nextLine();
        Cliente cliente = acharClienteNif(nif);

        if (cliente == null) {
            System.out.print("Cliente não encontrado");
            return;
        }

        int opcaoeditar;
        do {
            System.out.println("\n--- O que deseja editar? ---");
            System.out.println("1. Editar Nome");
            System.out.println("2. Editar Rua");
            System.out.println("3. Editar Cidade");
            System.out.println("4. Editar Código Postal");
            System.out.println("0. Voltar ao Menu de Clientes\n");
            System.out.print("Escolha uma opção: ");
            opcaoeditar = scanner.nextInt();
            scanner.nextLine();
            Morada moradaAtual = cliente.getMorada();

            switch (opcaoeditar) {
                case 1:
                    System.out.print("\nInsira o novo nome (" + cliente.getNome() + "): ");
                    String novoNome = scanner.nextLine();
                    cliente.setNome(novoNome);
                    System.out.println("Nome atualizado com sucesso!");
                    break;
                case 2:
                    System.out.print("\nNova rua (" + moradaAtual.getRua() + "): ");
                    String novaRua = scanner.nextLine();
                    moradaAtual.setRua(novaRua);
                    System.out.println("Rua atualizada com sucesso!");
                    break;
                case 3:
                    System.out.print("\nNova cidade (" + moradaAtual.getCidade() + "): ");
                    String novaCidade = scanner.nextLine();
                    moradaAtual.setCidade(novaCidade);
                    System.out.println("Cidade atualizada com sucesso!");
                    break;
                case 4:
                    System.out.print("\nNovo Código postal (" + moradaAtual.getCodPostal() + "): ");
                    String novaCodPostal = scanner.nextLine();
                    moradaAtual.setCodPostal(novaCodPostal);
                    System.out.println("Código postal atualizado com sucesso!");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcaoeditar != 0);
    }

    // Atualiza o estado de uma encomenda.
    public void atualizarEstadoEncomenda(Scanner scanner) {
        System.out.println();
        System.out.print("ID da encomenda a atualizar: ");
        int idEncomenda = scanner.nextInt();
        scanner.nextLine();

        System.out.println();
        Encomenda encomenda = acharEncomendaPorId(idEncomenda);
        if (encomenda == null) {
            System.out.println("Encomenda não encontrada.");
            return;
        }

        System.out.println("Selecione o novo estado:");
        int i = 1;
        for (Encomenda.EstadoEncomenda estado : Encomenda.EstadoEncomenda.values()) {
            System.out.println(i + ". " + estado.getDescricao());
            i++;
        }
        System.out.print("\nEscolha uma opção: ");
        int opcaoEstado = scanner.nextInt();
        scanner.nextLine();

        if (opcaoEstado > 0 && opcaoEstado <= Encomenda.EstadoEncomenda.values().length && opcaoEstado != encomenda.getEstado().ordinal() + 1) {
            Encomenda.EstadoEncomenda novoEstado = Encomenda.EstadoEncomenda.values()[opcaoEstado - 1];
            atualizarEstado(idEncomenda, novoEstado);
        } else {
            System.out.println("Opção inválida.");
        }
    }

    public boolean temClientes() {
        return !clientes.isEmpty();
    }
    public boolean temProdutos() {
        return !produtos.isEmpty();
    }
}

