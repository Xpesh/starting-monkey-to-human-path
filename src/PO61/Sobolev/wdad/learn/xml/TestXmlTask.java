package PO61.Sobolev.wdad.learn.xml;

import PO61.Sobolev.wdad.date.managers.PreferencesManager;

public class TestXmlTask {
    static final String PATH = "/Users/tatanasoboleva/IdeaProjects/starting-monkey-to-human-path/src/PO61.Sobolev.wdad/learn/xml/housekeeper1.xml";
    static final String PATH2 = "/Users/tatanasoboleva/IdeaProjects/starting-monkey-to-human-path/src/PO61.Sobolev.wdad/utils/appconfig.xml";
    public static void main(String[] args) throws Exception {
        XmlTask xml = new XmlTask(PATH);
        System.out.println(xml.getBill("северная", 2, 2, 2, 2002));
        xml.setTariff("hotwater", 15);
        xml.addRegistration("восточная", 2, 2, 2002, 3, 50, 500, 500, 500);

        PreferencesManager manager = PreferencesManager.getInstance(PATH2);
        manager.setCreateRegistry("no");
    }
}
