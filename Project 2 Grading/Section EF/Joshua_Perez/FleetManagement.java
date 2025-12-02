//=================================================================================================
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
//=================================================================================================
/**
 * Fleet Management System for the Coconut Grove Sailing Club.
 *
 * <p>The program tracks the club's fleet of sailing and power boats,
 * including the purchase price and money spent on maintenance. The
 * Commodore's policy is that the total expenses on a boat must not
 * exceed the amount paid for that boat.</p>
 *
 * <p>On an initializing run the program loads boat data from a CSV file
 * whose name is given as a command line argument, then saves the fleet
 * into a serialized database file {@code FleetData.db} when exiting.
 *
 * <p>Restriction: Only this class may have a {@link Scanner} object for
 * keyboard input.</p>
 *
 * @author  Joshua Perez
 * @version 1.0
 */
//=================================================================================================
//=================================================================================================
public class FleetManagement {
//-------------------------------------------------------------------------------------------------
    /**
     * Shared Scanner for keyboard input.
     */
    private static final Scanner INPUT = new Scanner(System.in);
    /**
     * Name of the serialized fleet database file.
     */
    private static final String DB_FILENAME = "FleetData.db";
    /**
     * Tolerance for comparing floating point values.
     */
    private static final double EPSILON = 1.0e-9;
//-------------------------------------------------------------------------------------------------
    /**
     * Program entry point. Loads fleet data from either a CSV file or a
     * database file, then loops on the menu until the user exits, and
     * finally saves the fleet.
     *
     * @param args optional command line parameter (CSV filename)
     */
    public static void main(String[] args) {
        List<Boat> fleet;
        char menuChoice;
        String menuLine;

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        fleet = new ArrayList<Boat>();

        // Initializing run: load from CSV, otherwise from DB
        if (args.length > 0) {
            loadFleetFromCsv(args[0], fleet);
        } else {
            loadFleetFromDb(fleet);
        }

        menuChoice = ' ';

        while (menuChoice != 'X') {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            menuLine = INPUT.nextLine().trim();

            if (menuLine.length() == 0) {
                System.out.println("Invalid menu option, try again");
                continue;
            } // end of if empty menu line

            menuChoice = Character.toUpperCase(menuLine.charAt(0));

            if (menuChoice == 'P') {
                printFleet(fleet);
            } else if (menuChoice == 'A') {
                addBoat(fleet);
            } else if (menuChoice == 'R') {
                removeBoat(fleet);
            } else if (menuChoice == 'E') {
                expenseBoat(fleet);
            } else if (menuChoice == 'X') {
                System.out.println();
                System.out.println("Exiting the Fleet Management System");
            } else {
                System.out.println("Invalid menu option, try again");
            }
        } // end of the while loop in main

        // Save fleet before exiting
        saveFleetToDb(fleet);

        INPUT.close();
    } // end of the main method
//-------------------------------------------------------------------------------------------------
    /**
     * Load fleet data from a CSV file. Each non-empty line describes one boat.
     *
     * <p>CSV format:
     * type,name,year,makeModel,lengthFeet,purchasePrice[,expenses]</p>
     *
     * @param filename name of the CSV file
     * @param fleet    list to populate with boats
     */
    private static void loadFleetFromCsv(String filename, List<Boat> fleet) {
        File csvFile;
        BufferedReader reader;
        String csvLine;
        Boat boat;

        csvFile = new File(filename);

        if (!csvFile.exists()) {
            // If there is no CSV file we simply start with an empty fleet.
            return;
        } // end of if file does not exist

        reader = null;

        try {
            reader = new BufferedReader(new FileReader(csvFile));
            csvLine = reader.readLine();

            while (csvLine != null) {
                csvLine = csvLine.trim();

                if (csvLine.length() > 0) {
                    boat = parseBoatCsvLine(csvLine);
                    if (boat != null) {
                        fleet.add(boat);
                    } // end of if boat successfully parsed
                } // end of if non-empty line

                csvLine = reader.readLine();
            } // end of the while loop reading lines
        } catch (IOException exception) {
            // If there is an error we just keep whatever we managed to load.
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException exception) {
                    // Ignore errors when closing.
                }
            }
        }
    } // end of loadFleetFromCsv method
