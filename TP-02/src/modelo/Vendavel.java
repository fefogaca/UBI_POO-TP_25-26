
package modelo;

/**
 * Interface que define o contrato para itens que podem ser vendidos.
 * Qualquer classe que implemente esta interface deve fornecer métodos para obter nome e preço.
 */
public interface Vendavel {
    String getNome();
    double getPreco();
}
