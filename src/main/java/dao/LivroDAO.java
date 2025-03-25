package dao;

import exception.DAOException;
import model.Livro;

import javax.persistence.*;
import java.util.List;

public class LivroDAO {

    private EntityManagerFactory emf;

    public LivroDAO() {
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void inserirLivro(Livro livro) throws DAOException {
        EntityManager em = getEntityManager();
        try {

            // Verifica se o ISBN já existe no banco de dados
            if (isbnExiste(livro.getIsbn(), null)) {
                throw new DAOException("O ISBN já está em uso por outro livro.");
            }

            em.getTransaction().begin();
            em.persist(livro);
            em.getTransaction().commit();
        } catch (RollbackException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DAOException("Erro ao inserir o livro: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void atualizarLivro(Livro livro) throws DAOException {
        EntityManager em = getEntityManager();
        try {

            // Verifica se o novo ISBN já está em uso por outro livro
            if (isbnExiste(livro.getIsbn(), livro.getId())) {
                throw new DAOException("O ISBN já está em uso por outro livro.");
            }

            em.getTransaction().begin();
            em.merge(livro);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new DAOException("Erro ao atualizar o livro: " + e.getCause().getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void excluirLivro(Long id) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Livro livro = em.find(Livro.class, id);
            if (livro != null) {
                em.remove(livro);
            }
            em.getTransaction().commit();
        } catch (RollbackException e) {
            throw new DAOException("Erro ao excluir o livro: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Livro buscarPorId(Long id) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            return em.find(Livro.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar o livro por ID: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    //consulta da listagem
    public List<Livro> listarTodos() throws DAOException {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT l FROM Livro l";
            return em.createQuery(jpql, Livro.class).getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar todos os livros: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Pesquisa livros através de todos os campos.
    public List<Livro> pesquisar(String termo) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT l FROM Livro l WHERE " +
                    "LOWER(l.titulo) LIKE LOWER(:termo) OR " +
                    "LOWER(l.autores) LIKE LOWER(:termo) OR " +
                    "l.isbn LIKE :termo OR " +
                    "LOWER(l.editora) LIKE LOWER(:termo) OR " +
                    "LOWER(l.livrosSemelhantes) LIKE LOWER(:termo) OR " +
                    "FUNCTION('TO_CHAR', l.dataPublicacao, 'DD/MM/YYYY') LIKE :termo OR " +
                    "FUNCTION('TO_CHAR', l.dataPublicacao, 'YYYY-MM-DD') LIKE :termo";

            return em.createQuery(jpql, Livro.class)
                    .setParameter("termo", "%" + termo + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar livros: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Retorna true se o ISBN já existe para outro livro
    public boolean isbnExiste(String isbn, Long livroId) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(l) FROM Livro l WHERE l.isbn = :isbn AND l.id != :livroId";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("isbn", isbn)
                    .setParameter("livroId", livroId == null ? -1 : livroId)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao procurar o isbn: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}