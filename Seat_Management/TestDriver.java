package Seat_Management;

import Driver.SystemClass;
import Hall_Management.Hall;

import java.util.Scanner;

public class TestDriver {
    public static void main(String[] args) throws Exception {



//
//
//        int rowAffected = 0;
//
//        try {
//            String insertSql = "INSERT INTO `seat`(`seat_id`,`seatrow`,`seatcol`) VALUES (?, ?, ?)";
//            Object[] params = {"A1",1,1};
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





//        Object[] params = {1};
//        ResultSet result = DatabaseUtils.selectQueryById("*", "seat", null, null);
//        String id=" ";
//        while (result.next()) {
//            //Movie movie = new Movie(id, mvName, releaseDate, duration, lang, director, writter, starring, musicProvider, country, metaDescription, childTicketPrice, adultTicketPrice);
//            id=(result.getString("seat_id"));
//
//
//        }
//        System.out.println(id);
//
//
//        int i = 1;
//        int j = 1;
//        for(int hallid=1;hallid<=5;hallid++){
//            for(i=1;i<=8;i++){
//                char letter = (char) ('A' + i - 1); // 将整数 i 转换为字母
//                for (j=1;j<=8;j++){
//                    String num = String.valueOf(hallid)+letter + String.valueOf(j); // 将字母和整数 j 拼接成字符串
//                    System.out.printf("insert into seat(seat_id,hall_id,seatrow,seatcol) value('%s',%d,%d,%d);\n",num,hallid,i,j);
//                }
//            }
//        }
//        char letter = (char) ('A' + i - 1); // 将整数 i 转换为字母
//        String num = letter + String.valueOf(j); // 将字母和整数 j 拼接成字符串
//
//        System.out.println("结果是：" + num); // 输出结果
//
//        Scanner sc = new Scanner(System.in);
        //SystemClass sclass = new SystemClass();
        //sclass.customer(sc);
       // Seat.viewSeat_status(1);
//        Hall hall=new Hall();
//        hall.setHallID(1);
//        Movie movie = new Movie();
//        Name name = new Name();
//        name.setName("Pokemon");
//        movie.setMvName(name);
//        ShowDate sd=new ShowDate(LocalDate.of(2023,4,22));


//
//        Seat newseat=new Seat("1A3",hall,1,3,1);
//        //newseat.addSeat();
//        Booking booking=new Booking(1,0,0,0,1);
//        booking.addBooking();
        //AdultTicket ticket=new AdultTicket(3,newseat,booking,"Adult");
        //ticket.addTicket();


//        ArrayList<Ticket> tickets=Ticket.getBookedTicketList(3);
//        for(Ticket t:tickets){
//            System.out.println(t.getSeat().getSeat_id());
//        }


//        Schedule schedule=new Schedule(4,movie,hall,sd, LocalTime.of(11,0,0));

//---------Start here----------------------------------------------------------------------------------------------------------------------
//        Booking.viewSeat_status(schedule);
//        booking.executeBooking(schedule);

        Scanner scanner = new Scanner(System.in);

//        SystemClass systemClass = new SystemClass();
//        systemClass.manageHall(scanner);
//        Hall hl = new Hall();
//        hl.setHallID(1);
//
//        hl.initSeatList();
//        for(Seat seats:hl.getSeats()){
//            System.out.println(seats.getSeat_id());
//        }
//        hl.viewSeat_status();
//        hl.getSeats().get(0).setSeat_status(0);
//        hl.getSeats().get(0).updateSeatStatus();
    }
}
