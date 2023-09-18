package Security_Management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidator {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String validateUsername(Scanner input) {
        String uname;
        do {
            uname = input.nextLine();
            if (uname.length() < 5 || uname.length() > 15) {
                System.out.println("Username length should be between 5 and 15 characters.\nPlease re-enter: ");
            }
        } while (uname.length() < 5 || uname.length() > 15);

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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        do {
            dob = input.nextLine();

            if (!isValidDateFormat(dob)) {
                System.out.println("Invalid date format. Please use the format dd-MM-yyyy.");
            } else if (!isValidDateOfBirth(dob)) {
                System.out.println("Invalid date of birth or age. Please enter a valid date of birth (dd-MM-yyyy) and ensure your age is between 12 and 100 years.");
            }
        } while (!isValidDateFormat(dob) || !isValidDateOfBirth(dob));

        return dob;
    }

    private static boolean isValidDateFormat(String dob) {
        try {
            LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isValidDateOfBirth(String dob) {
        try {
            LocalDate dateOfBirth = LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate currentDate = LocalDate.now();

            Period age = Period.between(dateOfBirth, currentDate);
            int years = age.getYears();

            return years >= 12 && years <= 100;
        } catch (Exception e) {
            return false;
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
    public static String validateStatus(Scanner scanner) {
        String status;
        do {
            System.out.print("Enter status (1 for active, 2 for inactive): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    status = "active";
                    break;
                case 2:
                    status = "inactive";
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid status option.");
                    status = null; // 设置为 null 以重新提示用户输入
            }
        } while (status == null);

        return status;
    }

    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.endsWith(".com");
    }
}
