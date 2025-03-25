import dao.LivroDAO;
import exception.DAOException;
import model.Livro;
import org.junit.jupiter.api.*;
import org.mockito.*;
import javax.persistence.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivroDAOMockTest {

    @Mock
    private EntityManagerFactory emf;

    @Mock
    private EntityManager em;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Livro> livroTypedQuery;

    @Mock
    private TypedQuery<Long> longTypedQuery;

    @InjectMocks
    private LivroDAO livroDAO;

    private Livro livroTeste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(transaction);

        livroTeste = new Livro();
        livroTeste.setId(1L);
        livroTeste.setTitulo("Teste Mock");
        livroTeste.setAutores("Autor Mock");
        livroTeste.setDataPublicacao(new Date());
        livroTeste.setIsbn("1234567890123");
        livroTeste.setEditora("Editora Mock");
    }

    @Test
    @DisplayName("Deve inserir livro com sucesso")
    void testInserirLivro() throws DAOException {
        // Configuração do mock para isbnExiste
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(longTypedQuery);
        when(longTypedQuery.setParameter(anyString(), any())).thenReturn(longTypedQuery);
        when(longTypedQuery.getSingleResult()).thenReturn(0L);

        livroDAO.inserirLivro(livroTeste);

        verify(transaction).begin();
        verify(em).persist(livroTeste);
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Deve lançar exceção ao inserir ISBN duplicado")
    void testInserirLivroComISBNExistente() {
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(longTypedQuery);
        when(longTypedQuery.setParameter(anyString(), any())).thenReturn(longTypedQuery);
        when(longTypedQuery.getSingleResult()).thenReturn(1L); // ISBN já existe

        assertThrows(DAOException.class, () -> livroDAO.inserirLivro(livroTeste));

        verify(transaction, never()).commit();
    }

    @Test
    @DisplayName("Deve atualizar livro com sucesso")
    void testAtualizarLivro() throws DAOException {
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(longTypedQuery);
        when(longTypedQuery.setParameter(anyString(), any())).thenReturn(longTypedQuery);
        when(longTypedQuery.getSingleResult()).thenReturn(0L);

        when(em.merge(livroTeste)).thenReturn(livroTeste);

        livroDAO.atualizarLivro(livroTeste);

        verify(transaction).begin();
        verify(em).merge(livroTeste);
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Deve buscar livro por ID")
    void testBuscarPorId() throws DAOException {
        when(em.find(Livro.class, 1L)).thenReturn(livroTeste);

        Livro resultado = livroDAO.buscarPorId(1L);

        assertEquals(livroTeste, resultado);
        verify(em).find(Livro.class, 1L);
    }

    @Test
    @DisplayName("Deve listar todos os livros")
    void testListarTodos() throws DAOException {
        List<Livro> livrosMock = Arrays.asList(livroTeste);

        when(em.createQuery("SELECT l FROM Livro l", Livro.class))
                .thenReturn(livroTypedQuery);
        when(livroTypedQuery.getResultList()).thenReturn(livrosMock);

        List<Livro> resultado = livroDAO.listarTodos();

        assertEquals(1, resultado.size());
        verify(em).createQuery("SELECT l FROM Livro l", Livro.class);
    }

    @Test
    @DisplayName("Deve pesquisar livros por termo")
    void testPesquisar() throws DAOException {
        List<Livro> livrosMock = Arrays.asList(livroTeste);

        when(em.createQuery(anyString(), eq(Livro.class)))
                .thenReturn(livroTypedQuery);
        when(livroTypedQuery.setParameter(eq("termo"), anyString()))
                .thenReturn(livroTypedQuery);
        when(livroTypedQuery.getResultList()).thenReturn(livrosMock);

        List<Livro> resultado = livroDAO.pesquisar("Teste");

        assertEquals(1, resultado.size());
        verify(livroTypedQuery).setParameter("termo", "%Teste%");
    }

    @Test
    @DisplayName("Deve verificar se ISBN existe")
    void testIsbnExiste() throws DAOException {
        when(em.createQuery(anyString(), eq(Long.class)))
                .thenReturn(longTypedQuery);
        when(longTypedQuery.setParameter(eq("isbn"), anyString()))
                .thenReturn(longTypedQuery);
        when(longTypedQuery.setParameter(eq("livroId"), anyLong()))
                .thenReturn(longTypedQuery);
        when(longTypedQuery.getSingleResult()).thenReturn(1L);

        boolean resultado = livroDAO.isbnExiste("1234567890123", null);

        assertTrue(resultado);
        verify(longTypedQuery).setParameter("isbn", "1234567890123");
    }
}