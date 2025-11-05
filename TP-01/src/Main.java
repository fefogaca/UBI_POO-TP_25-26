import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Farmacia farmacia = new Farmacia();
    private List<Cliente> clientes;

    // Menu principal para escolha de opções
    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n--- BEM-VINDO À FARMÁCIA UBI ---");
            System.out.println("1. Gestão de Clientes");
            System.out.println("2. Gestão de Produtos");
            System.out.println("3. Gestão de Encomendas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

;           // Switch case para escolher as opções
            switch (opcao) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuProdutos();
                    break;
                case 3:
                    menuEncomendas();
                    break;
                case 0:
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    // Menu de escolhas na Gestão de Clientes
    private static void menuClientes() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Clientes ---");
            System.out.println("1. Adicionar Cliente");
            System.out.println("2. Eliminar Cliente");
            System.out.println("3. Listar Clientes");
            System.out.println("4. Editar Clientes");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            // Switch case para escolher as opções
            switch (opcao) {
                case 1:
                    adicionarCliente();
                    break;
                case 2:
                    System.out.print("\n");
                    farmacia.listarClientes();
                    System.out.print("\n");
                    System.out.print("NIF do cliente a eliminar: ");
                    String nif = scanner.nextLine();
                    farmacia.eliminarCliente(nif);
                    break;
                case 3:
                    farmacia.listarClientes();
                    break;
                case 4:
                    editarCliente();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    // Recolha de informações do Cliente
    private static void adicionarCliente() {
        System.out.print("Nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("NIF do cliente: ");
        String nif = scanner.nextLine();

        System.out.println("--- Morada ---");
        System.out.print("Rua: ");
        String rua = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Código Postal: ");
        String codPostal = scanner.nextLine();

        Morada morada = new Morada(rua, cidade, codPostal);
        farmacia.adicionarCliente(nome, nif, morada);
    }

    private static void editarCliente() {
        if (!farmacia.temClientes()) {
            System.out.println("Não há nenhum cliente registado");
            return;
        }

        System.out.print("Nif do cliente a editar.");
        String nif = scanner.nextLine();
        Cliente cliente = farmacia.acharClienteNif(nif);


        if (cliente == null) {
            System.out.print("Cliente não encontrado");
            return;
        }
        private static void menuEditar() {
            int opcaoeditar;
            do {
                System.out.println("\n--- O que deseja editar? ---");
                System.out.println("1. Editar Nome");
                System.out.println("2. Editar Morada");
                System.out.println("3. Editar Rua");
                System.out.println("4. Editar Clientes");
                System.out.println("0. Voltar ao Menu Principal");
                System.out.print("Escolha uma opção: ");

                opcaoeditar = scanner.nextInt();
                scanner.nextLine();

                // Switch case para escolher as opções
                switch (opcaoeditar) {
                    case 1:
                        System.out.print("insira o novo nome(" + cliente.getNome() + ")");
                        String novoNome = scanner.nextLine();
                        break;
                    case 2:
                        System.out.print("\n");
                        farmacia.listarClientes();
                        System.out.print("\n");
                        System.out.print("NIF do cliente a eliminar: ");
                        String nif = scanner.nextLine();
                        farmacia.eliminarCliente(nif);
                        break;
                    case 3:
                        farmacia.listarClientes();
                        break;
                    case 4:
                        editarCliente();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } while (opcaoeditar != 0);
        }


        System.out.println("a editar o cliente" + cliente.getNome());
        System.out.println("deixe em branco se nao quiser alterar o nome");

        // pedir o novo nome
        System.out.print("insira o novo nome(" + cliente.getNome() + ")");
        String novoNome = scanner.nextLine();

        System.out.println(" Nova Morada");
        Morada moradaAtual = cliente.getMorada();

        System.out.print("nova rua(" + moradaAtual.getRua() + ")");
        String novaRua = scanner.nextLine();

        System.out.print("nova cidade(" + moradaAtual.getCidade() + ")");
        String novaCidade = scanner.nextLine();

        System.out.print("Novo codigo postal(" + moradaAtual.getCodPostal() + ")");
        String novaCodPostal = scanner.nextLine();

        farmacia.editarcliente(nif, novoNome, novaRua, novaCidade, novaCodPostal);
    }

    // Menu para Gestão de Produtos
    private static void menuProdutos() {
        int opcao ;
        do {
            System.out.println("\n--- Gestão de Produtos ---");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Eliminar Produto");
            System.out.println("3. Listar Produtos");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            // Switch case para escolher as opções
            switch (opcao) {
                case 1:
                    // Chama o método helper que definimos abaixo
                    adicionarProduto();
                    break;
                case 2:
                    farmacia.listarProdutos();
                    System.out.print("ID do produto a eliminar: ");
                    int idParaEliminar = scanner.nextInt();
                    scanner.nextLine();
                    farmacia.eliminarProduto(idParaEliminar);
                    break;
                case 3:
                    farmacia.listarProdutos();
                    break;
                case 0:
                    System.out.println("A voltar ao menu principal...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);
    }

    // Recolha de Informações para adicionar um produto
    private static void adicionarProduto() {
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        System.out.print("Stock inicial: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        farmacia.adicionarProduto(nome, preco, stock);
    }

    // Menu para Gestão de Encomendas
    private static void menuEncomendas() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Encomendas ---");
            System.out.println("1. Registar Encomenda");
            System.out.println("2. Listar Encomendas");
            System.out.println("3. Atualizar Estado da Encomenda");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            // Switch case para escolher as opções
            switch (opcao) {
                case 1:
                    registarEncomenda();
                    break;
                case 2:
                    farmacia.getEntregas().listarEncomendas();
                    break;
                case 3:
                    atualizarEstadoEncomenda();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    // Recolha de informações para Registrar uma Encomenda
    private static void registarEncomenda() {
        if (!farmacia.temClientes()) {
            System.out.println("Não existem clientes registados. Por favor, adicione um cliente primeiro.");
            return;
        }

        if (!farmacia.temProdutos()) {
            System.out.println("Não existem produtos registados. Por favor, adicione um produto primeiro.");
            return;
        }

        System.out.print("NIF do cliente: ");
        String nif = scanner.nextLine();
        Cliente cliente = farmacia.acharClienteNif(nif);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        Map<Produto, Integer> produtosEncomenda = new HashMap<>();
        int idProduto;

        // Ciclo while para o registro do produto
        while (true) {
            farmacia.listarProdutos();
            System.out.print("ID do produto a adicionar (ou 0 para terminar): ");
            idProduto = scanner.nextInt();
            scanner.nextLine();

            if (idProduto == 0) {
                break;
            }

            Produto produto = farmacia.acharProdutoPorId(idProduto);
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
                    System.out.println("Produto adicionado à encomenda.");
                } else {
                    System.out.println("Stock insuficiente. Stock disponível: " + produto.getStock());
                }
            } else {
                System.out.println("Produto não encontrado.");
            }
        }

        if (!produtosEncomenda.isEmpty()) {
            farmacia.getEntregas().registarEncomenda(cliente, produtosEncomenda);
        } else {
            System.out.println("Nenhum produto adicionado. Encomenda cancelada.");
        }
    }

    // Recolha de informações para atualizar o estado da encomenda
    private static void atualizarEstadoEncomenda() {
        System.out.print("ID da encomenda a atualizar: ");
        int idEncomenda = scanner.nextInt();
        scanner.nextLine();

        Encomenda encomenda = farmacia.getEntregas().acharEncomendaPorId(idEncomenda);
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
        System.out.print("Escolha uma opção: ");
        int opcaoEstado = scanner.nextInt();
        scanner.nextLine();

        if (opcaoEstado > 0 && opcaoEstado <= Encomenda.EstadoEncomenda.values().length) {
            Encomenda.EstadoEncomenda novoEstado = Encomenda.EstadoEncomenda.values()[opcaoEstado - 1];
            farmacia.getEntregas().atualizarEstado(idEncomenda, novoEstado);
        } else {
            System.out.println("Opção inválida.");
        }
    }
}