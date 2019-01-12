package PO61.Sobolev.wdad.date.managers;

import PO61.Sobolev.wdad.learn.rmi.Building;
import PO61.Sobolev.wdad.learn.rmi.Registration;
import PO61.Sobolev.wdad.learn.xml.NotFoundException;
import javax.xml.transform.TransformerException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface DataManager extends Remote{

    double getBill(Building building, int flatNumber, int month, int year) throws NotFoundException, RemoteException, SQLException;
    void setTariff(String tariffName, double newValue) throws RemoteException, TransformerException, SQLException;
    void addRegistration(Building building, int flatNumber, Registration registration, int month, int year) throws TransformerException, NotFoundException, RemoteException, SQLException;
}