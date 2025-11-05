public class Morada {
    private String rua;
    private String cidade;
    private String codPostal;

    public Morada(String rua, String cidade, String codPostal) {
        this.rua = rua;
        this.cidade = cidade;
        this.codPostal = codPostal;
    }

    public String getRua() {
        return this.rua;
    }
    public String getCidade() {
        return this.cidade;
    }
    public String getCodPostal() {
        return this.codPostal;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    @Override
    public String toString() {
        return rua + ", " + cidade + ", " + codPostal;
    }

}