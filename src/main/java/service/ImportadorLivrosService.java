package service;

import dao.LivroDAO;
import model.Livro;
import service.strategy.ImportadorLivrosStrategy;
import service.strategy.impl.ImportadorCSV;
import service.strategy.impl.ImportadorTexto;
import service.strategy.impl.ImportadorXML;

import java.io.File;
import java.util.List;

public class ImportadorLivrosService {
    private final LivroDAO livroDAO;

    public ImportadorLivrosService(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    public void importarArquivo(File arquivo) throws Exception {

        ImportadorLivrosStrategy importador = getImportador(arquivo);
        List<Livro> livros = importador.importar(arquivo);

        for (Livro livro : livros) {
            List<Livro> existentes = livroDAO.pesquisar(livro.getIsbn());
            if (!existentes.isEmpty()) {
                // Atualiza livro existente
                Livro existente = existentes.get(0);
                existente.setTitulo(livro.getTitulo());
                existente.setAutores(livro.getAutores());
                existente.setDataPublicacao(livro.getDataPublicacao());
                existente.setEditora(livro.getEditora());
                existente.setLivrosSemelhantes(livro.getLivrosSemelhantes());
                livroDAO.atualizarLivro(existente);
            } else {
                livroDAO.inserirLivro(livro);
            }
        }
    }

    private ImportadorLivrosStrategy getImportador(File arquivo) {
        String nome = arquivo.getName().toLowerCase();
        if (nome.endsWith(".csv")) return new ImportadorCSV();
        if (nome.endsWith(".xml")) return new ImportadorXML();
        if (nome.endsWith(".txt")) return new ImportadorTexto();
        throw new IllegalArgumentException("Formato não suportado");
    }
}