
package modelo;

import java.io.Serializable;

/**
 * Classe abstrata que representa uma Pessoa com atributos básicos.
 * Serve como base para outras classes como Cliente.
 */
public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nome;
    protected String nif;

    /**
     * Construtor para a classe Pessoa.
     *
     * @param nome O nome da pessoa.
     * @param nif  O Número de Identificação Fiscal (NIF) da pessoa.
     */
    public Pessoa(String nome, String nif) {
        this.nome = nome;
        this.nif = nif;
    }

    // Getters
    public String getNome() { return nome; }
    public String getNif() { return nif; }

    // Setters
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Método abstrato que deve ser implementado pelas subclasses para retornar uma representação em String.
     * Exemplo de polimorfismo.
     */
    @Override
    public abstract String toString();
}
