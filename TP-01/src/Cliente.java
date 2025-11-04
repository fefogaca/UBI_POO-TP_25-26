public class Cliente {
    private String nome;
    private String nif; // Número de Identificação Fiscal
    private Morada morada; // <--- ISTO É COMPOSIÇÃO

    public Cliente(String nome, String nif, Morada morada) {
        this.nome = nome;
        this.nif = nif;
        this.morada = morada;
    }

    // Getters
    public String getNome() { return nome; }
    public String getNif() { return nif; }
    public Morada getMorada() { return morada; }

    @Override
    public String toString() {
        return "Cliente: " + nome + " | NIF: " + nif + " | Morada: " + morada.toString();
    }
}