//-------------------------------------------------------------------------------------------------
    /**
     * Load fleet data from the serialized database file, if it exists.
     *
     * @param fleet list to populate with boats
     */
    @SuppressWarnings("unchecked")
    private static void loadFleetFromDb(List<Boat> fleet) {
        File dbFile;
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        Object objectRead;
        ArrayList<Boat> loadedFleet;

        dbFile = new File(DB_FILENAME);

        if (!dbFile.exists()) {
            return;
        } // end of if db file does not exist

        fileInputStream = null;
        objectInputStream = null;

        try {
            fileInputStream = new FileInputStream(dbFile);
            objectInputStream = new ObjectInputStream(fileInputStream);

            objectRead = objectInputStream.readObject();

            if (objectRead instanceof ArrayList<?>) {
                loadedFleet = (ArrayList<Boat>) objectRead;
                fleet.clear();
                fleet.addAll(loadedFleet);
            } // end of if correct type
        } catch (IOException exception) {
            // Ignore; start with empty or partially loaded fleet.
        } catch (ClassNotFoundException exception) {
            // Ignore; cannot load boats without the correct class.
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException exception) {
                    // Ignore errors when closing.
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException exception) {
                    // Ignore errors when closing.
                }
            }
        }
    } // end of loadFleetFromDb method
//-------------------------------------------------------------------------------------------------
    /**
     * Save the current fleet data to the serialized database file.
     *
     * @param fleet list of boats to save
     */
    private static void saveFleetToDb(List<Boat> fleet) {
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        ArrayList<Boat> copyToSave;

        fileOutputStream = null;
        objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(DB_FILENAME);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            copyToSave = new ArrayList<Boat>(fleet);
            objectOutputStream.writeObject(copyToSave);
        } catch (IOException exception) {
            // Ignore save failures.
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException exception) {
                    // Ignore errors when closing.
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException exception) {
                    // Ignore errors when closing.
                }
            }
        }
    } // end of saveFleetToDb method
//-------------------------------------------------------------------------------------------------
    /**
     * Parse one CSV line into a {@link Boat} object.
     *
     * <p>Expected format:
     * type,name,year,makeModel,lengthFeet,purchasePrice[,expenses]</p>
     *
     * @param csvLine line of CSV text
     * @return new {@code Boat} if valid, otherwise {@code null}
     */
    private static Boat parseBoatCsvLine(String csvLine) {
        String[] parts;
        String typeText;
        BoatType boatType;
        String boatName;
        int manufactureYear;
        String makeAndModel;
        int lengthFeet;
        double purchasePrice;
        double expenses;
        Boat newBoat;

        parts = csvLine.split(",");

        if (parts.length < 6) {
            return null;
        } // end of if not enough fields

        typeText = parts[0].trim().toUpperCase();

        if (typeText.equals("SAILING")) {
            boatType = BoatType.SAILING;
        } else if (typeText.equals("POWER")) {
            boatType = BoatType.POWER;
        } else {
            return null;
        }

        boatName = parts[1].trim();
        manufactureYear = Integer.parseInt(parts[2].trim());
        makeAndModel = parts[3].trim();
        lengthFeet = Integer.parseInt(parts[4].trim());
        purchasePrice = Double.parseDouble(parts[5].trim());

        if (parts.length >= 7) {
            expenses = Double.parseDouble(parts[6].trim());
        } else {
            expenses = 0.0;
        }

        newBoat = new Boat(
                boatType,
                boatName,
                manufactureYear,
                makeAndModel,
                lengthFeet,
                purchasePrice,
                expenses
        );

        return newBoat;
    } // end of parseBoatCsvLine method
