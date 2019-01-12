package PO61.Sobolev.wdad.date.managers;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TestPreferencesManager {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        PreferencesManager preferencesManager = PreferencesManager.getInstance("/Users/tatanasoboleva/IdeaProjects/starting-monkey-to-human-path/src/PO61.Sobolev.wdad/utils/appconfig.xml");
//        Properties properties = preferencesManager.getProperties();
//        System.out.println(properties);
//        preferencesManager.setProperty("appconfig.rmi.server.registry.createregistry","yes");
//        preferencesManager.removeBindedObject("MA");
    }
}
