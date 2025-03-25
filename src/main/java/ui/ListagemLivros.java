package ui;

import dao.LivroDAO;
import exception.DAOException;
import model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListagemLivros extends JFrame {

    private JTable tabelaLivros;
    private DefaultTableModel tableModel;
    private LivroDAO livroDAO;

    public ListagemLivros() {
        livroDAO = new LivroDAO();
        initUI();
        carregarLivros();
    }

    private void initUI() {

        setTitle("Listagem de Livros");
        setSize(1200, 800);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Botões
        JButton btnIncluir = new JButton("Incluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnPesquisar = new JButton("Pesquisar");

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        panelBotoes.add(btnIncluir);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnPesquisar);

        // Tabela de livros com coluna para Livros Semelhantes
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Título", "Autores", "Data Publicação", "ISBN", "Editora", "Livros Semelhantes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas as células são não editáveis
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Long.class; // ID
                if (columnIndex == 3) return java.util.Date.class; // Data Publicação
                return String.class; // Todas as outras colunas são Strings
            }
        };

        tabelaLivros = new JTable(tableModel);
        tabelaLivros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Permite ajustar manualmente as colunas

        // Configuração das colunas
        tabelaLivros.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabelaLivros.getColumnModel().getColumn(1).setPreferredWidth(250); // Título
        tabelaLivros.getColumnModel().getColumn(2).setPreferredWidth(200); // Autores
        tabelaLivros.getColumnModel().getColumn(3).setPreferredWidth(100); // Data Publicação
        tabelaLivros.getColumnModel().getColumn(4).setPreferredWidth(150); // ISBN
        tabelaLivros.getColumnModel().getColumn(5).setPreferredWidth(150); // Editora
        tabelaLivros.getColumnModel().getColumn(6).setPreferredWidth(300); // Livros Semelhantes

        JScrollPane scrollPane = new JScrollPane(tabelaLivros);

        // Layout com os posionamento na tela
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelBotoes, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Ações dos botões
        btnIncluir.addActionListener(e -> abrirCadastroLivro(null));
        btnEditar.addActionListener(e -> editarLivro());
        btnExcluir.addActionListener(e -> excluirLivro());
        btnPesquisar.addActionListener(e -> abrirPesquisaLivro());
    }

    private void carregarLivros() {
        try {
            List<Livro> livros = livroDAO.listarTodos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (Livro livro : livros) {
                // Adiciona a linha à tabela com o novo campo
                tableModel.addRow(new Object[]{
                        livro.getId(),
                        livro.getTitulo(),
                        livro.getAutores(),
                        livro.getDataPublicacao(),
                        livro.getIsbn(),
                        livro.getEditora(),
                        livro.getLivrosSemelhantes()
                });
            }
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar livros: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCadastroLivro(Livro livro) {
        CadastroLivro cadastro = new CadastroLivro(this, livro);
        cadastro.setVisible(true);
        cadastro.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                carregarLivros();
            }
        });
    }

    private void editarLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow();
        if (linhaSelecionada >= 0) {
            Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
            try {
                Livro livro = livroDAO.buscarPorId(id);
                abrirCadastroLivro(livro);
            } catch (DAOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao buscar livro: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.");
        }
    }

    private void excluirLivro() {
        int linhaSelecionada = tabelaLivros.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Tem certeza que deseja excluir este livro?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
                try {
                    livroDAO.excluirLivro(id);
                    carregarLivros();
                    JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
                } catch (DAOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir livro: " + e.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.");
        }
    }

    private void abrirPesquisaLivro() {
        PesquisaLivro pesquisa = new PesquisaLivro(this);
        pesquisa.setVisible(true);
        pesquisa.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                carregarLivros();
            }
        });
    }

}