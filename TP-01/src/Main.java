import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// A classe Main é a nossa interface com o utilizador. É aqui que tudo começa.
public class Main {

    // O scanner é o nosso leitor de input do utilizador.
    private static Scanner scanner = new Scanner(System.in);
    // A farmácia é a nossa classe principal, que gere tudo.
    private static Farmacia farmacia = new Farmacia();

    // O menu principal, onde o utilizador escolhe o que quer fazer.
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

           // Um switch para cada opção do menu.
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

    // O menu para gerir os clientes.
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

            // Um switch para cada opção do menu de clientes.
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

    // Pede ao utilizador a informação para adicionar um novo cliente.
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

    // Chama o método para editar um cliente.
    private static void editarCliente() {
        farmacia.editarCliente(scanner);
    }

    // O menu para gerir os produtos.
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

            // Um switch para cada opção do menu de produtos.
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

    // Pede ao utilizador a informação para adicionar um novo produto.
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

    // Chama o método para editar um produto.
    private static void editarProduto() {
        farmacia.editarProduto(scanner);
    }

    // O menu para gerir as encomendas.
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

            // Um switch para cada opção do menu de encomendas.
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

    // Chama o método para registar uma nova encomenda.
    private static void registarEncomenda() {
        farmacia.novaEncomenda(scanner);
    }

    // Chama o método para atualizar o estado de uma encomenda.
    private static void atualizarEstadoEncomenda() {
        farmacia.atualizarEstadoEncomenda(scanner);
    }
}
