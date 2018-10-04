package PO61.Sobolev.wdad.learn.rmi;

import java.io.Serializable;
import java.util.List;

public class Flat implements Serializable{
    int number;
    int personsQuantity;
    double area;

    public Flat(int number, int personsQuantity, double area, List<Registration> registrations) {
        this.number = number;
        this.personsQuantity = personsQuantity;
        this.area = area;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPersonsQuantity() {
        return personsQuantity;
    }

    public void setPersonsQuantity(int personsQuantity) {
        this.personsQuantity = personsQuantity;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

}
