
package modelo;

import java.io.Serializable;

/**
 * Representa um produto da farmácia que é um item Vendavel.
 * <p>
 * Implementa a interface Vendavel, demonstrando o uso de Interfaces.
 * Gere stock, preço e ID único para cada produto.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
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

    /**
     * Obtém o ID único do produto.
     *
     * @return O ID do produto.
     */
    public int getId() { return id; }

    /**
     * Obtém a quantidade em stock do produto.
     *
     * @return A quantidade em stock.
     */
    public int getStock() { return stock; }

    /**
     * Obtém o nome do produto (implementação da interface Vendavel).
     *
     * @return O nome do produto.
     */
    @Override
    public String getNome() { return nome; }

    /**
     * Obtém o preço do produto (implementação da interface Vendavel).
     *
     * @return O preço do produto em euros.
     */
    @Override
    public double getPreco() { return preco; }

    /**
     * Define o nome do produto.
     *
     * @param nome O novo nome.
     */
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

    /**
     * Compara dois produtos pelo seu ID.
     *
     * @param o O objeto a comparar.
     * @return true se os IDs forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id == produto.id;
    }

    /**
     * Retorna o código hash baseado no ID do produto.
     *
     * @return O código hash do produto.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    /**
     * Retorna uma representação textual do produto.
     *
     * @return String formatada com ID, nome, preço e stock.
     */
    @Override
    public String toString() {
        return "ID: " + id + " | Produto: " + nome + " | Preço: " + String.format("%.2f", preco) + "€ | Stock: " + stock;
    }
}
