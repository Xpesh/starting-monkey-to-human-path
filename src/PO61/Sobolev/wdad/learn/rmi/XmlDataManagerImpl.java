package PO61.Sobolev.wdad.learn.rmi;

import PO61.Sobolev.wdad.learn.xml.NotFoundException;
import PO61.Sobolev.wdad.learn.xml.XmlTask;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

/* todo все иные (кроме RemoteExcpetion) исключения, которые могут быть порождены в методах,
   нужно оборачивать в RemoteException и выбрасывать его. А в клиенте эти исключения обрабатывать и хотябы
   выводить сообщение в консоль о причине косяка=)
 */
public class XmlDataManagerImpl implements XmlDataManager, Serializable {
    private  XmlTask xmlTask;
    public XmlDataManagerImpl(String path) throws ParserConfigurationException, SAXException, IOException, RemoteException{
        xmlTask = new XmlTask(path);
    }

    @Override
    public double getBill(Building building, int flatNumber, int month, int year) throws NotFoundException,RemoteException {
        return xmlTask.getBill(building.street,Integer.valueOf(building.number),flatNumber, month, year);
    }

    @Override
    public void setTariff(String tariffName, double newValue) throws RemoteException, TransformerException {
        xmlTask.setTariff(tariffName,newValue);
    }

    @Override
    public void addRegistration(Building building, int flatNumber, Registration registration, int month, int year)
            throws TransformerException, NotFoundException, RemoteException {
        xmlTask.addRegistration(building.street,Integer.valueOf(building.number),flatNumber,year,month,registration.coldwater,
                registration.hotwater, registration.electricity, registration.gas);
    }
}
