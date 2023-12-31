package Schedule_Management;

import Database.DatabaseUtils;
import Driver.DatabaseOperations;
import Driver.Name;
import Cinema_Management.Cinema;
import Genre_Management.Genre;
import Hall_Management.Hall;
import Driver.DateTime;
import Movie_Management.Movie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Schedule implements DatabaseOperations {
    private int scheduleID;
    private Movie movie;
    private Hall hall;
    private DateTime showDate;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructor
    public Schedule() {
    }

    public Schedule(int scheduleID, Movie movie, Hall hall, DateTime showDate, LocalTime startTime) {
        this.scheduleID = scheduleID;
        this.movie = movie;
        this.hall = hall;
        this.showDate = showDate;
        this.startTime = startTime;
        calculateEndTime(movie, startTime);
    }

    public Schedule(Hall hall, DateTime showDate){
        this.hall = hall;
        this.showDate= showDate;
    }

    // Method

    // Used to select the schedule from the database
    public ArrayList<Schedule> getSchedule() throws Exception {
        ResultSet result = null;
        try {
            Object[] params = {hall.getHallID(), String.valueOf(showDate.getDate()), 1};
            result = DatabaseUtils.selectQuery("*", "timeTable", "hall_id = ? AND movie_showDate = ? AND timeTable_status = ?", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Schedule> schedules = new ArrayList<>();

        while (result.next()) {
            int timetableID = result.getInt("schedule_id");
            int movieID = result.getInt("movie_id");
            LocalTime startTime = result.getTime("movie_startTime").toLocalTime();

            // Movie
            ResultSet result2 = null;
            Movie movie = new Movie();

            try {
                Object[] params = {movieID};
                result2 = DatabaseUtils.selectQuery("*", "movie", "movie_id = ? LIMIT 1", params);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            while (result2.next()) {
                movie.setMovieID(result2.getInt("movie_id"));
                movie.setGenre(new Genre(result2.getInt("genre_id")));
                movie.setMvName(new Name(result2.getString("mv_name")));
                movie.setReleaseDate(new DateTime(result2.getDate("release_date").toLocalDate()));
                movie.setDuration(result2.getInt("duration"));
                movie.setLanguage(result2.getString("lang"));
                movie.setDirector(result2.getString("director"));
                movie.setWritter(result2.getString("writter"));
                movie.setStarring(result2.getString("starring"));
                movie.setMusicProvider(result2.getString("music"));
                movie.setCountry(result2.getString("country"));
                movie.setMetaDescription(result2.getString("meta_description"));
                movie.setBasicTicketPrice(result2.getDouble("basic_TicketPrice"));
            }

            Schedule schedule = new Schedule(timetableID, movie, hall, showDate, startTime);
            schedules.add(schedule);
        }

        return schedules;
    }


    // Used to print the schedule
    public static void printing(ArrayList<Schedule> schedules) {
        if (!schedules.isEmpty()) {
            System.out.println("\nMovie Schedule List for " + schedules.get(0).showDate.getDate() + " at Hall " + schedules.get(0).hall.getHallID() + ":");

            System.out.println("------------------------------------------------------------------------------");
            System.out.printf("%-3c %-4s %c %-31s %-3c %-12s %-4c %-11s %c\n", '|', "No", '|', "Movie Name", '|', "Start Time", '|', "End Time", '|');
            System.out.println("------------------------------------------------------------------------------");

            for (int i = 0; i < schedules.size(); i++) {
                System.out.printf("%-3c %-4d %c %-31s %-5c %-10s %-5c %-10s %c\n", '|', (i + 1), '|', schedules.get(i).movie.getMvName().getName(), '|', schedules.get(i).startTime, '|', schedules.get(i).endTime, '|');
                System.out.println("------------------------------------------------------------------------------");
            }
        }
        else {
            System.out.println("\nNo schedules available for the selected date and hall!");
        }
    }


    // Used to receive the input (to know which hall and which date the user want to view the schedule)
    public static Schedule acceptViewScheduleListInput(Scanner sc, Cinema cinemaSelected) {
        // Hall
        int hallNo = 0;
        boolean error = true;
        ArrayList<Hall> halls = new ArrayList<>();
        do {
            try {
                System.out.println("\nSelect the hall: ");
                halls = cinemaSelected.getHallList(1);

                System.out.print("------------------------------------------------------");
                System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Hall Name", '|');
                System.out.println("------------------------------------------------------");
                for (int i = 0; i < halls.size(); i++) {
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', (i + 1), '|', halls.get(i).getHallName().getName(), '|');
                    System.out.println("------------------------------------------------------");
                }

                System.out.print("\nEnter the hall no: ");
                hallNo = sc.nextInt();
                sc.nextLine();

                if (hallNo > 0 && hallNo <= halls.size()) {
                    error = false;
                }
                else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid hall no!");
                sc.nextLine();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } while (error);

        // Show Date
        error = true;
        String date = null;
        DateTime viewDate = null;
        boolean validDate = false;
        do {
            System.out.print("\nEnter movie show date (YYYY-MM-DD): ");
            date = sc.nextLine();

            if (date.trim().isEmpty()) {
                System.out.println("Please enter the show date.");
            } else {
                try {
                    String[] parts = date.split("-");
                    int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);

                    // 验证日期是否 valid
                    viewDate = new DateTime(year, month, day);
                    validDate = viewDate.isValidDate();

                    if (validDate == true) {
                        error = false;
                    } else {
                        System.out.println("Please enter a valid date!");
                        error = true;
                    }
                } catch (Exception e) {
                    System.out.println("The date format entered in wrong!");
                }
            }
        } while (error);

        Schedule schedule = new Schedule(halls.get(hallNo - 1), viewDate);
        return schedule;
    }


    // Used to insert the new schedule into the database
    public boolean add() throws SQLException {
        int rowAffected = 0;
        try {
            Object[] params = {hall.getHallID(), movie.getMovieID(), showDate.getDate(), startTime, endTime};
            String sql = "INSERT INTO `timeTable` (`hall_id`, `movie_id`, `movie_showDate`, `movie_startTime`, `movie_endTime`) VALUES (?, ?, ?, ?, ?)";
            rowAffected = DatabaseUtils.insertQuery(sql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nSchedule successfully added...");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }


    // Used to update the schedule in the database
    public boolean modify() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {hall.getHallID(), movie.getMovieID(), String.valueOf(showDate.getDate()), startTime, endTime, scheduleID};
            String sql = "UPDATE `timeTable` SET `hall_id` = ?, `movie_id` = ?, `movie_showDate` = ?, `movie_startTime` = ?, `movie_endTime` = ? WHERE `schedule_id` = ?";
            rowAffected = DatabaseUtils.updateQuery(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nSchedule successfully updated...");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }


    // Used to set the schedule_status to become 0 (0 means this schedule is deleted)
    public boolean delete() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {scheduleID};
            rowAffected = DatabaseUtils.delectQuery("timeTable", "timeTable_status", "schedule_id", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe schedule has been deleted.");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }


    // Generates a list of dates for one week starting from the current date.
    // The list contains dates for the next 7 days.
    public static ArrayList<LocalDate> generateOneWeekDateList() {
        ArrayList<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now(); // 获取当前日期

        // 生成一周内的日期
        System.out.println("--------------------------------");
        for (int i = 0; i <= 6; i++) { // 一周有7天
            dateList.add(currentDate);  // 将日期添加到列表中
            System.out.printf("%-4c %-4d %-5c %-14s %c\n", '|', (i + 1), '|', dateList.get(i), '|');
            System.out.println("--------------------------------");
            currentDate = currentDate.plusDays(1); // 增加一天
        }

        return dateList;
    }


    // Displays the hall and movie schedules based on specified criteria
    public int showHallAndTime(int count, ArrayList<Schedule> schedules) throws SQLException {
        boolean validSchedule = false;

        ResultSet result = null;
        try {
            Object[] params = {hall.getHallID(), movie.getMovieID(), String.valueOf(showDate.getDate()), 1};
            result = DatabaseUtils.selectQuery("*", "timeTable", "hall_id = ? AND movie_id = ? AND movie_showDate = ? AND timeTable_status = ?", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        while (result.next()) {
            // 检查如果是今天的 schedule, 看 start time 有没有超过现在的时间, 有的话才显示
            if (showDate.getDate().equals(LocalDate.now())) {
                if (result.getTime("movie_startTime").toLocalTime().isAfter(LocalTime.now())) {
                    validSchedule = true;
                }
                else {
                    validSchedule = false;
                }
            }
            else {
                validSchedule = true;
            }

            if (validSchedule == true) {
                Schedule schedule = new Schedule();

                schedule.scheduleID = result.getInt("schedule_id");
                schedule.startTime = result.getTime("movie_startTime").toLocalTime();
                schedule.endTime = result.getTime("movie_endTime").toLocalTime();
                schedule.setHall(hall);

                schedules.add(schedule);

                System.out.printf("%-3c %-4d %c %-31s %-5c %-10s %-5c %-10s %c\n", '|', count, '|', hall.getHallName().getName(), '|', schedule.startTime, '|', schedule.endTime, '|');
                System.out.println("------------------------------------------------------------------------------");
                count++;
            }
        }

        return count;
    }


    // Used to calculate the movie end time (movie startTime + duration)
    public void calculateEndTime(Movie movie, LocalTime startTime){
        // Change the duration's data type from int to Duration (in minutes)
        Duration duration = Duration.ofMinutes(movie.getDuration());

        endTime = startTime.plusMinutes(duration.toMinutes());
    }


    // Round up the duration of movie, for example, if the duration is 122 minutes, it will round up it to 125 minutes,
    // this is to make it easier to calculate available timeslots for schedule
    public static Duration roundUpToNearestFiveMinutes(int duration) {
        int hours = duration / 60;
        int minutes = duration % 60;
        LocalTime time = LocalTime.of(hours, minutes);

        int minute = time.getMinute();
        int roundUpMinute = ((int) Math.ceil(minute / 5.0)) * 5;

        if (roundUpMinute == 60) {
            hours++;
            roundUpMinute = 0;
        }

        return Duration.ofMinutes((hours * 60) + roundUpMinute);
    }


    // To get the available timeslots for schedule
    public LocalTime[] availableTimeSlots(Scanner sc) throws SQLException {
        ResultSet result = null;
        String showDate = String.valueOf(getShowDate().getDate());

        try {
            Object[] params2 = {hall.getHallID(), showDate};
            result = DatabaseUtils.selectQuery("movie_startTime, movie_endTime", "timeTable", "hall_id = ? AND movie_showDate = ?", params2);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<LocalTime[]> timeSlots = new ArrayList<>();
        while (result.next()) {
            LocalTime startTime = result.getTime("movie_startTime").toLocalTime();
            LocalTime endTime = result.getTime("movie_endTime").toLocalTime();

            LocalTime[] timeSlot = {startTime, endTime};
            timeSlots.add(timeSlot);
        }

        // 把 duration round up to 5 minutes
        Duration duration = roundUpToNearestFiveMinutes(getMovie().getDuration());

        // 间隔时间 15分钟
        int interval = 5;

        // 营业时间 11:00:00 - 23:00:00
        LocalTime openingTime = LocalTime.of(11, 0, 0);
        LocalTime closingTime = LocalTime.of(23, 0, 0);

        ArrayList<LocalTime[]> availableTimeSlots = new ArrayList<>();
        LocalTime startTime = openingTime;

        while (startTime.plus(duration).isBefore(closingTime)) {
            boolean conflict = false;

            for (LocalTime[] timeSlot : timeSlots) {
                LocalTime scheduledStartTime = timeSlot[0]; // 13:00:00
                LocalTime scheduledEndTime = timeSlot[1]; // 15:10:00

                // 电影开始时间 小过 已经被安排时间表的结束时间 和 电影结束时间 大过 已经被安排时间表的开始时间 => 在它们之间
                if (startTime.isBefore(scheduledEndTime.plusMinutes(15)) && startTime.plus(duration).isAfter(scheduledStartTime.minusMinutes(15))) {
                    conflict = true;
                    break;
                }
            }

            if (!conflict) {
                availableTimeSlots.add(new LocalTime[]{startTime, startTime.plus(duration)});
                startTime = startTime.plusMinutes(10);
            }

            startTime = startTime.plusMinutes(interval);
        }

        boolean error = true;
        int choice = 0;
        do {
            try {
                System.out.println("\nSelect the available time slot: ");

                System.out.println("\nSelect the operation: ");
                System.out.printf("--------------------------------------------");
                System.out.printf("\n%-3c %-4s %-3c %-12s %-4c %-11s %c\n", '|', "No", '|', "Start Time", '|', "End Time", '|');
                System.out.println("--------------------------------------------");
                for (int i = 0; i < availableTimeSlots.size(); i++) {
                    LocalTime[] availableTimeSlot = availableTimeSlots.get(i);
                    System.out.printf("%-3c %-4d %-5c %-10s %-5c %-10s %c\n", '|', (i + 1), '|', availableTimeSlot[0], '|', availableTimeSlot[1], '|');
                    System.out.println("--------------------------------------------");
                }

                System.out.print("\nEnter your selection: ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice > 0 && choice <= availableTimeSlots.size()) {
                    error = false;
                } else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
            }
        } while (error);

        return availableTimeSlots.get(choice - 1);
    }


    // Check whether the show date is earlier than the release date
    public String checkShowDate(){
        int comparison = showDate.getDate().compareTo(movie.getReleaseDate().getDate()); // Compare dates

        if (comparison <= 0) {
            return "The show date cannot be earlier than or equal to release date.";
        }
        else {
            return null;
        }
    }


    // Setter
    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public void setShowDate (DateTime showDate) {
        this.showDate = showDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    // Getter
    public int getScheduleID() {
        return scheduleID;
    }

    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public DateTime getShowDate() {
        return showDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}