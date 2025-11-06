
package modelo;

import java.io.Serializable;

/**
 * Representa um produto da farmácia que é um item Vendavel.
 */
public class Produto implements Serializable, Vendavel {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;

    private int id;
    private String nome;
    private double preco;
    private int stock;

    /**
     * Construtor para criar um novo produto.
     * @param nome O nome do produto.
     * @param preco O preço do produto.
     * @param stockInicial O stock inicial do produto.
     */
    public Produto(String nome, double preco, int stockInicial) {
        this.id = proximoId++;
        this.nome = nome;
        this.preco = preco;
        this.stock = stockInicial;
    }

    // Getters
    public int getId() { return id; }
    public int getStock() { return stock; }

    @Override
    public String getNome() { return nome; }

    @Override
    public double getPreco() { return preco; }

    // Setters
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) {
        if (preco >= 0) {
            this.preco = preco;
        } else {
            System.out.println("Erro: O preço não pode ser negativo!");
        }
    }

    /**
     * Adiciona uma quantidade ao stock do produto.
     * @param quantidade A quantidade a ser adicionada.
     */
    public void adicionarStock(int quantidade) {
        if (quantidade > 0) {
            this.stock += quantidade;
        }
    }

    /**
     * Remove uma quantidade do stock do produto.
     * @param quantidade A quantidade a ser removida.
     */
    public void removerStock(int quantidade) {
        if (quantidade > 0 && this.stock >= quantidade) {
            this.stock -= quantidade;
        } else {
            System.out.println("Erro: Stock insuficiente ou quantidade inválida.");
        }
    }

    /**
     * Define o próximo ID a ser usado para um novo produto.
     * Útil ao carregar dados de um ficheiro.
     * @param id O próximo ID.
     */
    public static void setProximoId(int id) {
        proximoId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id == produto.id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Produto: " + nome + " | Preço: " + String.format("%.2f", preco) + "€ | Stock: " + stock;
    }
}
