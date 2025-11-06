
package modelo;

import java.io.Serializable;

/**
 * Representa um Cliente da farmácia, que é uma especialização de Pessoa.
 * Demonstra o uso de Herança.
 */
public class Cliente extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    private Morada morada;

    /**
     * Construtor para a classe Cliente.
     *
     * @param nome   O nome do cliente.
     * @param nif    O NIF do cliente.
     * @param morada A morada do cliente.
     */
    public Cliente(String nome, String nif, Morada morada) {
        super(nome, nif);
        this.morada = morada;
    }

    // Getters e Setters
    public Morada getMorada() { return morada; }
    public void setMorada(Morada morada) { this.morada = morada; }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual do Cliente.
     * Demonstra o uso de Overriding.
     */
    @Override
    public String toString() {
        return "Cliente: " + nome + " | NIF: " + nif + " | Morada: " + morada.toString();
    }
}
