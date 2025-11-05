public class Cliente {
    private String nome;
    private String nif;
    private Morada morada;

    public Cliente(String nome, String nif, Morada morada) {
        this.nome = nome;
        this.nif = nif;
        this.morada = morada;
    }

    public String getNome() { return nome; }
    public String getNif() { return nif; }
    public Morada getMorada() { return morada; }

    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return "Cliente: " + nome + " | NIF: " + nif + " | Morada: " + morada.toString();
    }
}