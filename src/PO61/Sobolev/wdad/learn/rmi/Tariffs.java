package PO61.Sobolev.wdad.learn.rmi;

import java.util.HashMap;

public class Tariffs {
    static final HashMap<String, Double> values = new HashMap<>(); // todo в этой мапе должны быть пары: key - имя тарифа, value - соответственно стоимость
    static final String COLDWATER_KEY = "coldwater";
    static final String HOTWATER_KEY = "hotwater";
    static final String ELECTRICITY_KEY = "electricity";
    static final String GAS_KEY = "gas";
}
