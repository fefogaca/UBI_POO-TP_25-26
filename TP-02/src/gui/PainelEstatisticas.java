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
        btnAtualizar.setToolTipText("Atualiza todas as estatísticas exibidas com os dados mais recentes");
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
        aplicarCoresAlternadasTabela(tabelaProdutosMaisVendidos);

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
        aplicarCoresAlternadasTabela(tabelaProdutosMenosVendidos);

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
        aplicarCoresAlternadasTabela(tabelaClientesMaiorVolume);

        painel.add(new JScrollPane(tabelaClientesMaiorVolume), BorderLayout.CENTER);
        return painel;
    }

    /**
     * Aplica cores alternadas nas linhas da tabela para melhor legibilidade.
     *
     * @param tabela A tabela a ser formatada.
     */
    private void aplicarCoresAlternadasTabela(JTable tabela) {
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        });
    }

    /**
     * Cria o painel de faturação com campos para calcular faturação por dia, semana e mês.
     * Inclui separadores visuais e formatação melhorada para melhor legibilidade.
     *
     * @return Painel configurado com campos de faturação.
     */
    private JPanel criarPainelFaturacao() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Título da seção
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        JLabel titulo = new JLabel("Cálculo de Faturação");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
        painel.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Separador visual
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JSeparator separador1 = new JSeparator();
        painel.add(separador1, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Faturação por Dia
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblDia = new JLabel("Faturação por Dia:");
        lblDia.setToolTipText("Calcula a faturação total de um dia específico");
        painel.add(lblDia, gbc);
        gbc.gridx = 1;
        JTextField txtDataDia = new JTextField(15);
        txtDataDia.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDataDia.setToolTipText("Formato: dd/MM/yyyy (ex: 25/12/2024)");
        painel.add(txtDataDia, gbc);
        gbc.gridx = 2;
        JButton btnCalcularDia = new JButton("Calcular");
        btnCalcularDia.setToolTipText("Calcula a faturação do dia especificado");
        JLabel lblResultadoDia = new JLabel("0.00€");
        lblResultadoDia.setFont(lblResultadoDia.getFont().deriveFont(Font.BOLD));
        lblResultadoDia.setForeground(new Color(0, 100, 0)); // Verde escuro para resultados
        btnCalcularDia.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(txtDataDia.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                double faturacao = farmacia.getFaturacaoPorDia(data);
                lblResultadoDia.setText(String.format("%.2f€", faturacao));
                lblResultadoDia.setForeground(new Color(0, 100, 0));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        painel.add(btnCalcularDia, gbc);
        gbc.gridx = 3;
        painel.add(lblResultadoDia, gbc);

        // Faturação por Semana
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblSemana = new JLabel("Faturação por Semana:");
        lblSemana.setToolTipText("Calcula a faturação total da semana que contém a data especificada");
        painel.add(lblSemana, gbc);
        gbc.gridx = 1;
        JTextField txtDataSemana = new JTextField(15);
        txtDataSemana.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDataSemana.setToolTipText("Formato: dd/MM/yyyy - A semana vai de segunda a domingo");
        painel.add(txtDataSemana, gbc);
        gbc.gridx = 2;
        JButton btnCalcularSemana = new JButton("Calcular");
        btnCalcularSemana.setToolTipText("Calcula a faturação da semana que contém a data especificada");
        JLabel lblResultadoSemana = new JLabel("0.00€");
        lblResultadoSemana.setFont(lblResultadoSemana.getFont().deriveFont(Font.BOLD));
        lblResultadoSemana.setForeground(new Color(0, 100, 0));
        btnCalcularSemana.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(txtDataSemana.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                double faturacao = farmacia.getFaturacaoPorSemana(data);
                lblResultadoSemana.setText(String.format("%.2f€", faturacao));
                lblResultadoSemana.setForeground(new Color(0, 100, 0));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        painel.add(btnCalcularSemana, gbc);
        gbc.gridx = 3;
        painel.add(lblResultadoSemana, gbc);

        // Separador visual
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JSeparator separador2 = new JSeparator();
        painel.add(separador2, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Faturação por Mês
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblMes = new JLabel("Faturação por Mês:");
        lblMes.setToolTipText("Calcula a faturação total de um mês específico");
        painel.add(lblMes, gbc);
        gbc.gridx = 1;
        JPanel painelMes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtAno = new JTextField(5);
        txtAno.setText(String.valueOf(LocalDate.now().getYear()));
        txtAno.setToolTipText("Ano (ex: 2024)");
        JTextField txtMes = new JTextField(3);
        txtMes.setText(String.valueOf(LocalDate.now().getMonthValue()));
        txtMes.setToolTipText("Mês (1-12)");
        painelMes.add(txtAno);
        painelMes.add(new JLabel("/"));
        painelMes.add(txtMes);
        painel.add(painelMes, gbc);
        gbc.gridx = 2;
        JButton btnCalcularMes = new JButton("Calcular");
        btnCalcularMes.setToolTipText("Calcula a faturação do mês especificado");
        JLabel lblResultadoMes = new JLabel("0.00€");
        lblResultadoMes.setFont(lblResultadoMes.getFont().deriveFont(Font.BOLD));
        lblResultadoMes.setForeground(new Color(0, 100, 0));
        btnCalcularMes.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(txtAno.getText());
                int mes = Integer.parseInt(txtMes.getText());
                if (mes < 1 || mes > 12) {
                    throw new IllegalArgumentException("Mês deve estar entre 1 e 12");
                }
                double faturacao = farmacia.getFaturacaoPorMes(ano, mes);
                lblResultadoMes.setText(String.format("%.2f€", faturacao));
                lblResultadoMes.setForeground(new Color(0, 100, 0));
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

    /**
     * Atualiza a tabela de produtos mais vendidos.
     * Exibe mensagem informativa se não houver dados disponíveis.
     */
    private void atualizarProdutosMaisVendidos() {
        modelProdutosMais.setRowCount(0);
        List<Map.Entry<Produto, Integer>> produtosMaisVendidos = farmacia.getProdutosMaisVendidos(10);
        
        if (produtosMaisVendidos.isEmpty()) {
            modelProdutosMais.addRow(new Object[]{
                    "Nenhum produto vendido ainda", ""
            });
        } else {
            for (Map.Entry<Produto, Integer> entry : produtosMaisVendidos) {
                modelProdutosMais.addRow(new Object[]{
                        entry.getKey().getNome(),
                        entry.getValue()
                });
            }
        }
    }

    /**
     * Atualiza a tabela de produtos menos vendidos.
     * Exibe mensagem informativa se não houver dados disponíveis.
     */
    private void atualizarProdutosMenosVendidos() {
        modelProdutosMenos.setRowCount(0);
        List<Map.Entry<Produto, Integer>> produtosMenosVendidos = farmacia.getProdutosMenosVendidos(10);
        
        if (produtosMenosVendidos.isEmpty()) {
            modelProdutosMenos.addRow(new Object[]{
                    "Nenhum produto vendido ainda", ""
            });
        } else {
            for (Map.Entry<Produto, Integer> entry : produtosMenosVendidos) {
                modelProdutosMenos.addRow(new Object[]{
                        entry.getKey().getNome(),
                        entry.getValue()
                });
            }
        }
    }

    /**
     * Atualiza a tabela de clientes com maior volume de compras.
     * Exibe mensagem informativa se não houver dados disponíveis.
     */
    private void atualizarClientesMaiorVolume() {
        modelClientes.setRowCount(0);
        List<Map.Entry<Cliente, Double>> clientesMaiorVolume = farmacia.getClientesMaiorVolume(10);
        
        if (clientesMaiorVolume.isEmpty()) {
            modelClientes.addRow(new Object[]{
                    "Nenhum cliente com compras registadas", "", ""
            });
        } else {
            for (Map.Entry<Cliente, Double> entry : clientesMaiorVolume) {
                modelClientes.addRow(new Object[]{
                        entry.getKey().getNome(),
                        entry.getKey().getNif(),
                        String.format("%.2f", entry.getValue())
                });
            }
        }
    }
}

