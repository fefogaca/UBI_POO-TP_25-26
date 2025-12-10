
package gui;

import modelo.Farmacia;
import persistencia.GestorDados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Janela principal da aplicação da Farmácia.
 * <p>
 * Utiliza Swing para criar a interface gráfica com separadores (JTabbedPane)
 * para gestão de clientes, produtos e encomendas. Gere o ciclo de vida da aplicação,
 * incluindo confirmação ao encerrar e opção de guardar dados.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class FarmaciaGUI extends JFrame {

    private Farmacia farmacia;

    /**
     * Construtor que cria a janela principal da aplicação.
     *
     * @param farmacia A instância da farmácia que contém os dados a gerir.
     */
    public FarmaciaGUI(Farmacia farmacia) {
        this.farmacia = farmacia;

        setTitle("Gestão de Farmácia");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);



        // Adiciona um listener para guardar os dados ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Deseja guardar os dados antes de sair?", 
                    "Sair", JOptionPane.YES_NO_CANCEL_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    GestorDados.guardarDados(farmacia.getClientes(), farmacia.getProdutos(), farmacia.getEncomendas());
                    dispose();
                    System.exit(0);
                } else if (confirm == JOptionPane.NO_OPTION) {
                    dispose();
                    System.exit(0);
                } 
                // Se for CANCEL, não faz nada
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();

        PainelGestaoClientes painelClientes = new PainelGestaoClientes(farmacia);
        PainelGestaoProdutos painelProdutos = new PainelGestaoProdutos(farmacia);
        PainelGestaoEncomendas painelEncomendas = new PainelGestaoEncomendas(farmacia, painelProdutos); // Passa painelProdutos
        PainelEstatisticas painelEstatisticas = new PainelEstatisticas(farmacia);

        // Adicionar os painéis de gestão
        tabbedPane.addTab("Clientes", painelClientes);
        tabbedPane.addTab("Produtos", painelProdutos);
        tabbedPane.addTab("Encomendas", painelEncomendas);
        tabbedPane.addTab("Estatísticas", painelEstatisticas);

        add(tabbedPane);
    }
}
