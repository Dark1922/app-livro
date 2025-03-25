package service.strategy.impl;

import model.Livro;
import service.strategy.ImportadorLivrosStrategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ImportadorTexto implements ImportadorLivrosStrategy {

    @Override
    public List<Livro> importar(File arquivo) throws Exception {
        List<Livro> livros = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int numLinha = 0;

            while ((linha = br.readLine()) != null) {
                numLinha++;
                linha = linha.trim();

                // Ignora linhas vazias ou comentários
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                // Divide preservando campos vazios
                String[] partes = linha.split("\\|", -1);

                // Verifica se tem pelo menos 5 campos (último pode ser vazio)
                if (partes.length < 5) {
                    throw new IllegalArgumentException(
                            "Formato inválido na linha " + numLinha +
                                    ". Esperado: Título|Autor|Data|ISBN|Editora|Livros Semelhantes");
                }

                try {
                    Livro livro = new Livro();
                    livro.setTitulo(partes[0].trim());
                    livro.setAutores(partes[1].trim());

                    // Validação da data
                    if (!partes[2].trim().isEmpty()) {
                        livro.setDataPublicacao(java.sql.Date.valueOf(partes[2].trim()));
                    }

                    // Validação do ISBN (não pode ser vazio)
                    if (partes[3].trim().isEmpty()) {
                        throw new IllegalArgumentException("ISBN não pode ser vazio na linha " + numLinha);
                    }
                    livro.setIsbn(partes[3].trim());

                    livro.setEditora(partes[4].trim());

                    // Livros semelhantes (opcional)
                    if (partes.length > 5) {
                        livro.setLivrosSemelhantes(partes[5].trim());
                    }

                    livros.add(livro);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Erro na linha " + numLinha + ": " + e.getMessage(), e);
                }
            }
        }

        if (livros.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não contém dados válidos");
        }

        return livros;
    }
}