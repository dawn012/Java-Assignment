package Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidator {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String validateUsername(Scanner input) {
        String uname;
        do {
            uname = input.next();
            if (uname.length() < 5 || uname.length() > 15 || uname.contains(" ")) {
                System.out.println("Username length should be between 5 and 15 characters and should not contain spaces.\nPlease re-enter: ");
            }
        } while (uname.length() < 5 || uname.length() > 15 || uname.contains(" "));

        return uname;
    }

    public static String validatePassword(Scanner input) {
        String ps;
        do {
            ps = input.next();
            if (ps.length() < 8 || !isPasswordValid(ps)) {
                System.out.println("Password should be at least 8 characters and contain uppercase letter, lowercase letter, digit, and special character.\nPlease re-enter:");
            }
        } while (ps.length() < 8 || !isPasswordValid(ps));

        return ps;
    }

    private static boolean isPasswordValid(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";
        return password.matches(regex);
    }

    public static String validatePasswordConfirmation(Scanner input, String originalPassword) {
        String cps;
        do {
            cps = input.next();
            if (!cps.equals(originalPassword)) {
                System.out.println("Confirmation password does not match the password, please re-enter:");
            }
        } while (!cps.equals(originalPassword));

        return cps;
    }

    public static String validateEmail(Scanner input) {
        String email;
        do {
            email = input.next();
            if (!isValidEmail(email)) {
                System.out.println("Invalid email address, please re-enter:");
            }
        } while (!isValidEmail(email));

        return email;
    }

    public static String validateGender(Scanner input) {
        String gender;
        do {
            gender = input.next().toUpperCase();
            if (!isValidGender(gender)) {
                System.out.println("Invalid gender, please re-enter (M or F only):");
            }
        } while (!isValidGender(gender));

        return gender;
    }

    private static boolean isValidGender(String gender) {
        return gender.equals("M") || gender.equals("F");
    }

    public static String validateDateOfBirth(Scanner input) {
        String dob;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        do {
            dob = input.next();

            dob = formatToDesiredFormat(dob);
            if (!isValidDateOfBirth(dob, dateFormat)) {
                System.out.println("Invalid date of birth, please re-enter (format: dd-MM-yyyy):");
                dob = "";
            } else if (!isAgeAbove12(dob, dateFormat)) {
                System.out.println("You must be at least 12 years old to register. Please re-enter (format: dd-MM-yyyy):");
                dob = "";
            }
        } while (dob.isEmpty());

        return dob;
    }

    private static String formatToDesiredFormat(String dob) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("d-M-yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inputFormat.parse(dob);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dob;
        }
    }

    private static boolean isValidDateOfBirth(String dob, SimpleDateFormat dateFormat) {
        try {
            Date date = dateFormat.parse(dob);
            Date currentDate = new Date();

            if (date.after(currentDate)) {
                return false;
            }

            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static boolean isAgeAbove12(String dob, SimpleDateFormat dateFormat) {
        try {
            Date dateOfBirth = dateFormat.parse(dob);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -12);
            Date twelveYearsAgo = cal.getTime();

            if (dateOfBirth.after(twelveYearsAgo)) {
                return false;
            }

            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static int calculateAge(String dob) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        try {
            Date birthDate = dateFormat.parse(dob);
            Date currentDate = new Date();
            long ageInMillis = currentDate.getTime() - birthDate.getTime();
            long years = ageInMillis / (365L * 24L * 60L * 60L * 1000L);
            return (int) years;
        } catch (ParseException e) {
            return -1;
        }
    }

    public static String validatePhoneNumber(Scanner input) {
        String phone;
        do {
            phone = input.next();
            if (!isValidPhoneNumber(phone)) {
                System.out.println("Invalid mobile phone number, please re-enter (format: 01X-XXXX-XXXX):");
            }
        } while (!isValidPhoneNumber(phone));

        return phone;
    }

    private static boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "^01[0-9]-[0-9]{4}-[0-9]{3,4}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            return false;
        }

        String digitsOnly = phone.replaceAll("-", "");
        return digitsOnly.length() == 10 || digitsOnly.length() == 11;
    }

    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.endsWith(".com");
    }
}
