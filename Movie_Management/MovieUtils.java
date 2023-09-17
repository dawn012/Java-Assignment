package Movie_Management;

import Database.DatabaseUtils;
import Driver.DateTime;
import Driver.Name;
import Driver.SystemClass;
import Genre_Management.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MovieUtils {
    private MovieUtils(){
    }

    public static ArrayList<Movie> queryMovieByName(String mvName) throws SQLException {
        ArrayList<Movie> searchResults = new ArrayList<>();

        ResultSet result = null;
        try {
            Object[] params = {"%" + mvName + "%"};
            result = DatabaseUtils.selectQueryById("*", "movie", "mv_name LIKE ? AND release_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        while (result.next()) {
            Movie movie = new Movie();

            movie.setMovieID(result.getInt("movie_id"));
            movie.setGenre(new Genre(result.getInt("genre_id")));
            movie.setMvName(new Name(result.getString("mv_name")));
            movie.setReleaseDate(new DateTime(result.getDate("release_date").toLocalDate()));
            movie.setDuration(result.getInt("duration"));
            movie.setLang(result.getString("lang"));
            movie.setDirector(result.getString("director"));
            movie.setWritter(result.getString("writter"));
            movie.setStarring(result.getString("starring"));
            movie.setMusicProvider(result.getString("music"));
            movie.setCountry(result.getString("country"));
            movie.setMetaDescription(result.getString("meta_description"));
            movie.setBasicTicketPrice(result.getDouble("basic_TicketPrice"));
            movie.setStatus(result.getInt("movie_status"));

            searchResults.add(movie);
        }

        result.close();

        return searchResults;
    }

    public static ArrayList<Movie> queryMovieByGenre(Genre genre) throws SQLException {
        ArrayList<Movie> searchResults = new ArrayList<>();
        ResultSet result = null;

        try {
            Object[] params = {genre.getGenreID(), 1};
            result = DatabaseUtils.selectQueryById("*", "movie", "genre_id = ? AND release_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) AND movie_status = ?", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        while (result.next()) {
            Movie movie = new Movie();

            movie.setMovieID(result.getInt("movie_id"));
            movie.setGenre(new Genre(result.getInt("genre_id")));
            movie.setMvName(new Name(result.getString("mv_name")));
            movie.setReleaseDate(new DateTime(result.getDate("release_date").toLocalDate()));
            movie.setDuration(result.getInt("duration"));
            movie.setLang(result.getString("lang"));
            movie.setDirector(result.getString("director"));
            movie.setWritter(result.getString("writter"));
            movie.setStarring(result.getString("starring"));
            movie.setMusicProvider(result.getString("music"));
            movie.setCountry(result.getString("country"));
            movie.setMetaDescription(result.getString("meta_description"));
            movie.setBasicTicketPrice(result.getDouble("basic_TicketPrice"));
            movie.setStatus(result.getInt("movie_status"));

            searchResults.add(movie);
        }

        result.close();

        return searchResults;
    }

    public static ArrayList<Movie> viewMovieListByFilter(Scanner sc) throws Exception {
        LocalDate currentDate = LocalDate.now();
        ArrayList<Movie> moviesAfterFiltered = new ArrayList<>();
        boolean error = true;
        int choice = 0;

        do {
            do {
                try {
                    System.out.println("\nPlease select a movie filtering from the list below: ");
                    System.out.println("1. Future Movie");
                    System.out.println("2. Movie within 1 week");
                    System.out.println("3. Movie within 1 month");
                    System.out.println("4. Movie within 3 months");
                    System.out.println("5. Movie within 1 year");
                    System.out.println("6. All movies");
                    System.out.print("\nEnter your selection (0 - Back): ");
                    choice = sc.nextInt();
                    sc.nextLine();

                    if (choice < 0 || choice > 6) {
                        System.out.println("Your choice is not among the available options! PLease try again.");
                    } else {
                        error = false;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                }
            } while (error);

            switch (choice) {
                case 0:
                    return null;
                case 1:
                    // 1. future movie
                    LocalDate futureMovie = currentDate.plusDays(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(futureMovie, null, 1);
                    break;
                case 2:
                    // 2. within 1 week
                    LocalDate oneWeekAgo = currentDate.minusWeeks(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(oneWeekAgo, currentDate, 1);
                    break;
                case 3:
                    // 3. within 1 month
                    LocalDate oneMonthAgo = currentDate.minusMonths(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(oneMonthAgo, currentDate, 1);
                    break;
                case 4:
                    // 4. within 3 month
                    LocalDate threeMonthAgo = currentDate.minusMonths(3);
                    moviesAfterFiltered = showMovieListAfterFiltered(threeMonthAgo, currentDate, 1);
                    break;
                case 5:
                    // 5. within 1 year
                    LocalDate oneYearAgo = currentDate.minusYears(1);
                    moviesAfterFiltered = showMovieListAfterFiltered(oneYearAgo, currentDate, 1);
                    break;
                case 6:
                    // 6. all movie
                    moviesAfterFiltered = showMovieListAfterFiltered(null, null, 1);
                    break;
            }
        } while (moviesAfterFiltered.isEmpty() && choice != 0);

        return moviesAfterFiltered;
    }

    public static ArrayList<Movie> showMovieListAfterFiltered(LocalDate expectedDate, LocalDate currentDate, int status){
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            Object[] params = {status};
            ResultSet result = DatabaseUtils.selectQueryById("*", "movie", "movie_status = ?", params);

            while (result.next()) {
                Movie movie = new Movie();

                movie.setMovieID(result.getInt("movie_id"));
                movie.setGenre(new Genre(result.getInt("genre_id")));
                movie.setMvName(new Name(result.getString("mv_name")));
                movie.setReleaseDate(new DateTime(result.getDate("release_date").toLocalDate()));
                movie.setDuration(result.getInt("duration"));
                movie.setLang(result.getString("lang"));
                movie.setDirector(result.getString("director"));
                movie.setWritter(result.getString("writter"));
                movie.setStarring(result.getString("starring"));
                movie.setMusicProvider(result.getString("music"));
                movie.setCountry(result.getString("country"));
                movie.setMetaDescription(result.getString("meta_description"));
                movie.setBasicTicketPrice(result.getDouble("basic_TicketPrice"));
                movie.setStatus(result.getInt("movie_status"));

                movies.add(movie);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Movie> moviesAfterFiltered = new ArrayList<>();

        System.out.printf("\n%-5s %s\n", "No", "Movie Name");

        int countFuture = 1;
        int countAll = 1;
        int countOther = 1;

        for (int i = 0; i < movies.size(); i++) {
            LocalDate localReleaseDate = movies.get(i).getReleaseDate().getDate();

            if (expectedDate != null && currentDate == null) {  // Future Movie(s)
                if (localReleaseDate.equals(expectedDate) || localReleaseDate.isAfter(expectedDate)) {
                    System.out.printf("%-5d %s\n", countFuture, movies.get(i).getMvName().getName());
                    moviesAfterFiltered.add(movies.get(i));
                    countFuture++;
                }
            } else if (expectedDate == null && currentDate == null) {  // All Movies
                System.out.printf("%-5d %s\n", countAll, movies.get(i).getMvName().getName());
                moviesAfterFiltered.add(movies.get(i));
                countAll++;
            }
            else {
                if (localReleaseDate.equals(expectedDate) || (localReleaseDate.isAfter(expectedDate) && localReleaseDate.isBefore(currentDate))) {
                    System.out.printf("%-5d %s\n", countOther, movies.get(i).getMvName().getName());
                    moviesAfterFiltered.add(movies.get(i));
                    countOther++;
                }
            }
        }

        if (moviesAfterFiltered.isEmpty()) {
            System.out.println("Sorry, no movie found!");
        }

        return moviesAfterFiltered;
    }

    // Format double data type value to two decimal point
    public static double formatDouble(double value){
        return Math.round(value * 100.0) / 100.0;
    }

    // Method for add movie
    public static String getMultipleValues(Scanner sc, String propertyName, String propertyPluralName) throws Exception {
        StringBuilder result = new StringBuilder();
        String continues = "N";
        boolean error = true;

        do {
            System.out.print("\nEnter the movie " + propertyName + ": ");
            String value = sc.nextLine();

            Name name = new Name(value);

            String errorMessage = MovieValidator.checkValue(value, propertyName);

            if (errorMessage == null) {
                name.capitalizeWords();
                error = false;

                do {
                    System.out.println("\nIs there another " + propertyPluralName + " for this movie? (Y / N)");
                    System.out.print("Answer: ");
                    String answer = sc.next();
                    sc.nextLine();

                    continues = SystemClass.askForContinue(answer);

                } while (continues.equals("Invalid"));

                if (continues.equals("Y")) {
                    result.append(name.getName()).append(", ");
                } else {
                    result.append(name.getName());
                }
            } else {
                System.out.println(errorMessage);
                error = true;
            }
        } while (error || continues.equals("Y"));

        return result.toString();
    }

    public static String getMultipleChosens(Scanner sc, String[] array, String propertyName){
        boolean error;

        do {
            System.out.println("\nAvailable " + propertyName);
            try {
                for (int i = 0; i < array.length; i++) {
                    System.out.println((i + 1) + ". " + array[i]);
                }

                System.out.print("\nEnter your selection: ");
                int choice = sc.nextInt();
                sc.nextLine();

                String errorMessage = MovieValidator.checkRange(choice, array);

                if (errorMessage == null) {
                    return array[choice - 1];  // Array starts from index 0
                } else {
                    System.out.println(errorMessage);
                    error = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number!");
                sc.nextLine();
                error = true;
            }
        } while (error);

        return null;
    }

    public static double getTicketPrice(Scanner sc, String propertyName){
        boolean error;

        do {
            try {
                System.out.print("\nEnter movie " + propertyName + " ticket price (RM): ");
                double mvTicketPrice = sc.nextDouble();
                sc.nextLine();

                String errorMessage = MovieValidator.checkTicketPrice(mvTicketPrice);

                if (errorMessage == null) {
                    mvTicketPrice = MovieUtils.formatDouble(mvTicketPrice);
                    return mvTicketPrice;
                }
                else {
                    System.out.println(errorMessage);
                    error = true;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid " + propertyName + " ticket price!");
                sc.nextLine();
                error = true;
            }
        } while (error);

        return 0;
    }

























    public static ArrayList<Movie> getAllMovieList(){
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            Object[] params = {};
            ResultSet result = DatabaseUtils.selectQueryById("*", "movie", null, null);

            while (result.next()) {
                Movie movie = new Movie();

                movie.setMovieID(result.getInt("movie_id"));
                movie.setGenre(new Genre(result.getInt("genre_id")));
                movie.setMvName(new Name(result.getString("mv_name")));
                movie.setReleaseDate(new DateTime(result.getDate("release_date").toLocalDate()));
                movie.setDuration(result.getInt("duration"));
                movie.setLang(result.getString("lang"));
                movie.setDirector(result.getString("director"));
                movie.setWritter(result.getString("writter"));
                movie.setStarring(result.getString("starring"));
                movie.setMusicProvider(result.getString("music"));
                movie.setCountry(result.getString("country"));
                movie.setMetaDescription(result.getString("meta_description"));
                movie.setBasicTicketPrice(result.getDouble("basic_TicketPrice"));
                movie.setStatus(result.getInt("movie_status"));

                movies.add(movie);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }
}