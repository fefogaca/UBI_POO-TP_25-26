
package modelo;

import java.io.Serializable;

/**
 * Representa uma Morada com rua, cidade e código postal.
 * Utilizada pela classe Cliente para armazenar informações de endereço.
 * Demonstra o uso de Composição (Cliente possui uma Morada).
 */
public class Morada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rua;
    private String cidade;
    private String codPostal;

    /**
     * Construtor para criar uma nova morada.
     *
     * @param rua A rua da morada.
     * @param cidade A cidade da morada.
     * @param codPostal O código postal no formato 0000-000.
     */
    public Morada(String rua, String cidade, String codPostal) {
        this.rua = rua;
        this.cidade = cidade;
        this.codPostal = codPostal;
    }

    /**
     * Obtém a rua da morada.
     *
     * @return A rua da morada.
     */
    public String getRua() { return rua; }

    /**
     * Obtém a cidade da morada.
     *
     * @return A cidade da morada.
     */
    public String getCidade() { return cidade; }

    /**
     * Obtém o código postal da morada.
     *
     * @return O código postal no formato 0000-000.
     */
    public String getCodPostal() { return codPostal; }

    /**
     * Define a rua da morada.
     *
     * @param rua A nova rua.
     */
    public void setRua(String rua) { this.rua = rua; }

    /**
     * Define a cidade da morada.
     *
     * @param cidade A nova cidade.
     */
    public void setCidade(String cidade) { this.cidade = cidade; }

    /**
     * Define o código postal da morada.
     *
     * @param codPostal O novo código postal no formato 0000-000.
     */
    public void setCodPostal(String codPostal) { this.codPostal = codPostal; }

    /**
     * Retorna uma representação textual da morada.
     *
     * @return String formatada com rua, cidade e código postal.
     */
    @Override
    public String toString() {
        return rua + ", " + cidade + ", " + codPostal;
    }
}
