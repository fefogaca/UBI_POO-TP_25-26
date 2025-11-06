
package modelo;

import java.io.Serializable;

/**
 * Representa uma Morada.
 */
public class Morada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rua;
    private String cidade;
    private String codPostal;

    public Morada(String rua, String cidade, String codPostal) {
        this.rua = rua;
        this.cidade = cidade;
        this.codPostal = codPostal;
    }

    // Getters
    public String getRua() { return rua; }
    public String getCidade() { return cidade; }
    public String getCodPostal() { return codPostal; }

    // Setters
    public void setRua(String rua) { this.rua = rua; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setCodPostal(String codPostal) { this.codPostal = codPostal; }

    @Override
    public String toString() {
        return rua + ", " + cidade + ", " + codPostal;
    }
}