//-------------------------------------------------------------------------------------------------
    /**
     * Print a formatted fleet report, including totals for paid and spent.
     *
     * @param fleet list of boats
     */
    private static void printFleet(List<Boat> fleet) {
        int index;
        int fleetSize;
        Boat boat;
        double totalPaid;
        double totalSpent;

        System.out.println();
        System.out.println("Fleet report:");

        totalPaid = 0.0;
        totalSpent = 0.0;

        fleetSize = fleet.size();

        for (index = 0; index < fleetSize; index++) {
            boat = fleet.get(index);

            totalPaid = totalPaid + boat.getPurchasePrice();
            totalSpent = totalSpent + boat.getExpenses();

            System.out.println(boat.toString());
        } // end of the for loop over boats

        System.out.printf(
                "    %-47s : Paid $ %8.2f : Spent $ %9.2f%n",
                "Total",
                totalPaid,
                totalSpent
        );
        System.out.println();
    } // end of printFleet method
//-------------------------------------------------------------------------------------------------
    /**
     * Add a boat to the fleet by reading a CSV-style line from the keyboard.
     *
     * @param fleet list of boats
     */
    private static void addBoat(List<Boat> fleet) {
        String csvLine;
        Boat newBoat;

        System.out.print("Please enter the new boat CSV data          : ");
        csvLine = INPUT.nextLine().trim();

        if (csvLine.length() == 0) {
            return;
        } // end of if empty line

        newBoat = parseBoatCsvLine(csvLine);

        if (newBoat != null) {
            fleet.add(newBoat);
        }
    } // end of addBoat method
//-------------------------------------------------------------------------------------------------
    /**
     * Remove a boat from the fleet by name (case-insensitive).
     *
     * @param fleet list of boats
     */
    private static void removeBoat(List<Boat> fleet) {
        String boatName;
        int boatIndex;

        System.out.print("Which boat do you want to remove?           : ");
        boatName = INPUT.nextLine().trim();

        if (boatName.length() == 0) {
            return;
        } // end of if empty name

        boatIndex = findBoatByName(fleet, boatName);

        if (boatIndex < 0) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            fleet.remove(boatIndex);
        }
    } // end of removeBoat method
//-------------------------------------------------------------------------------------------------
    /**
     * Request permission to spend money on a boat, applying the Commodore's
     * policy that total expenses may not exceed the purchase price.
     *
     * @param fleet list of boats
     */
    private static void expenseBoat(List<Boat> fleet) {
        String boatName;
        int boatIndex;
        Boat boat;
        String amountText;
        double amountToSpend;
        double remainingAllowance;
        double newTotal;

        System.out.print("Which boat do you want to spend on?         : ");
        boatName = INPUT.nextLine().trim();

        if (boatName.length() == 0) {
            return;
        } // end of if empty name

        boatIndex = findBoatByName(fleet, boatName);

        if (boatIndex < 0) {
            System.out.println("Cannot find boat " + boatName);
            return;
        }

        boat = fleet.get(boatIndex);

        System.out.print("How much do you want to spend?              : ");
        amountText = INPUT.nextLine().trim();

        if (amountText.length() == 0) {
            return;
        } // end of if empty amount

        amountToSpend = Double.parseDouble(amountText);
        remainingAllowance = boat.getRemainingAllowance();

        if (amountToSpend <= remainingAllowance + EPSILON) {
            boat.addExpense(amountToSpend);
            newTotal = boat.getExpenses();
            System.out.printf("Expense authorized, $%.2f spent.%n", newTotal);
        } else {
            System.out.printf(
                    "Expense not permitted, only $%.2f left to spend.%n",
                    remainingAllowance
            );
        }
    } // end of expenseBoat method
//-------------------------------------------------------------------------------------------------
    /**
     * Find a boat by exact name, ignoring case.
     *
     * @param fleet    list of boats
     * @param boatName name to search for
     * @return index of matching boat, or {@code -1} if not found
     */
    private static int findBoatByName(List<Boat> fleet, String boatName) {
        int index;
        int fleetSize;
        String query;
        String currentName;

        query = boatName.trim().toLowerCase();
        fleetSize = fleet.size();

        for (index = 0; index < fleetSize; index++) {
            currentName = fleet.get(index).getName().trim().toLowerCase();
            if (currentName.equals(query)) {
                return index;
            }
        } // end of the for loop searching by name

        return -1;
    } // end of findBoatByName method
//-------------------------------------------------------------------------------------------------
} // end of the FleetManagement class
//=================================================================================================