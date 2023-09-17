package Report_Management;

import Booking_Management.Booking;
import Database.DatabaseUtils;
import Driver.DateTime;
import Driver.Name;
import Genre_Management.Genre;
import Movie_Management.Movie;
import Movie_Management.MovieUtils;
import Schedule_Management.Schedule;
import Ticket_Managemnet.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TopMovieReport extends Report {
    private Movie movie;
    private double totalRevenue;

    public static void main(String[] args) {
        ArrayList<Movie> movies = MovieUtils.getMovieListAfterFiltered(null, null, 1);

        for (int i = 0; i < 1; i++) {
            try {
                Object[] params = {7};
                ResultSet result = DatabaseUtils.selectQueryById("schedule_id", "timeTable", "movie_id = ?", params);

                while (result.next()) {
                    Schedule schedule = new Schedule();

                    schedule.setScheduleID(result.getInt("schedule_id"));

                    //schedules.add(schedule);
                }

                result.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public double calculateTotalRevenue() {
        ArrayList<Schedule> schedules = new ArrayList<>();
        ArrayList<Ticket> tickets = new ArrayList<>();
        ArrayList<Booking> bookings = new ArrayList<>();

        for (int i = 0; i < schedules.size(); i++) {
            try {
                Object[] params = {schedules.get(i).getScheduleID()};
                ResultSet result = DatabaseUtils.selectQueryById("DISTINCT booking_id", "ticket", "schedule_id = ?", params);

                while (result.next()) {
                    Ticket ticket = new Ticket();
                    Booking booking = new Booking();

                    booking.setBooking_id(result.getInt("booking_id"));
                    ticket.setBooking(booking);

                    tickets.add(ticket);
                }

                result.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < tickets.size(); i++) {
            try {
                Object[] params = {tickets.get(i).getBooking().getBooking_id()};
                ResultSet result = DatabaseUtils.selectQueryById("total_price", "booking", "booking_id = ?", params);

                while (result.next()) {
                    Booking booking = new Booking();

                    booking.setTotalPrice(result.getDouble("total_price"));

                    bookings.add(booking);
                }

                result.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        double totalPrice = 0;
        for (int i = 0; i < bookings.size(); i++) {
            totalPrice += bookings.get(i).getTotalPrice();
        }

        return totalPrice;
    }
}