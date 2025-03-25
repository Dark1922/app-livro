package ui;

import dao.LivroDAO;
import exception.DAOException;
import model.Livro;
import util.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PesquisaLivro extends JDialog {

    private JTextField txtPesquisa;
    private JButton btnPesquisar;
    private JTable tabelaLivros;
    private DefaultTableModel tableModel;
    private LivroDAO livroDAO;
    private JTextArea txtLivrosSemelhantes;

    public PesquisaLivro(JFrame parent) {
        super(parent, "Pesquisar Livros", true);
        this.livroDAO = new LivroDAO();
        Utils.setFrameIcon(parent, "img/logo.png", 100, 100);
        initUI();
    }

    private void initUI() {
        setSize(1000, 700);
        setLocationRelativeTo(getParent());

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel de pesquisa
        JPanel panelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelPesquisa.add(new JLabel("Pesquisar:"));
        txtPesquisa = new JTextField(30);
        panelPesquisa.add(txtPesquisa);
        btnPesquisar = new JButton("Pesquisar");
        panelPesquisa.add(btnPesquisar);

        // Tabela de resultados
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Título", "Autores", "Data Publicação", "ISBN", "Editora", "Livros Semelhantes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Long.class;
                if (columnIndex == 3) return java.util.Date.class;
                return String.class;
            }
        };

        tabelaLivros = new JTable(tableModel);
        tabelaLivros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Configuração das colunas
        tabelaLivros.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabelaLivros.getColumnModel().getColumn(1).setPreferredWidth(250); // Título
        tabelaLivros.getColumnModel().getColumn(2).setPreferredWidth(200); // Autores
        tabelaLivros.getColumnModel().getColumn(3).setPreferredWidth(100); // Data
        tabelaLivros.getColumnModel().getColumn(4).setPreferredWidth(150); // ISBN
        tabelaLivros.getColumnModel().getColumn(5).setPreferredWidth(150); // Editora
        tabelaLivros.getColumnModel().getColumn(6).setPreferredWidth(300); // Semelhantes

        JScrollPane tableScrollPane = new JScrollPane(tabelaLivros);

        // Painel de detalhes com livros semelhantes
        JPanel panelDetalhes = new JPanel(new BorderLayout());
        panelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes"));

        txtLivrosSemelhantes = new JTextArea(5, 80);
        txtLivrosSemelhantes.setEditable(false);
        txtLivrosSemelhantes.setLineWrap(true);
        txtLivrosSemelhantes.setWrapStyleWord(true);
        JScrollPane scrollSemelhantes = new JScrollPane(txtLivrosSemelhantes);
        panelDetalhes.add(scrollSemelhantes, BorderLayout.CENTER);

        // Layout principal
        mainPanel.add(panelPesquisa, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(panelDetalhes, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Ações
        btnPesquisar.addActionListener(e -> pesquisarLivros());
        tabelaLivros.getSelectionModel().addListSelectionListener(e -> mostrarLivrosSemelhantes());
    }

    private void pesquisarLivros() {
        String termo = txtPesquisa.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um termo para pesquisa");
            return;
        }

        try {
            List<Livro> livros = livroDAO.pesquisar(termo);
            tableModel.setRowCount(0);

            for (Livro livro : livros) {
                tableModel.addRow(new Object[]{
                        livro.getId(),
                        livro.getTitulo(),
                        livro.getAutores(),
                        livro.getDataPublicacao(),
                        livro.getIsbn(),
                        livro.getEditora(),
                        formatarSemelhantes(livro.getLivrosSemelhantes())
                });
            }

            if (livros.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum livro encontrado");
            }
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao pesquisar livros: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarLivrosSemelhantes() {
        int linhaSelecionada = tabelaLivros.getSelectedRow();
        if (linhaSelecionada >= 0) {
            String semelhantes = (String) tableModel.getValueAt(linhaSelecionada, 6);
            txtLivrosSemelhantes.setText(semelhantes.replace(", ", "\n"));
        }
    }

    private String formatarSemelhantes(String semelhantes) {
        if (semelhantes == null || semelhantes.isEmpty()) {
            return "Nenhum livro semelhante cadastrado";
        }
        return semelhantes;
    }
}