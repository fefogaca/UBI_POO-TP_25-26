
package gui;

import modelo.Farmacia;
import persistencia.GestorDados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Janela principal da aplicação da Farmácia.
 * Utiliza Swing para criar a interface gráfica.
 */
public class FarmaciaGUI extends JFrame {

    private Farmacia farmacia;

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

        // Adicionar os painéis de gestão
        tabbedPane.addTab("Clientes", painelClientes);
        tabbedPane.addTab("Produtos", painelProdutos);
        tabbedPane.addTab("Encomendas", painelEncomendas);

        add(tabbedPane);
    }
}
