package Report_Management;

import Booking_Management.Booking;
import Database.DatabaseUtils;
import Driver.DateTime;
import Movie_Management.Movie;
import Schedule_Management.Schedule;
import Ticket_Managemnet.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TopMovieReport extends Report {
    private ArrayList<Movie> movies;
    private ArrayList<Double> totalBoxOffices;
    private ArrayList<Integer> numOfScreenings;
    private ArrayList<Double> averageBoxOffices;

    // Constructor
    public TopMovieReport() {
        movies = new ArrayList<>();
        totalBoxOffices = new ArrayList<>();
        numOfScreenings = new ArrayList<>();
        averageBoxOffices = new ArrayList<>();
    }

    public TopMovieReport(String title, DateTime reportDate, String purpose, String conclusion, ArrayList<Movie> movies, ArrayList<Double> totalBoxOffices, ArrayList<Integer> numOfScreenings, ArrayList<Double> averageBoxOffices) {
        super(title, reportDate, purpose, conclusion);
        this.movies = movies;
        this.totalBoxOffices = totalBoxOffices;
        this.numOfScreenings = numOfScreenings;
        this.averageBoxOffices = averageBoxOffices;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString()); // 获取父类 toString() 的结果

        result.append(String.format("\n\n%-5s %-25s %-5s %-5s %s", "Ranking", "Movie Name", "Total Box Office", "Number of Screenings", "Average Box Office"));

        // 遍历电影列表，将电影信息添加到结果中
        for (int i = 0; i < movies.size(); i++) {
            result.append(String.format("%-5d %-25s %-5.2f %-5d %.2f", (i + 1), movies.get(i).getMvName().getName(), totalBoxOffices.get(i), numOfScreenings.get(i), averageBoxOffices.get(i)));
        }

        return result.toString(); // 返回完整的字符串
    }

    public void setReportValue(LocalDate expectedDate, ArrayList<Movie> moviesList) {
        for (int i = 0; i < moviesList.size(); i++) {
            ArrayList<Schedule> schedules = new ArrayList<>();

            try {
                Object[] params = {moviesList.get(i).getMovieID()};
                ResultSet result = DatabaseUtils.selectQueryById("*", "timeTable", "movie_id = ?", params);

                while (result.next()) {
                    Schedule schedule = new Schedule();

                    schedule.setShowDate(new DateTime(result.getDate("movie_showDate").toLocalDate()));

                    if (schedule.getShowDate().getDate().equals(expectedDate) || (schedule.getShowDate().getDate().isAfter(expectedDate) && schedule.getShowDate().getDate().isBefore(LocalDate.now().plusDays(1)))) {
                        schedule.setScheduleID(result.getInt("schedule_id"));

                        schedules.add(schedule);
                    }
                }

                result.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            if (!schedules.isEmpty()) {
                movies.add(moviesList.get(i));
                numOfScreenings.add(schedules.size());

                calculateTotalRevenue(schedules);
            }
        }
    }

    /*public static ArrayList<Report> getRanking(Report report) {
        ArrayList<Report> reportAfterRanking = new ArrayList<>(reports);

        for (int i = 0; i < reports.size(); i++) {
            for (int j = i + 1; j < reports.size(); j++) {
                TopMovieReport report1 = (TopMovieReport) reports.get(i);
                TopMovieReport report2 = (TopMovieReport) reports.get(j);

                if (report1.getTotalBoxOffices() < report2.getTotalBoxOffices()) {
                    // 交换位置
                    reportAfterRanking.set(i, report2);
                    reportAfterRanking.set(j, report1);
                }
            }
        }

        return reportAfterRanking;
    }*/

    private void calculateTotalRevenue(ArrayList<Schedule> schedules) {
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

        double totalBoxOffice = 0;
        for (int i = 0; i < bookings.size(); i++) {
            totalBoxOffice += bookings.get(i).getTotalPrice();
        }

        totalBoxOffices.add(totalBoxOffice);

        double averageBoxOffice = totalBoxOffice / schedules.size();
        averageBoxOffices.add(averageBoxOffice);
    }

    public static Report getRanking(Report report) {
        // 创建一个包含 Movie 和总票房的 Map
        Map<Movie, Double> movieBoxOffices = new HashMap<>();

        for (int i = 0; i < ((TopMovieReport)report).movies.size(); i++) {
            Movie movie = ((TopMovieReport)report).movies.get(i);
            double totalBoxOffice = ((TopMovieReport)report).totalBoxOffices.get(i);
            movieBoxOffices.put(movie, totalBoxOffice);
        }

        // 将 Map 转换为 List 进行排序
        List<Map.Entry<Movie, Double>> sortedList = new ArrayList<>(movieBoxOffices.entrySet());

        // 根据总票房降序排列
        Collections.sort(sortedList, new Comparator<Map.Entry<Movie, Double>>() {
            @Override
            public int compare(Map.Entry<Movie, Double> entry1, Map.Entry<Movie, Double> entry2) {
                return Double.compare(entry2.getValue(), entry1.getValue());
            }
        });

        // 创建一个新的报告对象
        Report reportAfterRanking = new TopMovieReport();

        // 找到票房最高的电影
        for (int i = 0; i < sortedList.size(); i++) {
            Movie rank = sortedList.get(i).getKey();

            // 将最高票房电影添加到新报告中
            ((TopMovieReport)reportAfterRanking).getMovie().add(rank);
            ((TopMovieReport)reportAfterRanking).getTotalBoxOffices().add(movieBoxOffices.get(rank));
            ((TopMovieReport)reportAfterRanking).getNumOfScreenings().add(((TopMovieReport)report).numOfScreenings.get(((TopMovieReport)report).movies.indexOf(rank)));
            ((TopMovieReport)reportAfterRanking).getAverageBoxOffices().add(((TopMovieReport)report).averageBoxOffices.get(((TopMovieReport)report).movies.indexOf(rank)));
        }

        return reportAfterRanking;
    }

    public void printing() {
        toString();
    }

    public void setMovie(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setTotalBoxOffices(ArrayList<Double> totalBoxOffices) {
        this.totalBoxOffices = totalBoxOffices;
    }

    public void setNumOfScreenings(ArrayList<Integer> numOfScreenings) {
        this.numOfScreenings = numOfScreenings;
    }

    public void setAverageBoxOffices(ArrayList<Double> averageBoxOffices) {
        this.averageBoxOffices = averageBoxOffices;
    }

    public ArrayList<Movie> getMovie() {
        return movies;
    }

    public ArrayList<Double> getTotalBoxOffices() {
        return totalBoxOffices;
    }

    public ArrayList<Integer> getNumOfScreenings() {
        return numOfScreenings;
    }

    public ArrayList<Double> getAverageBoxOffices() {
        return averageBoxOffices;
    }
}