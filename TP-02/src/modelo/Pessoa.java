
package modelo;

import java.io.Serializable;

/**
 * Classe abstrata que representa uma Pessoa com atributos básicos.
 * <p>
 * Serve como base para outras classes como Cliente, demonstrando o uso de Herança.
 * Obriga as subclasses a implementar o método toString(), demonstrando Polimorfismo.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
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

    /**
     * Obtém o nome da pessoa.
     *
     * @return O nome da pessoa.
     */
    public String getNome() { return nome; }

    /**
     * Obtém o NIF da pessoa.
     *
     * @return O NIF da pessoa.
     */
    public String getNif() { return nif; }

    /**
     * Define o nome da pessoa.
     *
     * @param nome O novo nome.
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Atualiza o NIF da pessoa.
     *
     * @param nif novo Número de Identificação Fiscal
     */
    public void setNif(String nif) { this.nif = nif; }

    /**
     * Método abstrato que deve ser implementado pelas subclasses para retornar uma representação em String.
     * Exemplo de polimorfismo.
     */
    @Override
    public abstract String toString();
}
