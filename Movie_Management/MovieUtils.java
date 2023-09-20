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

    public static ArrayList<Movie> queryMovies(String mvName, Genre genre) throws SQLException {
        ArrayList<Movie> searchResults = new ArrayList<>();
        ResultSet result = null;

        try {
            if (mvName != null && genre == null) {  // search by movie name
                Object[] params = {"%" + mvName + "%", 1};
                result = DatabaseUtils.selectQueryById("*", "movie", "mv_name LIKE ? AND release_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) AND movie_status = ?", params);
            }
            else if (mvName == null && genre != null) {  // search by genre
                Object[] params = {genre.getGenreID(), 1};
                result = DatabaseUtils.selectQueryById("*", "movie", "genre_id = ? AND release_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) AND movie_status = ?", params);
            }
            else {
                // 查询条件为空，不执行查询
                return searchResults;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (result.next()) {
            Movie movie = new Movie();

            movie.setMovieID(result.getInt("movie_id"));
            movie.setGenre(new Genre(result.getInt("genre_id")));
            movie.setMvName(new Name(result.getString("mv_name")));
            movie.setReleaseDate(new DateTime(result.getDate("release_date").toLocalDate()));
            movie.setDuration(result.getInt("duration"));
            movie.setLanguage(result.getString("lang"));
            movie.setDirector(result.getString("director"));
            movie.setWritter(result.getString("writter"));
            movie.setStarring(result.getString("starring"));
            movie.setMusicProvider(result.getString("music"));
            movie.setCountry(result.getString("country"));
            movie.setMetaDescription(result.getString("meta_description"));
            movie.setBasicTicketPrice(result.getDouble("basic_TicketPrice"));

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
                    System.out.println("\nPlease select a movie filtering from the list below:");
                    System.out.printf("------------------------------------------------------");
                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Filtering", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 1, '|', "Future Movie", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 2, '|', "Movie within 1 week", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 3, '|', "Movie within 1 month", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 4, '|', "Movie within 3 months", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 5, '|', "Movie within 1 year", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 6, '|', "All movies", '|');
                    System.out.println("------------------------------------------------------");
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
                    moviesAfterFiltered = getMovieListAfterFiltered(futureMovie, null, 1);
                    break;
                case 2:
                    // 2. within 1 week
                    LocalDate oneWeekAgo = currentDate.minusWeeks(1);
                    moviesAfterFiltered = getMovieListAfterFiltered(oneWeekAgo, currentDate, 1);
                    break;
                case 3:
                    // 3. within 1 month
                    LocalDate oneMonthAgo = currentDate.minusMonths(1);
                    moviesAfterFiltered = getMovieListAfterFiltered(oneMonthAgo, currentDate, 1);
                    break;
                case 4:
                    // 4. within 3 month
                    LocalDate threeMonthAgo = currentDate.minusMonths(3);
                    moviesAfterFiltered = getMovieListAfterFiltered(threeMonthAgo, currentDate, 1);
                    break;
                case 5:
                    // 5. within 1 year
                    LocalDate oneYearAgo = currentDate.minusYears(1);
                    moviesAfterFiltered = getMovieListAfterFiltered(oneYearAgo, currentDate, 1);
                    break;
                case 6:
                    // 6. all movie
                    moviesAfterFiltered = getMovieListAfterFiltered(null, null, 1);
                    break;
            }

            System.out.print("-----------------------------------------------------");
            System.out.printf("\n%-3c %-4s %c %-40s %c\n", '|', "No", '|', "Movie Name", '|');
            System.out.println("-----------------------------------------------------");

            if (!moviesAfterFiltered.isEmpty()) {
                for (int i = 0; i < moviesAfterFiltered.size(); i++) {
                    System.out.printf("%-3c %-4d %c %-40s %c\n", '|', (i + 1), '|', moviesAfterFiltered.get(i).getMvName().getName(), '|');
                    System.out.println("-----------------------------------------------------");
                }
            }
            else {
                System.out.println("-----------------------------------------------------");
                System.out.printf("%-15c %-35s %c\n", '|', "Sorry, no movie found!", '|');
                System.out.println("-----------------------------------------------------");
            }
        } while (moviesAfterFiltered.isEmpty() && choice != 0);

        return moviesAfterFiltered;
    }

    public static ArrayList<Movie> getMovieListAfterFiltered(LocalDate expectedDate, LocalDate currentDate, int status){
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
                movie.setLanguage(result.getString("lang"));
                movie.setDirector(result.getString("director"));
                movie.setWritter(result.getString("writter"));
                movie.setStarring(result.getString("starring"));
                movie.setMusicProvider(result.getString("music"));
                movie.setCountry(result.getString("country"));
                movie.setMetaDescription(result.getString("meta_description"));
                movie.setBasicTicketPrice(result.getDouble("basic_TicketPrice"));

                movies.add(movie);
            }

            result.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Movie> moviesAfterFiltered = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            LocalDate localReleaseDate = movies.get(i).getReleaseDate().getDate();

            if (expectedDate != null && currentDate == null) {  // Future Movie(s)
                if (localReleaseDate.equals(expectedDate) || localReleaseDate.isAfter(expectedDate)) {
                    moviesAfterFiltered.add(movies.get(i));
                }
            } else if (expectedDate == null && currentDate == null) {  // All Movies
                moviesAfterFiltered.add(movies.get(i));
            }
            else {
                if (localReleaseDate.equals(expectedDate) || (localReleaseDate.isAfter(expectedDate) && localReleaseDate.isBefore(currentDate))) {
                    moviesAfterFiltered.add(movies.get(i));
                }
            }
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

    public static void printWrappedTextForSynopsis(String header, String text, int lineLength) {
        String[] lines = text.split("\\s+");
        String border = new String(new char[lineLength]).replace('\0', '-');

        System.out.printf("| %-20s |                                                         |", header);

        StringBuilder currentLine = new StringBuilder();
        for (String word : lines) {
            if (currentLine.length() + word.length() - 3 <= lineLength) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                System.out.printf("\n| %-20s | %-55s |", "", currentLine.toString());
                currentLine = new StringBuilder();
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            System.out.printf("\n| %-20s | %-55s |\n", "", currentLine.toString());
        }
    }

    public static void printEditWrappedTextForSynopsis(String header, String text, int lineLength, int num) {
        String[] lines = text.split("\\s+");
        String border = new String(new char[lineLength]).replace('\0', '-');

        System.out.printf("| %-2d | %-20s |                                                    |", num, header);

        StringBuilder currentLine = new StringBuilder();
        for (String word : lines) {
            if (currentLine.length() + word.length() - 3 <= lineLength) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                System.out.printf("\n| %-2s | %-20s | %-50s |", "", "", currentLine.toString());
                currentLine = new StringBuilder();
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            System.out.printf("\n| %-2s | %-20s | %-50s |\n", "", "", currentLine.toString());
        }
    }
}