package Promotion_Management;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import Driver.DateTime;

public class PromotionValidator {
    private PromotionValidator() {
    }

    // Validate promotion description
    public static void checkDescription(Scanner sc, Promotion promotion) {
        while (true) {
            System.out.print("Description: ");
            String description = sc.nextLine().trim();

            if (!description.isEmpty()) {
                promotion.setDescription(description);
                break;
            }

            System.out.println("Your description can't be empty. Please enter again.\n");
        }
    }


    // Validate promotion discount value
    public static void checkDiscountValue(Scanner sc, Promotion promotion) {
        while (true) {
            try {
                System.out.print("Discount value: ");
                double newDiscountValue = sc.nextDouble();
                sc.nextLine();

                if (newDiscountValue > 0 && newDiscountValue <= 20) {
                    promotion.setDiscountValue(newDiscountValue);
                    break;
                } else if (newDiscountValue <= 0) {
                    System.out.println("Your discount value must bigger than RM 0. Please enter again.\n");
                } else {
                    System.out.println("Your discount value can't bigger than RM 20. Please enter again.\n");
                }

            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.\n");
                sc.nextLine();
            }
        }
    }

    // Validate promotion minimum spend
    public static void checkMinSpend(Scanner sc, Promotion promotion) {
        while (true) {
            try {
                System.out.print("Min. Spend: ");
                double newMinSpend = sc.nextDouble();
                sc.nextLine();

                if (newMinSpend >= 0 && newMinSpend <= 200) {
                    promotion.setMinSpend(newMinSpend);
                    break;
                } else if (newMinSpend < 0) {
                    System.out.println("Your minimum spend must bigger than RM 0. Please enter again.\n");
                } else {
                    System.out.println("Your minimum spend can't bigger than RM 200. Please enter again.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.\n");
                sc.nextLine();
            }
        }
    }

    // Validate promotion per limit
    public static void checkPerLimit(Scanner sc, Promotion promotion) {
        while (true) {
            try {
                System.out.print("The time(s) that one user can claimed: ");
                int newPerLimit = sc.nextInt();
                sc.nextLine();

                if (newPerLimit > 0 && newPerLimit <= 3) {
                    promotion.setPerLimit(newPerLimit);
                    break;
                } else if (newPerLimit <= 0) {
                    System.out.println("Your limit must bigger than 0. Please enter again.\n");
                } else {
                    System.out.println("Your limit can't bigger than 3 times. Please enter again.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.\n");
                sc.nextLine();
            }
        }
    }

    // Validate promotion start date
    public static void checkStartDate(Scanner sc, Promotion promotion) {
        DateTime startDateTime;

        while (true) {
            System.out.print("Start Date (yyyy-mm-dd): ");
            String newStartDate = sc.nextLine().trim();

            int[] dateParts = DateTime.dateFormatValidator(newStartDate, "^\\d{4}-\\d{2}-\\d{2}$");

            if (!(dateParts == null)) {
                startDateTime = new DateTime(dateParts[0], dateParts[1], dateParts[2]);

                if (startDateTime.isValidDate()) {
                    if (startDateTime.getDate().equals(LocalDate.now()) || startDateTime.getDate().isAfter(LocalDate.now())) {
                        if (promotion.getEndDate() == null) {
                            promotion.setStartDate(startDateTime);

                            break;
                        } else {
                            if (startDateTime.getDate().isBefore(promotion.getEndDate().getDate())) {
                                // To make sure if admin want to modify the start date, the start date must before the end date
                                promotion.setStartDate(startDateTime);
                                break;
                            } else {
                                System.out.println("Your new start date must before the end date.\n");
                            }
                        }

                    } else {
                        System.out.println("Your start date must be equal/after the current date.\n");
                    }
                } else {
                    System.out.println("Please enter valid date range.\n");
                }
            }
        }
    }

    // Validate promotion end date
    public static void checkEndDate(Scanner sc, Promotion promotion, DateTime startDateTime) {
        while (true) {
            System.out.print("End Date (yyyy-mm-dd): ");
            String newEndDate = sc.nextLine().trim();

            int[] dateParts = DateTime.dateFormatValidator(newEndDate, "^\\d{4}-\\d{2}-\\d{2}$");

            if (!(dateParts == null)) {
                DateTime endDateTime = new DateTime(dateParts[0], dateParts[1], dateParts[2]);

                if (endDateTime.isValidDate()) {
                    if (endDateTime.getDate().isAfter(startDateTime.getDate())) {
                        promotion.setEndDate(endDateTime);
                        break;
                    } else {
                        System.out.println("Your end date must be after the start date.\n");
                    }
                } else {
                    System.out.println("Please enter valid date range.\n");
                }
            }
        }
    }


    // Validate promotion publish count
    public static void checkPublishCount(Scanner sc, Promotion promotion) {
        while (true) {
            try {
                System.out.print("Publish count: ");
                int newPublishCount = sc.nextInt();
                sc.nextLine();

                if (newPublishCount > 0 && newPublishCount <= 10000 && newPublishCount > promotion.getReceiveCount()) {
                    promotion.setPublishCount(newPublishCount);
                    break;
                } else if (newPublishCount <= 0) {
                    System.out.println("Your publish count must bigger than 0. Please enter again.\n");
                } else if (newPublishCount <= promotion.getReceiveCount()) {
                    System.out.println("Your publish count must bigger than receive count. Please enter again.\n");
                } else {
                    System.out.println("Your publish count can't bigger than 10000. Please enter again.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.\n");
                sc.nextLine();
            }
        }
    }
}