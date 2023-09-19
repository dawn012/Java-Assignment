package Hall_Management;

import Database.DatabaseUtils;
import Driver.Name;
import Seat_Management.Seat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Hall {
    private int hallID;
    private Name hallName;
    private String hallType;
    private int hallCapacity;
    private ArrayList<Seat> seats;

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
        System.out.println("\n---------------");
        System.out.printf("| Hall Detail | \n");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%c %-20s %c %-55s %c\n", '|', "Hall Name", '|', hallName.getName(), '|');
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%c %-20s %c %-55s %c\n", '|', "Hall Type", '|', hallType + " HALL", '|');
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%c %-20s %c %-55s %c\n", '|', "Hall Capacity", '|', hallCapacity, '|');
        System.out.println("----------------------------------------------------------------------------------");
    }

    public int modifyHallDetails(Scanner sc) {
        boolean error = true;

        do {
            int count = 1;
            try {
                System.out.printf("\nHall Detail:\n");
                System.out.println(count + ". Hall Name: " + getHallName().getName());
                count++;
                System.out.println(count + ". Hall Type: " + getHallType() + " HALL");
                count++;
                System.out.println(count + ". Hall Capacity: " + getHallCapacity() + " (cannot be modified)");
                count++;
                System.out.println(count + ". Manage Seats Status");

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
            Object[] params = {getHallID()};
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

    //ChinYong Part
    //init the hall's seat from database
    public void initSeatList() throws SQLException {
        ArrayList<Seat> seats = new ArrayList<>();

        try {
            Object[] params = {this.hallID};
            ResultSet result = DatabaseUtils.selectQueryById("*", "seat", "hall_id = ?", params);

            while (result.next()) {
                Seat seat = new Seat();

                seat.setSeatId(result.getString("seat_id"));
                //seat.hall.setHallID(hallId);
                seat.setSeatRow(result.getInt("seatrow"));
                seat.setSeatCol(result.getInt("seatcol"));
                seat.setSeatStatus(result.getInt("seat_status"));

                seats.add(seat);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        this.seats = seats;
    }

    public void viewSeatStatus() {
        boolean error = false;

        int largestRow=0;
        int largestCol=0;

        for(Seat seats:this.getSeats()){
            largestRow=seats.getSeatRow();
            largestCol=seats.getSeatCol();
        }

        System.out.println("\t\t\t1\t\t2\t\t3\t\t4\t\t5\t\t6\t\t7\t\t8");
        int j=0;

        for(int i = 1 ; i <= largestRow ; i++) {
            System.out.printf("\t\t");
            char letter = (char) ('A' + i - 1);
            System.out.print(letter+" ");
            do {
                char st;
                if(seats.get(j).getSeatStatus()==1) {
                    st='O';
                }else{
                    st='X';
                }

                System.out.printf("[%s]:%c ",seats.get(j).getSeatId(),st);
                j++;
            } while (seats.get(j).getSeatCol()+1 <= largestCol);

            char st;

            if(seats.get(j).getSeatStatus()==1) {
                st='O';
            }else{
                st='X';
            }

            System.out.printf("[%s]:%c ",seats.get(j).getSeatId(),st);
            System.out.printf("\n");
            j += 1;
        }

        System.out.printf("\nO = Available/intact and undamaged\tX = Unavailable/damaged");
        //return 0;
    }
    public void addNewSeatList() throws Exception {

        try {
            Object[] params = {hallName.getName()};
            ResultSet result = DatabaseUtils.selectQueryById("*", "hall", "hall_name = ?", params);

            while (result.next()) {

                setHallID(result.getInt("hall_id"));

            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Seat> seatList = new ArrayList<>();
        int i=1;
        int j=1;
        int seatsPerRow = 8;
        int row = hallCapacity / seatsPerRow;
        int count=hallCapacity;
        if (hallCapacity % seatsPerRow != 0) {
            row++;
        }

        for(i=1;i<=row;i++){
                char letter = (char) ('A' + i - 1); // 将整数 i 转换为字母
                for (j=1;j<=seatsPerRow;j++){
                    if(count>0) {
                        Seat seat =new Seat();
                        String seatId = String.valueOf(hallID) + letter + String.valueOf(j);
                        seat.setSeatId(seatId);
                        seat.setSeatRow(i);
                        seat.setSeatCol(j);
                        seat.setSeatStatus(1);
                        seat.addSeat();
                        seatList.add(seat);
                    }
                    count--;
                }
            }
        setSeats(seatList);
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

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
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

    public ArrayList<Seat> getSeats() {
        return seats;
    }
}