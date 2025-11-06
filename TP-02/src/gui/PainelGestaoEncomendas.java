package gui;

import modelo.Cliente;
import modelo.Encomenda;
import modelo.Farmacia;
import modelo.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

public class PainelGestaoEncomendas extends JPanel {

    private Farmacia farmacia;
    private JTable tabelaEncomendas;
    private DefaultTableModel tableModel;
    private PainelGestaoProdutos painelProdutos; // Adicionar campo para PainelGestaoProdutos

    public PainelGestaoEncomendas(Farmacia farmacia, PainelGestaoProdutos painelProdutos) {
        this.farmacia = farmacia;
        this.painelProdutos = painelProdutos; // Armazenar a referência
        setLayout(new BorderLayout());

        // Tabela
        String[] colunas = {"ID", "Cliente", "Preço Total", "Estado"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna todas as células não editáveis
            }
        };
        tabelaEncomendas = new JTable(tableModel);
        atualizarTabela();

        // Adicionar MouseListener para duplo clique
        tabelaEncomendas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Deteta duplo clique
                    editarEncomenda();
                }
            }
        });

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        JButton btnNovaEncomenda = new JButton("Nova Encomenda");
        JButton btnEditarEncomenda = new JButton("Editar Encomenda");
        JButton btnRemoverEncomenda = new JButton("Remover Encomenda");

        painelBotoes.add(btnNovaEncomenda);
        painelBotoes.add(btnEditarEncomenda);
        painelBotoes.add(btnRemoverEncomenda);

        // Adicionar componentes ao painel
        add(new JScrollPane(tabelaEncomendas), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnNovaEncomenda.addActionListener(e -> novaEncomenda());
        btnEditarEncomenda.addActionListener(e -> editarEncomenda());
        btnRemoverEncomenda.addActionListener(e -> removerEncomenda());
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Encomenda> encomendas = farmacia.getEncomendas();
        for (Encomenda e : encomendas) {
            tableModel.addRow(new Object[]{e.getId(), e.getCliente().getNome(), String.format("%.2f€", e.getPrecoTotal()), e.getEstado().getDescricao()});
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

    private void editarEncomenda() {
        int linhaSelecionada = tabelaEncomendas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idEncomenda = (int) tableModel.getValueAt(linhaSelecionada, 0);
            Optional<Encomenda> encomendaOpt = farmacia.acharEncomendaPorId(idEncomenda);

            if (encomendaOpt.isPresent()) {
                Encomenda encomenda = encomendaOpt.get();
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Encomenda ID: " + idEncomenda, true);
                dialog.setSize(700, 550); // Ajustar tamanho
                dialog.setLocationRelativeTo(this);

                JPanel panelEdicao = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // --- Seleção de Cliente ---
                gbc.gridx = 0;
                gbc.gridy = 0;
                panelEdicao.add(new JLabel("Cliente:"), gbc);
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                JComboBox<Cliente> cbClientes = new JComboBox<>(new Vector<>(farmacia.getClientes()));
                cbClientes.setSelectedItem(encomenda.getCliente());
                panelEdicao.add(cbClientes, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.weightx = 0;
                panelEdicao.add(new JLabel("NIF:"), gbc);
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                JTextField txtNifCliente = new JTextField(encomenda.getCliente().getNif());
                txtNifCliente.setEditable(false);
                panelEdicao.add(txtNifCliente, gbc);

                cbClientes.addActionListener(e -> {
                    Cliente clienteSelecionado = (Cliente) cbClientes.getSelectedItem();
                    if (clienteSelecionado != null) {
                        txtNifCliente.setText(clienteSelecionado.getNif());
                    } else {
                        txtNifCliente.setText("");
                    }
                });

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
                    painelInput.add(btnAdd);
                    painelInput.add(btnRem);

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
                                        // A verificação de stock será feita no momento de salvar a encomenda
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
                JComboBox<String> cbEstado = new JComboBox<>();
                for (Encomenda.EstadoEncomenda estado : Encomenda.EstadoEncomenda.values()) {
                    cbEstado.addItem(estado.getDescricao());
                }
                cbEstado.setSelectedItem(encomenda.getEstado().getDescricao());
                panelEdicao.add(cbEstado, gbc);

                JButton btnSalvar = new JButton("Salvar");
                btnSalvar.addActionListener(e -> {
                    try {
                        // Salvar Cliente (se mudou)
                        Cliente novoCliente = (Cliente) cbClientes.getSelectedItem();
                        if (!encomenda.getCliente().equals(novoCliente)) {
                            // Se o cliente mudou, é uma operação complexa que pode exigir recriação da encomenda
                            // Por simplicidade, vamos apenas atualizar o objeto cliente na encomenda
                            // Em um sistema real, isso teria implicações maiores.
                            // encomenda.setCliente(novoCliente); // Se houvesse um setter
                            JOptionPane.showMessageDialog(dialog, "A alteração do cliente de uma encomenda existente não é suportada diretamente. Por favor, crie uma nova encomenda.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

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

                        atualizarTabela();
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