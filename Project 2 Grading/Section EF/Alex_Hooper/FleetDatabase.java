import java.util.ArrayList;

/**
 * Manages a collection of boats and executes any given operation after validating data.
 * @author alexhooper
 * @version 1.0
 */
public class FleetDatabase {

// DATA ATTRIBUTES

    /**
     * List of all boats in the current database.
     */
    private ArrayList<Boat> allBoats = new ArrayList<>();

    /**
     * The sum of how much all of the boats cost.
     */
    private double totalPaid = 0;

    /**
     * The amount spend in repairs on each boat.
     */
    private double totalExpenses = 0;

// CONSTRUCTORS

    /**
     * Constructor for when no CSV is present, just bring in the file database.
     * @param passFleet Fleet imported from the file
     */
    public FleetDatabase(ArrayList<Boat> passFleet) {
        importExistingFleet(passFleet);
        // No action needed for default constructor
    }

    /**
     * Constructor for when data must come from CSV and also the database.
     * @param passFleet Fleet imported from the file.
     * @param csvInput Contents of imported CSV file
     */
    public FleetDatabase(ArrayList<Boat> passFleet, ArrayList<String> csvInput) {
        int lineIterator;
        for (lineIterator = 0; lineIterator < csvInput.size(); lineIterator++) {
            addBoat(csvInput.get(lineIterator));
        }
        importExistingFleet(passFleet);
    } // End of FleetDatabase constructor method


// SETTERS

    /**
     * Bring in existing fleet members from file
     * @param fleetFromDatabase The fleet from the file in an ArrayList.
     */
    private void importExistingFleet(ArrayList<Boat> fleetFromDatabase) {
        int numOfBoats = fleetFromDatabase.size();
        int boatIterator;
        for (boatIterator = 0; boatIterator < numOfBoats; boatIterator++) {
            Boat thisBoat= fleetFromDatabase.get(boatIterator);
            allBoats.add(thisBoat);
            totalPaid += thisBoat.getPaid();
            totalExpenses += thisBoat.getExpenses();

        }
    }


    /**
     * Add additional boat
     * @param inputString String formatted correctly for new boat.
     */
    public void addBoat(String inputString) {
        Boat newBoat = new Boat(inputString);
        totalExpenses += newBoat.getExpenses();
        totalPaid += newBoat.getPaid();
        allBoats.add(newBoat);
    } // End of addBoat method

    /**
     * Get rid of an existing boat
     * @param boatName The name of the boat to get rid of
     * @see FleetManager
     */
    public void removeBoat(String boatName) {
        short searchIndexResult = searchBoatDatabase(boatName);
        if (searchIndexResult > -1) {
            try {
                totalExpenses -= allBoats.get(searchIndexResult).getExpenses();
                totalPaid -= allBoats.get(searchIndexResult).getPaid();
                allBoats.remove(searchIndexResult);
            } catch (Error e) {
                System.out.println("There was an error removing the boat " + boatName + ": " + e);
            }
            System.out.println("Boat " + boatName + " was successfully removed.");
        } else {
            System.out.println("Cannot find the boat " + boatName);
        }
    } // End of removeBoat method


// GETTERS

    /**
     * Print data about all the boats in the fleet
     * @see FleetDatabase
     */
    public void printFleetReport() {
        System.out.println("\nFLEET REPORT:");
        for (int boatObjIterator = 0; boatObjIterator < allBoats.size(); boatObjIterator++) {
            System.out.println("\t" + allBoats.get(boatObjIterator));
        } // end loop for printing each boat

        System.out.printf("%-51s  :  Paid: $%-10s  : Spent: $%-5s\n", "",String.format("%.2f",totalPaid), String.format("%.2f",totalExpenses));

    } // End of printFleetReport method

    /**
     * Take a passed boat name input and attempt to find it in the system.
     * @param searchInput The name trying to be searched
     * @return Index of found boat
     * @see FleetManager
     */
    public short searchBoatDatabase(String searchInput) {
        for (int boatObjIterator = 0; boatObjIterator < allBoats.size(); boatObjIterator++) {
            String searchBoatName = allBoats.get(boatObjIterator).getName().toLowerCase();
            if (searchBoatName.equals(searchInput.toLowerCase())) {
                return (short) boatObjIterator;
            } // end check name conditional
        } // End boat iterator loop
        return -1; // if not found, return  -1
    } // End of searchBoatDatabase method

    /**
     * Get number of boats.
     * @return The number of boats
     * @see FleetDatabase
     */
    public ArrayList<Boat> getBoats() {
        return allBoats;
    }


// MUTATORS

    /**
     * Check and see if you can spend more on the boat, if so, spend the money.
     * @param boatIndex Which boat is being selected.
     * @param expenseAmount How much the fix will cost
     * @see FleetDatabase
     */
    public void expenseBoat(short boatIndex, double expenseAmount) {
        Boat selectedBoat = allBoats.get(boatIndex);
        if (selectedBoat.addExpense(expenseAmount)) {
            totalExpenses += expenseAmount;
            System.out.println("Expense authorized, $" + String.format("%.2f",expenseAmount) + " spent.");
        } else {
            String amountRemaining = String.format("%.2f", (selectedBoat.getPaid() - selectedBoat.getExpenses()));
            System.out.println("Expense denied, only $" + amountRemaining + " left to spend.");
            // end check for adding expense
        }
    } // End of expenseBoat method

} // End of FleetDatabase class
