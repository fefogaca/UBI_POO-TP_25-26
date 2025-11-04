import java.util.Scanner;

public class Main {

    // O Scanner é estático para ser usado por todos os menus
    private static Scanner scanner = new Scanner(System.in);
    // A Farmacia guarda os nossos dados
    private static Farmacia farmacia = new Farmacia();

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n--- BEM-VINDO À FARMÁCIA UBI ---");
            System.out.println("1. Gestão de Clientes");
            System.out.println("2. Gestão de Produtos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner
;
            switch (opcao) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuProdutos();
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

    private static void menuClientes() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Clientes ---");
            System.out.println("1. Adicionar Cliente");
            System.out.println("2. Eliminar Cliente");
            System.out.println("3. Listar Clientes");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    adicionarCliente();
                    break;
                case 2:
                    System.out.print("\n");
                    farmacia.listarClientesNN();
                    System.out.print("\n");
                    System.out.print("NIF do cliente a eliminar: ");
                    String nif = scanner.nextLine();
                    farmacia.eliminarCliente(nif);
                    break;
                case 3:
                    farmacia.listarClientes();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

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
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    // Chama o método helper que definimos abaixo
                    adicionarProduto();
                    break;
                case 2:
                    // --- CORRIGIDO ---
                    farmacia.listarProdutos();
                    System.out.print("ID do produto a eliminar: ");
                    int idParaEliminar = scanner.nextInt(); // Ler como int
                    scanner.nextLine(); // Consumir o 'Enter'
                    farmacia.eliminarProduto(idParaEliminar); // Chamar o método correto (com int)
                    break; // <-- Adicionado o 'break'
                case 3:
                    farmacia.listarProdutos();
                    break;
                case 0:
                    // --- CORRIGIDO ---
                    System.out.println("A voltar ao menu principal...");
                    return; // 'return' sai do método e volta para quem chamou (o menu principal)
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);
    }

    private static void adicionarProduto() {
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        System.out.print("Stock inicial: ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        farmacia.adicionarProduto(nome, preco, stock);
    }
}