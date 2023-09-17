package Booking_Management;

import Database.DatabaseUtils;
import Driver.DateTime;
import Hall_Management.Hall;
import Schedule_Management.Schedule;
import Seat_Management.Seat;

import Ticket_Managemnet.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Scanner;


public class Booking {
    /////////////////////////////////////////////////////////
    private int customerId;  //暂时用，暂时代替Customer class///
    /////////////////////////////////////////////////////////
    private int booking_id;
    private int adultTicket_qty;
    private int childTicket_qty;
    private double totalPrice;
    private DateTime bookingDateTime;
    private LocalTime bookingTime;
    private int booking_status;
    private ArrayList<Ticket> ticketList;

    public Booking() {
        this.childTicket_qty=0;
        this.adultTicket_qty=0;
        this.totalPrice=0;
    }

    public Booking(int booking_id, int adultTicket_qty, int childTicket_qty, double totalPrice, int booking_status) {
        this.booking_id = booking_id;
        this.adultTicket_qty = adultTicket_qty;
        this.childTicket_qty = childTicket_qty;
        this.totalPrice = totalPrice;
        this.booking_status = booking_status;
    }

    //Getter
    public int getBooking_id() {
        return booking_id;
    }

    public void countBooking_id() {
        this.booking_id=1;
        //this.ticket_id = ticket_id;
        ArrayList<Booking> bookings=Booking.getBookingList();
        for(Booking b:bookings){
            this.booking_id++;
        }
//        this.ticket_id+=count;
        //return this.booking_id;
    }
    public int getAdultTicket_qty() {
        return adultTicket_qty;
    }
    public int getChildTicket_qty() {
        return childTicket_qty;
    }
    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public DateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public ArrayList<Ticket> getTicketList() {
        return ticketList;
    }

    public int getBooking_status() {
        return booking_status;
    }

