package exceptions;

public class ExHandling {

    public static double checkDoubles(String param, String entry) throws NoNegativeException {
        double value = 0;
        try {
            value = Double.parseDouble(entry);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(param + " must be a number.");
        }
        checkForNegative(param, value);
        return value;
    }

    public static int checkInts(String param, String entry) throws NoNegativeException {
        double value = 0;
        try {
            value = Double.parseDouble(entry);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(param + " must be a number.");
        }
        checkForNegative(param, value);
        return (int) value;
    }
    // Method to check that input numbers are correct - 
    // they cannot be empty, a non-number, or negative.
    public static double checkDoublesIncNullCheck(String param, String entry) throws NoNegativeException, NoNullException {
        checkForNull(param, entry);
        return checkDoubles(param, entry);
    }

    public static int checkIntsIncNullCheck(String param, String entry) throws NoNegativeException, NoNullException {
        checkForNull(param, entry);
        return checkInts(param, entry);
    }

    //Method to check that numbers are positive
    public static void checkForNegative(String param, double value) throws NoNegativeException {
        if (value < 0) {
            throw new NoNegativeException(param + " cannot be negative.");
        }
    }

    //Method to check that fields aren't null
    public static String checkForNull(String param, String entry) throws NoNullException {
        if (entry == null || entry.equals("")) {
            throw new NoNullException(param + " cannot be empty.");
        }
        return entry;
    }
}
