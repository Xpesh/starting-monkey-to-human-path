package PO61.Sobolev.wdad.learn.rmi;

import PO61.Sobolev.wdad.date.managers.DataManager;
import PO61.Sobolev.wdad.date.managers.PreferencesManager;
import PO61.Sobolev.wdad.learn.xml.NotFoundException;
import PO61.Sobolev.wdad.utils.PreferencesManagerConstants;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class Client {
    static private String securityPolicyPath;
    static private String codebaseUrl;
    static private int registryPort;
    static final private String EXECUTOR_NAME = "xmlDataManager";

    static private PreferencesManager preferencesManager;
    static private String registryAddres;
    static private String useCodebaseOnly;

    static final private String XML_APP_CONFIG_PATH = "/Users/tatanasoboleva/IdeaProjects/starting-monkey-to-human-path/src/PO61/Sobolev/wdad/configuration/appconfig.xml";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        preferencesManager = PreferencesManager.getInstance(XML_APP_CONFIG_PATH);
        registryPort = Integer.parseInt(preferencesManager.getProperty(PreferencesManagerConstants.registryport));
        registryAddres = preferencesManager.getProperty(PreferencesManagerConstants.registryaddress);
        codebaseUrl = "file://".concat(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
//        codebaseUrl = "file://".concat(preferencesManager.getProperty(PreferencesManagerConstants.classprovider));//берет путь из конфига


        securityPolicyPath = preferencesManager.getProperty(PreferencesManagerConstants.policypath);
        useCodebaseOnly = preferencesManager.getProperty(PreferencesManagerConstants.usecodebaseonly);

        System.setProperty("java.rmi.server.codebase", codebaseUrl);
        System.setProperty("java.rmi.server.useCodebaseOnly", useCodebaseOnly);
        System.setProperty("java.security.policy", securityPolicyPath);

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(registryAddres, registryPort); // получает ссылку на реестр
        } catch (RemoteException re) {
            System.err.println("cant locate registry");
            re.printStackTrace();
        }
        if (registry != null) {
            try {
                Remote remote = registry.lookup(EXECUTOR_NAME); // получаем ссылку на удаленный объект
                test((DataManager) remote);
            } catch (RemoteException re) {
                System.err.println("something unbelievable happens");
                re.printStackTrace();
            } catch (Exception e) {
                System.err.println("user input err");
                e.printStackTrace();
            }
        }
    }

    public static void test(DataManager dataManager) throws NotFoundException, RemoteException, TransformerException, SQLException {
        Building building = new Building("северная", "2");
        Date date = new Date();
        date.setYear(2002);
        date.setMonth(1);
        Registration registration = new Registration(date,10,10,10,10);
        while (true){
            System.out.println("Ввод команды");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.next()){
                case "setTariff" :         dataManager.setTariff("coldwater",40); break;
                case "addRegistration" :   dataManager.addRegistration(building,2,registration,1,2005);break;
                case "getBill" :         System.out.println(dataManager.getBill(building,2,2,2002));break;
                case "exit" : System.exit(0);
                default:
                    System.out.println("case \"setTariff\" :         dataManager.setTariff(\"coldwater\",40); break;\n" +
                            "                case \"addRegistration\" :   dataManager.addRegistration(building,2,registration,1,2005);break;\n" +
                            "                case \"getBill\" :         System.out.println(dataManager.getBill(building,2,2,2002));break;\n" +
                            "                case \"exit\" : System.exit(0);");
            }
        }
    }
}
