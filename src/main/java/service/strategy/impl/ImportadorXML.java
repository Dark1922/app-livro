package service.strategy.impl;

import model.Livro;
import service.strategy.ImportadorLivrosStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportadorXML implements ImportadorLivrosStrategy {

    @Override
    public List<Livro> importar(File arquivo) throws Exception {
        List<Livro> livros = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(arquivo);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("livro");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Livro livro = new Livro();

                    // Título (obrigatório)
                    livro.setTitulo(getTagValue("titulo", element, true));

                    // Autores (obrigatório)
                    livro.setAutores(getTagValue("autores", element, true));

                    // Data de publicação (opcional)
                    String dataStr = getTagValue("dataPublicacao", element, false);
                    if (dataStr != null && !dataStr.isEmpty()) {
                        livro.setDataPublicacao(java.sql.Date.valueOf(dataStr));
                    }

                    // ISBN (obrigatório)
                    livro.setIsbn(getTagValue("isbn", element, true));

                    // Editora (obrigatório)
                    livro.setEditora(getTagValue("editora", element, true));

                    // Livros semelhantes (opcional)
                    String semelhantes = getTagValue("livrosSemelhantes", element, false);
                    livro.setLivrosSemelhantes(semelhantes != null ? semelhantes : "");

                    livros.add(livro);
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao processar arquivo XML: " + e.getMessage(), e);
        }

        if (livros.isEmpty()) {
            throw new IllegalArgumentException("Nenhum livro válido encontrado no arquivo XML");
        }

        return livros;
    }

    private String getTagValue(String tag, Element element, boolean obrigatorio) throws Exception {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) {
            if (obrigatorio) {
                throw new Exception("Tag obrigatória '" + tag + "' não encontrada");
            }
            return null;
        }

        Node node = nodeList.item(0);
        if (node == null || node.getFirstChild() == null) {
            if (obrigatorio) {
                throw new Exception("Tag '" + tag + "' está vazia");
            }
            return "";
        }

        return node.getFirstChild().getNodeValue().trim();
    }
}