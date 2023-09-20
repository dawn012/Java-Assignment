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
import Payment_Management.*;
import Promotion_Management.*;
import Receipt_Management.Receipt;
import Report_Management.Report;
import Report_Management.SalesReport;
import Report_Management.TopMovieReport;
import Schedule_Management.Schedule;
import Booking_Management.Booking;
import Seat_Management.Seat;
import Security_Management.Customer;
import Ticket_Managemnet.Ticket;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SystemClass {
    private SystemClass(){
    }

    public static void run(Scanner sc) throws Exception {
        SystemClass system = new SystemClass();


        int choice = 0;
        boolean error = true, continues = true, back = false;

        do {
            do {
                try {
                    System.out.println("\nSelect the operation: ");
                    System.out.printf("------------------------------------------------------");
                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Operation", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 1, '|', "View Profile", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 2, '|', "View Movie", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 3, '|', "View Promotion", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 4, '|', "View Booking History", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 5, '|', "Search Movie", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 6, '|', "Log out", '|');
                    System.out.println("------------------------------------------------------");

                    System.out.print("\nEnter your selection: ");

                    choice = sc.nextInt();
                    sc.nextLine();

                    if (choice > 0 && choice <= 6) {
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
                                System.out.printf("------------------------------------------------------");
                                System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Time Period", '|');
                                System.out.println("------------------------------------------------------");
                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 1, '|', "Opening This Week", '|');
                                System.out.println("------------------------------------------------------");
                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 2, '|', "Opening This Month", '|');
                                System.out.println("------------------------------------------------------");
                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 3, '|', "Release Within 3 Months", '|');
                                System.out.println("------------------------------------------------------");
                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 4, '|', "Coming Soon", '|');
                                System.out.println("------------------------------------------------------");

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

                                System.out.print("\n---------------------\n");
                                System.out.println("| Opening This Week |");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(oneWeekAgo, currentDate, 1);
                                break;
                            case 2:
                                LocalDate oneMonthAgo = currentDate.minusMonths(1);

                                System.out.print("\n----------------------\n");
                                System.out.println("| Opening This Month |");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(oneMonthAgo, currentDate, 1);
                                break;
                            case 3:
                                LocalDate threeMonthAgo = currentDate.minusMonths(3);

                                System.out.printf("\n---------------------------\n");
                                System.out.println("| Release within 3 months |");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(threeMonthAgo, currentDate, 1);
                                break;
                            case 4:
                                LocalDate comingSoon = currentDate.plusDays(1);

                                System.out.print("\n---------------\n");
                                System.out.println("| Coming Soon |");
                                moviesAfterFiltered = MovieUtils.getMovieListAfterFiltered(comingSoon, null, 1);
                                break;
                        }

                        if (!moviesAfterFiltered.isEmpty()) {
                            System.out.print("-----------------------------------------------------");
                            System.out.printf("\n%-3c %-4s %c %-40s %c\n", '|', "No", '|', "Movie Name", '|');
                            System.out.println("-----------------------------------------------------");
                            for (int i = 0; i < moviesAfterFiltered.size(); i++) {
                                System.out.printf("%-3c %-4d %c %-40s %c\n", '|', (i + 1), '|', moviesAfterFiltered.get(i).getMvName().getName(), '|');
                                System.out.println("-----------------------------------------------------");
                            }
                        }
                        else {
                            System.out.println("-----------------------------------------------------");
                            System.out.printf("%-15c %-35s %c\n", '|', "Sorry, no movie found!", '|');
                            System.out.println("-----------------------------------------------------");
                            back = false;
                        }

                        if (periodSelected != 0 && !moviesAfterFiltered.isEmpty()) {
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
                                        boolean scheduleFound = false;
                                        Schedule schedule;

                                        do {
                                            schedule = new Schedule();
                                            schedule.setMovie(movie);

                                            // 1. Select the cinema
                                            int cinemaNo = 0;
                                            error = true;
                                            ArrayList<Cinema> cinemas = new ArrayList<>();
                                            do {
                                                try {
                                                    System.out.println("\nSelect the cinema you want to view the schedule:");
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
                                                System.out.println("\n------------------------------------------------------------------------------");
                                                System.out.printf("%-3c %-4s %c %-31s %-3c %-12s %-4c %-11s %c\n", '|', "No", '|', "Hall Name", '|', "Start Time", '|', "End Time", '|');
                                                System.out.println("------------------------------------------------------------------------------");
                                                for (int i = 0; i < halls.size(); i++) {
                                                    schedule.setHall(halls.get(i));
                                                    count = schedule.showHallAndTime(count, schedules);
                                                }

                                                if (count != 1) {
                                                    scheduleFound = true;

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
                                                            continues = false;
                                                        } else {
                                                            System.out.println("Your choice is not among the available options! PLease try again.");
                                                            error = true;
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please enter a valid schedule no!");
                                                        sc.nextLine();
                                                    }
                                                } else {
                                                    System.out.printf("| %50s %25c\n", "Sorry, no schedule found!", '|');
                                                    System.out.println("------------------------------------------------------------------------------");

                                                    error = false;
                                                    scheduleFound = false;

                                                    String confirmation;
                                                    do {
                                                        System.out.println("\nDo you want to search another schedule for this movie? (Y / N)");
                                                        System.out.print("Answer: ");
                                                        String answer = sc.nextLine();

                                                        confirmation = SystemClass.askForContinue(answer);
                                                    } while (confirmation.equals("Invalid"));

                                                    if (confirmation.equals("Y")) {
                                                        continues = true;
                                                    } else {
                                                        continues = false;
                                                    }
                                                }
                                            } while (error);
                                        } while (continues);

                                        if (scheduleFound == true) {
                                            // 4. Select the seat chin yong part
                                            Booking booking = new Booking();
                                            Customer c = new Customer();//暂时用
                                            c.setCustId(2);//暂时用
                                            booking.setCustomer(c);//暂时用
                                            String confirmStr = " ";
                                            boolean insert = false;
                                            while (confirmStr.equals(" ")) {
                                                if (booking.executeBooking(schedule)) {
                                                    do {
                                                        if (confirmStr.equals("R")) {
                                                            booking.executeBooking(schedule);
                                                        }

                                                        booking.printBookingDetail();

                                                        do {
                                                            try {
                                                                System.out.print("Confirm This Booking ? (Y=\"Yes\", R=\"Select Again\", N=\"Exit\") : ");
                                                                confirmStr = sc.next().toUpperCase();

                                                                if (confirmStr.equals("Y")) {
                                                                    if (!insert) {
                                                                        Booking.insertBooking(booking);

                                                                        for (Ticket t : booking.getTicketList()) {
                                                                            Ticket.insertTicket(t, booking);
                                                                        }

                                                                        insert = true;

                                                                    } else {
                                                                        // if already insert to the database so just to change the status from "cancelled" to "processing"😊
                                                                        booking.setBookingStatus("processing");

                                                                        for (Ticket t : booking.getTicketList()) {
                                                                            t.setTicketStatus(0);
                                                                            t.updateStatus();
                                                                        }

                                                                        booking.updateBooking();
                                                                    }

                                                                } else if (confirmStr.equals("N")) {
                                                                    if (insert) {
                                                                        booking.cancelBooking();
                                                                        insert = false;
                                                                    }
                                                                    //back = true;
                                                                    break;

                                                                } else if (confirmStr.equals("R")) {
                                                                    if (insert) {
                                                                        booking.cancelBooking();
                                                                        insert = false;
                                                                    }
                                                                }

                                                            } catch (Exception e) {
                                                                System.out.println("Something wrong...");
                                                                sc.nextLine();
                                                            }

                                                        } while (!confirmStr.equals("Y") && !confirmStr.equals("N") && !confirmStr.equals("R"));

                                                        if (confirmStr.equals("Y")) {
                                                            applyPromotion(sc, booking);

                                                            // Make Payment
                                                            Payment payment = makePayment(sc, booking);

                                                            if (payment != null) {
                                                                // Print Receipt
                                                                Receipt receipt = new Receipt(payment);

                                                                receipt.printReceipt();
                                                                receipt.add();

                                                                back = true;

                                                                confirmStr = "N";
                                                            } else {
                                                                confirmStr = "Y";
                                                            }
                                                        }

                                                    } while (!confirmStr.equals("N"));

                                                } while (error);
                                            }
                                        }
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
                            if (periodSelected == 0) {
                                break;
                            }
                        }
                    } while (back == false);
                    break;
                case 3:
                    Customer c1 = new Customer();////暂时用
                    c1.setCustId(2);//暂时用
                    back = customerPromotion(sc, c1);
                    break;
                case 4:
                    //View Booking History
                    Customer c =new Customer();////暂时用
                    c.setCustId(1);//暂时用
                    //Booking.viewBookingHistory(c);
                    boolean skip=false;
                    Customer customer = new Customer();
                    customer.setCustId(1);
                    int periodSelected = 0;
                    error = true;
                    ArrayList<Booking> bookingsAfterFiltered = new ArrayList<>();
                    int movieSelected = 0;
                    LocalDate currentDate = LocalDate.now();
                    do {
                        try {
                            System.out.println("\nSelect the time period: ");
                            System.out.printf("------------------------------------------------------");
                            System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Time Period", '|');
                            System.out.println("------------------------------------------------------");
                            System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 1, '|', "Booking History This Week", '|');
                            System.out.println("------------------------------------------------------");
                            System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 2, '|', "Booking History This Month", '|');
                            System.out.println("------------------------------------------------------");
                            System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 3, '|', "Booking History Within 3 Months", '|');
                            System.out.println("------------------------------------------------------");
                            System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 4, '|', "All The Booking History", '|');
                            System.out.println("------------------------------------------------------");

                            System.out.print("\nEnter your selection (0 - Back): ");

                            periodSelected = sc.nextInt();
                            sc.nextLine();

                            if (periodSelected >= 0 && periodSelected <= 3) {
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
                            skip = true;
                            break;
                        case 1:
                            LocalDate oneWeekAgo = currentDate.minusWeeks(1);
                            System.out.printf("\n------------------------------\n");
                            System.out.print("| Booking History This Week   |");
                            System.out.printf("\n------------------------------\n");


                            bookingsAfterFiltered = Booking.getBookingListAfterFiltered(oneWeekAgo, currentDate, customer.getCustId());
                            break;
                        case 2:
                            LocalDate oneMonthAgo = currentDate.minusMonths(1);
                            System.out.printf("\n------------------------------\n");
                            System.out.print("| Booking History This months |");
                            System.out.printf("\n------------------------------\n");

                            bookingsAfterFiltered = Booking.getBookingListAfterFiltered(oneMonthAgo, currentDate, customer.getCustId());
                            break;
                        case 3:
                            LocalDate threeMonthAgo = currentDate.minusMonths(3);

                            System.out.printf("\n---------------------------------\n");
                            System.out.print("| Booking History within 3 months |");
                            System.out.printf("\n---------------------------------\n");
                            bookingsAfterFiltered = Booking.getBookingListAfterFiltered(threeMonthAgo, currentDate, customer.getCustId());
                            break;
                        case 4:
                            System.out.printf("\n---------------------------------\n");
                            System.out.print("|        Booking History         |");
                            System.out.printf("\n---------------------------------\n");
                            bookingsAfterFiltered=Booking.getBookingList(customer.getCustId());
                            break;
                    }
                    if(!skip) {
                        ArrayList<Booking> bookingList = bookingsAfterFiltered;
                        Collections.reverse(bookingList);
                        int count = 1;
                        System.out.println("Booking History : ");
                        for (Booking b : bookingList) {
                            b.loadingTicketList();
                            System.out.printf("%2d. Booking id:%2d\t", count, b.getBookingId());
                            System.out.print("Date:" + b.getBookingDateTime().getDate().toString());
                            System.out.print("\t\tTime : " + b.getBookingTime().truncatedTo(ChronoUnit.SECONDS) + "\n");
                            if (count % 5 == 0) {
                                String answer = " ";
                                do {
                                    System.out.print("Continue Show More History? (Y=YES N=NO) : ");
                                    answer = sc.next().toUpperCase();

                                } while (SystemClass.askForContinue(answer).equals("Invalid"));
                                if (SystemClass.askForContinue(answer).equals("N"))
                                    break;
                            }
                            count++;
                        }
                        int no = 0;
                        do {
                            try {
                                System.out.print("\nEnter No. Booking to Show Detail (0 = Back) : ");
                                no = sc.nextInt();
                                if (no == 0)
                                    break;
                                else if (no <= count && no > 0) {
                                    //System.out.println("Invalid Input...");
                                    bookingList.get(no - 1).printBookingDetail();
                                }else {
                                    System.out.println("Invalid Input...");
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid Input...");
                                sc.nextLine();
                            }
                        } while (no != 0);
                    }


                    break;
                case 5:
                    do {
                        error = true;
                        int searchingMethod = 0;
                        continues = true;
                        ArrayList<Movie> searchResults;
                        String confirmation;

                        do {
                            try {
                                System.out.println("\nSelect the searching method:");
                                System.out.printf("------------------------------------------------------");
                                System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Searching Method", '|');
                                System.out.println("------------------------------------------------------");
                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 1, '|', "Search by movie name", '|');
                                System.out.println("------------------------------------------------------");
                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 2, '|', "Search by genre", '|');
                                System.out.println("------------------------------------------------------");

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
                                        searchResults = MovieUtils.queryMovies(searchedMvName, null);

                                        error = true;
                                        System.out.println("\nSearch Results: ");
                                        System.out.printf("------------------------------------------------------");
                                        System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Movie Name", '|');
                                        System.out.println("------------------------------------------------------");

                                        if (!searchResults.isEmpty()) {
                                            for (int i = 0; i < searchResults.size(); i++) {
                                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', (i + 1), '|', searchResults.get(i).getMvName().getName(), '|');
                                                System.out.println("------------------------------------------------------");
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
                                            System.out.printf("%-15c %-36s %c\n", '|', "Sorry, no movie found!", '|');
                                            System.out.println("------------------------------------------------------");
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

                                    System.out.println("\nSelect the genre you want to view the movie:");
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
                                        searchResults = MovieUtils.queryMovies(null, viewGenre);

                                        System.out.println("\nSearch Results: ");
                                        System.out.printf("------------------------------------------------------");
                                        System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Movie Name", '|');
                                        System.out.println("------------------------------------------------------");

                                        if (!searchResults.isEmpty()) {
                                            for (int i = 0; i < searchResults.size(); i++) {
                                                System.out.printf("%-3c %-4d %c %-41s %c\n", '|', (i + 1), '|', searchResults.get(i).getMvName().getName(), '|');
                                                System.out.println("------------------------------------------------------");
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
                                            System.out.printf("%-15c %-36s %c\n", '|', "Sorry, no movie found!", '|');
                                            System.out.println("------------------------------------------------------");
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
                case 6:
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
                    System.out.printf("------------------------------------------------------");
                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Operation", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 1, '|', "Manage Cinema", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 2, '|', "Manage Hall", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 3, '|', "Manage Movie", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 4, '|', "Manage Genre", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 5, '|', "Manage Schedule", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 6, '|', "Manage Promotion", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 7, '|', "View Report", '|');
                    System.out.println("------------------------------------------------------");
                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', 8, '|', "Log Out", '|');
                    System.out.println("------------------------------------------------------");

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
                    viewReport(sc);
                    break;
                case 8:
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
                                System.out.println("\nSelect the cinema you want to view: ");
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
                                result = DatabaseUtils.selectQuery("cinema_name", "cinema", null, null);
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
                                    System.out.printf("------------------------------------------------------");
                                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "State", '|');
                                    System.out.println("------------------------------------------------------");
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
                                    System.out.printf("------------------------------------------------------");
                                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "City", '|');
                                    System.out.println("------------------------------------------------------");
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
                                    System.out.println("\nSelect the postcode: ");
                                    System.out.printf("------------------------------------------------------");
                                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Postcode", '|');
                                    System.out.println("------------------------------------------------------");
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
                                result = DatabaseUtils.selectQuery("cinema_address", "cinema", null, null);
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
                                    if (newCinema.isPhoneNumberUnique()) {
                                        error = false;
                                    }
                                    else {
                                        System.out.println("The phone number is same as other cinema phone number.");
                                        error = true;
                                    }
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
                                                result = DatabaseUtils.selectQuery("cinema_name", "cinema", null, null);
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
                                                    System.out.printf("------------------------------------------------------");
                                                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "State", '|');
                                                    System.out.println("------------------------------------------------------");
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
                                                    System.out.printf("------------------------------------------------------");
                                                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "City", '|');
                                                    System.out.println("------------------------------------------------------");
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
                                                    System.out.println("\nSelect the postcode: ");
                                                    System.out.printf("------------------------------------------------------");
                                                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Postcode", '|');
                                                    System.out.println("------------------------------------------------------");
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
                                                result = DatabaseUtils.selectQuery("cinema_address", "cinema", null, null);
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
                                                    if (cinema.isPhoneNumberUnique()) {
                                                        error = false;
                                                    }
                                                    else {
                                                        System.out.println("The phone number is same as other cinema phone number.");
                                                        error = true;
                                                    }
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
        ArrayList<Cinema> cinemas = new ArrayList<>();
        int cinemaSelected = 0;

        do {
            try {
                System.out.println("\nSelect the cinema you want to manage it's hall: ");
                cinemas = Cinema.viewCinemaList(1);
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

                                System.out.print("------------------------------------------------------");
                                System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Hall Name", '|');
                                System.out.println("------------------------------------------------------");
                                for (int i = 0; i < halls.size(); i++) {
                                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', (i + 1), '|', halls.get(i).getHallName().getName(), '|');
                                    System.out.println("------------------------------------------------------");
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
                                System.out.println("\nDo you want view another hall? (Y / N)");
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
                                    result = DatabaseUtils.selectQuery("hall_name", "hall", "cinema_id = ?", params);
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

                                System.out.print("------------------------------------------------------");
                                System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Hall Name", '|');
                                System.out.println("------------------------------------------------------");
                                for (int i = 0; i < hallsModified.size(); i++) {
                                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', (i + 1), '|', hallsModified.get(i).getHallName().getName(), '|');
                                    System.out.println("------------------------------------------------------");
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
                                                result = DatabaseUtils.selectQuery("hall_name", "hall", "cinema_id = ?", params);
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
                                        break;
                                    case 3:
                                        System.out.println("Hall capacity cannot be modified! Please retry.");
                                        break;
                                    case 4: //ChinYong Part
                                        hallsModified.get(hallModified-1).initSeatList();
                                        String strRow=" ";
                                        int col = 0;
                                        String row =" ";
                                        Scanner scanner=new Scanner(System.in);
                                        boolean validInput = false;
                                        String strCon="Y";
                                        char chCon = strCon.charAt(0);
                                        while (chCon=='Y') {
                                            hallsModified.get(hallModified-1).viewSeatStatus();
                                            while (!validInput) {
                                                try {
                                                    System.out.print("\nSelect Row    : ");

                                                    row=scanner.next().toUpperCase();

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
                                            String combineSeatId = letter2 + row.charAt(0) + Integer.toString(col);

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
                                                    if (seats.getSeatId().equals(combineSeatId)) {
                                                        seats.setSeatStatus(seatStatus);
                                                        seats.updateSeatStatus();
                                                    }
                                                }
                                            }
                                            do {
                                                System.out.print("Continue ? (Y=Yes N=No) : ");
                                                strCon = scanner.next().toUpperCase();
                                                chCon = strCon.charAt(0);
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

                                System.out.print("------------------------------------------------------");
                                System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Hall Name", '|');
                                System.out.println("------------------------------------------------------");
                                for (int i = 0; i < hallsDeleted.size(); i++) {
                                    System.out.printf("%-3c %-4d %c %-41s %c\n", '|', (i + 1), '|', hallsDeleted.get(i).getHallName().getName(), '|');
                                    System.out.println("------------------------------------------------------");
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

                        System.out.println("\nSelect the movie you want to view:");
                        System.out.print("-----------------------------------------------------");
                        System.out.printf("\n%-3c %-4s %c %-40s %c\n", '|', "No", '|', "Movie Name", '|');
                        System.out.println("-----------------------------------------------------");

                        if (!moviesAfterFiltered.isEmpty()) {
                            for (int i = 0; i < moviesAfterFiltered.size(); i++) {
                                System.out.printf("%-3c %-4d %c %-40s %c\n", '|', (i + 1), '|', moviesAfterFiltered.get(i).getMvName().getName(), '|');
                                System.out.println("-----------------------------------------------------");
                            }
                        }
                        else {
                            System.out.println("-----------------------------------------------------");
                            System.out.printf("%-15c %-35s %c\n", '|', "Sorry, no movie found!", '|');
                            System.out.println("-----------------------------------------------------");
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
                                result = DatabaseUtils.selectQuery("mv_name", "movie", null, null);
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
                                ResultSet result = DatabaseUtils.selectQuery("genre_id, genre_name", "genre", "genre_status = ?", params);

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
                                                result = DatabaseUtils.selectQuery("mv_name", "movie", null, null);
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
                                                ResultSet result = DatabaseUtils.selectQuery("genre_id, genre_name", "genre", "genre_status = ?", params);

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
                                result = DatabaseUtils.selectQuery("genre_name", "genre", null, null);
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
                            Genre genre = new Genre(orgGenre.getGenreID(), orgGenre.getGenreName(), orgGenre.getPost());
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
                                        result = DatabaseUtils.selectQuery("genre_name", "genre", null, null);
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

                                    System.out.print("------------------------------------------------------");
                                    System.out.printf("\n%-3c %-4s %c %-41s %c\n", '|', "No", '|', "Hall Name", '|');
                                    System.out.println("------------------------------------------------------");
                                    for (int i = 0; i < halls.size(); i++) {
                                        System.out.printf("%-3c %-4d %c %-41s %c\n", '|', (i + 1), '|', halls.get(i).getHallName().getName(), '|');
                                        System.out.println("------------------------------------------------------");
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
                            System.out.println("\nSelect the cinema you want to view the schedule:");
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
                                    System.out.printf("---------------------------------------------------------------");
                                    System.out.printf("\n%-3c %-4s %c %-50s %c\n", '|', "No", '|', "Operation", '|');
                                    System.out.println("---------------------------------------------------------------");
                                    System.out.printf("%-3c %-4d %c %-50s %c\n", '|', 1, '|', "Modify the movie show time", '|');
                                    System.out.println("---------------------------------------------------------------");
                                    System.out.printf("%-3c %-4d %c %-50s %c\n", '|', 2, '|', "Modify the movie show date", '|');
                                    System.out.println("---------------------------------------------------------------");
                                    System.out.printf("%-3c %-4d %c %-50s %c\n", '|', 3, '|', "Modify the movie to be played", '|');
                                    System.out.println("---------------------------------------------------------------");
                                    System.out.printf("%-3c %-4d %c %-50s %c\n", '|', 4, '|', "Modify the location of the movie to be played", '|');
                                    System.out.println("---------------------------------------------------------------");
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
                                    System.out.println("\nThis schedule will not be modified.");
                                }

                                if (success == false) {
                                    do {
                                        System.out.println("\nDo you want to retry to modify the schedule? (Y / N)");
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
                            System.out.println("\nSelect the cinema you want to view the schedule:");
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

    public static void viewReport(Scanner sc) {
        boolean back = false;

        do {
            int choice = 0;
            boolean error = true;
            boolean continues = true;

            do {
                try {
                    System.out.println("\nSelect the report you want to view: ");
                    System.out.println("1. Sales Report");
                    System.out.println("2. Movie Box Office Report");
                    System.out.print("\nEnter your selection (0 - Back): ");
                    choice = sc.nextInt();

                    if (choice >= 0 && choice <= 2) {
                        error = false;
                    } else {
                        System.out.println("Your choice is not among the available options! PLease try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid choice!");
                    sc.nextLine();
                }
            } while (error);

            int selection = 0;
            error = true;
            LocalDate currentDate = LocalDate.now();
            Report report = null;
            SalesReport salesReport = new SalesReport();

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    do {
                        do {
                            try {
                                System.out.println("\nPlease select a sales report from the list below: ");
                                System.out.println("1. Daily");
                                System.out.println("2. Monthly");
                                System.out.print("\nEnter your selection (0 - Back): ");
                                selection = sc.nextInt();
                                sc.nextLine();

                                if (selection >= 0 && selection <= 4) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                            }
                        } while (error);

                        switch (selection) {
                            case 0:
                                continues = false;
                                back = false;
                                break;
                            case 1:
                                salesReport.setTitle("Daily Sales Report");
                                report = viewSalesReport(sc, salesReport);
                                break;
                            case 2:
                                salesReport.setTitle("Monthly Sales Report");
                                report = viewSalesReport(sc, salesReport);
                                break;
                            default:
                                System.out.println("Your choice is not among the available options! PLease try again.");
                        }

                        if (selection != 0) {
                            if (!(report == null)) {
                                System.out.println(report);

                                String confirmation;

                                do {
                                    System.out.println("\nDo you want to view another report? (Y / N)");
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
                                continues = true;
                                System.out.println("Sorry, no record found. Report can't be generated!");
                            }
                        }
                    } while (continues);

                    break;
                case 2:
                    do {
                        ArrayList<Report> reports = new ArrayList<>();

                        do {
                            try {
                                System.out.println("\nPlease select a box office ranking report from the list below: ");
                                System.out.println("1. Daily");
                                System.out.println("2. Weekly");
                                System.out.println("3. Monthly");
                                System.out.println("4. Yearly");
                                System.out.print("\nEnter your selection (0 - Back): ");
                                selection = sc.nextInt();
                                sc.nextLine();

                                if (selection >= 0 && selection <= 4) {
                                    error = false;
                                } else {
                                    System.out.println("Your choice is not among the available options! PLease try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid choice!");
                                sc.nextLine();
                            }
                        } while (error);

                        ArrayList<Movie> movies = MovieUtils.getMovieListAfterFiltered(null, null, 1);
                        Report reportAfterRanking = new TopMovieReport();
                        report = new TopMovieReport();
                        String title = null;

                        switch (selection) {
                            case 0:
                                continues = false;
                                back = false;
                                break;
                            case 1:
                                LocalDate today = currentDate;
                                ((TopMovieReport) report).setReportValue(today, movies);
                                title = "Daily Box Office Ranking Report";
                                break;
                            case 2:
                                LocalDate oneWeekAgo = currentDate.minusWeeks(1);
                                ((TopMovieReport) report).setReportValue(oneWeekAgo, movies);
                                title = "Weekly Box Office Ranking Report";
                                break;
                            case 3:
                                LocalDate oneMonthAgo = currentDate.minusMonths(1);
                                ((TopMovieReport) report).setReportValue(oneMonthAgo, movies);
                                title = "Monthly Box Office Ranking Report";
                                break;
                            case 4:
                                LocalDate oneYearAgo = currentDate.minusYears(1);
                                ((TopMovieReport) report).setReportValue(oneYearAgo, movies);
                                title = "Yearly Box Office Ranking Report";
                                break;
                        }

                        if (selection != 0) {
                            reportAfterRanking = TopMovieReport.getRanking(report);
                            reportAfterRanking.setTitle(title);

                            if (!((TopMovieReport) reportAfterRanking).getMovie().isEmpty()) {
                                System.out.println(((TopMovieReport) reportAfterRanking).toString());

                                String confirmation;
                                do {
                                    System.out.println("\nDo you want to view another report? (Y / N)");
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
                                continues = true;
                                System.out.println("Sorry, no report found!");
                            }
                        }
                    } while (continues);
                    break;
            }
        } while (back == false);
    }

    public static SalesReport viewSalesReport(Scanner sc, SalesReport salesReport) {
        String viewDate;
        DateTime searchDate = null;
        boolean error;

        do {
            error = true;

            if (salesReport.getTitle().contains("Daily")) {
                System.out.print("\nEnter the date (yyyy-mm-dd): ");
                viewDate = sc.nextLine().trim();

                int[] dateParts = DateTime.dateFormatValidator(viewDate, "^\\d{4}-\\d{2}-\\d{2}$");

                if (dateParts == null) {
                    continue;
                }

                searchDate = new DateTime(dateParts[0], dateParts[1], dateParts[2]);

                if (searchDate.isValidDate()) {
                    if (!(searchDate.getDate().equals(LocalDate.now()) || searchDate.getDate().isAfter(LocalDate.now()))) {
                        // Check the report generated is before today
                        salesReport.setReportDate(searchDate);

                        return salesReport.calcSalesReportInfo();
                    }

                    else {
                        System.out.println("The date you enter must be at least before today.\n");
                    }
                }

                else {
                    System.out.println("Please enter valid date range.\n");
                }
            }

            else {
                System.out.print("\nEnter the year and month (yyyy-mm): ");
                viewDate = sc.nextLine().trim();

                int[] dateParts = DateTime.dateFormatValidator(viewDate, "^\\d{4}-\\d{2}$");

                if (dateParts == null) {
                    continue;
                }

                searchDate = new DateTime(dateParts[0], dateParts[1], 1);

                if (searchDate.isValidDate()) {
                    if (searchDate.getDate().isBefore(LocalDate.now().minusMonths(1))) {
                        // Check the report generated is before today
                        salesReport.setReportDate(searchDate);

                        return salesReport.calcSalesReportInfo();
                    }

                    else {
                        System.out.println("The year and month you enter must ne at least before this month.\n");
                    }
                }

                else {
                    System.out.println("Please enter valid date range.\n");
                }
            }
        } while (error);


        return null;
    }


//    private static Report generateReport(Report report, Scanner sc) {
//        System.out.print("\n Please write the report purpose (0 - Use default purpose): ");
//        String purpose = sc.nextLine().trim();
//
//        if (purpose.equals("0")) {
//            purpose = report.getDefaultPurpose();
//        }
//
//        if (report instanceof SalesReport) {
//            SalesReport salesReport = (SalesReport) report;
//            salesReport.setPurpose(purpose);
//
//            return new SalesReport(salesReport.getTitle(), salesReport.getDefaultPurpose(), salesReport.getConclusion(), salesReport.getSalesDate(), salesReport.getTotalSales(), salesReport.getTotalOrders(), salesReport.getMostPaymentMtd());
//        } else {
//            TopMovieReport topMovieReport = (TopMovieReport) report;
//
//            return new TopMovieReport();
//        }
//
//        return null;
//    }

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
                                filteredPromotions = PromotionUtils.filteredPromotionList(startDate, endDate, 1);

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
                        filteredPromotions = PromotionUtils.filteredPromotionList(null, null, 1);

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
                        filteredPromotions = PromotionUtils.filteredPromotionList(null, null, 1);

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

    private static boolean customerPromotion(Scanner sc, Customer cust) {
        boolean back = false;
        boolean error = false;

        ArrayList<Promotion> validPromotions;
        Promotion promotion = new Promotion();
        promotion.setCustomer(cust);

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

                            validPromotions = promotion.validPromotionList();

                            if (validPromotions.isEmpty()) {
                                System.out.println("\nOops! There is no other promotion you can claim now.\n");

                                pressEnterToBack();

                                back = true;
                                break;
                            }

                            int count = 0;

                            System.out.println("\nThese are the promotion you can get now: ");

                            for(Promotion details: validPromotions) {
                                count++;
                                System.out.printf("%d. %s\n", count, details.getDescription());
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
                                    promotion = validPromotions.get(detailsChoice - 1);
                                    promotion.setCustomer(cust);
                                    System.out.println(promotion.viewNewPromotionDetails());

                                    String claim;

                                    do {
                                        System.out.println("\nDo you want to claim this promotion? (Y / N)");
                                        System.out.print("Answer: ");
                                        String answer = sc.next().trim();
                                        sc.nextLine();

                                        claim = SystemClass.askForContinue(answer);
                                    } while (claim.equals("Invalid"));

                                    if (claim.equals("Y")) {
                                        for (int i = 0; i < promotion.getPerLimit(); i++) {
                                            promotion.custClaimedPromotion();
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

                            ArrayList<Promotion> ownPromotions = promotion.ownPromotionList();
                            validPromotions = new ArrayList<>();

                            if (ownPromotions.isEmpty()) {
                                System.out.println("\nOops! You don't have any promotion.\n");

                                pressEnterToBack();

                                back = true;
                                break;
                            }

                            int found = 0;

                            // To check whether the promotion is deleted by admin or not
                            for(Promotion valid: ownPromotions) {
                                if (valid.getPromotionStatus() == 1) {
                                    validPromotions.add(valid);
                                    found++;
                                }
                            }

                            if (found == 0) {
                                System.out.println("\nOops! You don't have any promotion.\n");

                                pressEnterToBack();

                                back = true;
                                break;
                            }

                            System.out.println("\nYour promotion: ");

                            int count = 0;

                            for(Promotion details: validPromotions) {
                                if (details.getPromotionStatus() == 1) {
                                    count++;
                                    System.out.printf("%d. %s\n", count, details.getDescription());
                                }
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
                                    promotion = validPromotions.get(detailsChoice - 1);
                                    promotion.setCustomer(cust);
                                    System.out.println(promotion.viewOwnPromotionDetails() + "\n");

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

    private static boolean applyPromotion(Scanner sc, Booking booking) {
        Promotion promotion = new Promotion();
        promotion.setCustomer(booking.getCustomer());
        String apply;

        do {
            System.out.println("\nDo you want to apply promotion code? (Y/N) : ");
            System.out.print("Answer: ");
            String answer = sc.next().trim();
            sc.nextLine();

            apply = SystemClass.askForContinue(answer);
        } while (apply.equals("Invalid"));


        if (apply.equals("Y")) {
            ArrayList<Promotion> ownPromotions = promotion.ownPromotionList();
            ArrayList<Promotion> validPromotions = new ArrayList<>();
            Promotion bestPromotion = null; // 用于跟踪最佳促销
            double maxDiscount = 0;
            int i = 0;
            int record = 0;

            if (!ownPromotions.isEmpty()) {
                for (Promotion ownPromotion : ownPromotions) {
                    if (ownPromotion.getPromotionStatus() == 1) {
                        if (ownPromotion.getMinSpend() <= booking.getTotalPrice()) {
                            // Compare promotion min spend with booking total price to find out the valid promotion
                            validPromotions.add(ownPromotion);

                            if (ownPromotion.getDiscountValue() > maxDiscount) {
                                maxDiscount = ownPromotion.getDiscountValue();
                                bestPromotion = ownPromotion; // 更新最佳促销
                                record = i; // Record the position of best promotion in validPromotions
                            }

                            i++;
                        }
                    }
                }
            }

            if (bestPromotion != null) {
                Promotion firstValidPromotion = validPromotions.get(0);
                validPromotions.set(0, bestPromotion);
                validPromotions.set(record, firstValidPromotion);
            }


            if (validPromotions.isEmpty()) {
                System.out.println("\nOops! You don't have any applicable promotion.\n");

                pressEnterToContinue();

                return false;
            }


            System.out.println("\nValid Promotion: ");
            i = 1;

            for (Promotion validPromotion : validPromotions) {
                if (i == 1) {
                    System.out.printf("%d. %s (Most Prefer)\n", i, validPromotion.getDescription());
                } else {
                    System.out.printf("%d. %s\n", i, validPromotion.getDescription());
                }

                i++;
            }

            boolean ctn = false;

            do {
                try {
                    System.out.println("\nEnter the promotion no. to apply the promotion (0 - Continue with no promotion): ");
                    int applyChoice = sc.nextInt();
                    sc.nextLine();

                    if (applyChoice == 0) {
                        ctn = true;
                    }

                    else if (applyChoice > 0 && applyChoice <= validPromotions.size()) {
                        booking.setPromotion(validPromotions.get(applyChoice - 1));

                        return true;
                    }

                    else {
                        System.out.println("\nYour choice is not among the available options! PLease try again.");
                    }
                }

                catch (InputMismatchException e) {
                    System.out.println("\nPlease enter a valid choice!");
                    sc.nextLine();
                }
            } while (!ctn);
        }

        return false;
    }

    private static Payment makePayment(Scanner sc, Booking booking) {
        // Remain original amount
        double remainAmount = booking.getTotalPrice();

        // Deduct discount value
        if (booking.getPromotion() != null) {
            booking.setTotalPrice(booking.getTotalPrice() - booking.getPromotion().getDiscountValue());
        }

        // Payment payment;
        Payment payment;
        Payment validPayment = null;

        String paymentMethod;
        boolean back;
        boolean successPayment = false;

        do {
            if (booking.getPromotion() != null) {
                booking.printBookingDetail();
            }

            System.out.println("\nPayment Method: ");
            System.out.println("1. Credit/Debit Card");
            System.out.println("2. Touch 'n Go");
            System.out.print("\nSelect your payment method (0 - Back): ");

            paymentMethod = sc.nextLine().trim();

            do {
                back = false;

                switch (paymentMethod) {
                    case "0":
                        return null;

                    case "1":
                        // Process Credit/Debit Card Payment
                        while (true) {
                            payment = cardPaymentInfo(sc);

                            validPayment = validPayment(payment, booking);

                            if(validPayment != null) {
                                back = true;
                                successPayment = true;
                                break;
                            }

                            String changePaymentMtd;

                            do {
                                System.out.println("\nDo you want to change your payment method? (Y / N)");
                                System.out.print("Answer: ");
                                String answer = sc.next().trim();
                                sc.nextLine();

                                changePaymentMtd = SystemClass.askForContinue(answer);
                            } while (changePaymentMtd.equals("Invalid"));

                            if (changePaymentMtd.equals("Y")) {
                                back = true;
                                break;
                            }
                        }

                        break;
                    case "2":
                        // Process TNG Payment
                        payment = tngPaymentInfo(sc);

                        validPayment = validPayment(payment, booking);

                        back = true;
                        successPayment = true;

                        break;

                    default:
                        System.out.println("Invalid selection. Please retry.");
                        back = true;
                }

            } while (!back);

            if (back) {
                back = false;
            }

        } while (!back && !successPayment);

        String ctnMakePayment;
        String cancelPayment;

        do {
            validPayment.printPaymentDetail();

            do {
                System.out.println("\nContinue to make payment? (Y / N)");
                System.out.print("Answer: ");
                String answer = sc.next().trim();
                sc.nextLine();

                ctnMakePayment = SystemClass.askForContinue(answer);
            } while (ctnMakePayment.equals("Invalid"));

            if (ctnMakePayment.equals("Y")) {
                // Confirm to make payment
                if (validPayment != null) {
                    validPayment.addPayment();
                    validPayment.pay();

                    try {
                        // Update booking status
                        booking.completeBooking();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(booking.getPromotion() != null) {
                    // User use promotion code, update promotion code status
                    booking.getPromotion().custApplyPromotion(booking);
                }

                System.out.println("\nPayment Successfully! Thanks for your payment.");

                return validPayment;
            }

            else {
                do {
                    System.out.println("\nConfirm to cancel your payment? (Y / N)");
                    System.out.print("Answer: ");
                    String answer = sc.next().trim();
                    sc.nextLine();

                    cancelPayment = SystemClass.askForContinue(answer);
                } while (cancelPayment.equals("Invalid"));

                if (cancelPayment.equals("Y")) {
                    // If customer cancel payment, restore booking amount
                    booking.setTotalPrice(remainAmount);
                    booking.setPromotion(null);

                    try {
                        // Cancel booking
                        booking.cancelBooking();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } while (cancelPayment.equals("N"));

        return null;
    }

    private static Payment validPayment(Payment payment, Booking booking) {
        DateTime dateTime = new DateTime();

        if (payment instanceof Card) {
            Card card = (Card) payment;
            card.setPaymentAmount(booking.getTotalPrice());

            if(CardValidator.stripeValidator(card.createPaymentIntent())) {
                return new Card(booking, "CREDIT/DEBIT CARD", booking.getTotalPrice(), "MYR", "PAID", card.getCardNo(), card.getExpiredDate(), card.getCvc(), card.getEmail());
            }

        } else {
            TNG tng = (TNG) payment;

            return new TNG(booking, "TNG", booking.getTotalPrice(), "MYR", "PAID", tng.getPhoneNo(), tng.getPinNo());
        }

        return null;
    }

    private static Card cardPaymentInfo(Scanner input) {
        Card card = new Card();

        // Card Number
        String cardNo;

        while (true) {
            System.out.print("\nEnter card number: ");
            cardNo = input.nextLine().trim();

            if (CardValidator.cardNoFormatValidator(cardNo)) {
                break;
            }
        }

        card.setCardNo(cardNo);

        // Expired Date
        String expiredDate;

        while (true){
            System.out.print("\nEnter expired date (mm/yy): ");
            expiredDate = input.nextLine().trim();

            if (CardValidator.expiredDateFormatValidator(expiredDate)) {
                break;
            }
        }

        card.setExpiredDate(expiredDate);

        // Cvc
        String cvc;

        while (true){
            System.out.print("\nEnter Cvc: ");
            cvc = input.nextLine().trim();

            if (CardValidator.cvcFormatValidator(cvc)) {
                break;
            }
        }

        card.setCvc(cvc);

        System.out.print("\nEnter email: ");
        String email = input.nextLine().trim();

        card.setEmail(email);

        return card;
    }

    private static TNG tngPaymentInfo(Scanner input) {
        TNG tng = new TNG();

        while (true) {
            System.out.print("\nEnter your phone number (10-12 digits: 01xxxxxxxx) : ");
            String phoneNo = input.nextLine().trim();

            if (TNGValidator.phoneNoValidator(phoneNo)) {
                // While phone number is valid, set phoneNo
                tng.setPhoneNo(phoneNo);

                break;
            }
        }

        while (true) {
            System.out.print("\nEnter your pin number (6-digits): ");
            String pinNo = input.nextLine().trim();

            if (TNGValidator.pinNoValidator(pinNo)) {
                // While pin code is valid, set PIN
                tng.setPinNo(pinNo);
                break;
            }
        }

        return tng;
    }

    private static int displayMenu(String propertyName, Scanner sc){
        boolean error = true;
        int choice = 0;

        do {
            try {
                System.out.println("\nSelect the operation:");
                System.out.println("---------------------------------");
                System.out.printf("%c %d %c %-25s %c\n", '|', 1, '|', "View " + propertyName, '|');
                System.out.println("---------------------------------");
                System.out.printf("%c %d %c %-25s %c\n", '|', 2, '|', "Add " + propertyName, '|');
                System.out.println("---------------------------------");
                System.out.printf("%c %d %c %-25s %c\n", '|', 3, '|', "Modify " + propertyName, '|');
                System.out.println("---------------------------------");
                System.out.printf("%c %d %c %-25s %c\n", '|', 4, '|', "Delete " + propertyName, '|');
                System.out.println("---------------------------------");
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

    private static void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}