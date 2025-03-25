import exception.OpenLibraryServiceException;
import model.Livro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.OpenLibraryService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito no JUnit 5
public class OpenLibraryServiceTest {

    @Mock
    private OpenLibraryService openLibraryService; // Mock da classe OpenLibraryService

    @Test
    public void testBuscarLivroPorISBN() throws ParseException, OpenLibraryServiceException {
        // Arrange
        String isbn = "9780140328721"; // ISBN válido para teste

        // Cria um objeto de resposta simulada com base no JSON real
        Livro responseSimulada = new Livro();
        responseSimulada.setTitulo("Fantastic Mr. Fox");
        responseSimulada.setAutores("Roald Dahl");

        // Define o formato da data com Locale.ENGLISH
        SimpleDateFormat formato = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // Formato da data
        Date data = formato.parse("October 1, 1988"); // Converte a string para Date
        responseSimulada.setDataPublicacao(data);
        responseSimulada.setEditora("Puffin");

        // Configura o mock para retornar a resposta simulada
        when(openLibraryService.buscarLivroPorISBN(anyString())).thenReturn(responseSimulada);

        // Act
        Livro response = openLibraryService.buscarLivroPorISBN(isbn);

        // Assert
        assertNotNull(response, "A resposta não deve ser nula");
        assertNotNull(response.getTitulo(), "O título não deve ser nulo");
        assertNotNull(response.getAutores(), "A lista de autores não deve ser nula");
        assertNotNull(response.getDataPublicacao(), "A data de publicação não deve ser nula");
        assertNotNull(response.getEditora(), "A editora não deve ser nula");

        System.out.println("Título: " + response.getTitulo());
        System.out.println("Autores: " + response.getAutores());
        System.out.println("Data de Publicação: " + response.getDataPublicacao());
        System.out.println("Editora: " + response.getEditora());
    }

    @Test
    public void testBuscarLivroPorISBNInvalido() throws OpenLibraryServiceException {
        // Arrange
        String isbn = "0000000000000"; // ISBN inválido

        // Configura o mock para retornar null (simulando um ISBN inválido)
        when(openLibraryService.buscarLivroPorISBN(anyString())).thenReturn(null);

        // Act
        Livro response = openLibraryService.buscarLivroPorISBN(isbn);

        // Assert
        assertNull(response, "A resposta deve ser nula para um ISBN inválido");
    }
}