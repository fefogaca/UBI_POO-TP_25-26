
package persistencia;

import modelo.Cliente;
import modelo.Encomenda;
import modelo.Produto;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela persistência de dados da aplicação.
 * <p>
 * Utiliza ObjectOutputStream e ObjectInputStream para guardar e ler o estado da farmácia
 * através de serialização de objetos. Atualiza contadores estáticos de IDs após carregamento
 * para evitar colisões.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class GestorDados {

    private static final String NOME_FICHEIRO = "farmacia.dat";

    /**
     * Guarda os dados da farmácia (listas de clientes, produtos e encomendas) num ficheiro.
     *
     * @param clientes    A lista de clientes a ser guardada.
     * @param produtos    A lista de produtos a ser guardada.
     * @param encomendas  A lista de encomendas a ser guardada.
     */
    public static void guardarDados(List<Cliente> clientes, List<Produto> produtos, List<Encomenda> encomendas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_FICHEIRO))) {
            oos.writeObject(clientes);
            oos.writeObject(produtos);
            oos.writeObject(encomendas);
            System.out.println("Dados guardados com sucesso em " + NOME_FICHEIRO);
        } catch (IOException e) {
            System.err.println("Erro ao guardar os dados: " + e.getMessage());
        }
    }

    /**
     * Carrega os dados da farmácia a partir de um ficheiro.
     *
     * @return Um array de Object contendo as listas de clientes, produtos e encomendas, ou null se o ficheiro não existir.
     */
    @SuppressWarnings("unchecked")
    public static Object[] carregarDados() {
        File f = new File(NOME_FICHEIRO);
        if (!f.exists()) {
            return null; // Ficheiro não existe, retorna null para a aplicação começar com dados vazios.
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_FICHEIRO))) {
            List<Cliente> clientes = (List<Cliente>) ois.readObject();
            List<Produto> produtos = (List<Produto>) ois.readObject();
            List<Encomenda> encomendas = (List<Encomenda>) ois.readObject();

            // Atualizar os IDs estáticos para evitar colisões
            if (!produtos.isEmpty()) {
                int maxIdProduto = produtos.stream().mapToInt(Produto::getId).max().orElse(0);
                Produto.setProximoId(maxIdProduto + 1);
            }
            if (!encomendas.isEmpty()) {
                int maxIdEncomenda = encomendas.stream().mapToInt(Encomenda::getId).max().orElse(0);
                Encomenda.setProximoId(maxIdEncomenda + 1);
                
                // Garantir que todas as encomendas têm data/hora (para compatibilidade com versões antigas)
                for (Encomenda encomenda : encomendas) {
                    if (encomenda.getDataHora() == null) {
                        encomenda.setDataHora(LocalDateTime.now());
                    }
                }
            }

            System.out.println("Dados carregados com sucesso de " + NOME_FICHEIRO);
            return new Object[]{clientes, produtos, encomendas};

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
            return null;
        }
    }
}
