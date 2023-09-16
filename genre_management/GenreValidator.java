package genre_management;

import Connect.DatabaseUtils;

import java.sql.ResultSet;

public class GenreValidator {
    private GenreValidator(){
    }

    public static String checkGenreName(String genreName) throws Exception{
        if (genreName.trim().isEmpty()) {
            return "Please enter the genre name.";
        }
        else {
            boolean duplicateName = checkDuplicateGenreName(genreName);
            if(duplicateName == true) {
                return "Same genre name detected.";
            }
            else{
                return null;
            }
        }
    }

    public static boolean checkDuplicateGenreName(String mvName) throws Exception{
        try {
            ResultSet result = DatabaseUtils.selectQueryById("genre_name", "genre", null, null);

            while (result.next()) {
                String name = result.getString("genre_name");

                if (mvName.equals(name)) {
                    result.close();
                    return true;
                }
            }
            result.close();

            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // For Edit Genre
    public static String checkEditGenreName(String genreName, String orgGenreName) throws Exception{
        if (genreName.trim().isEmpty()) {
            return "Please enter the genre name.";
        }
        else {
            boolean duplicateName = checkEditDuplicateName(genreName, orgGenreName);
            if(duplicateName == true) {
                return "Same genre name detected.";
            }
            else{
                return null;
            }
        }
    }

    public static boolean checkEditDuplicateName(String genreName, String orgGenreName) throws Exception{
        try {
            ResultSet result = DatabaseUtils.selectQueryById("genre_name", "genre", null, null);

            while (result.next()) {
                String name = result.getString("genre_name");

                if (genreName.equals(name)) {
                    if (genreName.equals(orgGenreName)) {
                        result.close();
                        return false;
                    }
                    else {
                        result.close();
                        return true;
                    }
                }
            }
            result.close();

            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
