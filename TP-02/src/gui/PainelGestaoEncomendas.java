package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.Encomenda;
import modelo.Farmacia;
import modelo.Produto;

/**
 * Painel dedicado à gestão de encomendas.
 * <p>
 * Permite registar novas encomendas, editar as existentes (atualizando produtos e estado)
 * e removê-las com reposição automática de stock. Mantém sincronização com o painel de produtos.
 * A tabela de encomendas exibe informações sobre ID, cliente, preço total, quantidade,
 * data/hora de criação e estado da encomenda.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class PainelGestaoEncomendas extends JPanel {

    private Farmacia farmacia;
    private JTable tabelaEncomendas;
    private DefaultTableModel tableModel;
    private PainelGestaoProdutos painelProdutos;

    /**
     * Cria o painel de encomendas associado à instância da farmácia e ao painel de produtos.
     *
     * @param farmacia       fonte de dados para encomendas
     * @param painelProdutos painel de produtos a atualizar quando o stock é alterado
     */
    public PainelGestaoEncomendas(Farmacia farmacia, PainelGestaoProdutos painelProdutos) {
        this.farmacia = farmacia;
        this.painelProdutos = painelProdutos;
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Cliente", "Preço Total", "Quantidade", "Data/Hora", "Estado"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEncomendas = new JTable(tableModel);
        tabelaEncomendas.setRowHeight(25);
        aplicarCoresAlternadasTabela(tabelaEncomendas);
        atualizarTabela();

        tabelaEncomendas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarEncomenda();
                }
            }
        });

        JPanel painelBotoes = new JPanel();
        JButton btnNovaEncomenda = new JButton("Nova Encomenda");
        JButton btnEditarEncomenda = new JButton("Editar Encomenda");
        JButton btnRemoverEncomenda = new JButton("Remover Encomenda");

        btnNovaEncomenda.setToolTipText("Cria uma nova encomenda para um cliente existente");
        btnEditarEncomenda.setToolTipText("Edita a encomenda selecionada (duplo clique também funciona)");
        btnRemoverEncomenda.setToolTipText("Remove a encomenda selecionada e repõe o stock dos produtos");

        painelBotoes.add(btnNovaEncomenda);
        painelBotoes.add(btnEditarEncomenda);
        painelBotoes.add(btnRemoverEncomenda);

        add(new JScrollPane(tabelaEncomendas), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnNovaEncomenda.addActionListener(e -> novaEncomenda());
        btnEditarEncomenda.addActionListener(e -> editarEncomenda());
        btnRemoverEncomenda.addActionListener(e -> removerEncomenda());
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
     * Atualiza o conteúdo da tabela com o estado atual das encomendas.
     * Inclui informações sobre ID, cliente, preço total, quantidade, data/hora e estado.
     */
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Encomenda> encomendas = farmacia.getEncomendas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Encomenda e : encomendas) {
            String dataHoraStr = (e.getDataHora() != null) ? 
                e.getDataHora().format(formatter) : "N/A";
            
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getCliente().getNome(),
                    String.format("%.2f€", e.getPrecoTotal()),
                    e.getQuantidadeTotal(),
                    dataHoraStr,
                    e.getEstado().getDescricao()
            });
        }
    }

    private void novaEncomenda() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nova Encomenda", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(700, 500);

        // Painel superior para seleção de cliente
        JPanel painelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCliente.add(new JLabel("NIF:"));
        JTextField txtNifCliente = new JTextField(10);
        txtNifCliente.setEditable(false);
        painelCliente.add(txtNifCliente);
        JButton btnSelecionarCliente = new JButton("Selecionar Cliente");
        painelCliente.add(btnSelecionarCliente);
        JLabel lblNomeCliente = new JLabel(" (Nenhum cliente selecionado)");
        painelCliente.add(lblNomeCliente);

        // Tabela para produtos da encomenda
        String[] colunasProdutos = {"ID", "Nome", "Preço", "Stock Disponível", "Quantidade"};
        DefaultTableModel modelProdutosEncomenda = new DefaultTableModel(colunasProdutos, 0);
        JTable tabelaProdutosEncomenda = new JTable(modelProdutosEncomenda);
        JScrollPane scrollProdutos = new JScrollPane(tabelaProdutosEncomenda);

        // Painel para adicionar produtos (simplificado)
        JPanel painelAdicionarProduto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSelecionarProduto = new JButton("Selecionar Produto");
        painelAdicionarProduto.add(btnSelecionarProduto);

        Map<Produto, Integer> produtosSelecionados = new HashMap<>();
        Cliente[] clienteSelecionado = {null}; // Usar array para ser final e mutável

        // Ação para o botão Selecionar Cliente
        btnSelecionarCliente.addActionListener(e -> {
            JDialog selecaoClienteDialog = new JDialog(dialog, "Selecionar Cliente", true);
            selecaoClienteDialog.setLayout(new BorderLayout());
            selecaoClienteDialog.setSize(500, 300);

            String[] colunasClientes = {"Nome", "NIF", "Morada"};
            DefaultTableModel modelSelecaoClientes = new DefaultTableModel(colunasClientes, 0);
            JTable tabelaSelecaoClientes = new JTable(modelSelecaoClientes);
            for (Cliente c : farmacia.getClientes()) {
                modelSelecaoClientes.addRow(new Object[]{c.getNome(), c.getNif(), c.getMorada().toString()});
            }
            selecaoClienteDialog.add(new JScrollPane(tabelaSelecaoClientes), BorderLayout.CENTER);

            JButton btnConfirmarSelecao = new JButton("Confirmar Seleção");
            btnConfirmarSelecao.addActionListener(ev -> {
                int linhaSelecionada = tabelaSelecaoClientes.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    String nifSelecionado = (String) modelSelecaoClientes.getValueAt(linhaSelecionada, 1);
                    farmacia.acharClienteNif(nifSelecionado).ifPresent(c -> {
                        clienteSelecionado[0] = c;
                        txtNifCliente.setText(c.getNif());
                        lblNomeCliente.setText("Cliente: " + c.getNome());
                    });
                    selecaoClienteDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(selecaoClienteDialog, "Por favor, selecione um cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });
            selecaoClienteDialog.add(btnConfirmarSelecao, BorderLayout.SOUTH);
            selecaoClienteDialog.setLocationRelativeTo(dialog);
            selecaoClienteDialog.setVisible(true);
        });

        // Ação para o botão Selecionar Produto (agora com pesquisa)
        btnSelecionarProduto.addActionListener(e -> {
            JDialog selecaoProdutoDialog = new JDialog(dialog, "Selecionar Produto e Quantidade", true);
            selecaoProdutoDialog.setLayout(new BorderLayout());
            selecaoProdutoDialog.setSize(700, 500);

            // Painel de pesquisa
            JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JComboBox<String> cbTipoPesquisa = new JComboBox<>(new String[]{"Nome", "ID"});
            JTextField txtPesquisa = new JTextField(15);
            JButton btnPesquisar = new JButton("Pesquisar");
            painelPesquisa.add(new JLabel("Pesquisar por:"));
            painelPesquisa.add(cbTipoPesquisa);
            painelPesquisa.add(txtPesquisa);
            painelPesquisa.add(btnPesquisar);

            String[] colunasTodosProdutos = {"ID", "Nome", "Preço", "Stock Disponível"};
            DefaultTableModel modelTodosProdutos = new DefaultTableModel(colunasTodosProdutos, 0);
            JTable tabelaTodosProdutos = new JTable(modelTodosProdutos);
            JScrollPane scrollTodosProdutos = new JScrollPane(tabelaTodosProdutos);

            // Função para atualizar a tabela de produtos disponíveis
            Runnable atualizarTabelaTodosProdutos = () -> {
                modelTodosProdutos.setRowCount(0);
                String termoPesquisa = txtPesquisa.getText().toLowerCase();
                String tipoPesquisa = (String) cbTipoPesquisa.getSelectedItem();

                farmacia.getProdutos().stream()
                        .filter(p -> {
                            if (termoPesquisa.isEmpty()) return true;
                            if ("Nome".equals(tipoPesquisa)) {
                                return p.getNome().toLowerCase().contains(termoPesquisa);
                            } else { // ID
                                return String.valueOf(p.getId()).contains(termoPesquisa);
                            }
                        })
                        .forEach(p -> modelTodosProdutos.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f€", p.getPreco()), p.getStock()}));
            };

            btnPesquisar.addActionListener(ev -> atualizarTabelaTodosProdutos.run());
            txtPesquisa.addActionListener(ev -> atualizarTabelaTodosProdutos.run()); // Pesquisar ao pressionar Enter
            atualizarTabelaTodosProdutos.run(); // Carregar todos os produtos inicialmente

            JPanel painelInputQuantidade = new JPanel(new FlowLayout());
            painelInputQuantidade.add(new JLabel("Quantidade:"));
            JTextField txtQuantidade = new JTextField(5);
            painelInputQuantidade.add(txtQuantidade);

            JButton btnConfirmarProduto = new JButton("Adicionar Selecionado");
            btnConfirmarProduto.addActionListener(ev -> {
                int linhaSelecionada = tabelaTodosProdutos.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    try {
                        int idProduto = (int) modelTodosProdutos.getValueAt(linhaSelecionada, 0);
                        int quantidade = Integer.parseInt(txtQuantidade.getText());
                        adicionarProdutoAEncomenda(selecaoProdutoDialog, idProduto, quantidade, produtosSelecionados, modelProdutosEncomenda);
                        txtQuantidade.setText("");
                        selecaoProdutoDialog.dispose(); // Fechar o diálogo após adicionar
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(selecaoProdutoDialog, "Quantidade deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(selecaoProdutoDialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(selecaoProdutoDialog, "Por favor, selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });
            painelInputQuantidade.add(btnConfirmarProduto);

            JPanel painelNorteSelecaoProduto = new JPanel(new BorderLayout());
            painelNorteSelecaoProduto.add(painelPesquisa, BorderLayout.NORTH);
            painelNorteSelecaoProduto.add(scrollTodosProdutos, BorderLayout.CENTER);

            selecaoProdutoDialog.add(painelNorteSelecaoProduto, BorderLayout.CENTER);
            selecaoProdutoDialog.add(painelInputQuantidade, BorderLayout.SOUTH);
            selecaoProdutoDialog.setLocationRelativeTo(dialog);
            selecaoProdutoDialog.setVisible(true);
        });

        JButton btnFinalizarEncomenda = new JButton("Finalizar Encomenda");
        btnFinalizarEncomenda.addActionListener(e -> {
            if (clienteSelecionado[0] == null) {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um cliente válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (produtosSelecionados.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, adicione produtos à encomenda.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                farmacia.registarEncomenda(clienteSelecionado[0], produtosSelecionados);
                atualizarTabela();
                if (painelProdutos != null) {
                    painelProdutos.atualizarTabela();
                }
                dialog.dispose();
            } catch (IllegalStateException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.add(scrollProdutos, BorderLayout.CENTER);
        painelCentro.add(painelAdicionarProduto, BorderLayout.SOUTH);

        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelSul.add(btnFinalizarEncomenda);

        dialog.add(painelCliente, BorderLayout.NORTH);
        dialog.add(painelCentro, BorderLayout.CENTER);
        dialog.add(painelSul, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Adiciona um produto à lista de produtos selecionados para a encomenda.
     * @param parentComponent Componente pai para exibir mensagens de erro.
     * @param idProduto ID do produto a adicionar.
     * @param quantidade Quantidade do produto a adicionar.
     * @param produtosSelecionados Mapa de produtos já selecionados para a encomenda.
     * @param modelProdutosEncomenda Modelo da tabela que exibe os produtos da encomenda.
     */
    private void adicionarProdutoAEncomenda(Component parentComponent, int idProduto, int quantidade, Map<Produto, Integer> produtosSelecionados, DefaultTableModel modelProdutosEncomenda) {
        Optional<Produto> produtoOpt = farmacia.acharProdutoPorId(idProduto);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            if (quantidade > 0 && produto.getStock() >= quantidade) {
                produtosSelecionados.put(produto, produtosSelecionados.getOrDefault(produto, 0) + quantidade);
                // Atualizar a tabela de produtos da encomenda
                modelProdutosEncomenda.setRowCount(0);
                for (Map.Entry<Produto, Integer> entry : produtosSelecionados.entrySet()) {
                    modelProdutosEncomenda.addRow(new Object[]{
                            entry.getKey().getId(),
                            entry.getKey().getNome(),
                            String.format("%.2f€", entry.getKey().getPreco()),
                            entry.getKey().getStock(),
                            entry.getValue()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(parentComponent, "Stock insuficiente ou quantidade inválida para " + produto.getNome(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(parentComponent, "Produto com ID " + idProduto + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre um diálogo para editar uma encomenda existente.
     * Permite editar produtos, estado e data/hora da encomenda.
     * A data/hora pode ser editada para permitir análises temporais precisas nas estatísticas.
     */
    private void editarEncomenda() {
        int linhaSelecionada = tabelaEncomendas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idEncomenda = (int) tableModel.getValueAt(linhaSelecionada, 0);
            Optional<Encomenda> encomendaOpt = farmacia.acharEncomendaPorId(idEncomenda);

            if (encomendaOpt.isPresent()) {
                Encomenda encomenda = encomendaOpt.get();
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Encomenda ID: " + idEncomenda, true);
                dialog.setSize(700, 600); // Ajustar tamanho para acomodar campo de data/hora
                dialog.setLocationRelativeTo(this);

                JPanel panelEdicao = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                Cliente clienteEncomenda = encomenda.getCliente();

                // --- Informação do Cliente ---
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                panelEdicao.add(new JLabel("Cliente:"), gbc);

                gbc.gridx = 1;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JTextField txtClienteNome = new JTextField(clienteEncomenda.getNome());
                txtClienteNome.setEditable(false);
                panelEdicao.add(txtClienteNome, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                panelEdicao.add(new JLabel("NIF:"), gbc);

                gbc.gridx = 1;
                JTextField txtNifCliente = new JTextField(clienteEncomenda.getNif());
                txtNifCliente.setEditable(false);
                panelEdicao.add(txtNifCliente, gbc);

                // --- Edição de Produtos ---
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                panelEdicao.add(new JLabel("Produtos da Encomenda:"), gbc);

                String[] colunasProdutos = {"ID", "Nome", "Preço", "Quantidade"};
                DefaultTableModel modelProdutosEdicao = new DefaultTableModel(colunasProdutos, 0);
                JTable tabelaProdutosEdicao = new JTable(modelProdutosEdicao);
                JScrollPane scrollProdutosEdicao = new JScrollPane(tabelaProdutosEdicao);
                gbc.gridy = 3;
                gbc.weighty = 1.0; // Faz a tabela expandir verticalmente
                gbc.fill = GridBagConstraints.BOTH;
                panelEdicao.add(scrollProdutosEdicao, gbc);
                gbc.weighty = 0; // Resetar weighty
                gbc.fill = GridBagConstraints.HORIZONTAL;

                Map<Produto, Integer> produtosAtuais = new HashMap<>(encomenda.getProdutos());
                // Preencher a tabela com os produtos atuais da encomenda
                atualizarTabelaProdutosEdicao(modelProdutosEdicao, produtosAtuais);

                // Botão para adicionar/remover produtos
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 2;
                JButton btnGerirProdutos = new JButton("Gerir Produtos da Encomenda");
                panelEdicao.add(btnGerirProdutos, gbc);

                btnGerirProdutos.addActionListener(e -> {
                    JDialog gerirProdutosDialog = new JDialog(dialog, "Gerir Produtos da Encomenda", true);
                    gerirProdutosDialog.setLayout(new BorderLayout());
                    gerirProdutosDialog.setSize(700, 500);

                    // Painel de pesquisa
                    JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JComboBox<String> cbTipoPesquisa = new JComboBox<>(new String[]{"Nome", "ID"});
                    JTextField txtPesquisa = new JTextField(15);
                    JButton btnPesquisar = new JButton("Pesquisar");
                    painelPesquisa.add(new JLabel("Pesquisar por:"));
                    painelPesquisa.add(cbTipoPesquisa);
                    painelPesquisa.add(txtPesquisa);
                    painelPesquisa.add(btnPesquisar);

                    String[] colunasTodosProdutos = {"ID", "Nome", "Preço", "Stock Disponível"};
                    DefaultTableModel modelTodosProdutos = new DefaultTableModel(colunasTodosProdutos, 0);
                    JTable tabelaTodosProdutos = new JTable(modelTodosProdutos);
                    JScrollPane scrollTodosProdutos = new JScrollPane(tabelaTodosProdutos);

                    // Função para atualizar a tabela de produtos disponíveis
                    Runnable atualizarTabelaTodosProdutos = () -> {
                        modelTodosProdutos.setRowCount(0);
                        String termoPesquisa = txtPesquisa.getText().toLowerCase();
                        String tipoPesquisa = (String) cbTipoPesquisa.getSelectedItem();

                        farmacia.getProdutos().stream()
                                .filter(p -> {
                                    if (termoPesquisa.isEmpty()) return true;
                                    if ("Nome".equals(tipoPesquisa)) {
                                        return p.getNome().toLowerCase().contains(termoPesquisa);
                                    } else { // ID
                                        return String.valueOf(p.getId()).contains(termoPesquisa);
                                    }
                                })
                                .forEach(p -> modelTodosProdutos.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f€", p.getPreco()), p.getStock()}));
                    };

                    btnPesquisar.addActionListener(ev -> atualizarTabelaTodosProdutos.run());
                    txtPesquisa.addActionListener(ev -> atualizarTabelaTodosProdutos.run()); // Pesquisar ao pressionar Enter
                    atualizarTabelaTodosProdutos.run(); // Carregar todos os produtos inicialmente

                    JPanel painelInput = new JPanel(new FlowLayout());
                    painelInput.add(new JLabel("Quantidade:"));
                    JTextField txtQtd = new JTextField(5);
                    painelInput.add(txtQtd);
                    JButton btnAdd = new JButton("Adicionar");
                    JButton btnRem = new JButton("Remover");
                    JButton btnGuardar = new JButton("Guardar");
                    painelInput.add(btnAdd);
                    painelInput.add(btnRem);
                    painelInput.add(btnGuardar);

                    // Novo JTextArea para exibir produtos atuais
                    JTextArea txtAreaProdutosAtuais = new JTextArea(5, 30); // 5 linhas, 30 colunas
                    txtAreaProdutosAtuais.setEditable(false);
                    JScrollPane scrollProdutosAtuais = new JScrollPane(txtAreaProdutosAtuais);

                    // Helper para atualizar o display de produtos atuais
                    Runnable atualizarDisplayProdutosAtuais = () -> {
                        StringBuilder sb = new StringBuilder("Produtos na Encomenda:\n");
                        if (produtosAtuais.isEmpty()) {
                            sb.append("Nenhum produto adicionado.");
                        } else {
                            for (Map.Entry<Produto, Integer> entry : produtosAtuais.entrySet()) {
                                sb.append(entry.getKey().getNome())
                                  .append(" (ID: ")
                                  .append(entry.getKey().getId())
                                  .append("): ")
                                  .append(entry.getValue())
                                  .append(" unidades\n");
                            }
                        }
                        txtAreaProdutosAtuais.setText(sb.toString());
                    };

                    btnAdd.addActionListener(ev -> {
                        int linha = tabelaTodosProdutos.getSelectedRow();
                        if (linha >= 0) {
                            try {
                                int idProd = (int) modelTodosProdutos.getValueAt(linha, 0);
                                int qtd = Integer.parseInt(txtQtd.getText());
                                Optional<Produto> prodOpt = farmacia.acharProdutoPorId(idProd);
                                if (prodOpt.isPresent()) {
                                    Produto p = prodOpt.get();
                                    if (qtd > 0) {
                                        int quantidadeAtual = produtosAtuais.getOrDefault(p, 0);
                                        int quantidadeOriginal = encomenda.getProdutos().getOrDefault(p, 0);
                                        int stockDisponivel = p.getStock() + quantidadeOriginal - quantidadeAtual;
                                        if (qtd > stockDisponivel) {
                                            JOptionPane.showMessageDialog(gerirProdutosDialog, "Stock insuficiente para o produto selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
                                        produtosAtuais.put(p, quantidadeAtual + qtd);
                                        atualizarTabelaProdutosEdicao(modelProdutosEdicao, produtosAtuais);
                                        txtQtd.setText("");
                                        atualizarDisplayProdutosAtuais.run(); // Atualizar display
                                    } else {
                                        JOptionPane.showMessageDialog(gerirProdutosDialog, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(gerirProdutosDialog, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(gerirProdutosDialog, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(gerirProdutosDialog, "Selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        }
                    });

                    btnRem.addActionListener(ev -> {
                        int linha = tabelaTodosProdutos.getSelectedRow();
                        if (linha >= 0) {
                            try {
                                int idProd = (int) modelTodosProdutos.getValueAt(linha, 0);
                                int qtd = Integer.parseInt(txtQtd.getText());
                                Optional<Produto> prodOpt = farmacia.acharProdutoPorId(idProd);
                                if (prodOpt.isPresent()) {
                                    Produto p = prodOpt.get();
                                    if (qtd > 0 && produtosAtuais.containsKey(p) && produtosAtuais.get(p) >= qtd) {
                                        produtosAtuais.put(p, produtosAtuais.get(p) - qtd);
                                        if (produtosAtuais.get(p) == 0) {
                                            produtosAtuais.remove(p);
                                        }
                                        atualizarTabelaProdutosEdicao(modelProdutosEdicao, produtosAtuais);
                                        txtQtd.setText("");
                                        atualizarDisplayProdutosAtuais.run(); // Atualizar display
                                    } else {
                                        JOptionPane.showMessageDialog(gerirProdutosDialog, "Quantidade a remover inválida ou produto não na encomenda.", "Erro", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(gerirProdutosDialog, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(gerirProdutosDialog, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(gerirProdutosDialog, "Selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        }
                                        });

                    btnGuardar.addActionListener(ev -> gerirProdutosDialog.dispose());

                    JPanel painelNorteGerirProduto = new JPanel(new BorderLayout());
                    painelNorteGerirProduto.add(painelPesquisa, BorderLayout.NORTH);
                    painelNorteGerirProduto.add(scrollTodosProdutos, BorderLayout.CENTER);

                    JPanel painelSulGerirProduto = new JPanel(new BorderLayout());
                    painelSulGerirProduto.add(painelInput, BorderLayout.NORTH); // Botões em cima
                    painelSulGerirProduto.add(scrollProdutosAtuais, BorderLayout.CENTER); // Display abaixo

                    gerirProdutosDialog.add(painelNorteGerirProduto, BorderLayout.CENTER);
                    gerirProdutosDialog.add(painelSulGerirProduto, BorderLayout.SOUTH); // Novo painel no SOUTH
                    gerirProdutosDialog.setLocationRelativeTo(dialog);
                    atualizarDisplayProdutosAtuais.run(); // Carregar display inicial
                    gerirProdutosDialog.setVisible(true);
                });

                // --- Seleção de Estado ---
                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.gridwidth = 1;
                panelEdicao.add(new JLabel("Estado:"), gbc);
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                JComboBox<String> cbEstado = new JComboBox<>();
                for (Encomenda.EstadoEncomenda estado : Encomenda.EstadoEncomenda.values()) {
                    cbEstado.addItem(estado.getDescricao());
                }
                cbEstado.setSelectedItem(encomenda.getEstado().getDescricao());
                panelEdicao.add(cbEstado, gbc);

                // --- Edição de Data/Hora ---
                gbc.gridx = 0;
                gbc.gridy = 6;
                gbc.gridwidth = 1;
                JLabel lblDataHora = new JLabel("Data/Hora:");
                lblDataHora.setToolTipText("Formato: dd/MM/yyyy HH:mm (ex: 25/12/2024 14:30)");
                panelEdicao.add(lblDataHora, gbc);
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                JTextField txtDataHora = new JTextField(20);
                if (encomenda.getDataHora() != null) {
                    txtDataHora.setText(encomenda.getDataHora().format(formatter));
                } else {
                    txtDataHora.setText(LocalDateTime.now().format(formatter));
                }
                txtDataHora.setToolTipText("Formato: dd/MM/yyyy HH:mm (ex: 25/12/2024 14:30)");
                panelEdicao.add(txtDataHora, gbc);

                JButton btnSalvar = new JButton("Salvar");
                btnSalvar.addActionListener(e -> {
                    try {
                        // Salvar Produtos
                        farmacia.atualizarProdutosEncomenda(idEncomenda, produtosAtuais);

                        // Salvar Estado
                        String novoEstadoStr = (String) cbEstado.getSelectedItem();
                        Encomenda.EstadoEncomenda novoEstado = null;
                        for (Encomenda.EstadoEncomenda estado : Encomenda.EstadoEncomenda.values()) {
                            if (estado.getDescricao().equals(novoEstadoStr)) {
                                novoEstado = estado;
                                break;
                            }
                        }
                        if (novoEstado != null) {
                            farmacia.atualizarEstadoEncomenda(idEncomenda, novoEstado);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Estado inválido selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Salvar Data/Hora
                        try {
                            LocalDateTime novaDataHora = LocalDateTime.parse(txtDataHora.getText().trim(), formatter);
                            encomenda.setDataHora(novaDataHora);
                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(dialog, "Formato de data/hora inválido. Use o formato: dd/MM/yyyy HH:mm", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        atualizarTabela();
                        if (painelProdutos != null) {
                            painelProdutos.atualizarTabela();
                        }
                        dialog.dispose();
                    } catch (IllegalStateException | IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                });

                JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                panelBotoes.add(btnSalvar);

                dialog.add(panelEdicao, BorderLayout.CENTER);
                dialog.add(panelBotoes, BorderLayout.SOUTH);

                dialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Encomenda não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma encomenda para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Atualiza a tabela de produtos dentro do diálogo de edição de encomenda.
     * @param model Tabela de produtos da encomenda.
     * @param produtos Mapa de produtos e quantidades.
     */
    private void atualizarTabelaProdutosEdicao(DefaultTableModel model, Map<Produto, Integer> produtos) {
        model.setRowCount(0);
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            model.addRow(new Object[]{
                    entry.getKey().getId(),
                    entry.getKey().getNome(),
                    String.format("%.2f€", entry.getKey().getPreco()),
                    entry.getValue()
            });
        }
    }

    private void removerEncomenda() {
        int linhaSelecionada = tabelaEncomendas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idEncomenda = (int) tableModel.getValueAt(linhaSelecionada, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem a certeza que deseja remover a encomenda ID " + idEncomenda + "? Esta ação irá repor o stock dos produtos.", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    farmacia.removerEncomenda(idEncomenda);
                    atualizarTabela();
                    painelProdutos.atualizarTabela(); // Atualizar a tabela de produtos
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma encomenda para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}