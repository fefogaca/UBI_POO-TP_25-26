package gui;

import modelo.Cliente;
import modelo.Farmacia;
import modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Painel Swing responsável pela exibição de estatísticas da farmácia.
 * <p>
 * Apresenta informações sobre produtos mais/menos vendidos, clientes com maior volume de compras
 * e faturação por diferentes períodos (dia, semana, mês).
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class PainelEstatisticas extends JPanel {

    private Farmacia farmacia;
    private JTable tabelaProdutosMaisVendidos;
    private JTable tabelaProdutosMenosVendidos;
    private JTable tabelaClientesMaiorVolume;
    private DefaultTableModel modelProdutosMais;
    private DefaultTableModel modelProdutosMenos;
    private DefaultTableModel modelClientes;

    /**
     * Constrói o painel de estatísticas associado à instância de Farmacia fornecida.
     *
     * @param farmacia Fonte de dados para cálculo das estatísticas.
     */
    public PainelEstatisticas(Farmacia farmacia) {
        this.farmacia = farmacia;
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Aba de Produtos Mais Vendidos
        JPanel painelProdutosMais = criarPainelProdutosMaisVendidos();
        tabbedPane.addTab("Produtos Mais Vendidos", painelProdutosMais);

        // Aba de Produtos Menos Vendidos
        JPanel painelProdutosMenos = criarPainelProdutosMenosVendidos();
        tabbedPane.addTab("Produtos Menos Vendidos", painelProdutosMenos);

        // Aba de Clientes com Maior Volume
        JPanel painelClientes = criarPainelClientesMaiorVolume();
        tabbedPane.addTab("Clientes Maior Volume", painelClientes);

        // Aba de Faturação
        JPanel painelFaturacao = criarPainelFaturacao();
        tabbedPane.addTab("Faturação", painelFaturacao);

        add(tabbedPane, BorderLayout.CENTER);

        // Botão para atualizar estatísticas
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAtualizar = new JButton("Atualizar Estatísticas");
        btnAtualizar.addActionListener(e -> atualizarEstatisticas());
        painelBotoes.add(btnAtualizar);
        add(painelBotoes, BorderLayout.SOUTH);

        atualizarEstatisticas();
    }

    private JPanel criarPainelProdutosMaisVendidos() {
        JPanel painel = new JPanel(new BorderLayout());

        String[] colunas = {"Produto", "Quantidade Vendida"};
        modelProdutosMais = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutosMaisVendidos = new JTable(modelProdutosMais);
        tabelaProdutosMaisVendidos.setRowHeight(25);

        painel.add(new JScrollPane(tabelaProdutosMaisVendidos), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelProdutosMenosVendidos() {
        JPanel painel = new JPanel(new BorderLayout());

        String[] colunas = {"Produto", "Quantidade Vendida"};
        modelProdutosMenos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutosMenosVendidos = new JTable(modelProdutosMenos);
        tabelaProdutosMenosVendidos.setRowHeight(25);

        painel.add(new JScrollPane(tabelaProdutosMenosVendidos), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelClientesMaiorVolume() {
        JPanel painel = new JPanel(new BorderLayout());

        String[] colunas = {"Cliente", "NIF", "Valor Total (€)"};
        modelClientes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaClientesMaiorVolume = new JTable(modelClientes);
        tabelaClientesMaiorVolume.setRowHeight(25);

        painel.add(new JScrollPane(tabelaClientesMaiorVolume), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelFaturacao() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Faturação por Dia
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Faturação por Dia:"), gbc);
        gbc.gridx = 1;
        JTextField txtDataDia = new JTextField(15);
        txtDataDia.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        painel.add(txtDataDia, gbc);
        gbc.gridx = 2;
        JButton btnCalcularDia = new JButton("Calcular");
        JLabel lblResultadoDia = new JLabel("0.00€");
        btnCalcularDia.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(txtDataDia.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                double faturacao = farmacia.getFaturacaoPorDia(data);
                lblResultadoDia.setText(String.format("%.2f€", faturacao));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        painel.add(btnCalcularDia, gbc);
        gbc.gridx = 3;
        painel.add(lblResultadoDia, gbc);

        // Faturação por Semana
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(new JLabel("Faturação por Semana:"), gbc);
        gbc.gridx = 1;
        JTextField txtDataSemana = new JTextField(15);
        txtDataSemana.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        painel.add(txtDataSemana, gbc);
        gbc.gridx = 2;
        JButton btnCalcularSemana = new JButton("Calcular");
        JLabel lblResultadoSemana = new JLabel("0.00€");
        btnCalcularSemana.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(txtDataSemana.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                double faturacao = farmacia.getFaturacaoPorSemana(data);
                lblResultadoSemana.setText(String.format("%.2f€", faturacao));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        painel.add(btnCalcularSemana, gbc);
        gbc.gridx = 3;
        painel.add(lblResultadoSemana, gbc);

        // Faturação por Mês
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(new JLabel("Faturação por Mês:"), gbc);
        gbc.gridx = 1;
        JPanel painelMes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtAno = new JTextField(5);
        txtAno.setText(String.valueOf(LocalDate.now().getYear()));
        JTextField txtMes = new JTextField(3);
        txtMes.setText(String.valueOf(LocalDate.now().getMonthValue()));
        painelMes.add(txtAno);
        painelMes.add(new JLabel("/"));
        painelMes.add(txtMes);
        painel.add(painelMes, gbc);
        gbc.gridx = 2;
        JButton btnCalcularMes = new JButton("Calcular");
        JLabel lblResultadoMes = new JLabel("0.00€");
        btnCalcularMes.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(txtAno.getText());
                int mes = Integer.parseInt(txtMes.getText());
                if (mes < 1 || mes > 12) {
                    throw new IllegalArgumentException("Mês deve estar entre 1 e 12");
                }
                double faturacao = farmacia.getFaturacaoPorMes(ano, mes);
                lblResultadoMes.setText(String.format("%.2f€", faturacao));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Valores inválidos. Ano deve ser um número e mês entre 1 e 12.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        painel.add(btnCalcularMes, gbc);
        gbc.gridx = 3;
        painel.add(lblResultadoMes, gbc);

        return painel;
    }

    /**
     * Atualiza todas as estatísticas exibidas no painel.
     */
    public void atualizarEstatisticas() {
        atualizarProdutosMaisVendidos();
        atualizarProdutosMenosVendidos();
        atualizarClientesMaiorVolume();
    }

    private void atualizarProdutosMaisVendidos() {
        modelProdutosMais.setRowCount(0);
        List<Map.Entry<Produto, Integer>> produtosMaisVendidos = farmacia.getProdutosMaisVendidos(10);
        for (Map.Entry<Produto, Integer> entry : produtosMaisVendidos) {
            modelProdutosMais.addRow(new Object[]{
                    entry.getKey().getNome(),
                    entry.getValue()
            });
        }
    }

    private void atualizarProdutosMenosVendidos() {
        modelProdutosMenos.setRowCount(0);
        List<Map.Entry<Produto, Integer>> produtosMenosVendidos = farmacia.getProdutosMenosVendidos(10);
        for (Map.Entry<Produto, Integer> entry : produtosMenosVendidos) {
            modelProdutosMenos.addRow(new Object[]{
                    entry.getKey().getNome(),
                    entry.getValue()
            });
        }
    }

    private void atualizarClientesMaiorVolume() {
        modelClientes.setRowCount(0);
        List<Map.Entry<Cliente, Double>> clientesMaiorVolume = farmacia.getClientesMaiorVolume(10);
        for (Map.Entry<Cliente, Double> entry : clientesMaiorVolume) {
            modelClientes.addRow(new Object[]{
                    entry.getKey().getNome(),
                    entry.getKey().getNif(),
                    String.format("%.2f", entry.getValue())
            });
        }
    }
}

