package Driver;

import Cinema_Management.AddressUtils;
import Database.DatabaseUtils;
import Cinema_Management.Address;
import Cinema_Management.Cinema;
import Genre_Management.Genre;
import Hall_Management.Hall;
import Movie_Management.Movie;
import Movie_Management.MovieUtils;
import Movie_Management.MovieValidator;
import Promotion_Management.*;
import Schedule_Management.Schedule;
import Booking_Management.Booking;
import Seat_Management.Seat;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SystemClass {
    private SystemClass(){
    }

    public static void run(Scanner sc) throws Exception {
        int choice = 0;
        boolean error = true, back = false;

        do {
            do {
                try {
                    System.out.println("\nSelect the operation: ");
                    System.out.println("1. View Profile");
                    System.out.println("2. View Movie");
                    System.out.println("3. View Promotion");
                    System.out.println("4. Search Movie");
                    System.out.println("5. Log out");
                    System.out.print("\nEnter your selection: ");

                    choice = sc.nextInt();
                    sc.nextLine();

                    if (choice > 0 && choice <= 5) {
                        error = false;
                    } else {
                        System.out.println("Your choice is not among the available options! PLease try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                }
            } while (error);

            switch (choice) {
                case 1:
                    break;
                case 2:
                    do {
                        int periodSelected = 0;
                        error = true;
                        ArrayList<Movie> moviesAfterFiltered = new ArrayList<>();
                        int movieSelected = 0;
                        LocalDate currentDate = LocalDate.now();

                        do {
                            try {
                                System.out.println("\nSelect the time period: ");
                                System.out.println("1. Opening This Week");
                                System.out.println("2. Opening This Month");
                                System.out.println("3. Release Within 3 Months");
                                System.out.println("4. Coming Soon");
                                System.out.print("\nEnter your selection (0 - Back): ");

                                periodSelected = sc.nextInt();
                                sc.nextLine();

                                if (periodSelected >= 0 && periodSelected <= 4) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                            }
                        } while (error);

                        switch (periodSelected) {
                            case 0:
                                back = false;
                                break;
                            case 1:
                                LocalDate oneWeekAgo = currentDate.minusWeeks(1);

                                System.out.println("\nOpening This Week");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(oneWeekAgo, currentDate, 1);
                                break;
                            case 2:
                                LocalDate oneMonthAgo = currentDate.minusMonths(1);

                                System.out.println("\nOpening This Month");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(oneMonthAgo, currentDate, 1);
                                break;
                            case 3:
                                LocalDate threeMonthAgo = currentDate.minusMonths(3);

                                System.out.println("\nRelease within 3 months");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(threeMonthAgo, currentDate, 1);
                                break;
                            case 4:
                                LocalDate comingSoon = currentDate.plusDays(1);

                                System.out.println("\nComing Soon");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(comingSoon, null, 1);
                                break;
                        }

                        if (!moviesAfterFiltered.isEmpty()) {
                            System.out.printf("\n%-5s %s\n", "No", "Movie Name");
                            for (int i = 0; i < moviesAfterFiltered.size(); i++) {
                                System.out.printf("%-5d %s\n", (i + 1), moviesAfterFiltered.get(i).getMvName().getName());
                            }
                        }
                        else {
                            System.out.println("Sorry, no movie found!");
                        }

                        if (periodSelected != 0) {
                            do {
                                try {
                                    System.out.print("\nEnter the movie no (0 - Back): ");
                                    movieSelected = sc.nextInt();
                                    sc.nextLine();

                                    if (movieSelected >= 0 && movieSelected <= moviesAfterFiltered.size()) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid choice!");
                                    sc.nextLine();
                                    error = true;
                                }
                            } while (error);

                            if (movieSelected != 0) {
                                Movie movie = moviesAfterFiltered.get(movieSelected - 1);
                                movie.viewMovieDetails();

                                if (periodSelected != 4) {  // Coming Soon movie cannot be booked
                                    String bookNow;

                                    do {
                                        System.out.println("\nDo you want to book now? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next();
                                        sc.nextLine();

                                        bookNow = SystemClass.askForContinue(answer);
                                    } while (bookNow.equals("Invalid"));

                                    if (bookNow.equals("Y")) {
                                        Schedule schedule = new Schedule();
                                        schedule.setMovie(movie);

                                        // 1. Select the cinema
                                        int cinemaNo = 0;
                                        error = true;
                                        ArrayList<Cinema> cinemas = new ArrayList<>();
                                        do {
                                            try {
                                                System.out.print("\nSelect the cinema you want to view the schedule: ");
                                                cinemas = Cinema.viewCinemaList(1);
                                                System.out.print("\nEnter the cinema no: ");
                                                cinemaNo = sc.nextInt();
                                                sc.nextLine();

                                                if (cinemaNo > 0 && cinemaNo <= cinemas.size()) {
                                                    error = false;
                                                } else {
                                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println("Please enter a valid cinema no!");
                                                sc.nextLine();
                                            }
                                        } while (error);

                                        // 2. Select the show date
                                        int dateNo = 0;
                                        error = true;
                                        ArrayList<LocalDate> dateList;
                                        do {
                                            try {
                                                System.out.println("\nSelect the date you want to view the schedule: ");
                                                dateList = Schedule.generateOneWeekDateList();
                                                System.out.print("\nEnter the date no: ");
                                                dateNo = sc.nextInt();
                                                sc.nextLine();

                                                if (dateNo > 0 && dateNo <= dateList.size()) {
                                                    DateTime date = new DateTime(dateList.get(dateNo - 1));
                                                    schedule.setShowDate(date);
                                                    error = false;
                                                } else {
                                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println("Please enter a valid date no!");
                                                sc.nextLine();
                                            }
                                        } while (error);

                                        // 3. Select the time
                                        error = true;
                                        int scheduleSelected = 0;
                                        do {
                                            ArrayList<Hall> halls = cinemas.get(cinemaNo - 1).getHallList(1);
                                            ArrayList<Schedule> schedules = new ArrayList<>();

                                            int count = 1;
                                            System.out.printf("\n%-30s %15s %15s\n", "Hall Name", "Start Time", "End Time");
                                            for (int i = 0; i < halls.size(); i++) {
                                                schedule.setHall(halls.get(i));
                                                count = schedule.showHallAndTime(count, schedules);
                                            }

                                            try {
                                                System.out.print("\nEnter the schedule no: ");
                                                scheduleSelected = sc.nextInt();
                                                sc.nextLine();

                                                if (scheduleSelected > 0 && scheduleSelected <= schedules.size()) {
                                                    schedule.setScheduleID(schedules.get(scheduleSelected - 1).getScheduleID());
                                                    schedule.setHall(schedules.get(scheduleSelected - 1).getHall());
                                                    schedule.setStartTime(schedules.get(scheduleSelected - 1).getStartTime());
                                                    schedule.setEndTime(schedules.get(scheduleSelected - 1).getEndTime());
                                                    error = false;
                                                } else {
                                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println("Please enter a valid schedule no!");
                                                sc.nextLine();
                                            }
                                        } while (error);

                                        // 4. Select the seat chin yong part
                                        Booking booking = new Booking();
                                        booking.executeBooking(schedule);

                                    } else {
                                        back = false;
                                    }
                                } else {
                                    back = false;
                                }
                            } else {
                                back = false;
                            }
                        } else {
                            break;
                        }
                    } while (back == false);
                    break;
                case 3:
                    back = customerPromotion(sc, 1);
                    break;
                case 4:
                    do {
                        error = true;
                        int searchingMethod = 0;
                        boolean continues = true;
                        ArrayList<Movie> searchResults;
                        String confirmation;

                        do {
                            try {
                                System.out.println("\nSelect the searching method:");
                                System.out.println("1. Search by movie name");
                                System.out.println("2. Search by genre");
                                System.out.print("\nEnter your selection (0 - Back): ");
                                searchingMethod = sc.nextInt();
                                sc.nextLine();

                                if (searchingMethod >= 0 && searchingMethod <= 2) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                            }
                        } while (error);

                        switch (searchingMethod) {
                            case 0:
                                back = true;
                                break;
                            case 1:
                                // Search by movie name
                                do {
                                    error = true;
                                    String searchedMvName;
                                    int movieNum = 0;

                                    do {
                                        System.out.print("\nEnter the movie name you want to search for (0 - Back): ");
                                        searchedMvName = sc.nextLine();

                                        if (searchedMvName.trim().isEmpty()) {
                                            System.out.println("Please enter the movie name!");
                                        } else {
                                            error = false;
                                        }
                                    } while (error);

                                    if (!searchedMvName.equals("0")) {
                                        searchResults = MovieUtils.queryMovieByName(searchedMvName);

                                        error = true;
                                        System.out.println("\nSearch Results: ");

                                        if (!searchResults.isEmpty()) {
                                            for (int i = 0; i < searchResults.size(); i++) {
                                                System.out.println((i + 1) + ". " + searchResults.get(i).getMvName().getName());
                                            }

                                            do {
                                                try {
                                                    System.out.print("\nEnter your selection (0 - Back): ");
                                                    movieNum = sc.nextInt();
                                                    sc.nextLine();

                                                    if (movieNum >= 0 && movieNum <= searchResults.size()) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid choice!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            if (movieNum != 0) {
                                                Movie searchedMovie = searchResults.get(movieNum - 1);
                                                searchedMovie.viewMovieDetails();
                                            }
                                        } else {
                                            System.out.println("Sorry, no movie was found!");
                                        }

                                        do {
                                            System.out.println("\nDo you want to search another movie(s)? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);
                                        } while (confirmation.equals("Invalid"));

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            back = false;
                                            continues = false;
                                        }
                                    } else {
                                        back = false;
                                        continues = false;
                                    }
                                } while (continues);
                                break;
                            case 2:
                                // Search by genre
                                do {
                                    int genreSelected = 0;
                                    int movieNum = 0;

                                    System.out.println("Select the genre you want to view the movie:");
                                    ArrayList<Genre> genres = Genre.viewGenreList(1);

                                    do {
                                        try {
                                            System.out.print("\nEnter the genre no (0 - Back): ");
                                            genreSelected = sc.nextInt();
                                            sc.nextLine();

                                            if (genreSelected >= 0 && genreSelected <= genres.size()) {
                                                error = false;
                                            } else {
                                                System.out.println("Your choice is not among the available options! PLease try again.");
                                                error = true;
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please enter a valid choice!");
                                            sc.nextLine();
                                            error = true;
                                        }
                                    } while (error);

                                    if (genreSelected != 0) {
                                        Genre viewGenre = genres.get(genreSelected - 1);
                                        searchResults = MovieUtils.queryMovieByGenre(viewGenre);

                                        if (!searchResults.isEmpty()) {
                                            System.out.println("\nSearch Results: ");
                                            for (int i = 0; i < searchResults.size(); i++) {
                                                System.out.println((i + 1) + ". " + searchResults.get(i).getMvName().getName());
                                            }

                                            error = true;
                                            do {
                                                try {
                                                    System.out.print("\nEnter your selection (0 - Back): ");
                                                    movieNum = sc.nextInt();
                                                    sc.nextLine();

                                                    if (movieNum >= 0 && movieNum <= searchResults.size()) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid choice!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            if (movieNum != 0) {
                                                Movie searchedMovie = searchResults.get(movieNum - 1);
                                                searchedMovie.viewMovieDetails();
                                            }
                                        } else {
                                            System.out.println("Sorry, no movie was found!");
                                        }

                                        do {
                                            System.out.println("\nDo you want to search another movie(s) by genre? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);
                                        } while (confirmation.equals("Invalid"));

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            back = false;
                                            continues = false;
                                        }
                                    } else {
                                        back = false;
                                        continues = false;
                                    }
                                } while (continues);
                                break;
                        }
                    } while (back == false);
                    back = false;
                    break;
                case 5:
                    back = true;
                    break;
            }
        } while (back == false);

        // Admin
        back = false;
        error = true;

        do {
            do {
                try {
                    System.out.println("\nSelect the operation:");
                    System.out.println("1. Manage Cinema");
                    System.out.println("2. Manage Hall");
                    System.out.println("3. Manage Movie");
                    System.out.println("4. Manage Genre");
                    System.out.println("5. Manage Schedule");
                    System.out.println("6. Manage Promotion");
                    System.out.println("7. Log out");
                    System.out.print("\nEnter your selection: ");

                    choice = sc.nextInt();
                    sc.nextLine();

                    error = false;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                }
            } while (error);

            switch (choice) {
                case 1:
                    manageCinema(sc);
                    break;
                case 2:
                    manageHall(sc);
                    break;
                case 3:
                    manageMovie(sc);
                    break;
                case 4:
                    manageGenre(sc);
                    break;
                case 5:
                    manageSchedule(sc);
                    break;
                case 6:
                    back = managePromotion(sc);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Your choice is not among the available options! PLease try again.");
            }
        } while (back == false);
    }

    private static void manageCinema(Scanner sc) throws Exception {
        boolean back = false;

        do {
            int choice = displayMenu("Cinema", sc);
            boolean error = true;
            boolean continues = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    // View Cinema
                    do {
                        int cinemaNo = 0;
                        error = true;
                        ArrayList<Cinema> cinemas = new ArrayList<>();
                        do {
                            try {
                                System.out.println("\nSelect the cinema: ");
                                cinemas = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the cinema no (0 - Back): ");
                                cinemaNo = sc.nextInt();
                                sc.nextLine();

                                if (cinemaNo >= 0 && cinemaNo <= cinemas.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hall no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (cinemaNo != 0) {
                            cinemas.get(cinemaNo - 1).viewCinemaDetails();

                            String continueViewCinema;
                            do {
                                System.out.println("\nDo you want view another cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueViewCinema = SystemClass.askForContinue(answer);
                            } while (continueViewCinema.equals("Invalid"));

                            if (continueViewCinema.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    // Add Cinema
                    do {
                        Cinema newCinema = new Cinema();
                        // Cinema Name
                        Name name = null;
                        do {
                            System.out.print("\nEnter cinema name (0 - Back): ");
                            String cinemaName = sc.nextLine();

                            name = new Name(cinemaName);
                            name.capitalizeWords();

                            ResultSet result = null;
                            try {
                                result = DatabaseUtils.selectQueryById("cinema_name", "cinema", null, null);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            String errorMessage = name.checkName("cinema", result, "cinema_name");

                            if (errorMessage == null) {
                                newCinema.setCinemaName(name);
                                error = false;
                            } else {
                                System.out.println(errorMessage);
                                error = true;
                            }
                        } while (error);

                        boolean duplicatedAddress = false;
                        do {
                            // Cinema Address
                            int stateSelected = 0;
                            do {
                                try {
                                    System.out.println("\nSelect the state: ");
                                    AddressUtils.viewStateList();
                                    System.out.print("\nEnter your selection: ");
                                    stateSelected = sc.nextInt();
                                    sc.nextLine();

                                    if (stateSelected > 0 && stateSelected <= Address.getStateToCities().size()) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid state no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            String stateName = AddressUtils.getStateName(stateSelected - 1);

                            int citySelected = 0;
                            do {
                                try {
                                    System.out.println("\nSelect the city: ");
                                    int count = AddressUtils.viewCityList(stateSelected - 1);
                                    System.out.print("\nEnter your selection: ");
                                    citySelected = sc.nextInt();
                                    sc.nextLine();

                                    if (citySelected > 0 && citySelected <= count) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid city no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            String cityName = AddressUtils.getCityName(stateName, citySelected - 1);

                            int postcodeSelected = 0;
                            do {
                                try {
                                    System.out.println("\nSelect the city: ");
                                    int count = AddressUtils.viewPostcodeList(cityName);
                                    System.out.print("\nEnter your selection: ");
                                    postcodeSelected = sc.nextInt();
                                    sc.nextLine();

                                    if (postcodeSelected > 0 && postcodeSelected <= count) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid postcode no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            String postcode = AddressUtils.getPostcodeSelected(cityName, postcodeSelected - 1);

                            String streetName;
                            do {
                                System.out.print("\nEnter the street name: ");
                                streetName = sc.nextLine();

                                if (streetName.trim().isEmpty()) {
                                    System.out.println("Please enter the street name.");
                                    error = true;
                                } else {
                                    streetName = streetName.toUpperCase();
                                    error = false;
                                }
                            } while (error);

                            Address cinemaAddress = new Address(streetName.trim(), postcode, cityName, stateName);
                            newCinema.setCinemaAddress(cinemaAddress);

                            ResultSet result = null;
                            try {
                                result = DatabaseUtils.selectQueryById("cinema_address", "cinema", null, null);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            duplicatedAddress = cinemaAddress.checkAddressDuplicate(result, "cinema_address");

                            if (duplicatedAddress == true) {
                                System.out.println("Same cinema address detected.");
                            }
                        } while (duplicatedAddress);

                        // Cinema Phone
                        do {
                            System.out.print("\nEnter the cinema phone number: ");
                            String phoneNumber = sc.nextLine();

                            if (phoneNumber.trim().isEmpty()) {
                                System.out.println("Please enter the phone number.");
                                error = true;
                            } else {
                                newCinema.setCinemaPhone(phoneNumber.trim());

                                if (newCinema.isValidOfficePhoneNumber()) {
                                    error = false;
                                } else {
                                    System.out.println("The phone number is invalid.");
                                    error = true;
                                }
                            }
                        } while (error);

                        // Add Cinema
                        String confirmation;
                        do {
                            System.out.println("\nDo you want add the new cinema? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            confirmation = SystemClass.askForContinue(answer);
                        } while (confirmation.equals("Invalid"));

                        // Confirm that the cinema is successfully added
                        boolean success = false;
                        do {
                            if (confirmation.equals("Y")) {
                                success = newCinema.add();
                            } else {
                                success = true;
                                System.out.println("This cinema will not be added.");
                            }

                            if (success == false) {
                                do {
                                    System.out.println("\nDo you want to retry to add the new cinema? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    confirmation = SystemClass.askForContinue(answer);

                                    if (confirmation.equals("Y")) {
                                        continues = true;
                                    } else {
                                        continues = false;
                                    }
                                } while (confirmation.equals("Invalid"));
                            }
                            else {
                                continues = false;
                            }
                        } while (continues);

                        String continueAddCinema;
                        do {
                            System.out.println("\nDo you want add another new cinema? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            continueAddCinema = SystemClass.askForContinue(answer);
                        } while (continueAddCinema.equals("Invalid"));

                        if (continueAddCinema.equals("Y")) {
                            continues = true;
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 3:
                    // Modify Cinema
                    do {
                        error = true;
                        ArrayList<Cinema> cinemasModified = new ArrayList<>();
                        int cinemaModified = 0;

                        do {
                            try {
                                System.out.println("\nSelect the cinema you want to modify: ");
                                cinemasModified = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the cinema no (0 - Back): ");
                                cinemaModified = sc.nextInt();
                                sc.nextLine();

                                if (cinemaModified >= 0 && cinemaModified <= cinemasModified.size()) {
                                    error = false;
                                } else {
                                    error = true;
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid cinema no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (cinemaModified != 0) {
                            Cinema orgCinema = cinemasModified.get(cinemaModified - 1);
                            Cinema cinema = new Cinema(orgCinema.getCinemaID(), orgCinema.getCinemaName(), orgCinema.getCinemaAddress(), orgCinema.getCinemaPhone());
                            boolean stop = false;

                            do {
                                int serialNum = cinema.modifyCinemaDetails(sc);
                                switch (serialNum) {
                                    case 0:
                                        String confirmation;
                                        do {
                                            System.out.println("\nDo you want to save the changes? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);
                                        } while (confirmation.equals("Invalid"));

                                        stop = true;

                                        // Confirm that the cinema is successfully modified
                                        boolean success = false;
                                        do {
                                            if (confirmation.equals("Y")) {
                                                success = cinema.modify();
                                            } else {
                                                success = true;
                                                cinema = orgCinema;
                                                System.out.println("\nThe changes have not been saved.");
                                            }

                                            if (success == false) {
                                                do {
                                                    System.out.println("\nDo you want to retry to modify the cinema? (Y / N)");
                                                    System.out.print("Answer: ");
                                                    String answer = sc.next();
                                                    sc.nextLine();

                                                    confirmation = SystemClass.askForContinue(answer);

                                                    if (confirmation.equals("Y")) {
                                                        continues = true;
                                                    } else {
                                                        continues = false;
                                                    }
                                                } while (confirmation.equals("Invalid"));
                                            }
                                            else {
                                                continues = false;
                                            }
                                        } while (continues);

                                        String continueModifyCinema;
                                        do {
                                            System.out.println("\nDo you want modify another cinema? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            continueModifyCinema = SystemClass.askForContinue(answer);
                                        } while (continueModifyCinema.equals("Invalid"));

                                        if (continueModifyCinema.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                            back = false;
                                        }
                                        break;
                                    case 1:
                                        // Cinema Name
                                        Name name = null;
                                        do {
                                            System.out.print("\nEnter the new cinema name: ");
                                            String newCinemaName = sc.nextLine();

                                            name = new Name(newCinemaName);
                                            name.capitalizeWords();

                                            ResultSet result = null;
                                            try {
                                                result = DatabaseUtils.selectQueryById("cinema_name", "cinema", null, null);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }

                                            String errorMessage = name.checkEditName("cinema", result, "cinema_name", cinema.getCinemaName().getName());

                                            if (errorMessage == null) {
                                                cinema.setCinemaName(name);
                                                error = false;
                                            } else {
                                                System.out.println(errorMessage);
                                                error = true;
                                            }
                                        } while (error);
                                        break;
                                    case 2:
                                        // Cinema Address
                                        boolean duplicatedAddress;
                                        do {
                                            int stateSelected = 0;
                                            do {
                                                try {
                                                    System.out.println("\nSelect the state: ");
                                                    AddressUtils.viewStateList();
                                                    System.out.print("\nEnter your selection: ");
                                                    stateSelected = sc.nextInt();
                                                    sc.nextLine();

                                                    if (stateSelected > 0 && stateSelected <= Address.getStateToCities().size()) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                        error = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid state no!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            String stateName = AddressUtils.getStateName(stateSelected - 1);

                                            int citySelected = 0;
                                            do {
                                                try {
                                                    System.out.println("\nSelect the city: ");
                                                    int count = AddressUtils.viewCityList(stateSelected - 1);
                                                    System.out.print("\nEnter your selection: ");
                                                    citySelected = sc.nextInt();
                                                    sc.nextLine();

                                                    if (citySelected > 0 && citySelected <= count) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                        error = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid city no!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            String cityName = AddressUtils.getCityName(stateName, citySelected - 1);

                                            int postcodeSelected = 0;
                                            do {
                                                try {
                                                    System.out.println("\nSelect the city: ");
                                                    int count = AddressUtils.viewPostcodeList(cityName);
                                                    System.out.print("\nEnter your selection: ");
                                                    postcodeSelected = sc.nextInt();
                                                    sc.nextLine();

                                                    if (postcodeSelected > 0 && postcodeSelected <= count) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                        error = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid postcode no!");
                                                    sc.nextLine();
                                                }
                                            } while (error);

                                            String postcode = AddressUtils.getPostcodeSelected(cityName, postcodeSelected - 1);

                                            String streetName;
                                            do {
                                                System.out.print("\nEnter the street name: ");
                                                streetName = sc.nextLine();

                                                if (streetName.trim().isEmpty()) {
                                                    System.out.println("Please enter the street name.");
                                                    error = true;
                                                } else {
                                                    streetName = streetName.toUpperCase();
                                                    error = false;
                                                }
                                            } while (error);

                                            Address cinemaAddress = new Address(streetName.trim(), postcode, cityName, stateName);
                                            cinema.setCinemaAddress(cinemaAddress);

                                            ResultSet result = null;
                                            try {
                                                result = DatabaseUtils.selectQueryById("cinema_address", "cinema", null, null);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }

                                            duplicatedAddress = cinemaAddress.checkEditAddressDuplicate(result, "cinema_address", orgCinema.getCinemaAddress().getAddress());

                                            if (duplicatedAddress == true) {
                                                System.out.println("Same cinema address detected.");
                                            }
                                        } while (duplicatedAddress);
                                        break;
                                    case 3:
                                        // Cinema phone
                                        do {
                                            System.out.print("\nEnter the new cinema phone number: ");
                                            String phoneNumber = sc.nextLine();

                                            if (phoneNumber.trim().isEmpty()) {
                                                System.out.println("Please enter the phone number.");
                                                error = true;
                                            } else {
                                                cinema.setCinemaPhone(phoneNumber.trim());

                                                if (cinema.isValidOfficePhoneNumber()) {
                                                    error = false;
                                                } else {
                                                    System.out.println("The phone number is invalid.");
                                                    error = true;
                                                }
                                            }
                                        } while (error);
                                        break;
                                }
                            } while (stop == false);
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    // Delete Cinema
                    do {
                        error = true;
                        int cinemaDeleted = 0;
                        ArrayList<Cinema> cinemasDeleted = new ArrayList<>();
                        do {
                            try {
                                System.out.println("\nSelect the cinema you want to delete: ");
                                cinemasDeleted = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the cinema no (0 - Back): ");
                                cinemaDeleted = sc.nextInt();
                                sc.nextLine();

                                if (cinemaDeleted >= 0 && cinemaDeleted <= cinemasDeleted.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (cinemaDeleted != 0) {
                            Cinema cinema = cinemasDeleted.get(cinemaDeleted - 1);

                            String confirmation;
                            do {
                                System.out.println("\nAre you sure you want to delete this cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                confirmation = SystemClass.askForContinue(answer);
                            } while (confirmation.equals("Invalid"));

                            // Confirm that the cinema is successfully deleted
                            boolean success = false;
                            do {
                                if (confirmation.equals("Y")) {
                                    success = cinema.delete();
                                } else {
                                    success = true;
                                    System.out.println("\nThe cinema is safe :)");
                                }

                                if (success == false) {
                                    do {
                                        System.out.println("\nDo you want to retry to delete this cinema? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next();
                                        sc.nextLine();

                                        confirmation = SystemClass.askForContinue(answer);

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                        }
                                    } while (confirmation.equals("Invalid"));
                                }
                                else {
                                    continues = false;
                                }
                            } while (continues);

                            String continueDeleteCinema;
                            do {
                                System.out.println("\nDo you want to continue to delete another cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer2 = sc.next();
                                sc.nextLine();

                                continueDeleteCinema = SystemClass.askForContinue(answer2);
                            } while (continueDeleteCinema.equals("Invalid"));

                            if (continueDeleteCinema.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
            }
        } while (back == false);
    }

    private static void manageHall(Scanner sc) throws Exception {
        boolean back = false;
        boolean error = true;
        boolean continues = true;

        ArrayList<Cinema> cinemas = Cinema.viewCinemaList(1);
        int cinemaSelected = 0;

        do {
            try {
                System.out.println("\nSelect the cinema you want to manage it's hall: ");
                for (int i = 0; i < cinemas.size(); i++) {
                    System.out.println((i + 1) + ". " + cinemas.get(i).getCinemaName().getName());
                }
                System.out.print("\nEnter your selection: ");
                cinemaSelected = sc.nextInt();
                sc.nextLine();

                if (cinemaSelected > 0 && cinemaSelected <= cinemas.size()) {
                    error = false;
                }
                else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid cinema no!");
                sc.nextLine();
            }
        } while (error);

        Cinema cinema = cinemas.get(cinemaSelected - 1);

        do {
            int choice = displayMenu("Hall", sc);
            error = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    // Hall
                    do {
                        int hallNo = 0;
                        error = true;
                        ArrayList<Hall> halls = new ArrayList<>();
                        do {
                            try {
                                System.out.println("\nSelect the hall: ");
                                halls = cinema.getHallList(1);

                                for (int i = 0; i < halls.size(); i++) {
                                    System.out.println((i + 1) + ". " + halls.get(i).getHallName().getName());
                                }

                                System.out.print("\nEnter the hall no (0 - Back): ");
                                hallNo = sc.nextInt();
                                sc.nextLine();

                                if (hallNo >= 0 && hallNo <= halls.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hall no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (hallNo != 0) {
                            halls.get(hallNo - 1).viewHallDetails();

                            String continueViewHall;
                            do {
                                System.out.println("\nDo you want view another cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueViewHall = SystemClass.askForContinue(answer);
                            } while (continueViewHall.equals("Invalid"));

                            if (continueViewHall.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        }
                        else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    do {
                        Name name = null;
                        String hallName;

                        do {
                            System.out.print("\nEnter the hall name (0 - Back): ");
                            hallName = sc.nextLine();

                            if (!hallName.equals("0")) {
                                back = false;
                                name = new Name(hallName);
                                name.capitalizeWords();

                                ResultSet result = null;
                                try {
                                    Object[] params = {cinema.getCinemaID()};
                                    result = DatabaseUtils.selectQueryById("hall_name", "hall", "cinema_id = ?", params);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                                String errorMsg = name.checkName("hall", result, "hall_name");

                                if (errorMsg == null) {
                                    error = false;
                                } else {
                                    System.out.println(errorMsg);
                                    error = true;
                                }
                            }
                            else {
                                error = false;
                            }
                        } while (error);

                        if (!hallName.equals("0")) {
                            String hallType = null;

                            do {
                                try {
                                    System.out.println("\nSelect the hall type: ");
                                    System.out.println("1. Standard Hall");
                                    System.out.println("2. 3D Hall");
                                    System.out.print("\nEnter your selection: ");
                                    int hallTypeSelection = sc.nextInt();
                                    sc.nextLine();

                                    if (hallTypeSelection == 1) {
                                        hallType = "STANDARD";
                                        error = false;
                                    } else if (hallTypeSelection == 2) {
                                        hallType = "3D";
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid hall type no!");
                                    sc.nextLine();
                                    error = true;
                                }
                            } while (error);

                            Hall hall = new Hall(name, hallType);

                            String confirmation;
                            do {
                                System.out.println("\nDo you want add the new hall for this cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                confirmation = SystemClass.askForContinue(answer);
                            } while (confirmation.equals("Invalid"));

                            // Confirm that the hall is successfully added
                            boolean success;
                            do {
                                if (confirmation.equals("Y")) {
                                    success = cinema.addHall(hall);
                                } else {
                                    success = true;
                                    System.out.println("This hall will not be added for the cinema.");
                                }

                                if (success == false) {
                                    do {
                                        System.out.println("\nDo you want to retry to add the new hall for this cinema? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next();
                                        sc.nextLine();

                                        confirmation = SystemClass.askForContinue(answer);

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                        }
                                    } while (confirmation.equals("Invalid"));
                                }
                                else {
                                    continues = false;
                                }
                            } while (continues);

                            String continueAddHall;
                            do {
                                System.out.println("\nDo you want add another new cinema? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueAddHall = SystemClass.askForContinue(answer);
                            } while (continueAddHall.equals("Invalid"));

                            if (continueAddHall.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        }
                        else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 3:
                    // Modify Hall
                    do {
                        error = true;
                        boolean stop = true;
                        ArrayList<Hall> hallsModified = new ArrayList<>();
                        int hallModified = 0;

                        do {
                            try {
                                System.out.println("\nSelect the hall you want to modify: ");
                                hallsModified = cinema.getHallList(1);

                                for (int i = 0; i < hallsModified.size(); i++) {
                                    System.out.println((i + 1) + ". " + hallsModified.get(i).getHallName().getName());
                                }

                                System.out.print("\nEnter the hall no (0 - Back): ");
                                hallModified = sc.nextInt();
                                sc.nextLine();

                                if (hallModified >= 0 && hallModified <= hallsModified.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hall no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (hallModified != 0) {
                            do {
                                cinema.setHall(hallsModified.get(hallModified - 1));
                                int serialNo = cinema.getHall().modifyHallDetails(sc);

                                switch (serialNo) {
                                    case 0:
                                        String confirmation;
                                        do {
                                            System.out.println("\nDo you want to save the changes? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);
                                        } while (confirmation.equals("Invalid"));

                                        stop = false;

                                        // Confirm that the hall is successfully modified
                                        boolean success = false;
                                        do {
                                            if (confirmation.equals("Y")) {
                                                success = cinema.getHall().modifyHall();
                                            } else {
                                                success = true;
                                                System.out.println("\nThe changes have not been saved.");
                                            }

                                            if (success == false) {
                                                do {
                                                    System.out.println("\nDo you want to retry to modify the hall? (Y / N)");
                                                    System.out.print("Answer: ");
                                                    String answer = sc.next();
                                                    sc.nextLine();

                                                    confirmation = SystemClass.askForContinue(answer);

                                                    if (confirmation.equals("Y")) {
                                                        continues = true;
                                                    } else {
                                                        continues = false;
                                                    }
                                                } while (confirmation.equals("Invalid"));
                                            }
                                            else {
                                                continues = false;
                                            }
                                        } while (continues);

                                        String continueModifyHall;
                                        do {
                                            System.out.println("\nDo you want modify another hall? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            continueModifyHall = SystemClass.askForContinue(answer);
                                        } while (continueModifyHall.equals("Invalid"));

                                        if (continueModifyHall.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                            back = false;
                                        }
                                        break;
                                    case 1:
                                        Name name = null;
                                        do {
                                            System.out.print("\nEnter the new hall name: ");
                                            String hallName = sc.nextLine();

                                            name = new Name(hallName);
                                            name.capitalizeWords();

                                            ResultSet result = null;
                                            try {
                                                Object[] params = {cinema.getCinemaID()};
                                                result = DatabaseUtils.selectQueryById("hall_name", "hall", "cinema_id = ?", params);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }

                                            String errorMsg = name.checkEditName("hall", result, "hall_name", cinema.getHall().getHallName().getName());

                                            if (errorMsg == null) {
                                                hallsModified.get(hallModified - 1).setHallName(name);
                                                error = false;
                                            } else {
                                                System.out.println(errorMsg);
                                                error = true;
                                            }
                                        } while (error);
                                        break;
                                    case 2:
                                        System.out.println("Hall type cannot be modified! Please retry.");
                            /*do {
                                try {
                                    System.out.println("\nSelect the hall type: ");
                                    System.out.println("1. Standard Hall");
                                    System.out.println("2. 3D Hall");
                                    System.out.print("\nEnter your selection: ");
                                    int hallTypeSelection = sc.nextInt();

                                    if (hallTypeSelection == 1) {
                                        hallsModified.get(hallModified - 1).setHallType("STANDARD");
                                        //cinema.setHall(new Hall(hallsModified.get(hallModified - 1).getHallName(), "STANDARD"));
                                        error = false;
                                    } else if (hallTypeSelection == 2) {
                                        hallsModified.get(hallModified - 1).setHallType("3D");
                                        //cinema.setHall(new Hall(hallsModified.get(hallModified - 1).getHallName(), "3D"));
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                }
                                catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid hall type no!");
                                    sc.nextLine();
                                    error = true;
                                }
                            } while (error);*/
                                        break;
                                    case 3:
                                        System.out.println("Hall capacity cannot be modified! Please retry.");
                                        break;
                                    case 4: //ChinYong Part

                                        hallsModified.get(hallModified-1).initSeatList();
                                        String strRow=" ";
                                        int col = 0;
                                        char row =strRow.charAt(0);
                                        Scanner scanner=new Scanner(System.in);
                                        boolean validInput = false;
                                        String strCon="Y";
                                        char chCon = strCon.charAt(0);
                                        while (chCon=='Y') {
                                            hallsModified.get(hallModified-1).viewSeatStatus();
                                            while (!validInput) {
                                                try {
                                                    System.out.print("\nSelect Row    : ");
                                                    strRow=scanner.next().toUpperCase();
                                                    row=strRow.charAt(0);

                                                    System.out.print("Select Column : ");
                                                    col = scanner.nextInt();

                                                    if (!Seat.checkSeatValidation(row, col)) {
                                                        System.out.println("Invalid Input");
                                                        continue;
                                                    } else {
                                                        validInput = true;
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("Something wrong...");
                                                    scanner.nextLine();
                                                }
                                            }
                                            validInput=false;
                                            String letter2 = Integer.toString(hallsModified.get(hallModified - 1).getHallID());
                                            //char letter = (char) ('A' + row - 1);
                                            String combineSeatId = letter2 + row + Integer.toString(col);

                                            int seatStatus = 1;
                                            do {
                                                System.out.print("Enter Status (1=Available 0=Unavailable) : ");
                                                try {
                                                    seatStatus = sc.nextInt();
                                                    if (seatStatus != 1 && seatStatus != 0) {
                                                        System.out.println("Invalid Input");
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("Something wrong...");
                                                    scanner.nextLine();
                                                }
                                            } while (seatStatus != 1 && seatStatus != 0);
                                            String str = " ";
                                            char ch = str.charAt(0);
                                            do {
                                                System.out.print("Confirm ? (Y=Yes N=No) : ");
                                                str = scanner.next().toUpperCase();
                                                ch = str.charAt(0);
                                            } while (ch != 'Y' && ch != 'N');
                                            if (ch == 'Y') {
                                                for (Seat seats : hallsModified.get(hallModified - 1).getSeats()) {
                                                    if (seats.getSeat_id().equals(combineSeatId)) {
                                                        seats.setSeat_status(seatStatus);
                                                        seats.updateSeatStatus();
                                                    }
                                                }
                                            }
                                            do {
                                                System.out.print("Continue ? (Y=Yes N=No) : ");
                                                strCon = scanner.next().toUpperCase();
                                                chCon = str.charAt(0);
                                            } while (chCon != 'Y' && chCon != 'N');
                                        }
                                        break;
                                }
                            } while (stop);
                        }
                        else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    // Delete Hall
                    do {
                        error = true;
                        ArrayList<Hall> hallsDeleted = new ArrayList<>();
                        int hallDeleted = 0;

                        do {
                            try {
                                System.out.println("\nSelect the hall you want to delete: ");
                                hallsDeleted = cinema.getHallList(1);

                                for (int i = 0; i < hallsDeleted.size(); i++) {
                                    System.out.println((i + 1) + ". " + hallsDeleted.get(i).getHallName().getName());
                                }

                                System.out.print("\nEnter the hall no (0 - Back): ");
                                hallDeleted = sc.nextInt();
                                sc.nextLine();

                                if (hallDeleted >= 0 && hallDeleted <= hallsDeleted.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid hall no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (hallDeleted != 0) {
                            cinema.setHall(hallsDeleted.get(hallDeleted - 1));
                            String confirmation;
                            do {
                                System.out.println("\nDo you want to delete this hall? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                confirmation = SystemClass.askForContinue(answer);
                            } while (confirmation.equals("Invalid"));

                            if (confirmation.equals("Y")) {
                                cinema.getHall().deleteHall();
                            } else {
                                System.out.println("\nThe hall is saved.");
                            }

                            // Confirm that the cinema is successfully deleted
                            boolean success;
                            do {
                                if (confirmation.equals("Y")) {
                                    success = cinema.getHall().deleteHall();
                                } else {
                                    success = true;
                                    System.out.println("\nThe hall is safe :)");
                                }

                                if (success == false) {
                                    do {
                                        System.out.println("\nDo you want to retry to delete this hall? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next();
                                        sc.nextLine();

                                        confirmation = SystemClass.askForContinue(answer);

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                        }
                                    } while (confirmation.equals("Invalid"));
                                }
                                else {
                                    continues = false;
                                }
                            } while (continues);

                            String continueDeleteHall;
                            do {
                                System.out.println("\nDo you want to continue to delete another hall? (Y / N)");
                                System.out.print("Answer: ");
                                String answer2 = sc.next();
                                sc.nextLine();

                                continueDeleteHall = SystemClass.askForContinue(answer2);
                            } while (continueDeleteHall.equals("Invalid"));

                            if (continueDeleteHall.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
            }
        } while (back == false);
    }

    private static void manageMovie(Scanner sc) throws Exception {
        boolean back = false;

        do {
            int choice = displayMenu("Movie", sc);
            boolean error = true;
            boolean continues = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    // View Movie
                    do {
                        int choice1 = 0;
                        ArrayList<Movie> moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(null, null, 1);

                        if (!moviesAfterFiltered.isEmpty()) {
                            System.out.printf("\n%-5s %s\n", "No", "Movie Name");
                            for (int i = 0; i < moviesAfterFiltered.size(); i++) {
                                System.out.printf("%-5d %s\n", (i + 1), moviesAfterFiltered.get(i).getMvName().getName());
                            }
                        }
                        else {
                            System.out.println("Sorry, no movie found!");
                        }

                        do {
                            try {
                                System.out.print("\nEnter the movie no (0 - Back): ");
                                choice1 = sc.nextInt();
                                sc.nextLine();

                                if (choice1 >= 0 && choice1 <= moviesAfterFiltered.size()) {
                                    error = false;
                                }
                                else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (choice1 != 0) {
                            Movie viewMovie = moviesAfterFiltered.get(choice1 - 1);
                            viewMovie.viewMovieDetails();

                            String continueViewMovie;
                            do {
                                System.out.println("\nDo you want view another movie? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueViewMovie = SystemClass.askForContinue(answer);
                            } while (continueViewMovie.equals("Invalid"));

                            if (continueViewMovie.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    do {
                        Movie newMovie = new Movie();

                        // Movie Name
                        Name name = null;
                        do {
                            System.out.print("\nEnter movie name: ");
                            String mvName = sc.nextLine();

                            name = new Name(mvName);
                            name.capitalizeWords();

                            ResultSet result = null;
                            try {
                                result = DatabaseUtils.selectQueryById("mv_name", "movie", null, null);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            String errorMessage = name.checkName("movie", result, "mv_name");

                            if (errorMessage == null) {
                                newMovie.setMvName(name);
                                error = false;
                            } else {
                                System.out.println(errorMessage + "\n");
                                error = true;
                            }
                        } while (error);

                        // Movie Genre ID
                        do {
                            try {
                                Object[] params = {1};
                                ResultSet result = DatabaseUtils.selectQueryById("genre_id, genre_name", "genre", "genre_status = ?", params);

                                try {
                                    int i = 1;
                                    ArrayList<Integer> genreID = new ArrayList<>();

                                    System.out.println("\nAvailable Genres");
                                    while (result.next()) {
                                        System.out.println(i + ". " + result.getString("genre_name"));
                                        genreID.add(result.getInt("genre_id"));  // Store the genre ID
                                        i++;
                                    }
                                    System.out.print("\nEnter your selection: ");
                                    int genreSelected = sc.nextInt();
                                    sc.nextLine();

                                    String errorMessage = MovieValidator.checkGenreID(genreID.size(), genreSelected);

                                    if (errorMessage == null) {
                                        Genre genre = new Genre(genreID.get(genreSelected - 1));
                                        newMovie.setGenre(genre);  // ArrayList starts from index 0
                                        error = false;
                                    }
                                    else {
                                        System.out.println(errorMessage);
                                        error = true;
                                    }
                                }
                                catch (Exception e) {
                                    System.out.println("Please enter a valid choice!");
                                    sc.nextLine();
                                    error = true;
                                }
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } while (error);

                        // Movie Release Date
                        do {
                            System.out.print("\nEnter movie release date (YYYY-MM-DD): ");
                            String releaseDate = sc.nextLine();

                            if (releaseDate.trim().isEmpty()) {
                                System.out.println("Please enter the release date.");
                                error = true;
                            } else {
                                try {
                                    String[] parts = releaseDate.split("-");
                                    int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
                                    int month = Integer.parseInt(parts[1]);
                                    int day = Integer.parseInt(parts[2]);

                                    // 验证日期是否 valid
                                    DateTime date = new DateTime(year, month, day);
                                    boolean validDate = date.isValidDate();

                                    if (validDate == true) {
                                        String errorMessage = date.checkLocalDate();

                                        if (errorMessage == null) {
                                            newMovie.setReleaseDate(date);
                                            error = false;
                                        } else {
                                            System.out.println(errorMessage);
                                            error = true;
                                        }
                                    } else {
                                        System.out.println("Please enter a valid date!");
                                        error = true;
                                    }
                                } catch (Exception e) {
                                    System.out.println("The date format entered in wrong!");
                                    error = true;
                                }
                            }
                        } while (error);

                        // Movie Duration
                        do {
                            try {
                                System.out.print("\nEnter movie duration (in minutes): ");
                                int duration = sc.nextInt();
                                sc.nextLine();  // Consume the newline left by nextInt()

                                String errorMessage = MovieValidator.checkDuration(duration);

                                if (errorMessage == null) {
                                    newMovie.setDuration(duration);
                                    error = false;
                                } else {
                                    System.out.println(errorMessage);
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid duration!");
                                sc.nextLine(); // Consume the erroneous input
                                error = true;
                            }
                        } while (error);

                        // Movie Language
                        String[] languages = {"English", "Chinese", "Japanese", "Korean", "German", "Italian", "Spanish", "Cantonese", "French", "Russian", "Arabic", "Hindi", "Tamil"};

                        String mvLanguage = MovieUtils.getMultipleChosens(sc, languages, "languages");
                        newMovie.setLanguage(mvLanguage);

                        // Movie Director
                        String mvDirector = MovieUtils.getMultipleValues(sc, "director", "directors");
                        newMovie.setDirector(mvDirector);

                        // Movie Writter
                        String mvWritter = MovieUtils.getMultipleValues(sc, "writter", "writters");
                        newMovie.setWritter(mvWritter);

                        // Movie Starring
                        String mvStarring = MovieUtils.getMultipleValues(sc, "starring", "starrings");
                        newMovie.setStarring(mvStarring);

                        // Movie Music Provider
                        String mvMusicProvider = MovieUtils.getMultipleValues(sc, "music provider", "music prodivers");
                        newMovie.setMusicProvider(mvMusicProvider);

                        // Movie Country
                        String[] countries = {"United States", "United Kingdom", "Canada", "China", "Taiwan", "Malaysia", "Singapore", "Japan", "North Korea", "Italy", "Hong Kong", "France", "Russia", "India"};

                        String mvCountry = MovieUtils.getMultipleChosens(sc, countries, "countries");
                        newMovie.setCountry(mvCountry);

                        // Movie Basic Ticket Price
                        double mvBasicTicketPrice = MovieUtils.getTicketPrice(sc, "basic");
                        newMovie.setBasicTicketPrice(mvBasicTicketPrice);

                        // Movie Meta Description
                        do {
                            System.out.print("\nEnter movie meta description: ");
                            String mvDescription = sc.nextLine();

                            String errorMessage = MovieValidator.checkMetaDescription(mvDescription);

                            if (errorMessage == null) {
                                newMovie.setMetaDescription(mvDescription);
                                error = false;
                            }
                            else {
                                System.out.println(errorMessage);
                                error = true;
                            }
                        } while (error);

                        String confirmation;
                        do {
                            System.out.println("\nDo you want add this new movie? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            confirmation = SystemClass.askForContinue(answer);
                        } while (confirmation.equals("Invalid"));

                        // Confirm that the movie is successfully added
                        boolean success = false;
                        do {
                            if (confirmation.equals("Y")) {
                                success = newMovie.add();
                            } else {
                                success = true;
                                System.out.println("This movie will not be added.");
                            }

                            if (success == false) {
                                do {
                                    System.out.println("\nDo you want to retry to add the new movie? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    confirmation = SystemClass.askForContinue(answer);

                                    if (confirmation.equals("Y")) {
                                        continues = true;
                                    } else {
                                        continues = false;
                                    }
                                } while (confirmation.equals("Invalid"));
                            }
                            else {
                                continues = false;
                            }
                        } while (continues);

                        String continueAddMovie;
                        do {
                            System.out.println("\nDo you want add another new movie? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            continueAddMovie = SystemClass.askForContinue(answer);
                        } while (continueAddMovie.equals("Invalid"));

                        if (continueAddMovie.equals("Y")) {
                            continues = true;
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 3:
                    ArrayList<Movie> moviesAfterFiltered;
                    int movieID = 1;

                    do {
                        System.out.println("\nSelect the movie you want to modify: ");
                        moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(null, null, 1);

                        if (!moviesAfterFiltered.isEmpty()) {
                            System.out.printf("\n%-5s %s\n", "No", "Movie Name");
                            for (int i = 0; i < moviesAfterFiltered.size(); i++) {
                                System.out.printf("%-5d %s\n", (i + 1), moviesAfterFiltered.get(i).getMvName().getName());
                            }
                        }
                        else {
                            System.out.println("Sorry, no movie found!");
                        }

                        do {
                            try {
                                System.out.print("\nEnter the movie id (0 - Back): ");
                                movieID = sc.nextInt();
                                sc.nextLine();

                                if (movieID >= 0 && movieID <= moviesAfterFiltered.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid movie id!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (movieID != 0) {
                            Movie orgMovie = moviesAfterFiltered.get(movieID - 1);
                            Movie movie = new Movie(orgMovie.getMovieID(), orgMovie.getGenre(), orgMovie.getMvName(), orgMovie.getReleaseDate(), orgMovie.getDuration(), orgMovie.getLanguage(), orgMovie.getDirector(), orgMovie.getWritter(), orgMovie.getStarring(), orgMovie.getMusicProvider(), orgMovie.getCountry(), orgMovie.getMetaDescription(), orgMovie.getBasicTicketPrice());
                            boolean stop = true;

                            do {
                                int serialNum = movie.modifyMovieDetail(sc);
                                switch (serialNum) {
                                    case 0:
                                        String confirmation;
                                        do {
                                            System.out.println("\nDo you want to save the changes? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);
                                        } while (confirmation.equals("Invalid"));

                                        stop = false;

                                        // Confirm that the movie is successfully modified
                                        boolean success = false;
                                        do {
                                            if (confirmation.equals("Y")) {
                                                success = movie.modify();
                                            } else {
                                                success = true;
                                                movie = orgMovie;
                                                System.out.println("\nThe changes have not been saved.");
                                            }

                                            if (success == false) {
                                                do {
                                                    System.out.println("\nDo you want to retry to modify the movie? (Y / N)");
                                                    System.out.print("Answer: ");
                                                    String answer = sc.next();
                                                    sc.nextLine();

                                                    confirmation = SystemClass.askForContinue(answer);

                                                    if (confirmation.equals("Y")) {
                                                        continues = true;
                                                    } else {
                                                        continues = false;
                                                    }
                                                } while (confirmation.equals("Invalid"));
                                            }
                                            else {
                                                continues = false;
                                            }
                                        } while (continues);

                                        String continueModifyMovie;
                                        do {
                                            System.out.println("\nDo you want modify another movie? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            continueModifyMovie = SystemClass.askForContinue(answer);
                                        } while (continueModifyMovie.equals("Invalid"));

                                        if (continueModifyMovie.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                            back = false;
                                        }
                                        break;
                                    case 1:
                                        // Movie Name
                                        Name name = null;
                                        do {
                                            System.out.print("\nEnter the new movie name: ");
                                            String newMvName = sc.nextLine();

                                            name = new Name(newMvName);
                                            name.capitalizeWords();

                                            ResultSet result = null;
                                            try {
                                                result = DatabaseUtils.selectQueryById("mv_name", "movie", null, null);
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }

                                            String errorMessage = name.checkEditName("movie", result, "mv_name", orgMovie.getMvName().getName());

                                            if (errorMessage == null) {
                                                movie.setMvName(name);
                                                error = false;
                                            } else {
                                                System.out.println(errorMessage);
                                                error = true;
                                            }
                                        } while (error);
                                        break;
                                    case 2:
                                        // Movie Genre ID
                                        do {
                                            try {
                                                Object[] params = {1};
                                                ResultSet result = DatabaseUtils.selectQueryById("genre_id, genre_name", "genre", "genre_status = ?", params);

                                                try {
                                                    int i = 1;
                                                    ArrayList<Integer> genreID = new ArrayList<>();

                                                    System.out.println("\nAvailable Genres");
                                                    while (result.next()) {
                                                        System.out.println(i + ". " + result.getString("genre_name"));
                                                        genreID.add(result.getInt("genre_id"));  // Store the genre ID
                                                        i++;
                                                    }
                                                    System.out.print("\nEnter your selection: ");
                                                    int newGenre = sc.nextInt();
                                                    sc.nextLine();

                                                    String errorMessage = MovieValidator.checkGenreID(genreID.size(), newGenre);

                                                    if (errorMessage == null) {
                                                        Genre genre = new Genre(genreID.get(newGenre - 1));
                                                        movie.setGenre(genre);  // ArrayList starts from index 0
                                                        error = false;
                                                    } else {
                                                        System.out.println(errorMessage);
                                                        error = true;
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("Please enter a valid choice!");
                                                    sc.nextLine();
                                                    error = true;
                                                }
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        } while (error);
                                        break;
                                    case 3:
                                        // Movie Release Date
                                        do {
                                            System.out.print("\nEnter movie release date (YYYY-MM-DD) (X - Back): ");
                                            String editReleaseDate = sc.nextLine();

                                            if (editReleaseDate.trim().isEmpty()) {
                                                System.out.println("Please enter the release date.");
                                                error = true;
                                            } else if (editReleaseDate.equals("x") || editReleaseDate.equals("X")) {
                                                break;
                                            } else {
                                                try {
                                                    String[] editParts = editReleaseDate.split("-");
                                                    int editYear = Integer.parseInt(editParts[0]);  // Java's built-in method for converting strings to integers (int type)
                                                    int editMonth = Integer.parseInt(editParts[1]);
                                                    int editDay = Integer.parseInt(editParts[2]);

                                                    // 验证日期是否 valid
                                                    DateTime editDate = new DateTime(editYear, editMonth, editDay);
                                                    boolean editValidDate = editDate.isValidDate();

                                                    if (editValidDate == true) {
                                                        String errorMessage = editDate.checkLocalDate();

                                                        if (errorMessage == null) {
                                                            movie.setReleaseDate(editDate);
                                                            error = false;
                                                        } else {
                                                            System.out.println(errorMessage);
                                                            error = true;
                                                        }
                                                    } else {
                                                        System.out.println("Please enter a valid date!");
                                                        error = true;
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("The date format entered in wrong!");
                                                    error = true;
                                                }
                                            }
                                        } while (error);
                                        break;
                                    case 4:
                                        // Movie Duration
                                        do {
                                            try {
                                                System.out.print("\nEnter the new movie duration (in minutes): ");
                                                int editDuration = sc.nextInt();
                                                sc.nextLine();  // Consume the newline left by nextInt()

                                                String errorMessage = MovieValidator.checkDuration(editDuration);

                                                if (errorMessage == null) {
                                                    movie.setDuration(editDuration);
                                                    error = false;
                                                } else {
                                                    System.out.println(errorMessage);
                                                    error = true;
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println("Please enter a valid duration!");
                                                sc.nextLine(); // Consume the erroneous input
                                                error = true;
                                            }
                                        } while (error);
                                        break;
                                    case 5:
                                        // Movie Language
                                        String[] languages = {"English", "Chinese", "Japanese", "Korean", "German", "Italian", "Spanish", "Cantonese", "French", "Russian", "Arabic", "Hindi", "Tamil"};

                                        String editMvLanguage = MovieUtils.getMultipleChosens(sc, languages, "languages");
                                        movie.setLanguage(editMvLanguage);
                                        break;
                                    case 6:
                                        // Movie Director
                                        String editMvDirector = MovieUtils.getMultipleValues(sc, "director", "directors");
                                        movie.setDirector(editMvDirector);
                                        break;
                                    case 7:
                                        // Movie Writter
                                        String editMvWritter = MovieUtils.getMultipleValues(sc, "writter", "writters");
                                        movie.setWritter(editMvWritter);
                                        break;
                                    case 8:
                                        // Movie Starring
                                        String editMvStarring = MovieUtils.getMultipleValues(sc, "starring", "starrings");
                                        movie.setStarring(editMvStarring);
                                        break;
                                    case 9:
                                        // Movie Music Provider
                                        String editMvMusicProvider = MovieUtils.getMultipleValues(sc, "music provider", "music prodivers");
                                        movie.setMusicProvider(editMvMusicProvider);
                                        break;
                                    case 10:
                                        // Movie Country
                                        String[] countries = {"United States", "United Kingdom", "Canada", "China", "Taiwan", "Malaysia", "Singapore", "Japan", "North Korea", "Italy", "Hong Kong", "France", "Russia", "India"};

                                        String editMvCountry = MovieUtils.getMultipleChosens(sc, countries, "countries");
                                        movie.setCountry(editMvCountry);
                                        break;
                                    case 11:
                                        // Movie Basic Ticket Price
                                        double editMvBasicTicketPrice = MovieUtils.getTicketPrice(sc, "basic");
                                        movie.setBasicTicketPrice(editMvBasicTicketPrice);
                                        System.out.println(movie.getBasicTicketPrice());
                                        break;
                                    case 12:
                                        // Movie Meta Description
                                        do {
                                            System.out.print("\nEnter the new movie meta description: ");
                                            String editMvDescription = sc.nextLine();

                                            String errorMessage = MovieValidator.checkMetaDescription(editMvDescription);

                                            if (errorMessage == null) {
                                                movie.setMetaDescription(editMvDescription);
                                                error = false;
                                            }
                                            else {
                                                System.out.println(errorMessage);
                                                error = true;
                                            }
                                        } while (error);
                                        break;
                                }
                            } while (stop);
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    movieID = 0;
                    do {
                        System.out.println("\nSelect the movie you want to delete: ");
                        moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(null, null, 1);

                        if (!moviesAfterFiltered.isEmpty()) {
                            System.out.printf("\n%-5s %s\n", "No", "Movie Name");
                            for (int i = 0; i < moviesAfterFiltered.size(); i++) {
                                System.out.printf("%-5d %s\n", (i + 1), moviesAfterFiltered.get(i).getMvName().getName());
                            }
                        }
                        else {
                            System.out.println("Sorry, no movie found!");
                        }

                        do {
                            try {
                                System.out.print("\nEnter the movie id (0 - Back): ");
                                movieID = sc.nextInt();
                                sc.nextLine();

                                if (movieID >= 0 && movieID <= moviesAfterFiltered.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid movie id!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (movieID != 0) {
                            Movie movie = moviesAfterFiltered.get(movieID - 1);
                            movie.viewMovieDetails();
                            String confirmation;
                            do {
                                System.out.println("\nDo you want to delete this movie? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                confirmation = SystemClass.askForContinue(answer);
                            } while (confirmation.equals("Invalid"));

                            // Confirm that the movie is successfully deleted
                            boolean success = false;
                            do {
                                if (confirmation.equals("Y")) {
                                    success = movie.delete();
                                } else {
                                    success = true;
                                    System.out.println("\nThe movie is safe :)");
                                }

                                if (success == false) {
                                    do {
                                        System.out.println("\nDo you want to retry to delete this cinema? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next();
                                        sc.nextLine();

                                        confirmation = SystemClass.askForContinue(answer);

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                        }
                                    } while (confirmation.equals("Invalid"));
                                }
                                else {
                                    continues = false;
                                }
                            } while (continues);

                            String continueDeleteMovie;
                            do {
                                System.out.println("\nDo you want delete another movie? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueDeleteMovie = SystemClass.askForContinue(answer);
                            } while (continueDeleteMovie.equals("Invalid"));

                            if (continueDeleteMovie.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
            }
        } while (back == false);
    }

    private static void manageGenre(Scanner sc) throws Exception {
        boolean back = false;

        do {
            ArrayList<Genre> genres = new ArrayList<>();
            int choice = displayMenu("Genre", sc);
            boolean error = true;
            boolean continues = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    int genreSelected = 0;
                    genres = Genre.viewGenreList(1);

                    do {
                        try {
                            System.out.print("\nEnter the genre no (0 - Back): ");
                            genreSelected = sc.nextInt();
                            sc.nextLine();

                            if (genreSelected >= 0 && genreSelected <= genres.size()) {
                                error = false;
                            }
                            else {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                                error = true;
                            }
                        }
                        catch (InputMismatchException e) {
                            System.out.println("Please enter a valid choice!");
                            sc.nextLine();
                            error = true;
                        }
                    } while (error);

                    if (genreSelected != 0) {
                        Genre viewGenre = genres.get(genreSelected - 1);
                        viewGenre.viewGenreDetails();
                    }
                    back = false;
                    break;
                case 2:
                    do {
                        Genre newGenre = new Genre();

                        // Genre Name
                        Name name = null;
                        do {
                            System.out.print("\nEnter genre name: ");
                            String genreName = sc.nextLine();

                            name = new Name(genreName);
                            name.capitalizeWords();

                            ResultSet result = null;
                            try {
                                result = DatabaseUtils.selectQueryById("genre_name", "genre", null, null);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            String errorMessage = name.checkName("genre", result, "genre_name");

                            if (errorMessage == null) {
                                newGenre.setGenreName(name);
                                error = false;
                            } else {
                                System.out.println(errorMessage);
                                error = true;
                            }
                        } while (error);

                        String confirmation;
                        do {
                            System.out.println("\nDo you want add this new genre? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            confirmation = SystemClass.askForContinue(answer);
                        } while (confirmation.equals("Invalid"));

                        // Confirm that the genre is successfully added
                        boolean success = false;
                        do {
                            if (confirmation.equals("Y")) {
                                success = newGenre.add();
                            } else {
                                success = true;
                                System.out.println("This genre will not be added.");
                            }

                            if (success == false) {
                                do {
                                    System.out.println("\nDo you want to retry to add the new genre? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    confirmation = SystemClass.askForContinue(answer);

                                    if (confirmation.equals("Y")) {
                                        continues = true;
                                    } else {
                                        continues = false;
                                    }
                                } while (confirmation.equals("Invalid"));
                            }
                            else {
                                continues = false;
                            }
                        } while (continues);

                        String continueAddGenre;
                        do {
                            System.out.println("\nDo you want add another new genre? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            continueAddGenre = SystemClass.askForContinue(answer);
                        } while (continueAddGenre.equals("Invalid"));

                        if (continueAddGenre.equals("Y")) {
                            continues = true;
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 3:
                    do {
                        int genreModified = 0;
                        do {
                            try {
                                System.out.println("\nSelect the genre you want to modify: ");
                                genres = Genre.viewGenreList(1);
                                System.out.print("\nEnter the genre no (0 - Back): ");
                                genreModified = sc.nextInt();
                                sc.nextLine();

                                if (genreModified >= 0 && genreModified <= genres.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (genreModified != 0) {
                            Genre orgGenre = genres.get(genreModified - 1);
                            Genre genre = new Genre(orgGenre.getGenreID(), orgGenre.getGenreName(), orgGenre.getPost(), orgGenre.getStatus());
                            Name name = null;

                            do {
                                System.out.print("\nEnter the new genre name (0 - Back): ");
                                String editGenreName = sc.nextLine();

                                if (editGenreName.equals("0")) {
                                    error = false;
                                } else {
                                    name = new Name(editGenreName);
                                    name.capitalizeWords();

                                    ResultSet result = null;
                                    try {
                                        result = DatabaseUtils.selectQueryById("genre_name", "genre", null, null);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }

                                    String errorMessage = name.checkEditName("genre", result, "genre_name", genre.getGenreName().getName());

                                    if (errorMessage == null) {
                                        error = false;
                                        genre.setGenreName(name);

                                        String confirmation;
                                        do {
                                            System.out.println("\nDo you want to save the changes? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);
                                        } while (confirmation.equals("Invalid"));

                                        // Confirm that the genre is successfully modified
                                        boolean success = false;
                                        do {
                                            if (confirmation.equals("Y")) {
                                                success = genre.modify();
                                            } else {
                                                success = true;
                                                genre.setGenreName(orgGenre.getGenreName());
                                                System.out.println("\nThe changes have not been saved.");
                                            }

                                            if (success == false) {
                                                do {
                                                    System.out.println("\nDo you want to retry to modify the genre? (Y / N)");
                                                    System.out.print("Answer: ");
                                                    String answer = sc.next();
                                                    sc.nextLine();

                                                    confirmation = SystemClass.askForContinue(answer);

                                                    if (confirmation.equals("Y")) {
                                                        continues = true;
                                                    } else {
                                                        continues = false;
                                                    }
                                                } while (confirmation.equals("Invalid"));
                                            }
                                            else {
                                                continues = false;
                                            }
                                        } while (continues);
                                    } else {
                                        System.out.println(errorMessage);
                                        error = true;
                                    }
                                }
                            } while (error);

                            String continueModifyGenre;
                            do {
                                System.out.println("\nDo you want to continue to modify another genre? (Y / N)");
                                System.out.print("Answer: ");
                                String answer2 = sc.next();
                                sc.nextLine();

                                continueModifyGenre = SystemClass.askForContinue(answer2);
                            } while (continueModifyGenre.equals("Invalid"));

                            if (continueModifyGenre.equals("Y")) {
                                continues = true;
                            } else {
                                back = false;
                                continues = false;
                            }
                        } else {
                            back = false;
                            continues = false;
                        }
                    } while (continues);
                    break;
                case 4:
                    // Delere genre
                    do {
                        int genreDeleted = 0;
                        do {
                            try {
                                System.out.println("\nSelect the genre you want to delete: ");
                                genres = Genre.viewGenreList(1);
                                System.out.print("\nEnter the genre no (0 - Back): ");
                                genreDeleted = sc.nextInt();
                                sc.nextLine();

                                if (genreDeleted >= 0 && genreDeleted <= genres.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (genreDeleted != 0) {
                            int post = genres.get(genreDeleted - 1).getPost();

                            if (post == 0) {
                                Genre genre = genres.get(genreDeleted - 1);
                                String confirmation;
                                do {
                                    System.out.println("\nAre you sure you want to delete this genre? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    confirmation = SystemClass.askForContinue(answer);
                                } while (confirmation.equals("Invalid"));

                                // Confirm that the genre is successfully deleted
                                boolean success = false;
                                do {
                                    if (confirmation.equals("Y")) {
                                        success = genre.delete();
                                    } else {
                                        success = true;
                                        System.out.println("\nThe genre is safe :)");
                                    }

                                    if (success == false) {
                                        do {
                                            System.out.println("\nDo you want to retry to delete this genre? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);

                                            if (confirmation.equals("Y")) {
                                                continues = true;
                                            } else {
                                                continues = false;
                                            }
                                        } while (confirmation.equals("Invalid"));
                                    }
                                    else {
                                        continues = false;
                                    }
                                } while (continues);

                                String continueDeleteGenre;
                                do {
                                    System.out.println("\nDo you want to continue to delete another genre? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer2 = sc.next();
                                    sc.nextLine();

                                    continueDeleteGenre = SystemClass.askForContinue(answer2);
                                } while (continueDeleteGenre.equals("Invalid"));

                                if (continueDeleteGenre.equals("Y")) {
                                    continues = true;
                                } else {
                                    continues = false;
                                    back = false;
                                }
                            } else {
                                System.out.println("Sorry, you cannot delete this genre. Please make sure there are no movie posts in this genre!");
                                continues = true;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
            }
        } while (back == false);
    }

    private static void manageSchedule(Scanner sc) throws Exception {
        boolean back = false;

        do {
            int choice = displayMenu("Schedule", sc);
            boolean error = true;
            boolean continues = true;

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    do {
                        // Cinema
                        int cinemaNo = 0;
                        error = true;
                        ArrayList<Cinema> cinemas = new ArrayList<>();
                        do {
                            try {
                                System.out.print("\nSelect the cinema you want to view the schedule (0 - Back): ");
                                cinemas = Cinema.viewCinemaList(1);
                                System.out.print("\nEnter the cinema no: ");
                                cinemaNo = sc.nextInt();
                                sc.nextLine();

                                if (cinemaNo >= 0 && cinemaNo <= cinemas.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid cinema no!");
                                sc.nextLine();
                            }
                        } while (error);

                        if (cinemaNo != 0) {
                            Schedule schedule = Schedule.acceptViewScheduleListInput(sc, cinemas.get(cinemaNo - 1));

                            ArrayList<Schedule> schedules = schedule.viewSchedule();

                            Schedule.printing(schedules);

                            String continueViewSchedule;
                            do {
                                System.out.println("\nDo you want view another schedule? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueViewSchedule = SystemClass.askForContinue(answer);
                            } while (continueViewSchedule.equals("Invalid"));

                            if (continueViewSchedule.equals("Y")) {
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } else {
                            continues = false;
                            back = false;
                        }
                    } while (continues);
                    break;
                case 2:
                    Schedule newSchedule = new Schedule();

                    ArrayList<Movie> moviesAfterFiltered;
                    int movieID = 1, hallID = 1;
                    error = true;

                    do {
                        moviesAfterFiltered = MovieUtils.viewMovieListByFilter(sc);  // return null means user select 0 (back), return empty ArrayList means no movie was found

                        if (moviesAfterFiltered != null) {
                            do {
                                try {
                                    System.out.print("\nEnter the movie id (0 - Back): ");
                                    movieID = sc.nextInt();
                                    sc.nextLine();

                                    if (movieID >= 0 && movieID <= moviesAfterFiltered.size()) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                        error = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid movie id!");
                                    sc.nextLine();
                                    error = true;
                                }
                            } while (error);
                        } else {
                            continues = false;
                            back = false;
                            break;
                        }
                    } while (movieID == 0 && moviesAfterFiltered != null);

                    if (movieID != 0 && moviesAfterFiltered != null) {
                        do {
                            newSchedule.setMovie(moviesAfterFiltered.get(movieID - 1));

                            // Cinema
                            int cinemaNo = 0;
                            error = true;
                            ArrayList<Cinema> cinemas = new ArrayList<>();

                            do {
                                try {
                                    System.out.print("\nSelect the cinema you want to view the schedule: ");
                                    cinemas = Cinema.viewCinemaList(1);
                                    System.out.print("\nEnter the cinema no: ");
                                    cinemaNo = sc.nextInt();
                                    sc.nextLine();

                                    if (cinemaNo > 0 && cinemaNo <= cinemas.size()) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid cinema no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            // Hall
                            int hallNo = 0;
                            error = true;
                            ArrayList<Hall> halls = new ArrayList<>();
                            do {
                                try {
                                    System.out.println("\nSelect the hall: ");
                                    halls = cinemas.get(cinemaNo - 1).getHallList(1);

                                    for (int i = 0; i < halls.size(); i++) {
                                        System.out.println((i + 1) + ". " + halls.get(i).getHallName().getName());
                                    }

                                    System.out.print("\nEnter the hall no: ");
                                    hallNo = sc.nextInt();
                                    sc.nextLine();

                                    if (hallNo > 0 && hallNo <= halls.size()) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid hall no!");
                                    sc.nextLine();
                                }
                            } while (error);

                            newSchedule.setHall(halls.get(hallNo - 1));

                            // Show Date
                            error = true;
                            String date = null;
                            DateTime addDate = null;
                            boolean validDate = false;
                            do {
                                System.out.print("\nEnter movie show date (YYYY-MM-DD): ");
                                date = sc.nextLine();

                                if (date.trim().isEmpty()) {
                                    System.out.println("Please enter the show date.");
                                } else {
                                    try {
                                        String[] parts = date.split("-");
                                        int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
                                        int month = Integer.parseInt(parts[1]);
                                        int day = Integer.parseInt(parts[2]);

                                        // 验证日期是否 valid
                                        addDate = new DateTime(year, month, day);
                                        validDate = addDate.isValidDate();

                                        if (validDate == true) {
                                            String errorMessage = addDate.checkLocalDate();

                                            if (errorMessage == null) {
                                                newSchedule.setShowDate(addDate);

                                                if (movieID == 1) {  // 1 mean add the schedule for the future movie, thus need to check whether the show date later than the movie release date
                                                    errorMessage = newSchedule.checkShowDate();
                                                    if (errorMessage == null) {
                                                        error = false;
                                                    } else {
                                                        System.out.println(errorMessage);
                                                    }
                                                }
                                            } else {
                                                System.out.println(errorMessage);
                                            }
                                        } else {
                                            System.out.println("Please enter a valid date!");
                                            error = true;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("The date format entered in wrong!");
                                    }
                                }
                            } while (error);

                            LocalTime[] selectedTimeSlots = newSchedule.availableTimeSlots(sc);
                            newSchedule.setStartTime(selectedTimeSlots[0]);
                            newSchedule.setEndTime(selectedTimeSlots[1]);

                            // Add schedule
                            String confirmation;
                            do {
                                System.out.println("\nDo you want add the new schedule for this movie? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                confirmation = SystemClass.askForContinue(answer);
                            } while (confirmation.equals("Invalid"));

                            // Confirm that the schedule is successfully added
                            boolean success;
                            do {
                                if (confirmation.equals("Y")) {
                                    success = newSchedule.add();
                                } else {
                                    success = true;
                                    System.out.println("This schedule will not be added for the movie.");
                                }

                                if (success == false) {
                                    do {
                                        System.out.println("\nDo you want to retry to add the new schedule for this movie? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next();
                                        sc.nextLine();

                                        confirmation = SystemClass.askForContinue(answer);

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                        }
                                    } while (confirmation.equals("Invalid"));
                                }
                                else {
                                    continues = false;
                                }
                            } while (continues);

                            String continueAddSchedule;
                            do {
                                System.out.println("\nDo you want add another new schedule for this movie? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                continueAddSchedule = SystemClass.askForContinue(answer);
                            } while (continueAddSchedule.equals("Invalid"));

                            if (continueAddSchedule.equals("Y")) {
                                newSchedule = new Schedule();
                                continues = true;
                            } else {
                                continues = false;
                                back = false;
                            }
                        } while (continues);
                    }
                    break;
                case 3:
                    // Modify Schedule
                    error = true;
                    int scheduleNo = 0;

                    // Cinema
                    int cinemaNo = 0;
                    error = true;
                    ArrayList<Cinema> cinemas = new ArrayList<>();

                    do {
                        try {
                            System.out.print("\nSelect the cinema you want to view the schedule: ");
                            cinemas = Cinema.viewCinemaList(1);
                            System.out.print("\nEnter the cinema no (0 - Back): ");
                            cinemaNo = sc.nextInt();
                            sc.nextLine();

                            if (cinemaNo >= 0 && cinemaNo <= cinemas.size()) {
                                error = false;
                            } else {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid cinema no!");
                            sc.nextLine();
                        }
                    } while (error);

                    error = true;
                    if (cinemaNo != 0) {
                        Schedule schedule = Schedule.acceptViewScheduleListInput(sc, cinemas.get(cinemaNo - 1));

                        ArrayList<Schedule> schedules = schedule.viewSchedule();

                        Schedule.printing(schedules);

                        if (!schedules.isEmpty()) {
                            do {
                                try {
                                    System.out.print("\nEnter the schedule no. you want to modify: ");
                                    scheduleNo = sc.nextInt();
                                    sc.nextLine();

                                    if (scheduleNo > 0 && scheduleNo <= schedules.size()) {
                                        error = false;
                                    } else {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid schedule no.");
                                    sc.nextLine();
                                }
                            } while (error);

                            Schedule modifySchedule = new Schedule(schedules.get(scheduleNo - 1).getScheduleID(), schedules.get(scheduleNo - 1).getMovie(), schedules.get(scheduleNo - 1).getHall(), schedules.get(scheduleNo - 1).getShowDate(), schedules.get(scheduleNo - 1).getStartTime());

                            error = true;
                            int choice2 = 0;
                            do {
                                try {
                                    System.out.println("\nSelect the operation:");
                                    System.out.println("1. Modify the movie show time");
                                    System.out.println("2. Modify the movie show date");
                                    System.out.println("3. Modify the movie to be played");
                                    System.out.println("4. Modify the location of the movie to be played");
                                    System.out.print("\nEnter your selection: ");
                                    choice2 = sc.nextInt();
                                    sc.nextLine();

                                    if (choice2 <= 0 || choice2 > 4) {
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                    } else {
                                        error = false;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please enter a valid operation no.");
                                    sc.nextLine();
                                }
                            } while (error);

                            switch (choice2) {
                                case 1:
                                    // 时间调整
                                    LocalTime[] selectedTimeSlots = modifySchedule.availableTimeSlots(sc);
                                    modifySchedule.setStartTime(selectedTimeSlots[0]);
                                    modifySchedule.setEndTime(selectedTimeSlots[1]);

                                    break;
                                case 2:
                                    // 日期调整
                                    error = true;
                                    String date;
                                    DateTime modifyDate = null;  // Store the old show date
                                    boolean validDate;

                                    do {
                                        System.out.print("\nEnter the new movie show date (YYYY-MM-DD): ");
                                        date = sc.nextLine();

                                        if (date.trim().isEmpty()) {
                                            System.out.println("Please enter the show date.");
                                        } else {
                                            try {
                                                String[] parts = date.split("-");
                                                int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
                                                int month = Integer.parseInt(parts[1]);
                                                int day = Integer.parseInt(parts[2]);

                                                // 验证日期是否 valid
                                                modifyDate = new DateTime(year, month, day);
                                                validDate = modifyDate.isValidDate();

                                                if (validDate == true) {
                                                    String errorMessage = modifyDate.checkLocalDate();

                                                    if (errorMessage == null) {
                                                        modifySchedule.setShowDate(modifyDate);

                                                        errorMessage = modifySchedule.checkShowDate();
                                                        if (errorMessage == null) {
                                                            error = false;
                                                        } else {
                                                            System.out.println(errorMessage);
                                                        }
                                                    } else {
                                                        System.out.println(errorMessage);
                                                        error = true;
                                                    }
                                                } else {
                                                    System.out.println("Please enter a valid date!");
                                                    error = true;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("The date format entered in wrong!");
                                            }
                                        }
                                    } while (error);

                                    // 时间调整
                                    selectedTimeSlots = modifySchedule.availableTimeSlots(sc);
                                    modifySchedule.setStartTime(selectedTimeSlots[0]);
                                    modifySchedule.setEndTime(selectedTimeSlots[1]);

                                    break;
                                case 3:
                                    // Modify the movie to be played
                                    movieID = 1;

                                    do {
                                        moviesAfterFiltered = MovieUtils.viewMovieListByFilter(sc);

                                        if (moviesAfterFiltered != null) {
                                            do {
                                                try {
                                                    System.out.print("\nEnter the movie id: ");
                                                    movieID = sc.nextInt();
                                                    sc.nextLine();

                                                    if (movieID > 0 && movieID <= moviesAfterFiltered.size()) {
                                                        error = false;
                                                    } else {
                                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                                        error = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please enter a valid movie id!");
                                                    sc.nextLine();
                                                    error = true;
                                                }
                                            } while (error);
                                        }
                                    } while (movieID == 0 && moviesAfterFiltered != null);

                                    if (moviesAfterFiltered != null) {
                                        modifySchedule.setMovie(moviesAfterFiltered.get(movieID - 1));

                                        // 日期调整
                                        error = true;
                                        modifyDate = null;  // Store the old show date

                                        do {
                                            System.out.print("\nEnter the new movie show date (YYYY-MM-DD): ");
                                            date = sc.nextLine();

                                            if (date.trim().isEmpty()) {
                                                System.out.println("Please enter the show date.");
                                            } else {
                                                try {
                                                    String[] parts = date.split("-");
                                                    int year = Integer.parseInt(parts[0]);  // Java's built-in method for converting strings to integers (int type)
                                                    int month = Integer.parseInt(parts[1]);
                                                    int day = Integer.parseInt(parts[2]);

                                                    // 验证日期是否 valid
                                                    modifyDate = new DateTime(year, month, day);
                                                    validDate = modifyDate.isValidDate();

                                                    if (validDate == true) {
                                                        String errorMessage = modifyDate.checkLocalDate();

                                                        if (errorMessage == null) {
                                                            modifySchedule.setShowDate(modifyDate);

                                                            errorMessage = modifySchedule.checkShowDate();
                                                            if (errorMessage == null) {
                                                                error = false;
                                                            } else {
                                                                System.out.println(errorMessage);
                                                            }
                                                            error = false;
                                                        } else {
                                                            System.out.println(errorMessage);
                                                            error = true;
                                                        }
                                                    } else {
                                                        System.out.println("Please enter a valid date!");
                                                        error = true;
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("The date format entered in wrong!");
                                                }
                                            }
                                        } while (error);

                                        // 时间调整
                                        selectedTimeSlots = modifySchedule.availableTimeSlots(sc);
                                        modifySchedule.setStartTime(selectedTimeSlots[0]);
                                        modifySchedule.setEndTime(selectedTimeSlots[1]);
                                    }
                                    break;
                                case 4:
                                    // Cinema
                                    cinemaNo = 0;
                                    error = true;
                                    cinemas = new ArrayList<>();

                                    do {
                                        try {
                                            System.out.print("\nSelect the cinema you want to add the schedule: ");
                                            cinemas = Cinema.viewCinemaList(1);
                                            System.out.print("\nEnter the cinema no: ");
                                            cinemaNo = sc.nextInt();
                                            sc.nextLine();

                                            if (cinemaNo > 0 && cinemaNo <= cinemas.size()) {
                                                error = false;
                                            } else {
                                                System.out.println("Your choice is not among the available options! PLease try again.");
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please enter a valid cinema no!");
                                            sc.nextLine();
                                        }
                                    } while (error);

                                    // Receive the input of hall and show date
                                    schedule = Schedule.acceptViewScheduleListInput(sc, cinemas.get(cinemaNo - 1));

                                    modifySchedule.setHall(schedule.getHall());
                                    modifySchedule.setShowDate(schedule.getShowDate());

                                    selectedTimeSlots = modifySchedule.availableTimeSlots(sc);
                                    modifySchedule.setStartTime(selectedTimeSlots[0]);
                                    modifySchedule.setEndTime(selectedTimeSlots[1]);

                                    break;
                            }

                            // Modify schedule
                            String confirmation;
                            do {
                                System.out.println("\nDo you want to modify this schedule? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                confirmation = SystemClass.askForContinue(answer);
                            } while (confirmation.equals("Invalid"));

                            // Confirm that the schedule is successfully modified
                            boolean success = false;
                            do {
                                if (confirmation.equals("Y")) {
                                    success = modifySchedule.modify();
                                } else {
                                    success = true;
                                    System.out.println("This schedule will not be modified.");
                                }

                                if (success == false) {
                                    do {
                                        System.out.println("\nDo you want to retry to modify the hall? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next();
                                        sc.nextLine();

                                        confirmation = SystemClass.askForContinue(answer);

                                        if (confirmation.equals("Y")) {
                                            continues = true;
                                        } else {
                                            continues = false;
                                        }
                                    } while (confirmation.equals("Invalid"));
                                }
                                else {
                                    continues = false;
                                }
                            } while (continues);
                        }
                    }
                    back = false;
                    break;
                case 4:
                    // Cinema
                    cinemaNo = 0;
                    error = true;
                    cinemas = new ArrayList<>();

                    do {
                        try {
                            System.out.print("\nSelect the cinema you want to view the schedule: ");
                            cinemas = Cinema.viewCinemaList(1);
                            System.out.print("\nEnter the cinema no (0 - Back): ");
                            cinemaNo = sc.nextInt();
                            sc.nextLine();

                            if (cinemaNo >= 0 && cinemaNo <= cinemas.size()) {
                                error = false;
                            } else {
                                System.out.println("Your choice is not among the available options! PLease try again.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid cinema no!");
                            sc.nextLine();
                        }
                    } while (error);

                    if (cinemaNo != 0) {
                        Schedule schedule = Schedule.acceptViewScheduleListInput(sc, cinemas.get(cinemaNo - 1));

                        do {
                            ArrayList<Schedule> schedules = schedule.viewSchedule();

                            Schedule.printing(schedules);

                            if (!schedules.isEmpty()) {
                                error = true;
                                scheduleNo = 1;
                                String confirmation;

                                do {
                                    try {
                                        System.out.print("\nEnter the schedule no. you want to delete: ");
                                        scheduleNo = sc.nextInt();
                                        sc.nextLine();

                                        if (scheduleNo > 0 && scheduleNo <= schedules.size()) {
                                            error = false;
                                        } else {
                                            System.out.println("Your choice is not among the available options! PLease try again.");
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Please enter a valid schedule no.");
                                        sc.nextLine();
                                    }
                                } while (error);

                                Schedule deleteSchedule = new Schedule(schedules.get(scheduleNo - 1).getScheduleID(), schedules.get(scheduleNo - 1).getMovie(), schedules.get(scheduleNo - 1).getHall(), schedules.get(scheduleNo - 1).getShowDate(), schedules.get(scheduleNo - 1).getStartTime());

                                do {
                                    System.out.println("\nDo you want to delete this schedule? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    confirmation = SystemClass.askForContinue(answer);
                                } while (confirmation.equals("Invalid"));

                                // Confirm that the schedule is successfully deleted
                                boolean success;
                                do {
                                    if (confirmation.equals("Y")) {
                                        success = deleteSchedule.delete();
                                    } else {
                                        success = true;
                                        System.out.println("\nThe schedule is saved.");
                                    }

                                    if (success == false) {
                                        do {
                                            System.out.println("\nDo you want to retry to delete this schedule? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            confirmation = SystemClass.askForContinue(answer);

                                            if (confirmation.equals("Y")) {
                                                continues = true;
                                            } else {
                                                continues = false;
                                            }
                                        } while (confirmation.equals("Invalid"));
                                    }
                                    else {
                                        continues = false;
                                    }
                                } while (continues);

                                String continueDeleteSchedule;
                                do {
                                    System.out.println("\nDo you want delete another schedule? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    continueDeleteSchedule = SystemClass.askForContinue(answer);
                                } while (continueDeleteSchedule.equals("Invalid"));

                                if (continueDeleteSchedule.equals("Y")) {
                                    continues = true;
                                } else {
                                    continues = false;
                                }
                            } else {
                                continues = false;
                            }
                        } while (continues);
                    }
                    back = false;
                    break;
            }
        } while (back == false);
    }

    private static boolean managePromotion(Scanner sc) {
        boolean back = false;
        boolean error = false;

        do {
            int choice = displayMenu("Promotion", sc);

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    // View Promotion
                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    ArrayList<Promotion> filteredPromotions;

                    do {
                        error = false;

                        System.out.println("\nSelect the operation: ");
                        System.out.println("1. All Promotion");
                        System.out.println("2. Custom\n");
                        System.out.print("Enter your selection (0 - Back): ");

                        int filterChoice = sc.nextInt();
                        sc.nextLine();

                        switch (filterChoice) {
                            case 0:
                                back = true;
                                break;
                            case 1:
                                // No action
                                break;
                            case 2:
                                while (true) {
                                    try {
                                        System.out.print("\nEnter the start date (yyyy-MM-dd): ");
                                        String startDateStr = sc.nextLine();
                                        startDate = LocalDate.parse(startDateStr);
                                        break; // 日期有效，退出循环
                                    } catch (DateTimeParseException e) {
                                        System.out.println("Invalid date format. Please enter a valid date (yyyy-MM-dd).\n");
                                    }
                                }

                                while (true) {
                                    try {
                                        System.out.print("\nEnter the end date (yyyy-MM-dd): ");
                                        String endDateStr = sc.nextLine();
                                        endDate = LocalDate.parse(endDateStr);

                                        // 验证结束日期是否晚于开始日期
                                        if (endDate.isAfter(startDate)) {
                                            break; // 日期有效，退出循环
                                        } else {
                                            System.out.println("End date must be after start date. Please enter a valid date (yyyy-MM-dd).\n");
                                        }
                                    } catch (DateTimeParseException e) {
                                        System.out.println("Invalid date format. Please enter a valid date (yyyy-MM-dd).\n");
                                    }
                                }

                                break;
                            default:
                                System.out.println("Invalid input. Please retry.");
                                error = true;
                        }

                        if (back) {
                            // if no this code, it will directly back back back and no stop
                            back = false;
                            error = true;
                            break;

                        } else if (!error) {
                            while (true) {
                                filteredPromotions = Promotion.showFilteredPromotionList(startDate, endDate, 1);

                                if (filteredPromotions.size() > 0) {
                                    int detailsChoice;

                                    do {
                                        try {
                                            System.out.print("\nEnter the promotion no. to view the details (0 - Back): ");
                                            detailsChoice = sc.nextInt();
                                            sc.nextLine();

                                            if (detailsChoice == 0) {
                                                back = true;
                                                break;
                                            }

                                            else if (detailsChoice > 0 && detailsChoice <= filteredPromotions.size()) {
                                                Promotion viewPromotionDetails = filteredPromotions.get(detailsChoice - 1);
                                                System.out.println(viewPromotionDetails);
                                            }

                                            else {
                                                System.out.println("Your choice is not among the available options! PLease try again.");
                                                error = true;
                                            }
                                        }
                                        catch (InputMismatchException e) {
                                            System.out.println("Please enter a valid choice!");
                                            sc.nextLine();
                                            error = true;
                                        }
                                    } while (error);
                                }

                                else {
                                    System.out.println("\nNo record found!");
                                    error = true;
                                    break;
                                }

                                String ctnViewPromotion;

                                do {
                                    System.out.println("\nDo you want view another promotion? (Y / N)");
                                    System.out.print("Answer: ");
                                    String answer = sc.next();
                                    sc.nextLine();

                                    ctnViewPromotion = SystemClass.askForContinue(answer);
                                } while (ctnViewPromotion.equals("Invalid"));

                                if (!ctnViewPromotion.equals("Y") || back) {
                                    back = false;
                                    break;
                                }
                            }

                        }
                    } while (error);

                    break;

                case 2:
                    // Add Promotion
                    while (true) {
                        // Create promotion object
                        Promotion newPromotion = new Promotion();

                        System.out.println("\nPlease fill in all the following required information: ");

                        // Set promotion description
                        Promotion_Management.PromotionValidator.checkDescription(sc, newPromotion);

                        // Set promotion discount value
                        Promotion_Management.PromotionValidator.checkDiscountValue(sc, newPromotion);

                        // Set promotion minimum spend
                        Promotion_Management.PromotionValidator.checkMinSpend(sc, newPromotion);

                        // Set promotion per limit
                        Promotion_Management.PromotionValidator.checkPerLimit(sc, newPromotion);

                        // Set promotion start date
                        Promotion_Management.PromotionValidator.checkStartDate(sc, newPromotion);

                        // Set promotion end date
                        Promotion_Management.PromotionValidator.checkEndDate(sc, newPromotion, newPromotion.getStartDate());


                        // Set promotion publish count
                        Promotion_Management.PromotionValidator.checkPublishCount(sc, newPromotion);

                        String confirmAddPromotion;

                        while (true) {
                            do {
                                System.out.println("\nConfirm to add new Promotion? (Y / N)");
                                System.out.print("Answer: ");
                                String confirmAdd = sc.next();
                                sc.nextLine();

                                confirmAddPromotion = SystemClass.askForContinue(confirmAdd);
                            } while (confirmAddPromotion.equals("Invalid"));

                            if (confirmAddPromotion.equals("Y")) {
                                newPromotion.add();
                                break;
                            }
                        }

                        String ctnAddPromotion;

                        do {
                            System.out.println("\nDo you want to continue to add new Promotion? (Y / N)");
                            System.out.print("Answer: ");
                            String answer = sc.next();
                            sc.nextLine();

                            ctnAddPromotion = SystemClass.askForContinue(answer);
                        } while (ctnAddPromotion.equals("Invalid"));

                        if (ctnAddPromotion.equals("N")) {
                            error = true;
                            break;
                        }
                    }

                    break;

                case 3:
                    // Modify Promotion
                    int promotionId = 1;

                    while (true) {
                        System.out.println("\nSelect the promotion you want to modify: \n");
                        System.out.println("No     Promotion Description");
                        filteredPromotions = Promotion.showFilteredPromotionList(null, null, 1);

                        if (filteredPromotions == null) {
                            break;
                        }

                        do {
                            try {
                                System.out.print("\nEnter the promotion id (0 - Back): ");
                                promotionId = sc.nextInt();
                                sc.nextLine();

                                if (promotionId >= 0 && promotionId <= filteredPromotions.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid promotion id!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (promotionId == 0) {
                            break;
                        } else {
                            Promotion orgPromotion = filteredPromotions.get(promotionId - 1);
                            Promotion modifiedPromotion = new Promotion(orgPromotion.getPromotionId(), orgPromotion.getDescription(), orgPromotion.getDiscountValue(), orgPromotion.getMinSpend(), orgPromotion.getPerLimit(), orgPromotion.getStartDate(), orgPromotion.getEndDate(), orgPromotion.getPublishCount(), orgPromotion.getReceiveCount(), orgPromotion.getPromotionStatus());

                            do {
                                back = false;

                                System.out.println(modifiedPromotion);
                                System.out.println("\n(Note: Receive count can't be changed)");
                                System.out.print("\nEnter the serial number of the promotion information you want to change (0 - Stop): ");
                                String serialNo = sc.nextLine();

                                System.out.print("\n");

                                DateTime modifiedStartDate = modifiedPromotion.getStartDate();

                                switch (serialNo) {
                                    case "0":
                                        String save;

                                        do {
                                            System.out.println("\nDo you want to save the changes? (Y / N)");
                                            System.out.print("Answer: ");
                                            String answer = sc.next();
                                            sc.nextLine();

                                            save = SystemClass.askForContinue(answer);
                                        } while (save.equals("Invalid"));

                                        if (save.equals("Y")) {
                                            modifiedPromotion.modify();
                                        }

                                        else {
                                            modifiedPromotion = orgPromotion;
                                            System.out.println("\nThe changes have not been saved.");
                                        }

                                        back = true;

                                        break;
                                    case "1":
                                        // Promotion description
                                        Promotion_Management.PromotionValidator.checkDescription(sc, modifiedPromotion);
                                        break;
                                    case "2":
                                        // Promotion discount Value
                                        Promotion_Management.PromotionValidator.checkDiscountValue(sc, modifiedPromotion);
                                        break;
                                    case "3":
                                        // Promotion min. spend
                                        Promotion_Management.PromotionValidator.checkMinSpend(sc, modifiedPromotion);
                                        break;
                                    case "4":
                                        // Promotion per limit
                                        Promotion_Management.PromotionValidator.checkPerLimit(sc, modifiedPromotion);
                                        break;
                                    case "5":
                                        // Promotion start date
                                        Promotion_Management.PromotionValidator.checkStartDate(sc, modifiedPromotion);
                                        break;
                                    case "6":
                                        // Promotion end date
                                        Promotion_Management.PromotionValidator.checkEndDate(sc, modifiedPromotion, modifiedStartDate);
                                        break;
                                    case "7":
                                        // Promotion publish count
                                        Promotion_Management.PromotionValidator.checkPublishCount(sc, modifiedPromotion);
                                        break;
                                    default:
                                        System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } while (!back);
                        }
                    }

                    back = false;
                    error = true;

                    break;

                case 4:
                    // Delete Promotion
                    promotionId = 0;

                    do {
                        System.out.println("\nSelect the promotion you want to delete: ");
                        filteredPromotions = Promotion.showFilteredPromotionList(null, null, 1);

                        do {
                            try {
                                System.out.print("\nEnter the promotion id (0 - Back): ");
                                promotionId = sc.nextInt();
                                sc.nextLine();

                                if (promotionId >= 0 && promotionId <= filteredPromotions.size()) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid movie id!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (error);

                        if (promotionId != 0) {
                            Promotion promotion = filteredPromotions.get(promotionId - 1);
                            System.out.println(promotion);

                            String delete;
                            do {
                                System.out.println("\nDo you want to delete this promotion? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next();
                                sc.nextLine();

                                delete = SystemClass.askForContinue(answer);
                            } while (delete.equals("Invalid"));

                            if (delete.equals("Y")) {
                                promotion.delete();
                            }
                            else {
                                System.out.println("\nThe movie is saved.");
                            }
                            back = true;
                        } else {
                            back = false;
                        }
                    } while (back);

                    back = false;
                    error = true;

                    break;

                default:
                    System.out.println("Invalid input. Please retry.");
                    error = true;
            }

            if (back) {
                break;
            }
        } while (error);

        return back;
    }

    private static boolean customerPromotion(Scanner sc, int custId) {
        boolean back = false;
        boolean error = false;

        ArrayList<Promotion> validPromotions;

        do {
            System.out.println("\nSelect the operation: ");
            System.out.println("1. New promotion");
            System.out.println("2. My promotion");
            // System.out.println("3. Past promotion\n");
            System.out.print("\nEnter your selection (0 - Back): ");
            String operation = sc.nextLine();

            while (true) {
                switch (operation) {
                    case "0":
                        return false;

                    case "1":
                        int detailsChoice;

                        do {
                            error = false;

                            validPromotions = Promotion.showValidPromotionList(custId, sc);

                            if (validPromotions == null) {
                                System.out.println("\nOops! There is no other promotion you can claim now.\n");

                                pressEnterToBack();

                                back = true;
                                error = true;
                                break;
                            }

                            try {
                                System.out.print("\nEnter the promotion no. to view the details (0 - Back): ");
                                detailsChoice = sc.nextInt();
                                sc.nextLine();

                                if (detailsChoice == 0) {
                                    error = true;
                                    back = true;
                                }

                                else if (detailsChoice > 0 && detailsChoice <= validPromotions.size()) {
                                    Promotion promotion = validPromotions.get(detailsChoice - 1);
                                    System.out.println(promotion.viewNewPromotionDetails());

                                    String claim;

                                    do {
                                        System.out.println("Do you want to claim this promotion? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next().trim();
                                        sc.nextLine();

                                        claim = SystemClass.askForContinue(answer);
                                    } while (claim.equals("Invalid"));

                                    if (claim.equals("Y")) {
                                        for (int i = 0; i < promotion.getPerLimit(); i++) {
                                            promotion.custClaimedPromotion(custId);
                                        }

                                        promotion.updateReceiveCount();
                                    } else {
                                        back = false;
                                    }
                                }

                                else {
                                    System.out.println("\nYour choice is not among the available options! PLease try again.");
                                    error = true;
                                }
                            }

                            catch (InputMismatchException e) {
                                System.out.println("\nPlease enter a valid choice!");
                                sc.nextLine();
                                error = true;
                            }
                        } while (!back);

                        break;
                    case "2":
                        do {
                            error = false;
                            back = false;

                            validPromotions = Promotion.showOwnPromotionList(custId);

                            if (validPromotions == null) {
                                System.out.println("\nOops! You don't have any promotion.\n");

                                pressEnterToBack();

                                back = true;
                                error = true;
                                break;
                            }

                            try {
                                System.out.print("\nEnter the promotion no. to view the details (0 - Back): ");
                                detailsChoice = sc.nextInt();
                                sc.nextLine();

                                if (detailsChoice == 0) {
                                    back = true;
                                    error = true;

                                    break;
                                }

                                else if (detailsChoice > 0 && detailsChoice <= validPromotions.size()) {
                                    Promotion promotion = validPromotions.get(detailsChoice - 1);
                                    System.out.println(promotion.viewOwnPromotionDetails(custId) + "\n");

                                    pressEnterToBack();
                                }

                                else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            }

                            catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                            }
                        } while (!back);

                        break;

                    default:
                        System.out.println("Invalid input. Please try again.");
                }

                if (back) {
                    back = false;

                    break;
                }
            }
        } while (!back);

        return true;
    }

    public void registration(Scanner sc) {

    }

    private static int displayMenu(String propertyName, Scanner sc){
        boolean error = true;
        int choice = 0;

        do {
            try {
                System.out.println("\nSelect the operation:");
                System.out.println("1. View " + propertyName);
                System.out.println("2. Add " + propertyName);
                System.out.println("3. Modify " + propertyName);
                System.out.println("4. Delete " + propertyName);
                System.out.print("\nEnter your selection (0 - Back): ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice >= 0 && choice <= 4) {
                    error = false;
                }
                else {
                    System.out.println("Your choice is not among the available options! PLease try again.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid choice!");
                sc.nextLine();
            }
        } while (error);

        return choice;
    }

    public static String askForContinue(String answer){
        answer = answer.toUpperCase();

        if (answer.equals("Y") || answer.equals("YES")) {
            return "Y";
        }
        else if (answer.equals("N") || answer.equals("NO")) {
            return "N";
        }
        else {
            System.out.println("Please enter Y / N.");
            return "Invalid";
        }
    }

    private static void pressEnterToBack() {
        System.out.print("Press Enter to back...");

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}