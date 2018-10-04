package PO61.Sobolev.wdad.learn.rmi;
import PO61.Sobolev.wdad.learn.xml.NotFoundException;

import javax.xml.transform.TransformerException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface XmlDataManager extends Remote{

    public double getBill(Building building, int flatNumber, int month, int year) throws NotFoundException, RemoteException;
    public void setTariff(String tariffName, double newValue) throws RemoteException, TransformerException;
    public void addRegistration(Building building, int flatNumber, Registration registration, int month, int year) throws TransformerException, NotFoundException, RemoteException;
}