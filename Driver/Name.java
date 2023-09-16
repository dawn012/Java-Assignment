package Driver;

import Connect.DatabaseUtils;

import java.sql.ResultSet;

public class Name {
    private String name;

    public Name() {
    }

    public Name(String name) {
        this.name = name;
    }

    public void capitalizeWords(){
        StringBuilder result = new StringBuilder();
        name = name.trim();
        String[] words = name.split(" ");

        for (String word : words) {
            if (!word.isEmpty()) {
                char firstChar = Character.toUpperCase(word.charAt(0));  // Capitalize the first character of the word
                String restOfWord = word.substring(1).toLowerCase();  // substring(1) returns a substring from index 1 (the second character) to the end of the string
                result.append(firstChar).append(restOfWord).append(" ");  // Combine them and put them into result
            }
        }

        name = result.toString().trim();  // Replace it with a normal string and remove the leading and trailing spaces
    }

    public String checkName(String propertyName, ResultSet result, String searchThing) throws Exception{
        if (name.trim().isEmpty()) {
            return "Please enter the " + propertyName + " name.";
        }
        else {
            boolean duplicateName = checkDuplicateName(result, searchThing);
            if(duplicateName == true) {
                return "Same " + propertyName + " name detected.";
            }
            else{
                return null;
            }
        }
    }

    private boolean checkDuplicateName(ResultSet result, String searchThing) throws Exception {
        try {
            while (result.next()) {
                String name = result.getString(searchThing);

                if (this.name.equals(name)) {
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

    // For Edit Movie
    public String checkEditName(String propertyName, ResultSet result, String searchThing, String orgName) throws Exception{
        if (name.trim().isEmpty()) {
            return "Please enter the " + propertyName + " name.";
        }
        else {
            boolean duplicateName = checkEditDuplicateName(result, searchThing, orgName);
            if(duplicateName == true) {
                return "Same " + propertyName + " name detected.";
            }
            else{
                return null;
            }
        }
    }

    public boolean checkEditDuplicateName(ResultSet result, String searchThing, String orgName) throws Exception{
        try {
            while (result.next()) {
                String name = result.getString(searchThing);

                if (this.name.equals(name)) {
                    if (this.name.equals(orgName)) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
