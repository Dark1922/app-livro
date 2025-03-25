package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "livro")
@SequenceGenerator(name = "seq_livro", sequenceName = "seq_livro", allocationSize = 1, initialValue = 1)
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_livro")
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autores;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataPublicacao;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String editora;

    @Column(length = 1000)
    private String livrosSemelhantes;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(Date dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getLivrosSemelhantes() {
        return livrosSemelhantes;
    }

    public void setLivrosSemelhantes(String livrosSemelhantes) {
        this.livrosSemelhantes = livrosSemelhantes;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }
}