    //Setter
    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }
    public void setAdultTicket_qty(int adultTicket_qty) {
        this.adultTicket_qty = adultTicket_qty;
    }
    public void setChildTicket_qty(int childTicket_qty) {
        this.childTicket_qty = childTicket_qty;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setBookingDateTime(DateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public void setBooking_status(int booking_status) {
        this.booking_status = booking_status;
    }

    public void setTicketList(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void insertBooking(Booking b) throws Exception {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `booking` (`booking_id`,`adultTicket_qty`,`childTicket_qty`,`total_price`,`booking_date`,`booking_time`,`booking_status`) value(?,?,?,?,?,?,?);";
            Object[] params = {b.getBooking_id(),b.getAdultTicket_qty(),b.getChildTicket_qty(),b.getTotalPrice(),b.getBookingDateTime().getDate(),b.getBookingTime(),b.getBooking_status()};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("Booking successfully added...");
        }
        else {
            System.out.println("\nSomething went wrong!");
        }
    }

    public static int viewSeat_status(Schedule schedule) {
        boolean error = false;
        ArrayList<Seat> seats = new ArrayList<>();
        int largestRow=0;
        int largestCol=0;
        try {
            Object[] params = {schedule.getHall().getHallID()};
            ResultSet result = DatabaseUtils.selectQueryById("*", "seat", "hall_id = ?", params);
            //find hall

            while (result.next()) {

                Seat seat = new Seat();
                //Hall hl=new Hall();
                //seat.setHall(hl);
                seat.setSeat_id(result.getString("seat_id"));
                //seat.getHall().setHallID(hallId);
                Hall hall =new Hall();
                hall.setHallID(result.getInt("hall_id"));
                //seat.setHall(hall);
                seat.setSeatRow(result.getInt("seatrow"));
                seat.setSeatCol(result.getInt("seatcol"));
                seat.setSeat_status(result.getInt("seat_status"));
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

        ArrayList<Ticket> tickets=Ticket.getBookedTicketList(schedule.getScheduleID());
        System.out.printf("Movie : %s   Hall : %d   Date : %s   Start Time : %s:%s\n",schedule.getMovie().getMvName().getName(),schedule.getHall().getHallID(),schedule.getShowDate().getDate(),schedule.getStartTime().getHour(),schedule.getStartTime().getMinute());
        System.out.println("\t             [Screen]");
        //System.out.println("   1   2   3   4   5   6   7   8");
        int j=0;
        for(int i=1;i<=largestRow;i++) {
            char letter = (char) ('A' + i - 1);
            System.out.print("\t"+letter+" ");
            do {
                char st;
                if(seats.get(j).getSeat_status()==1) {
                    st='O';
                }else{
                    st='X';
                }
                for (Ticket t:tickets) {
                    if(t.getSeat().getSeat_id().equals(seats.get(j).getSeat_id())){
                        st='1';
                    }
                }
                System.out.printf("[%c] ",st);
                j++;
            } while (seats.get(j).getSeatCol() +1 <= largestCol);
            char st;
            if(seats.get(j).getSeat_status()==1) {
                st='O';
            }else{
                st='X';
            }
            for (Ticket t:tickets) {
                if(t.getSeat().getSeat_id().equals(seats.get(j).getSeat_id())){
                    st='1';
                }
            }
            System.out.printf("[%c] ",st);
            System.out.printf("\n");
            j += 1;
        }
        System.out.println("\t   1   2   3   4   5   6   7   8");
        System.out.printf("\nO = Available    1 = Booked    X = Unavailable/Broken\n");
        System.out.printf("Movie Basic Price : RM%.2f\n",schedule.getMovie().getBasicTicketPrice());
        return 0;
    }

    public boolean executeBooking(Schedule schedule) throws Exception {
        this.countBooking_id();
        this.setBooking_status(0);
        Scanner scanner = new Scanner(System.in);
        String confirmStr="R";
        char confirmChar=confirmStr.charAt(0);
        while (confirmChar!='Y'&& confirmChar!='N') {
            this.childTicket_qty=0;
            this.adultTicket_qty=0;
            this.totalPrice=0;
            System.out.println("Time table id : "+schedule.getScheduleID());
            Booking.viewSeat_status(schedule);
            ArrayList<Ticket> tickets = Ticket.getBookedTicketList(schedule.getScheduleID());
            ArrayList<Ticket> cartTicket = new ArrayList<>();
            System.out.println("Booking ID : " + this.booking_id);
            String strRow=" ";
            int col = 0;
            char row =strRow.charAt(0);

            String str = " ";
            char ch = str.charAt(0);

            int inputType = 0;
            String ticketType = "";
            double priceRate = 0.0;
            int count = 1;

            while (ch != 'N') {
                boolean validInput = false;
                while (!validInput) {
                    try {
                        System.out.print("\nSelect Row    : ");
                        strRow=scanner.next().toUpperCase();
                        row=strRow.charAt(0);

                        //System.out.println(row);/////////////////////
                        System.out.print("Select Column : ");
                        col = scanner.nextInt();
                        //System.out.println(col);///////////////////////
                        if (!Seat.checkSeatValidation(row, col)) {
                            System.out.println("Invalid Input");
                            continue;
                        } else {
                            validInput = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Something wrong...");
                        scanner.nextLine();
                    }
                }

                do {
                    try {
                        System.out.print("Select type(1.Adult 2.Child ) :");
                        inputType = scanner.nextInt();
                        if (inputType == 1) {
                            ticketType = "Adult";
                            priceRate = 1.2;
                            //this.adultTicket_qty++;

                        } else if (inputType == 2) {
                            ticketType = "Child";
                            priceRate = 0.8;
                            //this.childTicket_qty++;
                        } else {
                            System.out.println("Invalid Input...\n");
                        }
                    } catch (Exception e) {
                        System.out.println("something wrong...");
                        scanner.nextLine();
                    }
                } while (inputType != 1 && inputType != 2);
                String letter2 = Integer.toString(schedule.getHall().getHallID());
//                char letter = (char) ('A' + row - 1);
                String combineSeatId = letter2 + row + Integer.toString(col);

                boolean exist = false;
                for (Ticket t : tickets) {
                    if (t.getSeat().getSeat_id().equals(combineSeatId)) {
                        exist = true;
                        System.out.println("This seat not available/already be booked...");
                    }
                }
                for(Ticket t:cartTicket){
                    if (t.getSeat().getSeat_id().equals(combineSeatId)) {
                        exist = true;
                        System.out.println("This seat not available/already be booked...");
                    }
                }
                schedule.getHall().initSeatList();

                for (Seat seats:schedule.getHall().getSeats()){
                    if(seats.getSeat_id().equals(combineSeatId)){
                        if (seats.getSeat_status()==0){
                            exist=true;
                            System.out.println("This Seat is unavailable...");
                        }
                    }
                }

                if (!exist) {
                    //seat
                    Seat seat = new Seat(combineSeatId, row, col, 1);
                    Ticket ticket = new Ticket();
                    ticket.setPrice_rate(priceRate);
                    ticket.setTicketType(ticketType);
                    ticket.setTicket_id(ticket.countTicket_id(count+1));
                    ticket.setTicket_id(ticket.countTicket_id(count+1));
                    ticket.setBooking(this);
                    ticket.setSeat(seat);
                    ticket.setTimeTable(schedule);
                    cartTicket.add(ticket);
                }

                do {
                    System.out.print("Continue ? (Y=Yes N=Next) : ");
                    str = scanner.next().toUpperCase();
                    ch = str.charAt(0);
                }while(ch!='Y'&&ch!='N');
                count++;
            }


            if (cartTicket.size() > 0) {
                System.out.println("\nCart : ");
                for (Ticket t : cartTicket) {
                    System.out.println("\t\t-------------------------");
                    System.out.printf("\t\t| Ticket id :| %6d   |\n", t.getTicket_id());
                    System.out.printf("\t\t| Seat id   :| %6s   |\n", t.getSeat().getSeat_id());
                    System.out.printf("\t\t| Price     :| RM%6.2f |\n", t.calculateTicketPrice());
                    this.totalPrice += t.calculateTicketPrice();
                    if(t.getTicketType().equals("Adult")){
                        this.adultTicket_qty++;
                    }else if(t.getTicketType().equals("Child")){
                        this.childTicket_qty++;
                    }
                }

                LocalDate date = LocalDate.now();
                DateTime bookingDate=new DateTime(date);
                setBookingDateTime(bookingDate);

                LocalTime currentTime = LocalTime.now();
                setBookingTime(currentTime);

                System.out.println("\t\t-------------------------\n");
                System.out.println("Booking Details : ");
                System.out.println("\t\t-------------------------------------------");
                System.out.printf("\t\t Booking ID : %04d\t\tDate : %s\n", getBooking_id(), getBookingDateTime().getDate());
                System.out.println("\t\t-------------------------------------------");
                System.out.printf("\t\t Adult Ticket(RM%6.2f) x %d\n", schedule.getMovie().getBasicTicketPrice() * 1.2, getAdultTicket_qty());
                System.out.printf("\t\t Child Ticket(RM%6.2f) X %d\n", schedule.getMovie().getBasicTicketPrice() * 0.8, getChildTicket_qty());
                System.out.printf("\t\t\t\tTotal : RM%.2f\n", totalPrice);
                System.out.println("\t\t-------------------------------------------");
                do {
                    try {
                        System.out.println("Confirm This Booking ? (Y=Yes R=No, Select Again N=No Confirm, Exit) : ");
                        str = scanner.next().toUpperCase();
                        confirmChar = str.charAt(0);
                        if (confirmChar == 'Y') {
                            Booking.insertBooking(this);
                            for (Ticket t : cartTicket) {
                                Ticket.insertTicket(t);
                            }

                            return true;
                        }
                    }catch (Exception e){
                        System.out.println("Something wrong...");
                        scanner.nextLine();
                    }

                }while(confirmChar!='Y' && confirmChar!='N' && confirmChar!='R');
            } else {
                System.out.println("No Ticket be Selected.\nExit Booking Page...");
                confirmChar='N';
            }
        }

        return false;
    }


        //replace by getBookingList();
//    public static ArrayList<Booking> getBookedBookingList(){
//        boolean error = false;
//        ArrayList<Booking> bookings = new ArrayList<>();
//
//        try {
//            Object[] params = { };
//            ResultSet result = DatabaseUtils.selectQueryById("*", "booking",null,null);
//
//            while (result.next()) {
//                Booking booking =new Booking();
//                booking.setBooking_id(result.getInt("booking_id"));
//                booking.setAdultTicket_qty(result.getInt("adultTicket_qty"));
//                booking.setChildTicket_qty(result.getInt("childTicket_qty"));
//                booking.setBooking_status(result.getInt("booking_status"));
//
//                bookings.add(booking);
//
//            }
//
//            result.close();
//            //resultHall.close();
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return bookings;
//    }

    public static ArrayList<Booking> getBookingList(){
        boolean error = false;
        ArrayList<Booking> bookingList = new ArrayList<>();

        try {
            Object[] params = { };
            ResultSet result = DatabaseUtils.selectQueryById("*", "booking",null,null);

            while (result.next()) {

                Booking booking = new Booking();
                booking.setBooking_id(result.getInt("booking_id"));
                booking.setAdultTicket_qty(result.getInt("adultTicket_qty"));
                booking.setBookingDateTime(new DateTime(result.getDate("booking_date").toLocalDate()));
                booking.setChildTicket_qty(result.getInt("childTicket_qty"));
                booking.setTotalPrice(result.getDouble("total_price"));
                booking.setBooking_status(result.getInt("booking_status"));
                Time time =result.getTime("booking_time");
                booking.setBookingTime(time.toLocalTime());

                bookingList.add(booking);

            }

            result.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return bookingList;
    }

    //For loading all exist booking's tickets
    public void loadingTicketList() throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            Object[] params = {booking_id};
            ResultSet result = DatabaseUtils.selectQueryById("*", "ticket","booking_id = ?",params);

            while (result.next()) {

                Ticket ticket = new Ticket();

                ticket.setTicketType(result.getString("ticket_type"));
                ticket.setTicket_id(result.getInt("ticket_id"));
                Seat seat = new Seat();
                seat.setSeat_id(result.getString("seat_id"));
                ticket.setSeat(seat);

                Booking booking =new Booking();
                booking.setBooking_id(result.getInt("booking_id"));
                ticket.setBooking(booking);

                Schedule timetable=new Schedule();
                timetable.setScheduleID(result.getInt("schedule_id"));
                ticket.setBooking(booking);

                ticket.setPrice_rate(result.getDouble("price_rate"));
                tickets.add(ticket);


            }

            result.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        this.setTicketList(ticketList);

    }

//    public boolean deleteBooking() throws SQLException {
//        int rowAffected = 0;
//
//        try {
//            Object[] params = {booking_id};
//            rowAffected = DatabaseUtils.deleteQueryById("booking", "booking_status", "booking_id", params);
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        if (rowAffected > 0) {
//            System.out.println("\nThe booking has been deleted.");
//            return true;
//        } else {
//            System.out.println("\nSomething went wrong...");
//            return false;
//        }
//    }


}


