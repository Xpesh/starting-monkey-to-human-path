package PO61.Sobolev.wdad.learn.rmi;

import PO61.Sobolev.wdad.date.managers.DataManager;
import PO61.Sobolev.wdad.learn.xml.NotFoundException;
import PO61.Sobolev.wdad.learn.xml.XmlTask;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

public class XmlDataManagerImpl implements DataManager, Serializable {
    private  XmlTask xmlTask;
    public XmlDataManagerImpl(String path) throws RemoteException{
        try {
            xmlTask = new XmlTask(path);
        } catch (SAXException e) {
            throw new  RemoteException();
        } catch (IOException e) {
            throw new  RemoteException();
        } catch (ParserConfigurationException e) {
            throw new  RemoteException();
        }
    }

    public double getBill(Building building, int flatNumber, int month, int year) throws RemoteException {
        try {
            return xmlTask.getBill(building.street,Integer.valueOf(building.number),flatNumber, month, year);
        } catch (NotFoundException e) {
            throw new  RemoteException();
        }
    }

    public void setTariff(String tariffName, double newValue) throws RemoteException{
        try {
            xmlTask.setTariff(tariffName,newValue);
        } catch (TransformerException e) {
            throw new  RemoteException();
        }
    }

    public void addRegistration(Building building, int flatNumber, Registration registration, int month, int year)
            throws TransformerException, NotFoundException, RemoteException {
        xmlTask.addRegistration(building.street,Integer.valueOf(building.number),flatNumber,year,month,registration.coldwater,
                registration.hotwater, registration.electricity, registration.gas);
    }
}
