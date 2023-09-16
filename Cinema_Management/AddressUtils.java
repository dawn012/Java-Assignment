package Cinema_Management;

import java.util.ArrayList;
import java.util.List;

public class AddressUtils {
    private AddressUtils(){};

    public static void viewStateList() {
        int count = 0;

        for (String state : Address.getStateToCities().keySet()) {
            System.out.println((count + 1) + ". " + state);
            count++;
        }
    }

    public static String getStateName(int stateIndex) {
        List<String> states = new ArrayList<>(Address.getStateToCities().keySet());

        return states.get(stateIndex);
    }

    public static int viewCityList(int stateSelected) {
        List<String> states = new ArrayList<>(Address.getStateToCities().keySet());  // 使 stateToCities 里面的 state 可以被索引

        String selectedState = states.get(stateSelected);
        int count = 0;

        for (String city : Address.getStateToCities().get(selectedState)) {
            System.out.println((count + 1) + ". " + city);
            count++;
        }
        return count;
    }

    public static String getCityName(String stateName, int cityIndex) {
        List<String> cities = Address.getStateToCities().get(stateName);  // 根据州名来获取 city 列表

        return cities.get(cityIndex);  // 返回所选的 city 名字
    }

    public static int viewPostcodeList(String citySelected) {
        int count = 0;

        for (String postcode : Address.getCityToPostcode().get(citySelected)) {
            System.out.println((count + 1) + ". " + postcode);
            count++;
        }
        return count;
    }

    public static String getPostcodeSelected(String cityName, int postcodeIndex) {
        List<String> postcodes = Address.getCityToPostcode().get(cityName);  // 根据 city 名来获取 postcode 列表

        return postcodes.get(postcodeIndex);  // 返回所选的 postcode
    }
}
