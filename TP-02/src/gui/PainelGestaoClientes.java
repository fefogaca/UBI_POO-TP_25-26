
package gui;

import modelo.Cliente;
import modelo.Farmacia;
import modelo.Morada;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PainelGestaoClientes extends JPanel {

    private Farmacia farmacia;
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;

    public PainelGestaoClientes(Farmacia farmacia) {
        this.farmacia = farmacia;
        setLayout(new BorderLayout());

        // Tabela
        String[] colunas = {"Nome", "NIF", "Morada"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna todas as células não editáveis
            }
        };
        tabelaClientes = new JTable(tableModel);
        atualizarTabela();

        // Adicionar MouseListener para duplo clique
        tabelaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Deteta duplo clique
                    editarCliente();
                }
            }
        });

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        // Adicionar componentes ao painel
        add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> adicionarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnRemover.addActionListener(e -> removerCliente());
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Cliente> clientes = farmacia.getClientes();
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{c.getNome(), c.getNif(), c.getMorada().toString()});
        }
    }

    private void adicionarCliente() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Cliente", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNome = new JTextField(20);
        JTextField txtNif = new JTextField(15);
        JTextField txtRua = new JTextField(20);
        JTextField txtCidade = new JTextField(15);
        JTextField txtCodPostal = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("NIF:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtNif, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Rua:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtRua, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtCidade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("Código Postal:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtCodPostal, gbc);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            try {
                Morada morada = new Morada(txtRua.getText(), txtCidade.getText(), txtCodPostal.getText());
                Cliente cliente = new Cliente(txtNome.getText(), txtNif.getText(), morada);
                farmacia.adicionarCliente(cliente);
                atualizarTabela();
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotoes.add(btnSalvar);

        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(panelBotoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void editarCliente() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            String nifCliente = (String) tableModel.getValueAt(linhaSelecionada, 1);
            Cliente cliente = farmacia.acharClienteNif(nifCliente)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Cliente", true);
            dialog.setSize(400, 280);
            dialog.setLocationRelativeTo(this);

            JPanel panelForm = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField txtNome = new JTextField(cliente.getNome(), 20);
            JTextField txtNif = new JTextField(cliente.getNif(), 15);
            txtNif.setEditable(false); // NIF não deve ser editável
            JTextField txtRua = new JTextField(cliente.getMorada().getRua(), 20);
            JTextField txtCidade = new JTextField(cliente.getMorada().getCidade(), 15);
            JTextField txtCodPostal = new JTextField(cliente.getMorada().getCodPostal(), 10);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panelForm.add(new JLabel("Nome:"), gbc);
            gbc.gridx = 1;
            panelForm.add(txtNome, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panelForm.add(new JLabel("NIF:"), gbc);
            gbc.gridx = 1;
            panelForm.add(txtNif, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panelForm.add(new JLabel("Rua:"), gbc);
            gbc.gridx = 1;
            panelForm.add(txtRua, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panelForm.add(new JLabel("Cidade:"), gbc);
            gbc.gridx = 1;
            panelForm.add(txtCidade, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panelForm.add(new JLabel("Código Postal:"), gbc);
            gbc.gridx = 1;
            panelForm.add(txtCodPostal, gbc);

            JButton btnSalvar = new JButton("Salvar");
            btnSalvar.addActionListener(e -> {
                try {
                    cliente.setNome(txtNome.getText());
                    cliente.getMorada().setRua(txtRua.getText());
                    cliente.getMorada().setCidade(txtCidade.getText());
                    cliente.getMorada().setCodPostal(txtCodPostal.getText());
                    atualizarTabela();
                    dialog.dispose();
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
            JOptionPane.showMessageDialog(this, "Por favor, selecione um cliente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerCliente() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            String nif = (String) tableModel.getValueAt(linhaSelecionada, 1);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem a certeza que deseja remover o cliente com NIF " + nif + "?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    farmacia.removerCliente(nif);
                    atualizarTabela();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um cliente para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
