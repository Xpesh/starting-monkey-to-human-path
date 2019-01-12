package PO61.Sobolev.wdad.learn.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class XmlTask {
    private final String path;
    private final Path xmlFile;
    private Document document;
    private static final String DEFAULT_PERSONSQUANTITY = "1";
    private static final String DEFAULT_AREA = "34";
    private static final String DEFAULT_FLATSQUANTITY = "50";


    public XmlTask(String path) throws SAXException, IOException, ParserConfigurationException {
        this.path = path;
        this.xmlFile = Paths.get(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(true);
        this.document = builder.parse(xmlFile.toFile());
    }

    //  Возвращающий сумму, которую необходимо оплатить владельцам заданной квартиры.
    //  Счет определяется разницей показаний счетчиков за текущий месяц и предыдущий.
    //  Тарифы определены так же в xml-документе.
    public double getBill(String street, int buildingNumber, int flatNumber, int month, int year) throws NotFoundException {
        double bill = 0;
        Element flatElement = findFlat(findBuilding(street, buildingNumber), flatNumber);
        Element newRegistration = findRegistration(flatElement, month, year);
        Element pastRegistration;
        if(month==1){
            pastRegistration = findRegistration(flatElement, 12, year-1);
        }else {
            pastRegistration = findRegistration(flatElement, month - 1, year);
        }
        double coldWaterDiff = getMeterReading(newRegistration, "coldwater") - getMeterReading(pastRegistration, "coldwater");
        double hotWaterDiff = getMeterReading(newRegistration, "hotwater") - getMeterReading(pastRegistration, "hotwater");
        double electricity = getMeterReading(newRegistration, "electricity") - getMeterReading(pastRegistration, "electricity");
        double gas = getMeterReading(newRegistration, "gas") - getMeterReading(pastRegistration, "gas");

        return coldWaterDiff * getTariff("coldwater") + hotWaterDiff * getTariff("hotwater") +
                electricity * getTariff("electricity") + gas * getTariff("gas");
    }

    private double getMeterReading(Element registrationElement, String meterName) {
        return Double.valueOf(registrationElement.getElementsByTagName(meterName).item(0).getTextContent());
    }

    private double getTariff(String tariffName) {
        Element tariff = (Element) document.getElementsByTagName("tariffs").item(0);
        return Double.valueOf(tariff.getAttribute(tariffName));
    }

    private Element findBuilding(String street, int buildingNumber) throws NotFoundException {
        return find(document.getElementsByTagName("building"), s -> (s.getAttribute("street").equals(street)
                && s.getAttribute("number").equals(String.valueOf(buildingNumber))));
    }

    private Element findFlat(Element buildingElement, int flatNumber) throws NotFoundException {
        return find(buildingElement.getElementsByTagName("flat"),
                s -> (s.getAttribute("number").equals(String.valueOf(flatNumber))));
    }

    private Element findRegistration(Element flatElement, int month, int year) throws NotFoundException {
        return find(flatElement.getElementsByTagName("registration"),
                s -> (s.getAttribute("month").equals(String.valueOf(month)) &&
                        s.getAttribute("year").equals(String.valueOf(year))));
    }

    private Element find(NodeList nodeList, Predicate<Element> predicate) throws NotFoundException {
        Element element;
        for (int i = 0; i < nodeList.getLength(); i++) {
            element = (Element) nodeList.item(i);
            if (predicate.test(element)) return element;
        }
        throw new NotFoundException();
    }

    //  изменяющий стоимость заданной единицы показания счетчика (ХВС, ГВС,электроэнергия, газ).
    public void setTariff(String tariffName, double newValue) throws TransformerException {
        Element tariff = (Element) document.getElementsByTagName("tariffs").item(0);
        tariff.setAttribute(tariffName,String.valueOf(newValue));
        saveXML();
    }

    private void saveXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMImplementation domImpl = document.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype", "","housekeeper.dtd");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(this.path));
        transformer.transform(source, result);
    }

    //добавляющий показания счетчиков к заданной квартире в заданный период.
    public void addRegistration(String street, int buildingNumber, int flatNumber, int year,
                                int month, double coldWater, double hotWater, double electricity, double gas) throws TransformerException, NotFoundException {

        Element element;
        try {
            element = findBuilding(street, buildingNumber);
        } catch (NotFoundException e){
            element = document.createElement("building");
            document.getElementsByTagName("housekeeper").item(0).appendChild(element);
            element.setAttribute("flatsquantity", DEFAULT_FLATSQUANTITY);
            element.setAttribute("number", String.valueOf(buildingNumber));
            element.setAttribute("street", street);

        }

        Element bufElement = element;
        try{
        element = findFlat(element, flatNumber);
        } catch (NotFoundException e){
            element = document.createElement("flat");
            bufElement.appendChild(element);
            element.setAttribute("personsquantity", DEFAULT_PERSONSQUANTITY);
            element.setAttribute("number", String.valueOf(flatNumber));
            element.setAttribute("area", DEFAULT_AREA);
        }

        bufElement = element;
        try{
        element = findRegistration(element, month, year);
        } catch (NotFoundException e){
            element = document.createElement("registration");
            bufElement.appendChild(element);
            element.setAttribute("month", String.valueOf(month));
            element.setAttribute("year", String.valueOf(year));

            bufElement = element;
            bufElement.appendChild(document.createElement("coldwater"));
            bufElement.appendChild(document.createElement("hotwater"));
            bufElement.appendChild(document.createElement("electricity"));
            bufElement.appendChild(document.createElement("gas"));

        }
        element.getElementsByTagName("coldwater").item(0).setTextContent(String.valueOf(coldWater));
        element.getElementsByTagName("hotwater").item(0).setTextContent(String.valueOf(hotWater));
        element.getElementsByTagName("electricity").item(0).setTextContent(String.valueOf(electricity));
        element.getElementsByTagName("gas").item(0).setTextContent(String.valueOf(gas));

        saveXML();
    }

}
