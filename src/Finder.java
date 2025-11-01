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

    // Prime number to be an effective mod
    // Around 1 million elements to fit in memory
    private static final int SIZE = 1000003;

    public Finder() {
    }

    // Method to build data structure which holds all items in CSV file
    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {

        // table to store chained nodes
        Node[] table = new Node[SIZE];


        String line = br.readLine();

        // Until you reach the end of the file
        while (line != null) {
            // Read through each item
            String[] parts = line.split(",");

            // Identify the key and value for the item
            String key = parts[keyCol];
            String value = parts[valCol];

            // Create a unique AASCI value based on the key
            int AASCI = 0;
            for (int i = 0; i < key.length(); i++) {
                AASCI = 31 * AASCI + key.charAt(i);
            }

            // Make sure that fits in your table through modding
            int index = AASCI % SIZE;

            // Set this as your current node
            Node current = table[index];

            // And the node you are searching for is initially false
            boolean found = false;

            // Until there are no more nodes in this chain of table[index]
            while (current != null) {
                // See if the keys match for the chained element and the key of the item
                if (current.getKey().equals(key)) {
                    // If it matches, then set the item as found
                    found = true;
                    break;
                }

                // Move onto your next node in the table
                current = current.getNext();
            }

            // If you can't find the item in your table
            if (!found) {
                // Create a new entry for this item
                Node newEntry = new Node(key, value, table[index]);
                // Set the index to the new entry
                table[index] = newEntry;
            }
        }

        br.close();
    }

    public String query(String key) {
        // TODO: Complete the query() function!
        return INVALID;
    }
}