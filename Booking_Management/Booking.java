package Booking_Management;

import Database.DatabaseUtils;
import Driver.DateTime;
import Driver.Name;
import Driver.SystemClass;
import Hall_Management.Hall;
import Movie_Management.Movie;
import Promotion_Management.Promotion;
import Schedule_Management.Schedule;
import Seat_Management.Seat;

import Security_Management.Customer;
import Ticket_Managemnet.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Booking {

    private Customer customer;
    private Promotion promotion;
    private int bookingId;
    private int adultTicketQty;
    private int childTicketQty;
    private double totalPrice;
    private DateTime bookingDateTime;
    private LocalTime bookingTime;
    private String bookingStatus;      //processing     completed   cancelled
    private ArrayList<Ticket> ticketList;

    public Booking() {
        this.childTicketQty =0;
        this.adultTicketQty =0;
        this.totalPrice=0;
        this.bookingStatus ="processing";
        //this.customerId=customerId;
    }


    public Booking(int bookingId, int adultTicketQty, int childTicketQty, double totalPrice, String bookingStatus, Customer customer) {
        this.bookingId = bookingId;
        this.adultTicketQty = adultTicketQty;
        this.childTicketQty = childTicketQty;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
        this.customer=customer;
    }

    public Booking(Customer customer, Promotion promotion, int bookingId, int adultTicketQty, int childTicketQty, double totalPrice, DateTime bookingDateTime, LocalTime bookingTime, String bookingStatus, ArrayList<Ticket> ticketList) {
        this.customer = customer;
        this.promotion = promotion;
        this.bookingId = bookingId;
        this.adultTicketQty = adultTicketQty;
        this.childTicketQty = childTicketQty;
        this.totalPrice = totalPrice;
        this.bookingDateTime = bookingDateTime;
        this.bookingTime = bookingTime;
        this.bookingStatus = bookingStatus;
        this.ticketList = ticketList;
    }

    //Getter
    public int getBookingId() {
        return bookingId;
    }

    public void countBooking_id() {
        this.bookingId =1;
        int largeId=0;
        ArrayList<Booking> bookings=Booking.getBookingList();
        for(Booking b:bookings){
            if(b.getBookingId()>=largeId)
                largeId=b.getBookingId();
        }
        this.bookingId =largeId+1;
//        this.ticket_id+=count;
        //return this.booking_id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getAdultTicketQty() {
        return adultTicketQty;
    }

    public void setAdultTicketQty(int adultTicketQty) {
        this.adultTicketQty = adultTicketQty;
    }

    public int getChildTicketQty() {
        return childTicketQty;
    }

    public void setChildTicketQty(int childTicketQty) {
        this.childTicketQty = childTicketQty;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public DateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(DateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public ArrayList<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void insertBooking(Booking b) throws Exception {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `booking` (`booking_id`,`adultTicket_qty`,`childTicket_qty`,`total_price`,`booking_date`,`booking_time`,`booking_status`,`customer_id`) value(?,?,?,?,?,?,?,?);";
            Object[] params = {b.getBookingId(),b.getAdultTicketQty(),b.getChildTicketQty(),b.getTotalPrice(),b.getBookingDateTime().getDate(),b.getBookingTime(),b.getBookingStatus(),b.getCustomer().getCustId()};
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

    public static int viewSeatBooking_status(Schedule schedule) {
        boolean error = false;
        ArrayList<Seat> seats = new ArrayList<>();
        int largestRow=0;
        int largestCol=0;
        try {
            Object[] params = {schedule.getHall().getHallID()};
            ResultSet result = DatabaseUtils.selectQuery("*", "seat", "hall_id = ?", params);
            //find hall

            while (result.next()) {

                Seat seat = new Seat();

                seat.setSeatId(result.getString("seat_id"));
                Hall hall =new Hall();
                hall.setHallID(result.getInt("hall_id"));
                //seat.setHall(hall);
                seat.setSeatRow(result.getInt("seatrow"));
                seat.setSeatCol(result.getInt("seatcol"));
                seat.setSeatStatus(result.getInt("seat_status"));
                largestRow=result.getInt("seatrow");
                largestCol=result.getInt("seatcol");
                seats.add(seat);
            }

            result.close();
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
                if(seats.get(j).getSeatStatus()==1) {
                    st='O';
                }else{
                    st='X';
                }
                for (Ticket t:tickets) {
                    if(t.getSeat().getSeatId().equals(seats.get(j).getSeatId())){
                        st='1';
                    }
                }
                System.out.printf("[%c] ",st);
                j++;
            } while (seats.get(j).getSeatCol() +1 <= largestCol);
            char st;
            if(seats.get(j).getSeatStatus()==1) {
                st='O';
            }else{
                st='X';
            }
            for (Ticket t:tickets) {
                if(t.getSeat().getSeatId().equals(seats.get(j).getSeatId())){
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
        this.setBookingStatus("processing");
        Scanner scanner = new Scanner(System.in);
            this.childTicketQty =0;
            this.adultTicketQty =0;
            this.totalPrice=0;
            System.out.println("Time table id : "+schedule.getScheduleID());
            Booking.viewSeatBooking_status(schedule);
            ArrayList<Ticket> tickets = Ticket.getBookedTicketList(schedule.getScheduleID());
            ArrayList<Ticket> cartTicket = new ArrayList<>();
            System.out.println("Booking ID : " + this.bookingId);
            String strRow=" ";
            int col = 0;
            String row =" ";
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
                        row=scanner.next().toUpperCase();
                        System.out.print("Select Column : ");
                        col = scanner.nextInt();

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
                String combineSeatId = letter2 + row.charAt(0) + Integer.toString(col);

                boolean exist = false;
                for (Ticket t : tickets) {
                    if (t.getSeat().getSeatId().equals(combineSeatId)) {
                        exist = true;
                        System.out.println("This seat not available/already be booked...");
                        count--;
                    }
                }
                for(Ticket t:cartTicket){
                    if (t.getSeat().getSeatId().equals(combineSeatId)) {
                        exist = true;
                        System.out.println("This seat not available/already be booked...");
                        count--;
                    }
                }
                schedule.getHall().initSeatList();

                for (Seat seats:schedule.getHall().getSeats()){
                    if(seats.getSeatId().equals(combineSeatId)){
                        if (seats.getSeatStatus()==0){
                            exist=true;
                            System.out.println("This Seat is unavailable...");
                            count--;
                        }
                    }
                }

                if (!exist) {
                    //seat
                    Seat seat = new Seat(combineSeatId, row.charAt(0), col, 1);
                    Ticket ticket = new Ticket();
                    ticket.setPriceRate(priceRate);
                    ticket.setTicketType(ticketType);
                    ticket.setTicket_id(ticket.countTicket_id(count));
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
                    System.out.printf("\t\t| Seat id   :| %6s   |\n", t.getSeat().getSeatId());
                    System.out.printf("\t\t| Price     :| RM%6.2f |\n", t.calculateTicketPrice());
                    this.totalPrice += t.calculateTicketPrice();
                    if(t.getTicketType().equals("Adult")){
                        this.adultTicketQty++;
                    }else if(t.getTicketType().equals("Child")){
                        this.childTicketQty++;
                    }
                }
                System.out.println("\t\t-------------------------\n");
                this.setTicketList(cartTicket);

                LocalDate date = LocalDate.now();
                DateTime bookingDate=new DateTime(date);
                setBookingDateTime(bookingDate);
                LocalTime currentTime = LocalTime.now();
                setBookingTime(currentTime);
                return true;
            } else {
                System.out.println("No Ticket be Selected.\nExit Booking Page...");
                //confirmChar='N';
            }
        //}
        return false;
    }

    public static ArrayList<Booking> getBookingList(){
        boolean error = false;
        ArrayList<Booking> bookingList = new ArrayList<>();

        try {
            Object[] params = { };
            ResultSet result = DatabaseUtils.selectQuery("*", "booking",null,null);

            while (result.next()) {

                Booking booking = new Booking();
                booking.setBookingId(result.getInt("booking_id"));
                booking.setAdultTicketQty(result.getInt("adultTicket_qty"));
                booking.setBookingDateTime(new DateTime(result.getDate("booking_date").toLocalDate()));
                booking.setChildTicketQty(result.getInt("childTicket_qty"));
                booking.setTotalPrice(result.getDouble("total_price"));
                booking.setBookingStatus(result.getString("booking_status"));
                Time time =result.getTime("booking_time");
                booking.setBookingTime(time.toLocalTime());
                Customer c =new Customer();
                c.setCustId(result.getInt("customer_id"));
                booking.setCustomer(c);
                bookingList.add(booking);

            }

            result.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return bookingList;
    }
    public static ArrayList<Booking> getBookingList(int customerId){
        boolean error = false;
        ArrayList<Booking> bookingList = new ArrayList<>();

        try {
            Object[] params = {customerId};
            ResultSet result = DatabaseUtils.selectQuery("*", "booking","customer_id=?",params);
            while (result.next()) {
                Booking booking = new Booking();
                booking.setBookingId(result.getInt("booking_id"));
                booking.setAdultTicketQty(result.getInt("adultTicket_qty"));
                booking.setBookingDateTime(new DateTime(result.getDate("booking_date").toLocalDate()));
                booking.setChildTicketQty(result.getInt("childTicket_qty"));
                booking.setTotalPrice(result.getDouble("total_price"));
                booking.setBookingStatus(result.getString("booking_status"));
                Time time =result.getTime("booking_time");
                booking.setBookingTime(time.toLocalTime());
                Customer c =new Customer();
                c.setCustId(result.getInt("customer_id"));
                booking.setCustomer(c);
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
            Object[] params = {bookingId};
            ResultSet result = DatabaseUtils.selectQuery("*", "ticket","booking_id = ?",params);
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
                ticket.setTimeTable(timetable);
                ticket.setPriceRate(result.getDouble("price_rate"));
                tickets.add(ticket);
            }
            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        for (Ticket t:tickets) {
            try {
                Object[] params = {t.getTimeTable().getScheduleID()};
                ResultSet result = DatabaseUtils.selectQuery("*", "timetable", "schedule_id = ?", params);

                while (result.next()) {
//                    Schedule schedule=new Schedule();
                    Movie movie = new Movie();
                    movie.setMovieID(result.getInt("movie_id"));
                    Hall hall =new Hall();
                    hall.setHallID(result.getInt("hall_id"));
//                    schedule.setMovie(movie);
                    t.getTimeTable().setMovie(movie);
                    t.getTimeTable().setHall(hall);
                }

                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (Ticket t:tickets) {
            try {
                Object[] params = {t.getTimeTable().getMovie().getMovieID()};
                ResultSet result = DatabaseUtils.selectQuery("*", "movie", "movie_id = ?", params);
                while (result.next()) {
                    Name name =new Name();
                    name.setName(result.getString("mv_name"));
                    t.getTimeTable().getMovie().setMvName(name);
                    t.getTimeTable().getMovie().setBasicTicketPrice(result.getDouble("basic_TicketPrice"));

                }
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.setTicketList(tickets);
    }

    public void printBookingDetail() {
        //System.out.println("\t\t-------------------------\n");
        System.out.println("Booking Details : ");
        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Booking ID : %04d\t\tDate : %s\n", getBookingId(), getBookingDateTime().getDate());
        System.out.printf("\t\t Status     : %s\n", getBookingStatus().toUpperCase());
        System.out.println("\t\t-------------------------------------------");
        System.out.printf("\t\t Hall ID    : %d\n",getTicketList().get(0).getTimeTable().getHall().getHallID());
        System.out.printf("\t\t Movie Name : %s\n\n",getTicketList().get(0).getTimeTable().getMovie().getMvName().getName());
        System.out.println("\t\t Ticket Type\tUnit Price\tQty\t   Subtotal");
        if(getAdultTicketQty()>0)
            System.out.printf("\t\t Adult Ticket\tRM%6.2f     %2d\t   RM%6.2f\n", this.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 1.2, getAdultTicketQty(),this.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 1.2* getAdultTicketQty());
        if(getChildTicketQty()>0)
            System.out.printf("\t\t Child Ticket\tRM%6.2f     %2d\t   RM%6.2f\n", this.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 0.8, getChildTicketQty(),this.getTicketList().get(0).getTimeTable().getMovie().getBasicTicketPrice() * 0.8* getChildTicketQty());
        System.out.println("\n\t\t Seat ID : ");
        System.out.print("\t\t\t");
        for(Ticket t:getTicketList()){
            System.out.print(t.getSeat().getSeatId()+"  ");
        }
        if (promotion == null) {
            System.out.println("\n\t\t-------------------------------------------");
            System.out.printf("\t\t\tTotal : \t\t\t\t\t   RM%6.2f\n", totalPrice);
        } else {
            System.out.println("\n\t\t-------------------------------------------");
            System.out.printf("\t\t\tTotal : \t\t\t\t\t   RM%6.2f\n", totalPrice + promotion.getDiscountValue());
            System.out.printf("\t\t\tDiscount\t\t\t\t- RM%6.2f\n", promotion.getDiscountValue());
            System.out.println("\t\t-------------------------------------------");
            System.out.printf("\t\t\tTotal Amount : \t\t\t\tRM%6.2f\n", totalPrice);
        }
        System.out.println("\t\t-------------------------------------------");
    }

    public boolean updateBooking() throws SQLException {
        int rowAffected = 0;
        try {
            String updateSql = "UPDATE `booking` SET `booking_status`= ? , `total_price`= ? , `adultTicket_qty`=? , `childTicket_qty`=?  WHERE `booking_id` = ?";
            Object[] params = {bookingStatus,totalPrice,adultTicketQty,childTicketQty, bookingId};
            rowAffected = DatabaseUtils.updateQuery(updateSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe changes have been saved.");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }
    public void cancelBooking() throws SQLException {
        this.setBookingStatus("cancelled");
        for(Ticket t:this.getTicketList()){
            t.setTicketStatus(0);
            t.updateStatus();
        }
        this.updateBooking();
    }
    public void completeBooking() throws SQLException {
        this.setBookingStatus("completed");
        this.updateBooking();
    }
}



    /*
    //For Customer View Their Booking History
    public static void viewBookingHistory(Customer customer) throws SQLException {
        Scanner scanner=new Scanner(System.in);
        ArrayList<Booking> bookingList=Booking.getBookingList(customer.getCustId());
        Collections.reverse(bookingList);
        int count=1;
        System.out.println("Booking History : ");
        for(Booking b:bookingList){
            b.loadingTicketList();
            System.out.printf("%2d. Booking id:%d  ",count,b.getBookingId());
            System.out.print("Date:"+b.getBookingDateTime().getDate().toString()+"\n");
            if(count%5==0){
                String answer=" ";
                do{
                    System.out.print("Continue Show More History? (Y=YES N=NO) : ");

                    answer=scanner.next().toUpperCase();

                } while (SystemClass.askForContinue(answer).equals("Invalid"));

                if(SystemClass.askForContinue(answer).equals("N"))
                    break;
            }
            count++;
        }
        int no=0;
        do {
            try {
                System.out.print("\nEnter No. Booking to Show Detail (0=Back) : ");
                no = scanner.nextInt();
                if (no == 0)
                    break;
                else if (no <= bookingList.size() && no >0) {
                    bookingList.get(no - 1).printBookingDetail();
                }
            }catch (Exception e){
                scanner.nextLine();
                System.out.println("Invalid Input...");
            }
        }while(no!=0);
    }*/

//    public boolean deleteBooking() throws SQLException {
//        int rowAffected = 0;
//
//        try {
//            Object[] params = {booking_id};
//            rowAffected = DatabaseUtils.delectQuery("booking", "booking_status", "booking_id", params);
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