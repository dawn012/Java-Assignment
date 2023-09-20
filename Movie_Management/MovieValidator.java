package Movie_Management;

public class MovieValidator {

    // Not allowed the programmers to create object for the MovieValidator class
    private MovieValidator(){
    }

    // Method

    // Check whether the release date later than the current date
    public static String checkDuration(int minutes){
        if (minutes < 80 || minutes > 300) {
            return "The movie must be between 80 and 300 minutes long.";
        }
        return null;
    }

    public static String checkRange(int choice, String[] languages){
        if (choice < 0 || choice > languages.length) {
            return "The selection must be between 1 and " + languages.length + ".";
        }
        return null;
    }

    public static String checkValue(String value, String propertyName) throws Exception{
        if (value.trim().isEmpty()) {
            return "Please enter the movie " + propertyName + ".";
        }
        return null;
    }

    public static String checkMetaDescription(String metaDescription){
        if (metaDescription.trim().isEmpty()) {
            return "Please enter the movie meta description.";
        }
        else {
            if (metaDescription.contains("'")) {
                return "Apostrophe cannot be contains in the description.";
            }
            else {
                return null;
            }
        }
    }

    public static String checkTicketPrice(double ticketPrice){
        if (ticketPrice < 0 || ticketPrice > 100) {
            return "The ticket price must be between RM0 and RM100.";
        }
        return null;
    }
}