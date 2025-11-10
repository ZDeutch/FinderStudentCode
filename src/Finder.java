import java.io.BufferedReader;
import java.io.IOException;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 * <p>
 * Completed by: Zander Deutch
 **/

public class Finder {

    private static final String INVALID = "INVALID KEY";
    private int itemCount;

    // Prime number to be an effective mod
    // Around 1 million elements to fit in memory
    private static int SIZE = 1000003;

    // Arrays which hold the keys for the file and the values for the file respectively
    String[] keys = new String[SIZE];
    String[] values = new String[SIZE];


    public Finder() {
    }

    // Method to build data structure which holds all items in CSV file
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {

        String line = br.readLine();

        // Until you reach the end of the file
        while (line != null) {
            // Read through each item
            String[] parts = line.split(",");

            // Create and initialize the keys and values for the given file
            String key = parts[keyCol];
            String value = parts[valCol];

            // Check if the array is 50% full in which case you resize to make it bigger
            if (itemCount >= SIZE / 2) {
                resize();
            }

            // Generate a rolling hash value through Horner's Method
            long hashVal = hash(key, 0, key.length());

            // Make sure that fits in your array through modding
            int index = (int) Math.abs(hashVal % SIZE);


            // If there is an entry at the given index which is different from the current element
            // Move your index to the right in search of an empty entry
            while (keys[index] != null && !keys[index].equals(key)) {
                index = (index + 1) % SIZE;
            }

            // Once you find an empty spot in the array you know that it can be filled with an entry
            if (keys[index] == null) {
                itemCount++;
            }

            // Move the key and value into the empty spot in the array and move to the next line
            keys[index] = key;
            values[index] = value;
            line = br.readLine();
        }
    }

    // Method to find a given item with its key
    public String query(String key) {

        // Generate the rolling hash value using Horner's Method
        long hasVal = hash(key, 0, key.length());

        // Create its array index by modding
        int index = (int) Math.abs(hasVal % SIZE);

        // Use a starting index to ensure you don't wrap around the whole array
        int startIndex = index;

        // While the element is not empty
        // Check if they are equal and return the match
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                return values[index];
            }
            // Otherwise keep moving to the right until you find the correct element
            index = (index + 1) % SIZE;

            // If you loop around or don't get a hit then it was an invalid search
            if (index == startIndex) {
                return INVALID;
            }
        }
        return INVALID;
    }

    // Horner's Method
    public static long hash(String sequence, int start, int end) {
        long hash = 0;
        for (int i = start; i < end; i++) {
            // Use extended ASCII as test files have numerous types of characters
            hash = (hash * 256 + sequence.charAt(i));
        }
        return hash;
    }

    // Method to resize arrays after 50% capacity
    public void resize() {
        // Save old size and reset item count
        int oldSize = SIZE;
        itemCount = 0;

        // Double the size of your new arrays
        SIZE *= 2;

        // Store old arrays and create expanded arrays
        String[] oldKeys = keys;
        String[] oldValues = values;

        keys = new String[SIZE];
        values = new String[SIZE];

        // For every element in your old array
        for (int i = 0; i < oldSize; i++) {
            // If that index wasn't null
            if (oldKeys[i] != null) {
                // Rehash the element with the updated size
                long hashVal = hash(oldKeys[i], 0, oldKeys[i].length());

                // Make the index using mod
                int index = (int) Math.abs(hashVal % SIZE);

                // increment your index if the current position is filled
                while (keys[index] != null) {
                    // Make sure you are still in bounds of the array
                    index = (index + 1) % SIZE;
                }

                // Now update the new array with the old array's contents
                keys[index] = oldKeys[i];
                values[index] = oldValues[i];

                // Indicate that a new item has been added
                itemCount++;
            }
        }
    }
}