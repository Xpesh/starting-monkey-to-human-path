package PO61.Sobolev.wdad.date.storage;

import PO61.Sobolev.wdad.date.managers.PreferencesManager;
import PO61.Sobolev.wdad.utils.PreferencesManagerConstants;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class DataSourceFactory {

    /**
     – создающий экземпляр класса
     (определение класса берется из JDBC-драйвера, предоставляемого поставщиком
     выбранной СУБД), реализующего интерфейс DataSource. Класс, и параметры
     соединения с БД получаются из appconfig.xml через PreferencesManager.
     **/
    public static DataSource createDataSource() throws SQLException {
        try {
            PreferencesManager pm = PreferencesManager.getInstance("/Users/xpesh/IdeaProjects/starting-monkey-to-human-path/src/main/java/PO61/Sobolev/wdad/configuration/appconfig.xml");
            return createDataSource(pm.getProperty(PreferencesManagerConstants.classname),
                    pm.getProperty(PreferencesManagerConstants.drivertype),
                    pm.getProperty(PreferencesManagerConstants.hostName),
                    Integer.valueOf(pm.getProperty(PreferencesManagerConstants.port)),
                    pm.getProperty(PreferencesManagerConstants.DBName),
                    pm.getProperty(PreferencesManagerConstants.user),
                    pm.getProperty(PreferencesManagerConstants.pass));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        throw new SQLException();
    }

    /**
     создающий экземпляр класса (определение класса берется из JDBC-драйвера,
     предоставляемого поставщиком выбранной СУБД), реализующего интерфейс
     DataSource, принимающий имя класса и параметры соединения в качестве входных
     параметров.
     */
    public static DataSource createDataSource(String className, String
            driverType, String host, int port, String dbName, String user, String password){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setPortNumber(port);
        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}
