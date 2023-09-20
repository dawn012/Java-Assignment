package Report_Management;

import Booking_Management.Booking;
import Database.DatabaseUtils;
import Driver.DateTime;
import Movie_Management.Movie;
import Schedule_Management.Schedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class BoxOfficeReport extends Report {
    private ArrayList<Movie> movies;
    private ArrayList<Double> totalBoxOffices;
    private ArrayList<Integer> numOfScreenings;
    private ArrayList<Double> averageBoxOffices;

    // Constructor
    public BoxOfficeReport() {
        movies = new ArrayList<>();
        totalBoxOffices = new ArrayList<>();
        numOfScreenings = new ArrayList<>();
        averageBoxOffices = new ArrayList<>();
    }

    public BoxOfficeReport(String title, DateTime reportDate, ArrayList<Movie> movies, ArrayList<Double> totalBoxOffices, ArrayList<Integer> numOfScreenings, ArrayList<Double> averageBoxOffices) {
        super(title, reportDate);
        this.movies = movies;
        this.totalBoxOffices = totalBoxOffices;
        this.numOfScreenings = numOfScreenings;
        this.averageBoxOffices = averageBoxOffices;
    }

    public BoxOfficeReport(String title, DateTime reportDate, String conclusion, ArrayList<Movie> movies, ArrayList<Double> totalBoxOffices, ArrayList<Integer> numOfScreenings, ArrayList<Double> averageBoxOffices) {
        super(title, reportDate, conclusion);
        this.movies = movies;
        this.totalBoxOffices = totalBoxOffices;
        this.numOfScreenings = numOfScreenings;
        this.averageBoxOffices = averageBoxOffices;
    }

    @Override
    public String toString() {
        String duration;

        if (getTitle().contains("Daily")) {
            duration = "day";
        }
        else {
            duration = "month";
        }

        super.setConclusion("From this report we can know that the best selling movie of the " + duration + " is " + movies.get(0).getMvName().getName() + " and the least popular movie is " + movies.get(movies.size() - 1).getMvName().getName() + ".");

        StringBuilder result = new StringBuilder("\n---------------------------------------------------------------------------------------------------------------------------------");

        result.append(super.toString());// 获取父类 toString() 的结果

        result.append("|          -----------------------------------------------------------------------------------------------------------          |\n");
        result.append(String.format("%-38c %s", '|', title)).append(" - ");

        if (super.getTitle().contains("Daily")) {
            result.append(reportDate.getDay()).append(" ").append(reportDate.getDate().getMonth()).append(" ").append(reportDate.getYear());
            result.append(String.format("%39c", '|'));
        } else {
            result.append(reportDate.getDate().getMonth()).append(" ").append(reportDate.getDate().getYear());
            result.append(String.format("%44c", '|'));
        }

        result.append("\n|          -----------------------------------------------------------------------------------------------------------          |\n");

        int looping;

        result.append(String.format("%-10c %-10s %-30s %-20s %-25s %-27s %c", '|', "Ranking", "Movie Name", "Total Box Office", "Number of Screenings", "Average Box Office", '|'));
        result.append("\n|          -----------------------------------------------------------------------------------------------------------          |\n");

        // 遍历电影列表，将电影信息添加到结果中
        if (movies.size() > 10) {
            looping = 10;  // 最多出现 top 10 的电影
        }
        else {
            looping = movies.size();
        }

        for (int i = 0; i < looping; i++) {
            result.append(String.format("%-13c %-7d %-36s %-24.2f %-21d %-21.2f %c\n", '|', (i + 1), movies.get(i).getMvName().getName(), totalBoxOffices.get(i), numOfScreenings.get(i), averageBoxOffices.get(i), '|'));
        }

        result.append("|          -----------------------------------------------------------------------------------------------------------          |\n");

        result.append(String.format("%-127c %c\n%-10c %-116s %c\n| %127c\n", '|', '|', '|', "Conclusion", '|', '|'));

        // 添加 Conclusion 时处理自动换行
        int maxLineLength = 115; // 适合你的报告宽度的最大行长度

        StringBuilder conclusionBuilder = new StringBuilder("|          ");
        int currentLineLength = 12; // 当前行长度，包括前导空格

        for (char c : conclusion.toCharArray()) {
            if (currentLineLength >= maxLineLength && c == ' ') {
                // 如果当前行已满且下一个字符是空格，就在这里换行
                conclusionBuilder.append(String.format(" %10c\n|          ", '|'));
                currentLineLength = 12; // 重新计算行长度
            } else {
                conclusionBuilder.append(c);
                currentLineLength++;
            }
        }

        result.append(conclusionBuilder.toString());
        result.append(String.format(" %93c\n| %127c\n", '|', '|'));

        result.append("---------------------------------------------------------------------------------------------------------------------------------\n");

        return result.toString(); // 返回完整的字符串
    }

    public BoxOfficeReport generateBoxOfficeReport(ArrayList<Movie> moviesList) {
        ArrayList<Schedule> schedules;
        LocalDate reportDate = getReportDate().getDate();
        YearMonth reportYearMonth = YearMonth.of(reportDate.getYear(), reportDate.getMonth());

        for (int i = 0; i < moviesList.size(); i++) {
            schedules = new ArrayList<>();

            try {
                Object[] params = {moviesList.get(i).getMovieID()};
                ResultSet result = DatabaseUtils.selectQuery("*", "timeTable", "movie_id = ?", params);

                while (result.next()) {
                    Schedule schedule = new Schedule();

                    schedule.setShowDate(new DateTime(result.getDate("movie_showDate").toLocalDate()));

                    if (getTitle().contains("Daily")) {
                        if (schedule.getShowDate().getDate().isEqual(getReportDate().getDate())) {
                            schedule.setScheduleID(result.getInt("schedule_id"));

                            schedules.add(schedule);
                        }
                    }
                    else {
                        LocalDate scheduleDate = schedule.getShowDate().getDate();
                        YearMonth scheduleYearMonth = YearMonth.of(scheduleDate.getYear(), scheduleDate.getMonth());

                        if (scheduleYearMonth.equals(reportYearMonth)) {
                            schedule.setScheduleID(result.getInt("schedule_id"));

                            schedules.add(schedule);
                        }
                    }
                }

                result.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!schedules.isEmpty()) {
                movies.add(moviesList.get(i));
                numOfScreenings.add(schedules.size());

                calculateTotalRevenue(schedules);
            }
        }

        if (!movies.isEmpty()) {
            return new BoxOfficeReport(getTitle(), getReportDate(), movies, totalBoxOffices, numOfScreenings, averageBoxOffices);
        }
        return null;
    }

    /*public static ArrayList<Report> getRanking(Report report) {
        ArrayList<Report> reportAfterRanking = new ArrayList<>(reports);

        for (int i = 0; i < reports.size(); i++) {
            for (int j = i + 1; j < reports.size(); j++) {
                BoxOfficeReport report1 = (BoxOfficeReport) reports.get(i);
                BoxOfficeReport report2 = (BoxOfficeReport) reports.get(j);

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
        ArrayList<Booking> bookings = new ArrayList<>();
        double totalBoxOffice = 0;

        for (int i = 0; i < schedules.size(); i++) {
            try {
                Object[] params = {schedules.get(i).getScheduleID()};
                ResultSet result = DatabaseUtils.selectQuery("DISTINCT booking_id", "ticket", "schedule_id = ?", params);

                while (result.next()) {
                    Booking booking = new Booking();

                    booking.setBookingId(result.getInt("booking_id"));

                    bookings.add(booking);
                }

                result.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < bookings.size(); i++) {
            try {
                Object[] params = {bookings.get(i).getBookingId()};
                ResultSet result = DatabaseUtils.selectQuery("total_price", "booking", "booking_id = ?", params);

                while (result.next()) {
                    if (result.getString("booking_status").equals("completed")) {
                        totalBoxOffice += result.getDouble("total_price");
                    }
                }

                result.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        totalBoxOffices.add(totalBoxOffice);

        double averageBoxOffice = totalBoxOffice / schedules.size();
        averageBoxOffices.add(averageBoxOffice);
    }

    public static BoxOfficeReport getRanking(Report report) {
        // 创建一个包含 Movie 和总票房的 Map
        Map<Movie, Double> movieBoxOffices = new HashMap<>();

        for (int i = 0; i < ((BoxOfficeReport)report).movies.size(); i++) {
            Movie movie = ((BoxOfficeReport)report).movies.get(i);
            double totalBoxOffice = ((BoxOfficeReport)report).totalBoxOffices.get(i);
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
        BoxOfficeReport reportAfterRanking = new BoxOfficeReport();

        // 找到票房最高的电影
        for (int i = 0; i < sortedList.size(); i++) {
            Movie rank = sortedList.get(i).getKey();

            // 将最高票房电影添加到新报告中
            reportAfterRanking.getMovie().add(rank);
            reportAfterRanking.getTotalBoxOffices().add(movieBoxOffices.get(rank));
            reportAfterRanking.getNumOfScreenings().add(((BoxOfficeReport)report).numOfScreenings.get(((BoxOfficeReport)report).movies.indexOf(rank)));
            reportAfterRanking.getAverageBoxOffices().add(((BoxOfficeReport)report).averageBoxOffices.get(((BoxOfficeReport)report).movies.indexOf(rank)));
        }

        reportAfterRanking.setTitle(report.getTitle());
        reportAfterRanking.setReportDate(report.getReportDate());

        return reportAfterRanking;
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