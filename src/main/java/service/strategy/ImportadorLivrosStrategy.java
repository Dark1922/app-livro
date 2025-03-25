package service.strategy;

import model.Livro;

import java.io.File;
import java.util.List;

public interface ImportadorLivrosStrategy {
    List<Livro> importar(File arquivo) throws Exception;
}