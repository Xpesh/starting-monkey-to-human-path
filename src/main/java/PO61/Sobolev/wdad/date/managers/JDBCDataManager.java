package PO61.Sobolev.wdad.date.managers;

import PO61.Sobolev.wdad.date.storage.DataSourceFactory;
import PO61.Sobolev.wdad.learn.rmi.Building;
import PO61.Sobolev.wdad.learn.rmi.Registration;
import PO61.Sobolev.wdad.learn.xml.NotFoundException;

import javax.sql.DataSource;
import javax.xml.transform.TransformerException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class JDBCDataManager implements DataManager {

    DataSource dataSource;

    public JDBCDataManager() {
        try {
            this.dataSource = DataSourceFactory.createDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBill(Building building, int flatNumber, int month, int year) throws NotFoundException, RemoteException, SQLException {
        try(Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement(); Statement statement2 = connection.createStatement()){
            int buildingId = findIdStreet(building.getStreet(),statement);
            int flatId = findIdFlat(flatNumber,buildingId,statement);
            int registrationId = findIdRegistration(flatId,month,year,statement);
            ResultSet registrationTariff = findRegistrationTariff(registrationId,statement);
            double bill=0;
            while (registrationTariff.next()){
                bill+=registrationTariff.getDouble(2) * findCostTariff(registrationTariff.getString(4),statement2);
            }
            return bill;
        }
    }


    private int findIdStreet(String street, Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM `street` WHERE `name` LIKE '%s'",street));
        resultSet.next();
        return resultSet.getInt(1);
    }
    private int findIdFlat(int flatNumber, int buildingId, Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM `flats` WHERE `number` = %d AND `buildings_id` = %d",flatNumber, buildingId));
        resultSet.next();
        return resultSet.getInt(1);
    }
    private int findIdRegistration(int flatId, int month, int year, Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM `registrations` WHERE `date` >= '%d-%d-1' AND `date` <= '%d-%d-31' AND `flats_id` = %d",year,month,year,month,flatId));
        resultSet.next();
        return resultSet.getInt(1);
    }
    private ResultSet findRegistrationTariff(int registrationId, Statement statement) throws SQLException {
       return statement.executeQuery(String.format("SELECT * FROM `registrations-tarifs` WHERE `registrations_id` = %d",registrationId));
    }
    private double findCostTariff(String tariffName, Statement statement) throws SQLException {
       ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM `tariffs` WHERE `name` LIKE '%s'",tariffName));
       resultSet.next();
       return resultSet.getDouble(2);
    }

    public void setTariff(String tariffName, double newValue) throws RemoteException, TransformerException, SQLException {
        Locale.setDefault(new Locale("en", "USA"));//todo
        try(Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()){
            statement.executeUpdate(String.format("UPDATE `tariffs` SET `cost` = '%f' WHERE `tariffs`.`name` = '%s'",newValue,tariffName));
        }
    }


    public void addRegistration(Building building, int flatNumber, Registration registration, int month, int year) throws TransformerException, NotFoundException, RemoteException, SQLException {
        Locale.setDefault(new Locale("en", "USA"));//todo
        try(Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement(); Statement statement2 = connection.createStatement()){
            int buildingId = findIdStreet(building.getStreet(),statement);
            int flatId = findIdFlat(flatNumber,buildingId,statement);

            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM `registrations` WHERE `date` >= '%d-%d-1' AND `date` <= '%d-%d-31' AND `flats_id` = %d",year,month,year,month,flatId));
            if(resultSet.next()){
                int registrationId = resultSet.getInt(1);
//                System.out.println(registrationId);
                ResultSet rs = findRegistrationTariff(registrationId,statement);
                while (rs.next()){
                    System.out.println(rs.getInt(1));
                    switch (rs.getString(4)){
                        case "hot_water" : {
                            statement2.executeUpdate(String.format("UPDATE `registrations-tarifs` SET `amount` = '%f' WHERE `registrations-tarifs`.`id` = %d;",registration.getHotwater(),rs.getInt(1)));
                            break;
                        }
                        case "cold_water" :{
                            statement2.executeUpdate(String.format("UPDATE `registrations-tarifs` SET `amount` = '%f' WHERE `registrations-tarifs`.`id` = %d;",registration.getColdwater(),rs.getInt(1)));
                            break;
                        }
                        case "gas" :{
                            statement2.executeUpdate(String.format("UPDATE `registrations-tarifs` SET `amount` = '%f' WHERE `registrations-tarifs`.`id` = %d;",registration.getGas(),rs.getInt(1)));
                            break;
                        }
                        case "electricity" :{
                            statement2.executeUpdate(String.format("UPDATE `registrations-tarifs` SET `amount` = '%f' WHERE `registrations-tarifs`.`id` = %d;",registration.getElectricity(),rs.getInt(1)));
                            break;
                        }

                    }
                }
            }else {
                statement.execute(String.format("INSERT INTO `registrations` (`id`, `date`, `flats_id`) VALUES (NULL, '%d-%d-01', '%d');",year,month,flatId));
                int registrationId = findIdRegistration(flatId,month,year,statement);
                statement.execute(String.format("INSERT INTO `registrations-tarifs` (`id`, `amount`, `registrations_id`, `tariffs_name`) VALUES (NULL, '%f', '%d', '%s');",registration.getElectricity(),registrationId,"electricity"));
                statement.execute(String.format("INSERT INTO `registrations-tarifs` (`id`, `amount`, `registrations_id`, `tariffs_name`) VALUES (NULL, '%f', '%d', '%s');",registration.getGas(),registrationId,"gas"));
                statement.execute(String.format("INSERT INTO `registrations-tarifs` (`id`, `amount`, `registrations_id`, `tariffs_name`) VALUES (NULL, '%f', '%d', '%s');",registration.getColdwater(),registrationId,"cold_water"));
                statement.execute(String.format("INSERT INTO `registrations-tarifs` (`id`, `amount`, `registrations_id`, `tariffs_name`) VALUES (NULL, '%f', '%d', '%s');",registration.getHotwater(),registrationId,"hot_water"));
            }
        }
    }
    /**
     реализующий интерфейс DataManager. Реализация методов интерфейса должна быть
     основана на SQL-запросах к созданной в 1-м задании БД, с использованием средств JDBC.
     Ссылку на источник данных (DataSource), используемый для получения соединения и
     отправки SQL-запросов, получаем с помощью фабрики DataSourceFactory.




     public double getBill (Building building, int flatNumber) – возвращающий
     cумму платежа за текущий месяц.

     public void setTariff (String tariffName, double newValue) – изменяющий
     стоимость заданной единицы показания счетчика (ХВС, ГВС,
     электроэнергия, газ).

     public void addRegistration (Building building, int flatNumber, Registration
     registration) – добавляющий (или заменяющий) показания счетчиков к
     заданной квартире в заданный период.

     */

}
