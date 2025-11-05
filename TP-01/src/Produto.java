public class Produto {
    private static int proximoId = 1;

    private String nome;
    private double preco;
    private int stock;
    private int id;

    public Produto(String nome, double preco, int stockInicial) {
        this.nome = nome;
        this.preco = preco;
        this.stock = stockInicial;
        this.id = proximoId;
        proximoId++;
    }

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getStock() { return stock; }
    public int getId() {return id; }

    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) {
        if (preco >= 0) {
            this.preco = preco;
        } else {
            System.out.println("Erro: O preço não pode ser negativo!");
        }
    }

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

    @Override
    public String toString() {
        return "ID: " + id + " | Produto: " + nome + " | Preço: " + preco + "€ | Stock: " + stock;
    }
}