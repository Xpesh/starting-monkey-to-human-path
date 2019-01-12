package PO61.Sobolev.wdad.learn.rmi;

import PO61.Sobolev.wdad.date.managers.PreferencesManager;
import PO61.Sobolev.wdad.utils.PreferencesManagerConstants;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private static XmlDataManagerImpl xmlDataManager;
    static private String securityPolicyPath;
    static final private String EXECUTOR_NAME = "xmlDataManager";
    static final private String XML_APP_CONFIG_PATH = "/Users/tatanasoboleva/IdeaProjects/HOT/starting-monkey-to-human-path/src/PO61/Sobolev/wdad/configuration/appconfig.xml";
    static final private String XML_DATA_MANAGER_PATH = "/Users/tatanasoboleva/IdeaProjects/HOT/starting-monkey-to-human-path/src/PO61/Sobolev/wdad/learn/xml/housekeeper1.xml";

    static private PreferencesManager preferencesManager;
    static private String codebaseUrl;
    static private int registryPort;
    static private String registryAddres;
    static private int executorPort = 1604; // Должен быть не занят
    static private String useCodebaseOnly;


    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        preferencesManager = PreferencesManager.getInstance(XML_APP_CONFIG_PATH);
        xmlDataManager = new XmlDataManagerImpl(XML_DATA_MANAGER_PATH);
        registryAddres = preferencesManager.getProperty(PreferencesManagerConstants.registryaddress);
        codebaseUrl = "file://".concat(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());//получает путь до jar файла
//        codebaseUrl = "file://".concat(preferencesManager.getProperty(PreferencesManagerConstants.classprovider));//берет путь из конфига
        registryPort = Integer.parseInt(preferencesManager.getProperty(PreferencesManagerConstants.registryport));
        registryAddres = preferencesManager.getProperty(PreferencesManagerConstants.registryaddress);
        useCodebaseOnly = preferencesManager.getProperty(PreferencesManagerConstants.usecodebaseonly);
        securityPolicyPath = preferencesManager.getProperty(PreferencesManagerConstants.policypath);
        System.setProperty("java.rmi.server.codebase", codebaseUrl);
        System.setProperty("java.rmi.server.useCodebaseOnly", useCodebaseOnly); // искать классы в codebase, а не в class path
        System.setProperty("java.rmi.server.logCalls", "true"); // чтоб сервер в консоль выводил инку по коннекта - запросы на поиск объекта
        System.setProperty("java.security.policy", securityPolicyPath); // путь к файлу с правами доступа
        System.setProperty("java.rmi.server.hostname", registryAddres);
        System.setSecurityManager(new SecurityManager());
        Registry registry = null;
        try{
            if(preferencesManager.getProperty(PreferencesManagerConstants.createregistry).equals("yes"))
                registry = LocateRegistry.createRegistry(registryPort);
            else registry = LocateRegistry.getRegistry(registryPort);
        }catch (RemoteException e){
            e.printStackTrace();
        }

        try {
            System.out.println("exporting object...");
            UnicastRemoteObject.exportObject(xmlDataManager, executorPort); // экспорт объекта - необходимо, если класс, реализующий удаленный интерфейс, не наследуется от UnicastRemoteObject
            registry.rebind(EXECUTOR_NAME, xmlDataManager); // бинтом объект, чтоб клиент мог найти его по имени
            System.out.println("idl-ing");
        } catch (RemoteException re) {
            System.err.println("cant export or bind object");
            re.printStackTrace();
        }

    }
}
