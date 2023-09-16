package movie_management;

import Connect.DatabaseUtils;
import Driver.Name;
import genre_management.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    // Format double data type value to two decimal point
    public static double formatDouble(double value){
        return Math.round(value * 100.0) / 100.0;
    }
}