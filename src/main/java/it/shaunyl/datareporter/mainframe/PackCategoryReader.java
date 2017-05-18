package it.shaunyl.datareporter.mainframe;

import it.shaunyl.datareporter.exception.PackQueryReaderException;
import it.tidalwave.role.ui.spi.DefaultPresentationModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Filippo Testino
 */
@Slf4j @Service
public class PackCategoryReader {

    private final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    private List<PackQuery> queries;

    private DocumentBuilder documentBuilder;

    private static final String DEFAULT_TAG_CATEGORY = "category";

    private static final String DEFAULT_TAG_QUERY = "query";

    public PackCategoryReader() {
        try {
            documentBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            log.error("", ex);
        }

    }

    public Document parse(final File file) throws IOException, SAXException {
        try {
            documentBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException("", ex);
        }
        Document document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();

        return document;
    }

    public List<PackCategory> readCategories(Document document) throws IOException, SAXException {
        this.readQueries();
        NodeList nodeList = document.getElementsByTagName(DEFAULT_TAG_CATEGORY);
        List<PackCategory> categories = new ArrayList<>();

        List<String> categoryNames = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                PackCategory category = new PackCategory();
                category.setId(Integer.parseInt(element.getAttribute("id")));

                String name = element.getElementsByTagName("name").item(0).getTextContent();
                category.setName(name);
                category.setDescription(element.getElementsByTagName("description").item(0).getTextContent());

                for (int j = 0; j < queries.size(); j++) {
                    try {
                        PackQuery pqm = this.verifyQuery(j, name);
                        if (pqm != null) {
                            category.add(pqm);
                        }
                    } catch (PackQueryReaderException ex) {
                        log.error("", ex);
                    }
                }

                if (!categoryNames.contains(name)) { //le categorie non accettano duplicati..
                    categoryNames.add(name);
                }

                categories.add(category);
            }
        }
        categoryNames.clear();

        return categories;
    }

    private void readQueries() throws IOException, SAXException {
        Document document = this.parse(new File("./config/queries.xml"));
        NodeList nodeList = document.getElementsByTagName(DEFAULT_TAG_QUERY);
        this.queries = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                PackQuery query = new PackQuery();
                query.setId(Integer.parseInt(element.getAttribute("id")));
                query.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
                query.setSql(element.getElementsByTagName("sql").item(0).getTextContent());

                String category = element.getAttribute("category");
                query.setCategory(category);

                queries.add(query);
            }
        }
    }

    private PackQuery verifyQuery(final int index, final String name) throws PackQueryReaderException {
        PackQuery pqm = queries.get(index);
        String category = pqm.getCategory();
        if (category == null) {
            throw new PackQueryReaderException("The pack query '%s' will be skipped because its Category '%s' does not exist or is not valid.");
        }

        if (pqm.getCategory().equals(name)) {
            return pqm;
        }
        return null;
    }
}