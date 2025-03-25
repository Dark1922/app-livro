package ui;

import dao.LivroDAO;
import exception.DAOException;
import exception.OpenLibraryServiceException;
import model.Livro;
import service.OpenLibraryService;
import util.DateUtils;
import util.Utils;
import validator.LivroValidator;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.ParseException;
import java.util.Date;

public class CadastroLivro extends JDialog {

    private JTextField txtTitulo;
    private JTextField txtAutores;
    private JFormattedTextField txtDataPublicacao;
    private JTextField txtIsbn;
    private JTextField txtEditora;
    private JTextArea txtLivrosSemelhantes;
    private JButton btnSalvar;
    private JButton btnBuscarIsbn;
    private Livro livro;
    private LivroDAO livroDAO;
    private OpenLibraryService openLibraryService;

    public CadastroLivro(JFrame parent, Livro livro) {
        super(parent, "Cadastro de Livro", true);
        this.livro = livro;
        this.livroDAO = new LivroDAO();
        this.openLibraryService = new OpenLibraryService();
        Utils.setFrameIcon(parent, "img/logo.png", 100, 100);
        initUI();
    }

    private void initUI() {
        setTitle("Cadastro de Livro");
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Painel principal com borda e padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel de formulário com GridBagLayout para melhor controle
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuração comum para labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setFont(labelFont);
        formPanel.add(lblTitulo, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtTitulo = new JTextField(25);
        formPanel.add(txtTitulo, gbc);

        // Autores
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblAutores = new JLabel("Autores:");
        lblAutores.setFont(labelFont);
        formPanel.add(lblAutores, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtAutores = new JTextField(25);
        formPanel.add(txtAutores, gbc);

        // Data Publicação
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDataPublicacao = new JLabel("Data Publicação:");
        lblDataPublicacao.setFont(labelFont);
        formPanel.add(lblDataPublicacao, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            txtDataPublicacao = new JFormattedTextField(dateFormatter);
            txtDataPublicacao.setColumns(10);
            txtDataPublicacao.setText(DateUtils.formatDate(new Date()));
        } catch (ParseException e) {
            txtDataPublicacao = new JFormattedTextField();
        }
        formPanel.add(txtDataPublicacao, gbc);

        // ISBN com botão de busca
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblIsbn = new JLabel("ISBN:");
        lblIsbn.setFont(labelFont);
        formPanel.add(lblIsbn, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JPanel isbnPanel = new JPanel(new BorderLayout(5, 0));
        txtIsbn = new JTextField(20);

        ((AbstractDocument) txtIsbn.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                // Remove caracteres não numéricos
                newText = newText.replaceAll("[^\\d]", "");

                // Permite apenas se tiver até 13 dígitos
                if (newText.matches("\\d{0,13}")) {
                    super.replace(fb, offset, length, text.replaceAll("[^\\d]", ""), attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                replace(fb, offset, 0, text, attr);
            }
        });
        isbnPanel.add(txtIsbn, BorderLayout.CENTER);

        // Botão com ícone de lupa
        JButton btnBuscarIsbn = new JButton();
        try {
            ImageIcon searchIcon = new ImageIcon(getClass().getResource("/img/search.png"));
            btnBuscarIsbn.setIcon(new ImageIcon(searchIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            btnBuscarIsbn.setText("Buscar");
        }
        btnBuscarIsbn.setToolTipText("Buscar livro por ISBN");
        btnBuscarIsbn.setPreferredSize(new Dimension(30, 25));
        btnBuscarIsbn.addActionListener(e -> buscarDadosPorIsbn());
        isbnPanel.add(btnBuscarIsbn, BorderLayout.EAST);

        formPanel.add(isbnPanel, gbc);

        // Editora
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblEditora = new JLabel("Editora:");
        lblEditora.setFont(labelFont);
        formPanel.add(lblEditora, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        txtEditora = new JTextField(25);
        formPanel.add(txtEditora, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblLivrosSemelhantes = new JLabel("Livros Semelhantes:");
        lblLivrosSemelhantes.setFont(labelFont);
        formPanel.add(lblLivrosSemelhantes, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        txtLivrosSemelhantes = new JTextArea(3, 25);
        txtLivrosSemelhantes.setLineWrap(true);
        txtLivrosSemelhantes.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtLivrosSemelhantes);
        formPanel.add(scrollPane, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setBackground(new Color(70, 130, 180));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.addActionListener(e -> {
            try {
                salvarLivro();
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(btnSalvar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Preenche campos se estiver editando
        if (livro != null) {
            preencherCampos(livro);
        }
    }

    private void buscarDadosPorIsbn() {
        String isbn = txtIsbn.getText().trim();
        try {
            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, informe o ISBN antes de buscar",
                        "ISBN Vazio",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Livro livro = openLibraryService.buscarLivroPorISBN(isbn);

            txtTitulo.setText(livro.getTitulo());
            txtAutores.setText(livro.getAutores());
            txtEditora.setText(livro.getEditora());
            txtDataPublicacao.setText(DateUtils.formatDate(livro.getDataPublicacao()));

            if (livro.getLivrosSemelhantes() != null && !livro.getLivrosSemelhantes().isEmpty()) {
                txtLivrosSemelhantes.setText(livro.getLivrosSemelhantes());
            } else {
                txtLivrosSemelhantes.setText("Nenhum livro semelhante encontrado");
            }

        } catch (OpenLibraryServiceException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Erro na Busca",
                    JOptionPane.ERROR_MESSAGE);
            txtLivrosSemelhantes.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ocorreu um erro inesperado: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos(Livro livro) {
        txtTitulo.setText(livro.getTitulo());
        txtAutores.setText(livro.getAutores());
        txtDataPublicacao.setText(DateUtils.formatDate(livro.getDataPublicacao()));
        txtEditora.setText(livro.getEditora());
        txtIsbn.setText(livro.getIsbn());

        // Exibe com quebras de linha para melhor visualização
        if (livro.getLivrosSemelhantes() != null && !livro.getLivrosSemelhantes().isEmpty()) {
            // Mantém as vírgulas no banco, mas exibe com quebras de linha
            txtLivrosSemelhantes.setText(livro.getLivrosSemelhantes().replace(", ", "\n"));
        }
    }

    private void salvarLivro() throws DAOException {
        if (livro == null) {
            livro = new Livro();
        }

        if (!LivroValidator.validarCampos(txtTitulo, txtAutores, txtDataPublicacao, txtIsbn, txtEditora, this)) {
            return;
        }

        livro.setTitulo(txtTitulo.getText());
        livro.setAutores(txtAutores.getText());
        livro.setDataPublicacao(DateUtils.parseDate(txtDataPublicacao.getText()));
        livro.setIsbn(txtIsbn.getText());
        livro.setEditora(txtEditora.getText());
        String semelhantes = txtLivrosSemelhantes.getText().trim();
        livro.setLivrosSemelhantes(semelhantes.replace("\n", ", "));
        if (livro.getId() == null) {
            livroDAO.inserirLivro(livro);
        } else {
            livroDAO.atualizarLivro(livro);
        }

        JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!");
        dispose();
    }
}