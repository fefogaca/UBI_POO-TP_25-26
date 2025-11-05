public class Produto {
    // --- MUDANÇA 1: Contador estático para gerar IDs ---
    private static int proximoId = 1;

    private String nome;
    private double preco;
    private int stock;
    private int id; // Este ID agora será gerado automaticamente

    // --- MUDANÇA 2: Construtor modificado ---
    // Removemos o 'id' dos parâmetros. Ele será atribuído internamente.
    public Produto(String nome, double preco, int stockInicial) {
        this.nome = nome;
        this.preco = preco;
        this.stock = stockInicial;
        this.id = proximoId; // Atribui o ID atual
        proximoId++;         // Incrementa o contador para o próximo produto
    }

    // Getters e Setters (métodos para aceder e modificar os dados)
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getStock() { return stock; }
    public int getId() {return id; }

    // --- MUDANÇA 3: O 'setId' foi removido ---
    // Não queremos que o ID seja alterado manualmente.

    public void adicionarStock(int quantidade) {
        if (quantidade > 0) {
            this.stock += quantidade;
        }
    }

    public void removerStock(int quantidade) {
        if (quantidade > 0 && this.stock >= quantidade) {
            this.stock -= quantidade;
        } else {
            System.out.println("Erro: Stock insuficiente ou quantidade inválida.");
        }
    }



    // --- MUDANÇA 4: Método toString() atualizado para mostrar o ID ---
    @Override
    public String toString() {
        // Agora o ID é visível quando listamos o produto
        return "ID: " + id + " | Produto: " + nome + " | Preço: " + preco + "€ | Stock: " + stock;
    }
}