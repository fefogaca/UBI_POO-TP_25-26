
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

/**
 * Painel Swing responsável pela gestão de clientes.
 * <p>
 * Exibe uma tabela com os clientes registados e disponibiliza operações de
 * criação, edição e remoção através de diálogos modais. A tabela utiliza cores
 * alternadas para melhor legibilidade e inclui validação de NIF português
 * usando o algoritmo oficial de validação.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class PainelGestaoClientes extends JPanel {

    private Farmacia farmacia;
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;

    /**
     * Cria o painel associado à farmácia fornecida.
     *
     * @param farmacia instância a partir da qual serão lidos e atualizados os clientes
     */
    public PainelGestaoClientes(Farmacia farmacia) {
        this.farmacia = farmacia;
        setLayout(new BorderLayout());

        String[] colunas = {"Nome", "NIF", "Morada"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaClientes = new JTable(tableModel);
        tabelaClientes.setRowHeight(25);
        aplicarCoresAlternadasTabela(tabelaClientes);
        atualizarTabela();

        tabelaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarCliente();
                }
            }
        });

        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.setToolTipText("Adiciona um novo cliente à base de dados");
        btnEditar.setToolTipText("Edita os dados do cliente selecionado (duplo clique também funciona)");
        btnRemover.setToolTipText("Remove o cliente selecionado da base de dados");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnRemover.addActionListener(e -> removerCliente());
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

    private void atualizarTabela() {
        tableModel.setRowCount(0);
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
            String nome = txtNome.getText().trim();
            String nif = txtNif.getText().trim();
            String rua = txtRua.getText().trim();
            String cidade = txtCidade.getText().trim();
            String codigoPostal = txtCodPostal.getText().trim();

            if (nome.isEmpty() || nif.isEmpty() || rua.isEmpty() || cidade.isEmpty() || codigoPostal.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!ValidadorUtil.nifValido(nif)) {
                JOptionPane.showMessageDialog(dialog, "O NIF inserido é inválido. Verifique se contém 9 dígitos e se o dígito de controlo está correto.", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!ValidadorUtil.codigoPostalValido(codigoPostal)) {
                JOptionPane.showMessageDialog(dialog, "O Código Postal deve seguir o formato 0000-000.", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (farmacia.acharClienteNif(nif).isPresent()) {
                JOptionPane.showMessageDialog(dialog, "Já existe um cliente com o NIF indicado.", "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Morada morada = new Morada(rua, cidade, codigoPostal);
            Cliente cliente = new Cliente(nome, nif, morada);
            farmacia.adicionarCliente(cliente);
            atualizarTabela();
            dialog.dispose();
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
                String nome = txtNome.getText().trim();
                String nif = txtNif.getText().trim();
                String rua = txtRua.getText().trim();
                String cidade = txtCidade.getText().trim();
                String codigoPostal = txtCodPostal.getText().trim();

                if (nome.isEmpty() || nif.isEmpty() || rua.isEmpty() || cidade.isEmpty() || codigoPostal.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios.", "Validação", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!ValidadorUtil.nifValido(nif)) {
                    JOptionPane.showMessageDialog(dialog, "O NIF inserido é inválido. Verifique se contém 9 dígitos e se o dígito de controlo está correto.", "Validação", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Cliente clienteExistente = farmacia.acharClienteNif(nif).orElse(null);
                if (clienteExistente != null && clienteExistente != cliente) {
                    JOptionPane.showMessageDialog(dialog, "Já existe outro cliente com o NIF indicado.", "Validação", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!ValidadorUtil.codigoPostalValido(codigoPostal)) {
                    JOptionPane.showMessageDialog(dialog, "O Código Postal deve seguir o formato 0000-000.", "Validação", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                cliente.setNome(nome);
                cliente.setNif(nif);
                cliente.getMorada().setRua(rua);
                cliente.getMorada().setCidade(cidade);
                cliente.getMorada().setCodPostal(codigoPostal);
                atualizarTabela();
                dialog.dispose();
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
