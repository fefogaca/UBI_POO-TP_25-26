import gui.FarmaciaGUI;
import modelo.Cliente;
import modelo.Encomenda;
import modelo.Farmacia;
import modelo.Produto;
import persistencia.GestorDados;

import javax.swing.*;
import java.util.List;

/**
 * Classe principal da aplicação da Farmácia.
 * Responsável por carregar/salvar dados e iniciar a interface gráfica.
 */
public class Main {

    public static void main(String[] args) {
        Farmacia farmacia = new Farmacia();

        // Carregar dados ao iniciar
        Object[] dadosCarregados = GestorDados.carregarDados();
        if (dadosCarregados != null) {
            farmacia.setClientes((List<Cliente>) dadosCarregados[0]);
            farmacia.setProdutos((List<Produto>) dadosCarregados[1]);
            farmacia.setEncomendas((List<Encomenda>) dadosCarregados[2]);
        }

        // Iniciar a interface gráfica
        SwingUtilities.invokeLater(() -> {
            FarmaciaGUI frame = new FarmaciaGUI(farmacia);
            frame.setVisible(true);
        });
    }
}