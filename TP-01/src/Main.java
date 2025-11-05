import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Farmacia farmacia = new Farmacia();

    // Menu principal para escolha de opções
    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n--- BEM-VINDO À FARMÁCIA UBI ---");
            System.out.println("1. Gestão de Clientes");
            System.out.println("2. Gestão de Produtos");
            System.out.println("3. Gestão de Encomendas");
            System.out.println("0. Sair");
            System.out.print("\nEscolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

           // Switch case para escolher as opções
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
                    System.out.println("Saindo do programa...");
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
            System.out.println("0. Voltar ao Menu Principal\n");
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
                    System.out.print("\n");
                    farmacia.listarClientes();
                    break;
                case 4:
                    System.out.print("\n");
                    farmacia.listarClientes();
                    System.out.print("\n");
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
        System.out.print("\nNome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("NIF do cliente: ");
        String nif = scanner.nextLine();
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
        farmacia.editarCliente(scanner);
    }

    // Menu para Gestão de Produtos
    private static void menuProdutos() {
        int opcao ;
        do {
            System.out.println("\n--- Gestão de Produtos ---");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Eliminar Produto");
            System.out.println("3. Listar Produtos");
            System.out.println("4. Editar Produtos");
            System.out.println("0. Voltar ao Menu Principal\n");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            // Switch case para escolher as opções
            switch (opcao) {
                case 1:
                    adicionarProduto();
                    break;
                case 2:
                    System.out.print("\n");
                    farmacia.listarProdutos();
                    System.out.print("\nID do produto a eliminar: ");
                    int idParaEliminar = scanner.nextInt();
                    scanner.nextLine();
                    farmacia.eliminarProduto(idParaEliminar);
                    break;
                case 3:
                    System.out.print("\n");
                    farmacia.listarProdutos();
                    break;
                case 4:
                    System.out.print("\n");
                    editarProduto();
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
        System.out.print("\nNome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        System.out.print("Stock inicial: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        farmacia.adicionarProduto(nome, preco, stock);
    }

    // Editar produto
    private static void editarProduto() {
        farmacia.editarProduto(scanner);
    }

    // Menu para Gestão de Encomendas
    private static void menuEncomendas() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Encomendas ---");
            System.out.println("1. Registar Encomenda");
            System.out.println("2. Listar Encomendas");
            System.out.println("3. Atualizar Estado da Encomenda");
            System.out.println("0. Voltar ao Menu Principal\n");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            // Switch case para escolher as opções
            switch (opcao) {
                case 1:
                    registarEncomenda();
                    break;
                case 2:
                    farmacia.listarEncomendas();
                    break;
                case 3:
                    farmacia.listarEncomendas();
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
        farmacia.novaEncomenda(scanner);
    }

    // Recolha de informações para atualizar o estado da encomenda
    private static void atualizarEstadoEncomenda() {
        farmacia.atualizarEstadoEncomenda(scanner);
    }
}
