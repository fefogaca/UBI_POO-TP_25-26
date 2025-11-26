
package modelo;

/**
 * Interface que define o contrato para itens que podem ser vendidos.
 * <p>
 * Qualquer classe que implemente esta interface deve fornecer métodos para obter
 * nome e preço. Demonstra o uso de Interfaces e Polimorfismo.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public interface Vendavel {
    /**
     * Obtém o nome do item vendável.
     *
     * @return O nome do item.
     */
    String getNome();

    /**
     * Obtém o preço do item vendável.
     *
     * @return O preço do item em euros.
     */
    double getPreco();
}
