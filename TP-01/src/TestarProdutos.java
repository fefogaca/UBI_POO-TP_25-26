public class TestarProdutos     {
    public static void main(String[] args) {
        Produto p1 = new Produto("Paracetamol", 5.99, 100);
        Produto p2 = new Produto("Xarope", 8.49, 50);

        System.out.println(p1.toString());
        System.out.println(p2.toString());

        p1.adicionarStock(10);
        System.out.println("Stock atualizado do " + p1.getNome() + ": " + p1.getStock());
    }
}