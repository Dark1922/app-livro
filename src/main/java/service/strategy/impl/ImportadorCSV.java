package service.strategy.impl;

import model.Livro;
import service.strategy.ImportadorLivrosStrategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ImportadorCSV implements ImportadorLivrosStrategy {

    @Override
    public List<Livro> importar(File arquivo) throws Exception {
        List<Livro> livros = new ArrayList<>();
        boolean primeiraLinha = true; // Flag para identificar o cabeçalho

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

                // Pula o cabeçalho
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                // Divide os campos, tratando valores entre aspas
                String[] partes = linha.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                // Remove aspas extras e trim
                for (int i = 0; i < partes.length; i++) {
                    partes[i] = partes[i].replaceAll("^\"|\"$", "").trim();
                }

                // Verifica se tem pelo menos 5 campos
                if (partes.length < 5) {
                    throw new IllegalArgumentException(
                            "Formato inválido na linha " + numLinha +
                                    ". Esperado: Título;Autor;Data;ISBN;Editora;LivrosSemelhantes");
                }

                try {
                    Livro livro = new Livro();
                    livro.setTitulo(partes[0]);
                    livro.setAutores(partes[1]);

                    // Validação da data
                    if (!partes[2].isEmpty()) {
                        try {
                            livro.setDataPublicacao(java.sql.Date.valueOf(partes[2]));
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Formato de data inválido na linha " + numLinha +
                                    ". Use AAAA-MM-DD");
                        }
                    }

                    // Validação do ISBN (não pode ser vazio)
                    if (partes[3].isEmpty()) {
                        throw new IllegalArgumentException("ISBN não pode ser vazio na linha " + numLinha);
                    }
                    livro.setIsbn(partes[3]);

                    livro.setEditora(partes[4]);

                    // Livros semelhantes (opcional)
                    if (partes.length > 5) {
                        livro.setLivrosSemelhantes(partes[5]);
                    } else {
                        livro.setLivrosSemelhantes("");
                    }

                    livros.add(livro);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Erro na linha " + numLinha + ": " + e.getMessage(), e);
                }
            }
        }

        if (livros.isEmpty()) {
            throw new IllegalArgumentException("O arquivo CSV não contém dados válidos");
        }

        return livros;
    }
}