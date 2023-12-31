package Ticket_Managemnet;

import Database.DatabaseUtils;
import Booking_Management.Booking;
import Schedule_Management.Schedule;
import Seat_Management.Seat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Ticket {
    private int ticketId;
    private Seat seat;
    private Schedule schedule;
    private String ticketType;
    private double priceRate;
    private int ticketStatus;

    public Ticket() {
        this.ticketStatus=1;
    }

    public Ticket(int ticket_id, Seat seat, String ticketType, double priceRate, Schedule schedule) {
        this.ticketId = ticket_id;
        this.seat = seat;
        this.ticketType = ticketType;
        this.priceRate = priceRate;
        this.schedule = schedule;
    }

    //Getter
    public int getTicket_id() {
        return ticketId;
    }
    public Seat getSeat() {
        return seat;
    }
    public Schedule getTimeTable() {
        return schedule;
    }
    public String getTicketType() {
        return ticketType;
    }
    public double getPriceRate() {
        return priceRate;
    }

    //Setter
    public void setTicket_id(int ticket_id) {
        this.ticketId=ticket_id;
    }
    public int countTicket_id(int count) {
        this.ticketId=1;
        int largeId=0;
        //ArrayList<Ticket> tickets=Ticket.getBookedTicketList();
        boolean error = false;
        ArrayList<Ticket> tickets = new ArrayList<>();
        try {
            Object[] params = {};
            ResultSet result = DatabaseUtils.selectQuery("*", "ticket",null,null);

            while (result.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketType(result.getString("ticket_type"));
                ticket.setTicket_id(result.getInt("ticket_id"));
                Seat seat = new Seat();
                seat.setSeatId(result.getString("seat_id"));
                ticket.setSeat(seat);
                Booking booking =new Booking();
                booking.setBookingId(result.getInt("booking_id"));
                Schedule timetable=new Schedule();
                timetable.setScheduleID(result.getInt("schedule_id"));
                ticket.setTicketStatus(result.getInt("ticket_status"));
                ticket.setPriceRate(result.getDouble("price_rate"));
                tickets.add(ticket);
            }
            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        for(Ticket t:tickets){
            if(t.getTicket_id()>=largeId)
                largeId=t.getTicket_id();
        }
        this.ticketId=largeId;
        this.ticketId+=count;
        return this.ticketId;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    public void setTimeTable(Schedule schedule) {
        this.schedule = schedule;
    }
    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
    public void setPriceRate(double priceRate) {
        this.priceRate = priceRate;
    }



    //--------------------------------------------------------------------------------------------------------------------------------

    public static ArrayList<Ticket> getBookedTicketList(){
        boolean error = false;
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            Object[] params = {1};
            ResultSet result = DatabaseUtils.selectQuery("*", "ticket","ticket_status = ?",params);

            while (result.next()) {

                Ticket ticket = new Ticket();

                ticket.setTicketType(result.getString("ticket_type"));
                ticket.setTicket_id(result.getInt("ticket_id"));
                Seat seat = new Seat();
                seat.setSeatId(result.getString("seat_id"));
                ticket.setSeat(seat);

                Booking booking =new Booking();
                booking.setBookingId(result.getInt("booking_id"));
                //ticket.setBooking(booking);
                //ticket.booking.setBooking_id(result.getInt("booking_id"));
                Schedule timetable=new Schedule();
                timetable.setScheduleID(result.getInt("schedule_id"));
                //ticket.setBooking(booking);
                ticket.setTicketStatus(result.getInt("ticket_status"));
                ticket.setPriceRate(result.getDouble("price_rate"));
                tickets.add(ticket);


            }

            result.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }


    public static ArrayList<Ticket> getBookedTicketList(int schedule_id){
        boolean error = false;
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            Object[] params = {schedule_id,1};
            ResultSet result = DatabaseUtils.selectQuery("*", "ticket","schedule_id = ? AND ticket_status = ?",params);
            //Ticket ticket = null;
            while (result.next()) {

                Ticket ticket = new Ticket();

                ticket.setTicketType(result.getString("ticket_type"));
                ticket.setTicket_id(result.getInt("ticket_id"));
                Seat seat = new Seat();
                seat.setSeatId(result.getString("seat_id"));
                ticket.setSeat(seat);
                ticket.setTicketStatus(result.getInt("ticket_status"));
                Booking booking =new Booking();
                booking.setBookingId(result.getInt("booking_id"));
                //ticket.setBooking(booking);

                Schedule timetable=new Schedule();
                timetable.setScheduleID(result.getInt("schedule_id"));
                //ticket.setBooking(booking);

                ticket.setPriceRate(result.getDouble("price_rate"));
                tickets.add(ticket);

            }

            result.close();
            //resultHall.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }
    public static void insertTicket(Ticket ticket,Booking booking) throws Exception {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `ticket` (`ticket_id`,`booking_id`,`seat_id`,`schedule_id`,`ticket_type`,`price_rate`,`ticket_status`) value(?,?,?,?,?,?,?);";
            Object[] params = {ticket.getTicket_id(),booking.getBookingId(),ticket.getSeat().getSeatId(),ticket.schedule.getScheduleID(),ticket.getTicketType(),ticket.getPriceRate(),ticket.getTicketStatus()};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("Ticket successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }
    public double calculateTicketPrice(){
        return this.schedule.getMovie().getBasicTicketPrice()*this.priceRate;

    }
    public boolean updateStatus() throws SQLException {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `ticket` SET `ticket_status`= ? WHERE `ticket_id` = ?";
            Object[] params = {getTicketStatus(),getTicket_id()};
            rowAffected = DatabaseUtils.updateQuery(updateSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            return true;
        }
        else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }


}
//    public void addTicket() throws Exception {
//        int rowAffected = 0;
//
//        try {
//            String insertSql = "INSERT INTO `ticket` (`ticket_id`,`booking_id`,``,`seat_id`,`schedule_id`,`ticket_type`,`price_rate`) value(?,?,?,?,?,?);";
//            Object[] params = {getSeat_id(),this.hall.getHallID(),getSeatRow(),getSeatCol(),getSeat_status()};
//            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        if (rowAffected > 0) {
//            System.out.println("\nSeat successfully added...");
//        }
//        else {
//            System.out.println("\nSomething went wrong!");
//        }
//    }