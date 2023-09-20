package Payment_Management;

public class TNGValidator {
    private TNGValidator(){
    }

    public static boolean phoneNoValidator(String phoneNo) {
        // 定义一个正则表达式，表示只包含字母和空格的名字
        String regex = "^(01\\d{8,10})";

        // 使用正则表达式进行匹配
        if (phoneNo.matches(regex)) {
            return true;
        } else {
            System.out.println("Phone Number is invalid. Please follow the format 01xxxxxxxxx (10-12 digits).");
        }

        return false;
    }

    public static boolean pinNoValidator(String pinNo) {
        String regex = "^(\\d{6})$";

        // 使用正则表达式进行匹配
        if (pinNo.matches(regex)) {
            return true;
        } else {
            System.out.println("Pin number is invalid. It should follow the format of 6-digits.");
        }

        return false;
    }
}
