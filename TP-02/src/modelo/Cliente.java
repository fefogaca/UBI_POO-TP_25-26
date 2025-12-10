
package modelo;

import java.io.Serializable;

/**
 * Representa um Cliente da farmácia, que é uma especialização de Pessoa.
 * <p>
 * Demonstra o uso de Herança (extends Pessoa) e Composição (possui uma Morada).
 * Sobrescreve o método toString() para fornecer uma representação personalizada.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
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

    /**
     * Obtém a morada do cliente.
     *
     * @return A morada do cliente.
     */
    public Morada getMorada() { return morada; }

    /**
     * Define a morada do cliente.
     *
     * @param morada A nova morada.
     */
    public void setMorada(Morada morada) { this.morada = morada; }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual do Cliente.
     * Demonstra o uso de Overriding.
     */
    @Override
    public String toString() {
        return "Cliente: " + nome + " | NIF: " + nif + " | Morada: " + morada.toString();
    }

    /**
     * Compara dois clientes pelo seu NIF.
     * Dois clientes são considerados iguais se tiverem o mesmo NIF.
     *
     * @param o O objeto a comparar.
     * @return true se os NIFs forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return nif != null && nif.equals(cliente.nif);
    }

    /**
     * Retorna o código hash baseado no NIF do cliente.
     *
     * @return O código hash do cliente.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(nif);
    }
}
