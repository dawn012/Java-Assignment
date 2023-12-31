package Seat_Management;

import Database.DatabaseUtils;
import Hall_Management.Hall;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Seat {
    //Data Field
    private String seatId;
    private int seatRow;
    private int seatCol;
    private int seatStatus;

    //Constructor
    public Seat() {
    }
    public Seat(String seatId, int seatRow, int seatCol, int seatStatus) {
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.seatStatus = seatStatus;
    }



    //Getter

    public String getSeatId() {
        return seatId;
    }
    public int getSeatRow() {
        return seatRow;
    }
    public int getSeatCol() {
        return seatCol;
    }
    public int getSeatStatus() {
        return seatStatus;
    }

    //Setter
    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }
    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }
    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }
    public void setSeatStatus(int seatStatus) {
        this.seatStatus = seatStatus;
    }
//---------------------------------------------------------------------------------------------------------------------------------

    public void addSeat() throws Exception {


        int hallId=0;
        int rowAffected = 0;
        if(seatId.length()==3) {
            char firstChar = seatId.charAt(0);
            hallId = Character.getNumericValue(firstChar);
        }else if(seatId.length()==4){
            String firstTwoChars = seatId.substring(0, 2);
            hallId = Integer.parseInt(firstTwoChars);
        }
        try {
            String insertSql = "INSERT INTO `seat` (`seat_id`,`hall_id`,`seatrow`,`seatcol`,`seat_status`) value(?,?,?,?,?);";
            Object[] params = {getSeatId(),hallId,getSeatRow(),getSeatCol(), getSeatStatus()};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            //System.out.println("\nSeat successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public static int viewSeatList(int hallId) {
        boolean error = false;
        ArrayList<Seat> seats = new ArrayList<>();
        int largestRow=0;
        int largestCol=0;
        try {
            Object[] params = {hallId};
            ResultSet result = DatabaseUtils.selectQuery("*", "seat", "hall_id = ?", params);
            //find hall

            while (result.next()) {

                Seat seat = new Seat();
                Hall hl=new Hall();
                //seat.setHall(hl);
                seat.setSeatId(result.getString("seat_id"));
                //seat.hall.setHallID(hallId);
                seat.setSeatRow(result.getInt("seatrow"));
                seat.setSeatCol(result.getInt("seatcol"));
                seat.setSeatStatus(result.getInt("seat_status"));

                largestRow=result.getInt("seatrow");
                largestCol=result.getInt("seatcol");
                seats.add(seat);
            }

            result.close();
            //resultHall.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }



        int j=0;
        for(int i=1;i<=largestRow;i++) {
            do {
                System.out.printf("[%s] ", seats.get(j).getSeatId());
                j++;
            } while (seats.get(j).seatCol+1 <= largestCol);
            System.out.printf("[%s] ", seats.get(j).getSeatId());
            System.out.printf("\n");
            j += 1;
        }
        System.out.printf("test:OK");
        return 0;
    }

    public static int viewSeat_status(int hallId) {
        boolean error = false;
        ArrayList<Seat> seats = new ArrayList<>();
        int largestRow=0;
        int largestCol=0;
        try {
            Object[] params = {hallId};
            ResultSet result = DatabaseUtils.selectQuery("*", "seat", "hall_id = ?", params);
            //find hall

            while (result.next()) {

                Seat seat = new Seat();
                //Hall hl=new Hall();
                //seat.setHall(hl);
                seat.setSeatId(result.getString("seat_id"));
                //seat.hall.setHallID(hallId);
                seat.setSeatRow(result.getInt("seatrow"));
                seat.setSeatCol(result.getInt("seatcol"));
                seat.setSeatStatus(result.getInt("seat_status"));
                //System.out.printf("%d",seat.hall.getHallID());
                largestRow=result.getInt("seatrow");
                largestCol=result.getInt("seatcol");
                seats.add(seat);
            }

            result.close();
            //resultHall.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println("   1   2   3   4   5   6   7   8");
        int j=0;
        for(int i=1;i<=largestRow;i++) {
            char letter = (char) ('A' + i - 1);
            System.out.print(letter+" ");
            do {
                char st;
                if(seats.get(j).getSeatStatus()==1) {
                    st='O';
                }else{
                    st='X';
                }

                System.out.printf("[%c] ",st);
                j++;
            } while (seats.get(j).seatCol+1 <= largestCol);
            char st;
            if(seats.get(j).getSeatStatus()==1) {
                st='O';
            }else{
                st='X';
            }
            System.out.printf("[%c] ",st);
            System.out.printf("\n");
            j += 1;
        }
        System.out.printf("test:OK");
        return 0;
    }
    public static boolean checkSeatValidation(String row,int col){
        if(col<1||col>8)
            return false;
        else if(row.length()>1)
            return false;
        else if (row.charAt(0) < 'A' || row.charAt(0) > 'H')
            return false;
        else
            return true;
    }

    public void updateSeatStatus() throws SQLException {
        try {
            String updateSql = "UPDATE `seat` SET `seat_status`= ? WHERE seat_id = ?";
            Object[] params = {getSeatStatus(), getSeatId()};
            int rowAffected = DatabaseUtils.updateQuery(updateSql, params);
            if (rowAffected > 0) {
                System.out.printf("[%s] Updated...", getSeatId());
            } else {
                System.out.println("\nSomething went wrong...");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
