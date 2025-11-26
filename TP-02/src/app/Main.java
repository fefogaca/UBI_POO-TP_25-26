package app;

import gui.FarmaciaGUI;
import modelo.Cliente;
import modelo.Encomenda;
import modelo.Farmacia;
import modelo.Produto;
import persistencia.GestorDados;

import javax.swing.*;
import java.util.List;

/**
 * Ponto de entrada da aplicação de gestão de farmácia.
 * <p>
 * Responsável por carregar o estado persistido, inicializar a instância de {@link modelo.Farmacia}
 * e lançar a interface gráfica baseada em Swing na Event Dispatch Thread.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class Main {

    /**
     * Arranca a aplicação carregando dados previamente guardados e apresentando a GUI.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Farmacia farmacia = new Farmacia();

        Object[] dadosCarregados = GestorDados.carregarDados();
        if (dadosCarregados != null) {
            farmacia.setClientes((List<Cliente>) dadosCarregados[0]);
            farmacia.setProdutos((List<Produto>) dadosCarregados[1]);
            farmacia.setEncomendas((List<Encomenda>) dadosCarregados[2]);
        }

        SwingUtilities.invokeLater(() -> {
            FarmaciaGUI frame = new FarmaciaGUI(farmacia);
            frame.setVisible(true);
        });
    }
}