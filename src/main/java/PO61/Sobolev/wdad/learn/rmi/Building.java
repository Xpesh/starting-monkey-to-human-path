package PO61.Sobolev.wdad.learn.rmi;

import java.io.Serializable;
import java.util.List;

public class Building implements Serializable {
    String street;
    String number;

    public Building(String street, String number) {

        this.street = street;
        this.number = number;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
