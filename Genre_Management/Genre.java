package Genre_Management;

import Database.DatabaseUtils;
import Driver.DatabaseOperations;
import Driver.Name;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Genre implements DatabaseOperations {
    private int genreID;
    private Name genreName;
    private int post;

    public Genre(){
        post = 0;
    }

    // Constructor for getting data from the database
    public Genre(int genreID){
        this.genreID = genreID;
    }

    public Genre(int genreID, Name genreName) {
        this.genreID = genreID;
        this.genreName = genreName;
    }

    public Genre(int genreID, Name genreName, int post){
        this.genreID = genreID;
        this.genreName = genreName;
        this.post = post;
    }

    // Method
    public static ArrayList<Genre> viewGenreList(int status) throws SQLException {
        ArrayList<Genre> genres = new ArrayList<>();
        ResultSet result = null;
        int count = 1;

        try {
            Object[] params = {status};
            result = DatabaseUtils.selectQuery("*", "genre", "genre_status = ?", params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (result.next()) {
            Genre genre = new Genre(result.getInt("genre_id"), new Name(result.getString("genre_name")), result.getInt("post"));
            genres.add(genre);
        }

        System.out.print("-----------------------------------------------------");
        System.out.printf("\n%-3c %-4s %c %-40s %c\n", '|', "No", '|', "Genre Name", '|');
        System.out.println("-----------------------------------------------------");

        for (Genre genre : genres) {
            System.out.printf("%-3c %-4d %c %-40s %c\n", '|', count, '|', genre.getGenreName().getName(), '|');
            System.out.println("-----------------------------------------------------");
            count++;
        }

        return genres;
    }

    public void viewGenreDetails() throws SQLException {
        System.out.println("\n----------------");
        System.out.printf("| Genre Detail | \n");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%c %-20s %c %-55s %c\n", '|', "Genre Name", '|', genreName.getName(), '|');
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%c %-20s %c %-55s %c\n", '|', "Number of Post", '|', getPost(), '|');
        System.out.println("----------------------------------------------------------------------------------");
    }

    public boolean add() throws SQLException {
        int rowAffected = 0;

        try {
            String insertSql = "INSERT INTO `genre`(`genre_name`, `post`) VALUES (?, ?)";
            Object[] params = {genreName.getName(), getPost()};
            rowAffected = DatabaseUtils.insertQuery(insertSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nGenre successfully added...");
            return true;
        }
        else {
            System.out.println("\nSomething went wrong!");
            return false;
        }
    }

    public boolean modify() throws SQLException {
        int rowAffected = 0;

        try {
            String updateSql = "UPDATE `genre` SET `genre_name`= ? WHERE genre_id = ?";
            Object[] params = {genreName.getName(), genreID};
            rowAffected = DatabaseUtils.updateQuery(updateSql, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThe changes have been saved.");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    public boolean delete() throws SQLException {
        int rowAffected = 0;

        try {
            Object[] params = {genreID};
            rowAffected = DatabaseUtils.delectQuery("genre", "genre_status", "genre_id", params);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (rowAffected > 0) {
            System.out.println("\nThis genre has been deleted.");
            return true;
        } else {
            System.out.println("\nSomething went wrong...");
            return false;
        }
    }

    // Setter
    // No setter methods for genreID and post
    public void setGenreName(Name genreName){
        this.genreName = genreName;
    }

    // Getter
    public int getGenreID(){
        return genreID;
    }

    public Name getGenreName(){
        return genreName;
    }

    public int getPost() throws SQLException {
        Object[] params = {1, genreID};
        ResultSet result = DatabaseUtils.selectQuery("count(*) AS POST", "movie m, genre g", "m.genre_id = g.genre_id AND m.movie_status = ? AND g.genre_id = ?", params);
        while (result.next()) {
            post = result.getInt("post");
        }
        return post;
    }
}