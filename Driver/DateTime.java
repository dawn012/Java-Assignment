package Driver;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTime {
    private int year;
    private int month;
    private int day;
    private LocalDate date;

    public DateTime(){
    }

    // For initializing the value of date (retrieve from database)
    public DateTime(LocalDate date){
        this.date = date;
        year = this.date.getYear();
        month = this.date.getMonthValue();
        day = this.date.getDayOfMonth();
    }

    public DateTime(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public String getCurrentTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(new Date());
    }

    public String checkLocalDate(){
        LocalDate currentDate = LocalDate.now(); // Current date

        int comparison = date.compareTo(currentDate); // Compare dates

        if (comparison <= 0) {
            return "The date cannot be earlier than or equal to today.";
        }
        else {
            return null;
        }
    }

    public boolean isValidDate(){
        // The standard range of months is 1 to 12. If you enter 13, it will by default adjust the out-of-range months (and days) appropriately,
        // e.g., adjusting month 13 to month 1, without throwing an exception
        if (getYear() > 9999) {
            return false;
        }
        else if (getMonth() < 1 || getMonth() > 12) {
            return false;
        }

        try {
            date = LocalDate.of(getYear(), getMonth(), getDay());
            return true;
        }
        catch (java.time.DateTimeException e) {
            return false;
        }
    }

    public static int[] dateFormatValidator(String date) {
        // 使用正则表达式验证日期字符串格式
        String regex = "^\\d{4}-\\d{2}-\\d{2}$"; // yyyy-MM-dd 格式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);

        if (!matcher.matches()) {
            System.out.println("Invalid date format. Please enter a valid date (yyyy-MM-dd).\n");
            return null;
        }

        try {
            String[] strDatePart = date.split("-");
            int year = Integer.parseInt(strDatePart[0]);  // Java's built-in method for converting strings to integers (int type)
            int month = Integer.parseInt(strDatePart[1]);
            int day = Integer.parseInt(strDatePart[2]);

            return new int[]{year, month, day};
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    public boolean run(String date) {
        boolean validDate;

        try {
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            // 验证日期是否 valid
            viewDate = new ShowDate(year, month, day);
            validDate = viewDate.isValidDate();

            if (validDate == true) {
                viewDate.setMonth(month - 1);  // Months are 0-based in calendar
                viewDate.parseDate();
                error = false;
            } else {
                System.out.println("Please enter a valid date!");
                error = true;
            }
        } catch (Exception e) {
            System.out.println("The date format entered in wrong!");
        }
    }
    */

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public LocalDate getDate() {
        return date;
    }
}
