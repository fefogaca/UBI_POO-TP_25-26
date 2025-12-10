package gui;

import modelo.Farmacia;
import modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

/**
 * Painel Swing responsável pela manutenção do catálogo de produtos.
 * <p>
 * Exibe uma tabela com todos os produtos registados e permite a criação,
 * atualização e remoção com validação de dados. A tabela utiliza cores alternadas
 * para melhor legibilidade e inclui tooltips informativos nos botões.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class PainelGestaoProdutos extends JPanel {

    private Farmacia farmacia;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    /**
     * Constrói o painel de produtos, ligado à instância de {@link Farmacia} fornecida.
     *
     * @param farmacia fonte de dados e operações sobre produtos
     */
    public PainelGestaoProdutos(Farmacia farmacia) {
        this.farmacia = farmacia;
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Nome", "Preço", "Stock"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(tableModel);
        tabelaProdutos.setRowHeight(25);
        aplicarCoresAlternadasTabela(tabelaProdutos);
        atualizarTabela();

        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarProduto();
                }
            }
        });

        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.setToolTipText("Adiciona um novo produto ao catálogo");
        btnEditar.setToolTipText("Edita os dados do produto selecionado (duplo clique também funciona)");
        btnRemover.setToolTipText("Remove o produto selecionado do catálogo");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnRemover.addActionListener(e -> removerProduto());
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
     * Atualiza o conteúdo da tabela com o estado atual da lista de produtos.
     */
    public void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = farmacia.getProdutos();
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f€", p.getPreco()), p.getStock()});
        }
    }

    private void adicionarProduto() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Produto", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNome = new JTextField(20);
        JTextField txtPreco = new JTextField(10);
        JTextField txtStock = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Faz o campo de texto expandir
        panelForm.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0; // Resetar weightx
        panelForm.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Faz o campo de texto expandir
        panelForm.add(txtPreco, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0; // Resetar weightx
        panelForm.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Faz o campo de texto expandir
        panelForm.add(txtStock, gbc);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText();
                double preco = Double.parseDouble(txtPreco.getText());
                int stock = Integer.parseInt(txtStock.getText());

                if (preco < 0 || stock < 0) {
                    throw new IllegalArgumentException("Preço e Stock não podem ser negativos.");
                }

                Produto produto = new Produto(nome, preco, stock);
                farmacia.adicionarProduto(produto);
                atualizarTabela();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Por favor, insira valores válidos para Preço e Stock.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        panelBotoes.add(btnCancelar);
        panelBotoes.add(btnSalvar);

        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(panelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void editarProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idProduto = (int) tableModel.getValueAt(linhaSelecionada, 0);
            Optional<Produto> produtoOpt = farmacia.acharProdutoPorId(idProduto);

            if (produtoOpt.isPresent()) {
                Produto produto = produtoOpt.get();
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Produto", true);
                dialog.setSize(350, 220);
                dialog.setLocationRelativeTo(this);

                JPanel panelForm = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JTextField txtNome = new JTextField(produto.getNome(), 20);
                JTextField txtPreco = new JTextField(String.valueOf(produto.getPreco()), 10);
                JTextField txtStock = new JTextField(String.valueOf(produto.getStock()), 10);

                gbc.gridx = 0;
                gbc.gridy = 0;
                panelForm.add(new JLabel("Nome:"), gbc);
                gbc.gridx = 1;
                gbc.weightx = 1.0; // Faz o campo de texto expandir
                panelForm.add(txtNome, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.weightx = 0; // Resetar weightx
                panelForm.add(new JLabel("Preço:"), gbc);
                gbc.gridx = 1;
                gbc.weightx = 1.0; // Faz o campo de texto expandir
                panelForm.add(txtPreco, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.weightx = 0; // Resetar weightx
                panelForm.add(new JLabel("Stock:"), gbc);
                gbc.gridx = 1;
                gbc.weightx = 1.0; // Faz o campo de texto expandir
                panelForm.add(txtStock, gbc);

                JButton btnSalvar = new JButton("Salvar");
                btnSalvar.addActionListener(e -> {
                    try {
                        String novoNome = txtNome.getText();
                        double novoPreco = Double.parseDouble(txtPreco.getText());
                        int novoStock = Integer.parseInt(txtStock.getText());

                        if (novoPreco < 0 || novoStock < 0) {
                            throw new IllegalArgumentException("Preço e Stock não podem ser negativos.");
                        }

                        produto.setNome(novoNome);
                        produto.setPreco(novoPreco);
                        int diferencaStock = novoStock - produto.getStock();
                        if (diferencaStock > 0) {
                            produto.adicionarStock(diferencaStock);
                        } else if (diferencaStock < 0) {
                            produto.removerStock(Math.abs(diferencaStock));
                        }

                        atualizarTabela();
                        dialog.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Por favor, insira valores válidos para Preço e Stock.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                });

                JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                panelBotoes.add(btnSalvar);

                dialog.add(panelForm, BorderLayout.CENTER);
                dialog.add(panelBotoes, BorderLayout.SOUTH);

                dialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idProduto = (int) tableModel.getValueAt(linhaSelecionada, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem a certeza que deseja remover o produto com ID " + idProduto + "?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    farmacia.removerProduto(idProduto);
                    atualizarTabela();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}