package in.co.rays.proj4.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for validating common data types such as String, number,
 * email, name, roll number, password, phone number and dates.
 * <p>
 * This class provides static helper methods that can be reused across the
 * project for server-side validation.
 * </p>
 *
 * @author Deepak Verma
 * @version 1.0
 */
public class DataValidator {

    /**
     * Checks if the given String is null or empty after trimming.
     *
     * @param val the String value to check
     * @return {@code true} if the value is null or empty, otherwise {@code false}
     */
    public static boolean isNull(String val) {
        if (val == null || val.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the given String is not null and not empty.
     *
     * @param val the String value to check
     * @return {@code true} if the value is not null and not empty,
     *         otherwise {@code false}
     */
    public static boolean isNotNull(String val) {
    	
        return !isNull(val);
    }
    /**
     * Checks whether a String is a valid Double number
     * 
     * @param val input String
     * @return true if valid double, false otherwise
     */
    public static boolean isDouble(String val) {

    	if (val == null || val.trim().length() == 0) {
    		return false;
    	}

    	try {
    		Double.parseDouble(val.trim());
    		return true;
    	} catch (NumberFormatException e) {
    		return false;
    	}
    }


    /**
     * Checks if the given String is a valid integer value.
     *
     * @param val the String to validate
     * @return {@code true} if the value is a valid integer,
     *         otherwise {@code false}
     */
    public static boolean isInteger(String val) {

        if (isNotNull(val)) {
            try {
                Integer.parseInt(val);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if the given String is a valid long value.
     *
     * @param val the String to validate
     * @return {@code true} if the value is a valid long,
     *         otherwise {@code false}
     */
    public static boolean isLong(String val) {
        if (isNotNull(val)) {
            try {
                Long.parseLong(val);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Validates if the given String is in a valid email format.
     *
     * @param val the email String to validate
     * @return {@code true} if the value matches the email pattern,
     *         otherwise {@code false}
     */
    public static boolean isEmail(String val) {

        String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (isNotNull(val)) {
            try {
                return val.matches(emailreg);
            } catch (NumberFormatException e) {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Validates if the given String is a valid name.
     * <p>
     * Name can contain letters, spaces, dots, apostrophes and hyphens.
     * It cannot start with a hyphen or whitespace.
     * </p>
     *
     * @param val the name String to validate
     * @return {@code true} if the value matches the name pattern,
     *         otherwise {@code false}
     */
    public static boolean isName(String val) {

        String namereg = "^[^-\\s][\\p{L} .'-]+$";

        if (isNotNull(val)) {
            try {
                return val.matches(namereg);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Validates if the given String is a valid roll number.
     * <p>
     * Format: two alphabets followed by three digits (e.g., AB123).
     * </p>
     *
     * @param val the roll number String to validate
     * @return {@code true} if the value matches the roll number pattern,
     *         otherwise {@code false}
     */
    public static boolean isRollNo(String val) {

        String rollreg = "[a-zA-Z]{2}[0-9]{3}";

        if (isNotNull(val)) {
            try {
                return val.matches(rollreg);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Validates if the given String is a strong password.
     * <p>
     * Password must:
     * <ul>
     *   <li>contain at least one digit</li>
     *   <li>contain at least one lowercase letter</li>
     *   <li>contain at least one uppercase letter</li>
     *   <li>contain at least one special character from @#$%^&+=</li>
     *   <li>have no whitespace</li>
     *   <li>be 8 to 12 characters long</li>
     * </ul>
     * </p>
     *
     * @param val the password String to validate
     * @return {@code true} if the value matches the password pattern,
     *         otherwise {@code false}
     */
    public static boolean isPassword(String val) {

        String passreg = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,12}";

        if (isNotNull(val)) {
            try {
                return val.matches(passreg);
            } catch (NumberFormatException e) {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Checks only the length of the password.
     * <p>
     * Valid length is between 8 and 12 characters (inclusive).
     * </p>
     *
     * @param val the password String to check
     * @return {@code true} if the password length is valid,
     *         otherwise {@code false}
     */
    public static boolean isPasswordLength(String val) {

        if (isNotNull(val) && val.length() >= 8 && val.length() <= 12) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates if the given String is a valid phone number.
     * <p>
     * Indian mobile number format: 10 digits, starting from 6 to 9.
     * </p>
     *
     * @param val the phone number String to validate
     * @return {@code true} if the value matches the phone pattern,
     *         otherwise {@code false}
     */
    public static boolean isPhoneNo(String val) {

        String phonereg = "^[6-9][0-9]{9}$";

        if (isNotNull(val)) {
            try {
                return val.matches(phonereg);
            } catch (NumberFormatException e) {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Checks only the length of the phone number.
     * <p>
     * Valid length is exactly 10 characters.
     * </p>
     *
     * @param val the phone number String to check
     * @return {@code true} if the length is 10, otherwise {@code false}
     */
    public static boolean isPhoneLength(String val) {

        if (isNotNull(val) && val.length() == 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates if the given String can be parsed into a valid {@link Date}.
     * <p>
     * The actual parsing format depends on {@link DataUtility#getDate(String)}.
     * </p>
     *
     * @param val the date String to validate
     * @return {@code true} if the value can be converted to a Date,
     *         otherwise {@code false}
     */
    public static boolean isDate(String val) {

        Date d = null;
        if (isNotNull(val)) {
            d = DataUtility.getDate(val);
        }
        return d != null;
    }

    /**
     * Checks whether the given date falls on a Sunday.
     * <p>
     * The String is converted to a {@link Date} using
     * {@link DataUtility#getDate(String)}, then the day of week is checked.
     * </p>
     *
     * @param val the date String to check
     * @return {@code true} if the date is Sunday, otherwise {@code false}
     */
    public static boolean isSunday(String val) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(DataUtility.getDate(val));
        int i = cal.get(Calendar.DAY_OF_WEEK);

        if (i == Calendar.SUNDAY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Simple main method to test the validation methods.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Test isNull and isNotNull
        System.out.println("isNull Test:");
        System.out.println("Empty String: " + isNull(""));
        System.out.println("Null String: " + isNull(null));
        System.out.println("Non-null String: " + isNotNull("Hello"));

        // Test isInteger
        System.out.println("\nisInteger Test:");
        System.out.println("Valid Integer String: '123' -> " + isInteger("123"));
        System.out.println("Invalid Integer String: 'abc' -> " + isInteger("abc"));
        System.out.println("Null String: -> " + isInteger(null));

        // Test isLong
        System.out.println("\nisLong Test:");
        System.out.println("Valid Long String: '1234567890' -> " + isLong("1234567890"));
        System.out.println("Invalid Long String: 'abc' -> " + isLong("abc"));

        // Test isEmail
        System.out.println("\nisEmail Test:");
        System.out.println("Valid Email: 'test@example.com' -> " + isEmail("test@example.com"));
        System.out.println("Invalid Email: 'test@.com' -> " + isEmail("test@.com"));

        // Test isName
        System.out.println("\nisName Test:");
        System.out.println("Valid Name: 'John Doe' -> " + isName("John Doe"));
        System.out.println("Invalid Name: '123John' -> " + isName("123John"));

        // Test isRollNo
        System.out.println("\nisRollNo Test:");
        System.out.println("Valid RollNo: 'AB123' -> " + isRollNo("AB123"));
        System.out.println("Invalid RollNo: 'A1234' -> " + isRollNo("A1234"));

        // Test isPassword
        System.out.println("\nisPassword Test:");
        System.out.println("Valid Password: 'Passw0rd@123' -> " + isPassword("Passw0rd@123"));
        System.out.println("Invalid Password: 'pass123' -> " + isPassword("pass123"));

        // Test isPasswordLength
        System.out.println("\nisPasswordLength Test:");
        System.out.println("Valid Password Length: 'Passw0rd' -> " + isPasswordLength("Passw0rd"));
        System.out.println("Invalid Password Length: 'pass' -> " + isPasswordLength("pass"));

        // Test isPhoneNo
        System.out.println("\nisPhoneNo Test:");
        System.out.println("Valid PhoneNo: '9876543210' -> " + isPhoneNo("9876543210"));
        System.out.println("Invalid PhoneNo: '1234567890' -> " + isPhoneNo("1234567890"));

        // Test isPhoneLength
        System.out.println("\nisPhoneLength Test:");
        System.out.println("Valid Phone Length: '9876543210' -> " + isPhoneLength("9876543210"));
        System.out.println("Invalid Phone Length: '98765' -> " + isPhoneLength("98765"));

        // Test isDate
        System.out.println("\nisDate Test:");
        System.out.println("Valid Date: '2025-01-01' -> " + isDate("2025-01-01"));
        System.out.println("Invalid Date: '10/15/2024' -> " + isDate("10/15/2024"));

        // Test isSunday
        System.out.println("\nisSunday Test:");
        System.out.println("Date on Sunday: '13-10-2024' -> " + isSunday("13-10-2024"));
        System.out.println("Date not on Sunday: '15-10-2024' -> " + isSunday("15-10-2024"));
    }
}
