package Cinema_Management;

import Database.DatabaseUtils;
import Driver.DatabaseOperations;
import Driver.Name;
import Hall_Management.Hall;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cinema implements DatabaseOperations {
    private Hall hall;
    private int cinemaID;
    private Name cinemaName;
    private Address cinemaAddress;
    private String cinemaPhone;
    private int status;
    private static final String OFFICE_PHONE_REGEX = "^(01[023456789]-[0-9]{7}|011-[0-9]{8}|03-[0-9]{8}|08[0-9]-[0-9]{6}|0[12456789]-[0-9]{7})$";

    public Cinema(){
    }

    public Cinema(int cinemaID, Name cinemaName, Address cinemaAddress, String cinemaPhone) {
        this.cinemaID = cinemaID;
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.cinemaPhone = cinemaPhone;
    }

    public Cinema(Hall hall, int cinemaID, Name cinemaName, Address cinemaAddress, String cinemaPhone) {
        this.hall = hall;
        this.cinemaID = cinemaID;
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.cinemaPhone = cinemaPhone;
    }

    // Method
    public static ArrayList<Cinema> viewCinemaList(int status) throws SQLException {
        ArrayList<Cinema> cinemas = new ArrayList<>();

        try {
            Object[] params = {status};
            ResultSet result = DatabaseUtils.selectQueryById("*", "cinema", "cinema_status = ?", params);

            while (result.next()) {
                Cinema cinema = new Cinema();

                cinema.setCinemaID(result.getInt("cinema_id"));
                cinema.setCinemaName(new Name(result.getString("cinema_name")));
                cinema.setCinemaAddress(new Address(result.getString("cinema_address")));
                cinema.setCinemaPhone(result.getString("cinema_phone"));
                cinema.setStatus(result.getInt("cinema_status"));

                cinemas.add(cinema);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.printf("\n%-5s %s\n", "No", "Cinema Name");
        for (int i = 0; i < cinemas.size(); i++) {
            System.out.printf("%-5d %s\n", (i + 1), cinemas.get(i).getCinemaName().getName());
        }

        return cinemas;
    }

    public void viewCinemaDetails() throws SQLException {
        System.out.printf("\nCinema Detail:\n");
        System.out.println("Cinema Name: " + cinemaName.getName());
        System.out.println("Cinema Address: " + cinemaAddress.getAddress());
        System.out.println("Cinema Phone: " + cinemaPhone);
    }

    public boolean isValidOfficePhoneNumber() {
        Pattern pattern = Pattern.compile(OFFICE_PHONE_REGEX);
        Matcher matcher = pattern.matcher(cinemaPhone);

        return matcher.matches();
    }

    public boolean add() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {cinemaName.getName(), cinemaAddress.getAddress(), cinemaPhone};
            String sql = "INSERT INTO `cinema` (`cinema_name`, `cinema_address`, `cinema_phone`) VALUES (?, ?, ?)";
            rowAffected = DatabaseUtils.insertQuery(sql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nCinema successfully added...");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public int modifyCinemaDetails(Scanner sc) {
        boolean error = true;

        do {
            try {
                int count = 1;
                System.out.printf("\nCinema Detail:\n");
                System.out.println(count + ". Cinema Name: " + cinemaName.getName());
                count++;
                System.out.println(count + ". Cinema Address: " + cinemaAddress.getAddress());
                count++;
                System.out.println(count + ". Cinema Phone: " + cinemaPhone);

                System.out.print("\nEnter the serial number of the cinema information you want to change (0 - Stop): ");
                int serialNum = sc.nextInt();
                sc.nextLine();

                if (serialNum < 0 || serialNum > count) {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                } else {
                    return serialNum;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
                error = true;
            }
        } while (error);

        return 0;
    }

    public boolean modify() throws SQLException {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `cinema` SET `cinema_name`= ?, `cinema_address`= ?, `cinema_phone`= ? WHERE cinema_id = ?";
            Object[] params = {cinemaName.getName(), cinemaAddress.getAddress(), cinemaPhone, cinemaID};
            rowAffected = DatabaseUtils.updateQuery(updateSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe changes have been saved.");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public boolean delete() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {cinemaID};
            rowAffected = DatabaseUtils.deleteQueryById("cinema", "cinema_status", "cinema_id", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe cinema has been deleted.");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public ArrayList<Hall> getHallList(int status) throws SQLException {
        ArrayList<Hall> halls = new ArrayList<>();

        try {
            Object[] params = {cinemaID, status};
            ResultSet result = DatabaseUtils.selectQueryById("*", "hall", "cinema_id = ? AND hall_status = ?", params);

            while (result.next()) {
                Hall hall = new Hall();

                hall.setHallID(result.getInt("hall_id"));
                hall.setHallName(new Name(result.getString("hall_name")));
                hall.setHallType(result.getString("hall_type"));
                hall.calHallCapacity();
                hall.setStatus(result.getInt("hall_status"));

                halls.add(hall);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return halls;
    }

    public boolean addHall(Hall hall){
        int rowAffected = 0;

        try {
            Object[] params = {cinemaID, hall.getHallName().getName(), hall.getHallType(), hall.getHallCapacity()};
            String sql = "INSERT INTO `hall` (`cinema_id`, `hall_name`, `hall_type`, `hall_capacity`) VALUES (?, ?, ?, ?)";
            rowAffected = DatabaseUtils.insertQuery(sql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nHall successfully added...");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public void setCinemaID(int cinemaID) {
        this.cinemaID = cinemaID;
    }

    public void setCinemaName(Name cinemaName) {
        this.cinemaName = cinemaName;
    }

    public void setCinemaAddress(Address cinemaAddress) {
        this.cinemaAddress = cinemaAddress;
    }

    public void setCinemaPhone(String cinemaPhone) {
        this.cinemaPhone = cinemaPhone;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Hall getHall() {
        return hall;
    }

    public int getCinemaID() {
        return cinemaID;
    }

    public Name getCinemaName() {
        return cinemaName;
    }

    public Address getCinemaAddress() {
        return cinemaAddress;
    }

    public String getCinemaPhone() {
        return cinemaPhone;
    }

    public int getStatus() {
        return status;
    }
}