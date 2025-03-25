package service;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import dto.OpenLibraryResponse;
import exception.OpenLibraryServiceException;
import model.Livro;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class OpenLibraryService {

    private static final String BASE_URL = "https://openlibrary.org/";
    private final CloseableHttpClient httpClient;
    private final Gson gson;
    private final SimpleDateFormat dateFormat;

    public OpenLibraryService() {
        this.httpClient = HttpClients.createDefault();
        this.gson = new Gson();
        this.dateFormat = new SimpleDateFormat("yyyy");
    }

    public Livro buscarLivroPorISBN(String isbn) throws OpenLibraryServiceException {
        validateISBN(isbn);
        try {
            String json = fetchBookData(isbn);
            OpenLibraryResponse response = parseBookResponse(json);
            return createBookFromResponse(response, isbn);
        } catch (IllegalArgumentException e) {
            throw new OpenLibraryServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new OpenLibraryServiceException("ISBN não encontrado na api: " + isbn, e);
        }
    }

    private void validateISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN não pode ser vazio");
        }
        if (!isbn.matches("\\d{13}")) {
            throw new IllegalArgumentException("ISBN deve conter exatamente 13 dígitos");
        }
    }

    private String fetchBookData(String isbn) throws Exception {
        String url = BASE_URL + "isbn/" + isbn + ".json";
        return EntityUtils.toString(httpClient.execute(new HttpGet(url)).getEntity());
    }

    private String fetchAuthordata(String key) throws Exception {
        String url = BASE_URL + key + ".json";
        return EntityUtils.toString(httpClient.execute(new HttpGet(url)).getEntity());
    }

    private OpenLibraryResponse parseBookResponse(String json) {
        return gson.fromJson(json, OpenLibraryResponse.class);
    }

    private Livro createBookFromResponse(OpenLibraryResponse response, String isbn) throws Exception {
        Livro livro = new Livro();
        livro.setIsbn(isbn);
        livro.setTitulo(getSafeTitle(response));
        livro.setAutores(extractAuthors(response));
        livro.setEditora(extractPublishers(response));
        livro.setDataPublicacao(parsePublicationDate(response));

        // Adiciona livros semelhantes formatados com vírgulas
        String json = fetchBookData(isbn);
        JsonObject livroJson = JsonParser.parseString(json).getAsJsonObject();

        if (livroJson.has("works")) {
            String workId = extractWorkId(livroJson);
            List<String> semelhantes = fetchRelatedBooks(workId);
            if (!semelhantes.isEmpty()) {
                String semelhantesFormatado = String.join(", ", semelhantes);
                livro.setLivrosSemelhantes(semelhantesFormatado);
            }
        }

        return livro;
    }

    private String getSafeTitle(OpenLibraryResponse response) {
        return Optional.ofNullable(response.getTitle()).orElse("");
    }

    private String extractAuthors(OpenLibraryResponse response) {
        try {
            if (response.getAuthors() == null || response.getAuthors().isEmpty()) {
                return "";
            }

            return response.getAuthors().stream()
                    .map(this::processAuthorObject)
                    .filter(name -> !name.isEmpty())
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            return "";
        }
    }

    private String processAuthorObject(Object author) {
        if (author instanceof String) {
            return (String) author;
        } else if (author instanceof Map) {
            return fetchAuthorName((Map<?, ?>) author);
        }
        return "";
    }

    private String fetchAuthorName(Map<?, ?> authorData) {
        try {
            String authorKey = authorData.get("key").toString();
            JsonObject authorJson = fetchAuthorDetails(authorKey);
            return extractAuthorNameFromJson(authorJson);
        } catch (Exception e) {
            return "";
        }
    }

    private JsonObject fetchAuthorDetails(String authorKey) throws Exception {
        String json = fetchAuthordata(authorKey);
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    //busca o nome do author
    private String extractAuthorNameFromJson(JsonObject authorJson) {
        if (authorJson.has("name")) return authorJson.get("name").getAsString();
        if (authorJson.has("personal_name")) return authorJson.get("personal_name").getAsString();
        return "";
    }

    private String extractPublishers(OpenLibraryResponse response) {
        return Optional.ofNullable(response.getPublishers())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    private Date parsePublicationDate(OpenLibraryResponse response) {
        try {
            return response.getPublishDate() != null ?
                    dateFormat.parse(response.getPublishDate()) :
                    new Date();
        } catch (Exception e) {
            return new Date();
        }
    }

    private String fetchJson(String url) throws Exception {
        return EntityUtils.toString(httpClient.execute(new HttpGet(url)).getEntity());
    }

    private String extractWorkId(JsonObject livroJson) {
        return livroJson.getAsJsonArray("works")
                .get(0).getAsJsonObject()
                .get("key").getAsString()
                .replace("/works/", "");
    }

    //consulta livros semelhante
    private List<String> fetchRelatedBooks(String workId) throws Exception {
        String url = BASE_URL + "works/" + workId + "/editions.json?limit=5";
        String json = fetchJson(url);
        JsonObject response = JsonParser.parseString(json).getAsJsonObject();

        List<String> semelhantes = new ArrayList<>();
        if (response.has("entries")) {
            for (JsonElement entry : response.getAsJsonArray("entries")) {
                JsonObject edition = entry.getAsJsonObject();
                if (edition.has("title")) {
                    String title = edition.get("title").getAsString();
                    if (!title.isEmpty() && !semelhantes.contains(title)) {
                        semelhantes.add(title);
                    }
                }
            }
        }
        return semelhantes;
    }
}