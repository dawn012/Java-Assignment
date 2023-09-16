package hall_management;

import Connect.DatabaseUtils;
import Driver.Name;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Hall {
    private int hallID;
    private Name hallName;
    private String hallType;
    private int hallCapacity;
    private int status;

    // Constructor
    public Hall(){
    }

    public Hall(Name hallName, String hallType){
        this.hallName = hallName;
        this.hallType = hallType;
        calHallCapacity();
    }

    public Hall(int hallID, Name hallName, String hallType){
        this.hallID = hallID;
        this.hallName = hallName;
        this.hallType = hallType;
        calHallCapacity();
    }

    public void calHallCapacity() {
        if (hallType.equals("STANDARD")) {
            hallCapacity = 64;
        }
        else if (hallType.equals("3D")) {
            hallCapacity = 32;
        }
    }

    // Method
    public void viewHallDetails(){
        System.out.printf("\nHall Detail:\n");
        System.out.println("Hall Name: " + hallName.getName());
        System.out.println("Hall Type: " + hallType + " HALL");
        System.out.println("Hall Capacity: " + hallCapacity);
    }

    public int modifyHallDetails(Scanner sc) {
        boolean error = true;

        do {
            int count = 1;
            try {
                System.out.printf("\nHall Detail:\n");
                System.out.println(count + ". Hall Name: " + hallName.getName());
                count++;
                System.out.println(count + ". Hall Type: " + hallType + " HALL");
                count++;
                System.out.println(count + ". Hall Capacity: " + hallCapacity + " (cannot be modified)");

                System.out.print("\nEnter the serial number of the hall information you want to change (0 - Stop): ");
                int serialNum = sc.nextInt();
                sc.nextLine();

                if (serialNum < 0 || serialNum > count) {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                    error = true;
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

    public boolean modifyHall() throws SQLException {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `hall` SET `hall_name`= ?, `hall_type`= ?, `hall_capacity`= ? WHERE hall_id = ?";
            Object[] params = {hallName.getName(), hallType, hallCapacity, hallID};
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

    public boolean deleteHall() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {hallID};
            rowAffected = DatabaseUtils.deleteQueryById("hall", "hall_status", "hall_id", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe hall has been deleted.");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    // Setter
    public void setHallID(int hallID) {
        this.hallID = hallID;
    }

    public void setHallName(Name hallName) {
        this.hallName = hallName;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
        calHallCapacity();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Getter
    public int getHallID(){
        return hallID;
    }

    public Name getHallName() {
        return hallName;
    }

    public String getHallType() {
        return hallType;
    }

    public int getHallCapacity() {
        return hallCapacity;
    }

    public int getStatus() {
        return status;
    }
}
