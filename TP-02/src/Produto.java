import java.io.Serializable;

/**
 * Representa um produto da farmácia.
 */

public class Produto implements Serializable {
    private static int proximoId = 1;

    private String nome;

    private double preco;

    private int stock;

    private int id;

    /**
     * Cria um novo produto.
     * @param nome O nome do produto.
     * @param preco O preço do produto.
     * @param stockInicial O stock inicial do produto.
     */



    public Produto(String nome, double preco, int stockInicial) {

        this.nome = nome;
        this.preco = preco;
        this.stock = stockInicial;
        this.id = proximoId;
        proximoId++;
    }
    /**
     * Obtém o nome do produto.
     * @return O nome do produto.
     */

    public String getNome() { return nome; }

    /**
     * Obtém o preço do produto.
     * @return O preço do produto.
     */

    public double getPreco() { return preco; }

    /**
     * Obtém o stock do produto.
     * @return O stock do produto.
     */

    public int getStock() { return stock; }

    /**
     * Obtém o ID do produto.
     * @return O ID do produto.
     */

    public int getId() {return id; }

    /**
     * Define o nome do produto.
     * @param nome O novo nome do produto.
     */

    public void setNome(String nome) { this.nome = nome; }

    /**
     * Define o preço do produto.
     * @param preco O novo preço do produto.
     */

    public void setPreco(double preco) {
        if (preco >= 0) {
            this.preco = preco;
        } else {
            System.out.println("Erro: O preço não pode ser negativo!");
        }
    }

    /**
     * Adiciona stock a um produto.
     * @param quantidade A quantidade a adicionar.
     */

    public void adicionarStock(int quantidade) {
        if (quantidade > 0) {
            this.stock += quantidade;
        }
    }

    /**
     * Remove stock de um produto.
     * @param quantidade A quantidade a remover.
     */

    public void removerStock(int quantidade) {
        if (quantidade > 0 && this.stock >= quantidade) {
            this.stock -= quantidade;
        } else {
            System.out.println("Erro: Stock insuficiente ou quantidade inválida.");
        }
    }

    /**
     * Devolve uma representação do produto em formato de texto.
     * @return Uma string com os detalhes do produto.
     */

    @Override
    public String toString() {
        return "ID: " + id + " | Produto: " + nome + " | Preço: " + preco + "€ | Stock: " + stock;
    }
}




