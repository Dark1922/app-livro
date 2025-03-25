package ui;

import dao.LivroDAO;
import service.ImportadorLivrosService;
import util.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
        initUI();
    }

    private void initUI() {

        setTitle("Menu Principal");
        setSize(600, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Botões
        JButton btnListagem = new JButton("Listagem de Livros");
        JButton btnCadastro = new JButton("Cadastrar Livro");
        JButton btnPesquisa = new JButton("Pesquisar Livros");
        JButton btnImportar = new JButton("Importar Livros");

        // Definindo um tamanho fixo para os botões
        Dimension tamanhoBotao = new Dimension(200, 40);
        btnListagem.setPreferredSize(tamanhoBotao);
        btnListagem.setMinimumSize(tamanhoBotao); // Garante que o botão não fique menor
        btnListagem.setMaximumSize(tamanhoBotao); // Garante que o botão não fique maior
        btnCadastro.setPreferredSize(tamanhoBotao);
        btnCadastro.setMinimumSize(tamanhoBotao);
        btnCadastro.setMaximumSize(tamanhoBotao);
        btnPesquisa.setPreferredSize(tamanhoBotao);
        btnPesquisa.setMinimumSize(tamanhoBotao);
        btnPesquisa.setMaximumSize(tamanhoBotao);
        btnImportar.setPreferredSize(tamanhoBotao);
        btnImportar.setMinimumSize(tamanhoBotao);
        btnImportar.setMaximumSize(tamanhoBotao);

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        panelBotoes.setLayout(new BoxLayout(panelBotoes, BoxLayout.Y_AXIS));
        panelBotoes.add(Box.createVerticalGlue()); // Espaço flexível no topo
        panelBotoes.add(btnListagem);
        panelBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotoes.add(btnCadastro);
        panelBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotoes.add(btnPesquisa);
        panelBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotoes.add(btnImportar);
        panelBotoes.add(Box.createVerticalGlue()); // Espaço flexível na base




        // Adicionando um espaçamento à esquerda no painel de botões
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // 50px de espaçamento à esquerda

        // Carregando as imagens
        ImageIcon livroIcon = new ImageIcon(getClass().getClassLoader().getResource("img/livro.png"));

        // Redimensionando as imagens
        Image livroImage = livroIcon.getImage().getScaledInstance(250, 300, Image.SCALE_SMOOTH);

        // Criando JLabels para as imagens
        JLabel livroLabel = new JLabel(new ImageIcon(livroImage));

        Utils.setFrameIcon(this, "img/logo.png", 100, 100);

        // Painel para a imagem do livro
        JPanel panelImagem = new JPanel(new BorderLayout());
        panelImagem.add(livroLabel, BorderLayout.CENTER);

        // Painel principal para organizar imagem e botões
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panelImagem, BorderLayout.WEST);
        panelPrincipal.add(panelBotoes, BorderLayout.CENTER);

        // Adicionando o painel principal ao JFrame
        getContentPane().add(panelPrincipal, BorderLayout.CENTER);

        // Ações dos botões
        btnListagem.addActionListener(e -> abrirListagemLivros());

        btnCadastro.addActionListener(e -> abrirCadastroLivro());

        btnPesquisa.addActionListener(e -> abrirPesquisaLivro());

        btnImportar.addActionListener(e -> importarLivros());
    }

    private void abrirListagemLivros() {
        ListagemLivros listagem = new ListagemLivros();
        listagem.setVisible(true);
    }

    private void abrirCadastroLivro() {
        CadastroLivro cadastro = new CadastroLivro(this, null);
        cadastro.setVisible(true);
    }

    private void abrirPesquisaLivro() {
        PesquisaLivro pesquisa = new PesquisaLivro(this);
        pesquisa.setVisible(true);
    }

    private void importarLivros() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Arquivos de Livros (*.csv, *.xml, *.txt)", "csv", "xml", "txt"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File arquivo = fileChooser.getSelectedFile();
                new ImportadorLivrosService(new LivroDAO()).importarArquivo(arquivo);
                JOptionPane.showMessageDialog(this, "Importação concluída com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro na importação: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal();
            menu.setVisible(true);
        });
    }
}