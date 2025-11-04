public class Morada {
    private String rua;
    private String cidade;
    private String codPostal;

    public Morada(String rua, String cidade, String codPostal) {
        this.rua = rua;
        this.cidade = cidade;
        this.codPostal = codPostal;
    }

    @Override
    public String toString() {
        return rua + ", " + cidade + ", " + codPostal;
    }
}