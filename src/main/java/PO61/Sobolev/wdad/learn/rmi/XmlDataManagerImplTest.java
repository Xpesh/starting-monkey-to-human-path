package PO61.Sobolev.wdad.learn.rmi;

import PO61.Sobolev.wdad.learn.xml.NotFoundException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Date;

public class XmlDataManagerImplTest {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException, NotFoundException {
        XmlDataManagerImpl xmlDataManager = new XmlDataManagerImpl("/Users/tatanasoboleva/IdeaProjects/starting-monkey-to-human-path/src/PO61.Sobolev.wdad/learn/xml/housekeeper1.xml");
        Building building = new Building("северная", "2");
        Date date = new Date();
        date.setYear(2002);
        date.setMonth(1);
        Registration registration = new Registration(date,10,10,10,10);
        xmlDataManager.setTariff("coldwater",40);
        System.out.println(xmlDataManager.getBill(building,2,2,2002));
        xmlDataManager.addRegistration(building,2,registration,1,2002);
    }
}