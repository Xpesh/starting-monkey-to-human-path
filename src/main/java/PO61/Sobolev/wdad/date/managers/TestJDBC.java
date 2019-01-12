package PO61.Sobolev.wdad.date.managers;

import PO61.Sobolev.wdad.learn.rmi.Building;
import PO61.Sobolev.wdad.learn.rmi.Registration;
import PO61.Sobolev.wdad.learn.xml.NotFoundException;

import javax.xml.transform.TransformerException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;

public class TestJDBC {
    public static void main(String[] args) throws RemoteException, NotFoundException, SQLException, TransformerException {
        Building building = new Building("Нищебродская", "437");
        Date date = new Date();
        date.setYear(2019);
        date.setMonth(2);
        Registration registration = new Registration(date,2,12,211,2100);


        JDBCDataManager jdbcDataManager = new JDBCDataManager();
        System.out.println(jdbcDataManager.getBill(building,438,1,2019));
        jdbcDataManager.setTariff("electricity", 100.40);
        jdbcDataManager.addRegistration(building,438,registration,2,2019);
    }
}
