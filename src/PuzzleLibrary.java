/**
 * Author: Shrey Nimeshkumar Patel
 * Banner-Id: B00960433, email id: sh644024@dal.ca
 * Assignment 3, The Puzzle Problem
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class PuzzleLibrary {

    // Class variables
    private int support; // Support level for comparisons
    private HashMap<String[], Double> comparisonsOfPuzzle; // HashMap to store puzzle comparisons
    public ArrayList<String[]> tempPuzzleList; // Temporary list to store puzzle pairs
    private Map<String, Set<String>> puzzleGraph; //Graph to store all the puzzles with edges going from harder to easier puzzle
    private Map<String, Set<String>> hardestGraph; //Graph to store all the puzzles with edges as incoming puzzles to every puzzle


    /**
     * Constructor to initialize the PuzzleLibrary with a support level.
     *
     * @param support The support level for comparisons.
     * @throws IllegalArgumentException If the support level is not within the range [0, 100].
     */
    public PuzzleLibrary(int support) {
        if (support < 0 || support > 100) {
            throw new IllegalArgumentException("Support level must be between 0 and 100 inclusive.");
        }
        this.support = support;
        this.comparisonsOfPuzzle = new HashMap<>();
        this.tempPuzzleList = new ArrayList<>();
        this.puzzleGraph = new HashMap<>();
        this.hardestGraph = new HashMap<>();
    }

    // Default constructor
    public PuzzleLibrary() {
    }

    // Method to set a new support level
    /**
     * Sets a new support level for comparisons.
     *
     * @param support The new support level to be set.
     * @return True if the support level is set successfully, false otherwise.
     */
    public boolean setSupport(int support) {
        if (support >= 0 && support <= 100) {
            this.support = support;
            this.comparisonsOfPuzzle.clear(); // Clear existing comparisons
            try{
                addtoHashMap(tempPuzzleList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    // Method to read comparisons between puzzles
    /**
     * Reads puzzle comparisons from a BufferedReader.
     *
     * @param streamOfComparisons The BufferedReader containing puzzle comparisons.
     * @return True if the comparisons are read successfully, false otherwise.
     */
    public boolean comparePuzzles(BufferedReader streamOfComparisons){
        String line;
        List<String> lines = new ArrayList<>();
        // Read all lines from the BufferedReader and store them in a list
        if(streamOfComparisons==null){
            return false;
        }
        while (true) {
            try {
                if ((line = streamOfComparisons.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line.trim().isEmpty()) {
                // Skip empty lines
                continue;
            }
            lines.add(line); // Add non-empty lines to the list
        }
        if(lines.isEmpty()){
            return false;
        }
        // Check each line for valid puzzle pairs and add them to the temporary list
        for (String currentLine : lines) {
            if (currentLine.matches("^\\S+\t\\S+$")){
                String[] parts = currentLine.split("\\t");

                if (parts.length != 2) {
                    return false; // Invalid format
                }
                String puzzleA = parts[0];
                String puzzleB = parts[1];
                // Ensure puzzleA and puzzleB are different
                if (puzzleA.equals(puzzleB)) {
                    return false;
                }
            }
            else{
                return false;
            }

        }
        // Add valid puzzle pairs from the list to the HashMap
        for (String currentLine : lines) {
            String[] parts = currentLine.split("\\t");
            tempPuzzleList.add(parts);
        }
        try {
            addtoHashMap(tempPuzzleList); // Add puzzle pairs to HashMap
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    // Method to add puzzle pairs to the HashMap with their support level
    /**
     * Adds puzzle pairs to the HashMap with their support level.
     *
     * @param puzzles The list of puzzle pairs to be added.
     * @return The updated HashMap containing the puzzle pairs.
     * @throws IOException If an IO error occurs.
     */
    public HashMap<String[], Double> addtoHashMap(ArrayList<String[]> puzzles) throws IOException {
        comparisonsOfPuzzle.clear(); // Clear existing comparisons
        puzzleGraph.clear(); // Clear existing graph
        hardestGraph.clear(); // Clear existing graph
        double supportForHash;
        for (String[] puzzleArray : puzzles) {
            double maxsupport = 0;
            maxsupport = ((countforMax(puzzleArray))/tempPuzzleList.size()*100);
            supportForHash = maxsupport;
            // Check if support level meets the specified threshold
            if (supportForHash >= support) {
                boolean keyExists = false;
                for (String[] key : comparisonsOfPuzzle.keySet()) {
                    // Check if the puzzle pair already exists in the HashMap
                    if (Arrays.equals(key, puzzleArray)) {
                        keyExists = true;
                        break;
                    }
                }
                // If the key doesn't exist, add a new entry to the HashMap
                if (!keyExists) {
                    comparisonsOfPuzzle.put(puzzleArray, supportForHash);
                }
            }
        }
        addComparison();
        return comparisonsOfPuzzle; // Return the updated HashMap
    }

    // Method to count the maximum support for a puzzle pair
    /**
     * Counts the maximum support for a puzzle pair.
     *
     * @param array1 The puzzle pair to count support for.
     * @return The count of the maximum support for the puzzle pair.
     */
    public double countforMax(String[] array1) {
        double count = 0;
        for (String[] array : tempPuzzleList) {
            // Count occurrences of array1 in the list
            if (Arrays.equals(array, array1)) {
                count++;
            }
        }
        return count;
    }
    public void EquivalentPuzzles() {
        puzzleGraph = new HashMap<>();
    }

    //Create graph using hash map.
    public void addComparison() {
        for (String[] key : comparisonsOfPuzzle.keySet()) {
            puzzleGraph.computeIfAbsent(key[0], k -> new HashSet<>()).add(key[1]);
        }
        for (String[] key : comparisonsOfPuzzle.keySet()) {
            hardestGraph.computeIfAbsent(key[1], k -> new HashSet<>()).add(key[0]);
        }

    }

    /**
     * Finds the set of puzzles equivalent to the given puzzle.
     *
     * @param puzzle The puzzle for which equivalent puzzles are to be found.
     * @return A set of puzzles equivalent to the given puzzle.
     * I have take reference from the given two sites for knowing logic for dfs in graphs and also for detecting cycles
     * https://www.geeksforgeeks.org/java-program-for-depth-first-search-or-dfs-for-a-graph/
     * https://www.baeldung.com/java-graph-has-a-cycle
     */
    public Set<String> equivalentPuzzles(String puzzle) {
        // Initialize sets and a stack for tracking visited puzzles and cycles
        Set<String> equivalentSet = new HashSet<>(); // Set to store equivalent puzzles
        Set<String> visited = new HashSet<>(); // Set to track visited puzzles
        Set<String> cycle = new HashSet<>(); // Set to identify cycles
        Stack<String> sec = new Stack<>(); // Stack for DFS traversal

        // Iterate through puzzle comparisons
        for (String[] key : comparisonsOfPuzzle.keySet()) {
            // Check if the given puzzle matches either puzzle in the comparison
            if (key[0].equals(puzzle) || key[1].equals(puzzle)) {
                // Search for equivalent puzzles recursively
                searchForEquivalent(puzzle, puzzle, equivalentSet, visited, sec, cycle);

                // Handle cycles in the equivalence relationship
                for(String s : cycle){
                    if(equivalentSet.contains(s)){
                        equivalentSet.addAll(cycle);
                    }
                }

                // Add the given puzzle to the equivalent set
                equivalentSet.add(puzzle);
            }
        }

        // Return the set of equivalent puzzles
        return equivalentSet;
    }

    /**
     * Recursively searches for equivalent puzzles starting from a given puzzle.
     *
     * @param root          The root puzzle from which the search begins.
     * @param puzzle        The current puzzle being processed in the search.
     * @param equivalentSet The set to store equivalent puzzles found during the search.
     * @param visited       The set to track visited puzzles to avoid revisiting them.
     * @param sec           The stack to track the path of puzzles visited during the search.
     * @param cycle         The set to store puzzles forming a cycle during the search.
     */
    private void searchForEquivalent(String root, String puzzle, Set<String> equivalentSet, Set<String> visited, Stack<String> sec, Set<String> cycle) {
        // If the puzzle has not been visited yet
        if (!visited.contains(puzzle)) {
            visited.add(puzzle); // Mark the puzzle as visited
            sec.add(puzzle); // Add the puzzle to the stack of visited puzzles

            // Get related puzzles from the puzzleGraph, defaulting to an empty set if none found
            Set<String> relatedPuzzles = puzzleGraph.getOrDefault(puzzle, Collections.emptySet());

            // Iterate through related puzzles
            for (String relatedPuzzle : relatedPuzzles) {
                // If the related puzzle is the root, add all puzzles in the stack to the equivalent set
                if (relatedPuzzle.equals(root)) {
                    equivalentSet.addAll(sec);
                }
                // If the related puzzle is already in the equivalent set, add all puzzles in the stack to the equivalent set
                else if (equivalentSet.contains(relatedPuzzle)) {
                    equivalentSet.addAll(sec);
                }
                // Recursively search for equivalent puzzles starting from the related puzzle
                searchForEquivalent(root, relatedPuzzle, equivalentSet, visited, sec, cycle);
            }
            sec.pop(); // Remove the current puzzle from the stack as it is being backtracked
        }
        // If the puzzle has been visited before and it's not the root puzzle
        else if (!puzzle.equals(root)) {
            // Backtrack to find the cycle formed and add it to the cycle set
            for (int i = sec.size() - 1; i >= 0 && !Objects.equals(sec.get(i), puzzle) && !cycle.contains(sec.get(i)); i--) {
                cycle.add(sec.get(i));
            }
            cycle.add(puzzle); // Add the current puzzle to the cycle set
        }
    }


    /**
     * Finds the set of harder puzzles reachable from the given puzzle, excluding those that are equivalent.
     *
     * @param puzzle The puzzle to find harder puzzles for.
     * @return A set of harder puzzles than the given puzzle, excluding equivalent ones.
     * I have take reference from the given two sites for knowing logic for dfs in graphs and also for detecting cycles
     * https://www.geeksforgeeks.org/java-program-for-depth-first-search-or-dfs-for-a-graph/
     * https://www.baeldung.com/java-graph-has-a-cycle
     */
    public Set<String> harderPuzzles(String puzzle) {
        Set<String> harderPuzzles = new HashSet<>();
        // Set to store visited puzzles
        Set<String> visited = new HashSet<>();
        Set<String> test;

        // Queue for breadth-first search
        Queue<String> queue = new LinkedList<>();
        queue.add(puzzle); // Add the given puzzle to the queue
        visited.add(puzzle); // Mark the given puzzle as visited

        // Perform breadth-first search to find all puzzles reachable from the given puzzle
        while (!queue.isEmpty()) {
            String currentPuzzle = queue.poll();
            // Check if the current puzzle has any puzzles in the hardestGraph
            if (hardestGraph.containsKey(currentPuzzle)) {
                for (String harderPuzzle : hardestGraph.get(currentPuzzle)) {
                    // Check if the harder puzzle has not been visited yet
                    if (!visited.contains(harderPuzzle)) {
                        queue.add(harderPuzzle); // Add the harder puzzle to the queue
                        visited.add(harderPuzzle); // Mark the harder puzzle as visited
                        harderPuzzles.add(harderPuzzle); // Add the harder puzzle to the result set
                    }
                }
            }
        }
        test = equivalentPuzzles(puzzle);
        Iterator<String> iterator = harderPuzzles.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (test.contains(s)) {
                iterator.remove(); // remove the element using iterator
            }
        }
        return harderPuzzles;
    }

    /**
     * Finds the set of hardest puzzles based on the comparison of puzzles and their relationships.
     *
     * @return A set of hardest puzzles.
     */
    public Set<String> hardestPuzzles() {
        Set<String> hardestPuzzles = new HashSet<>(); // Initialize the set to store hardest puzzles
        Set<Set<String>> testNew = new HashSet<>(); // Initialize a set to store sets of equivalent puzzles

        // Iterate through the puzzle comparisons
        for (String[] key : comparisonsOfPuzzle.keySet()) {
            // Add equivalent puzzles of each puzzle in the comparison to the set
            testNew.add(equivalentPuzzles(key[0]));
            testNew.add(equivalentPuzzles(key[1]));
        }

        Boolean flag = false; // Flag to indicate if a puzzle is not found in the hardestGraph

        // Iterate through each set of equivalent puzzles
        for (Set<String> key : testNew) {
            // Iterate through each puzzle in the set
            for (String puzzle : key) {
                // Check if the puzzle is not found in the hardestGraph
                if (!hardestGraph.containsKey(puzzle)) {
                    hardestPuzzles.add(puzzle); // Add the puzzle to the set of hardest puzzles
                    break;
                }
                // Iterate through the incoming puzzles to the current puzzle in the hardestGraph
                for (String x : hardestGraph.get(puzzle)) {
                    // If any puzzle in the equivalent set is not found in the incoming puzzles of the current puzzle
                    if (!key.contains(x)) {
                        flag = true; // Set flag to true
                        break; // Exit the loop
                    }
                }
            }
            // If the flag is false, add all puzzles in the equivalent set to the set of hardest puzzles
            if (flag.equals(false)) {
                hardestPuzzles.addAll(key);
            } else {
                flag = false; // Reset flag for the next iteration
            }
        }

        return hardestPuzzles; // Return the set of hardest puzzles
    }

    /**
     * Groups puzzles based on their comparisons and support level.
     *
     * @return A set of sets where each set contains puzzles belonging to the same group.
     */
    public Set<Set<String>> puzzleGroup() {
        // Initialize a map to store puzzles of each group
        Map<String, Set<String>> puzzleGroupMap = new HashMap<>();

        // Iterate through the comparisons to gather puzzles in the same group
        for (Map.Entry<String[], Double> entry : comparisonsOfPuzzle.entrySet()) {
            String puzzleA = entry.getKey()[0];
            String puzzleB = entry.getKey()[1];

            // Skip comparisons with insufficient support
            if (entry.getValue() <= support) {
                continue;
            }
            // Add both puzzles to the same group
            addToPuzzleGroup(puzzleGroupMap, puzzleA, puzzleB);
        }
        // Convert the map values (sets of puzzles) to a set of sets
        Set<Set<String>> puzzleGroups = new HashSet<>(puzzleGroupMap.values());
        return puzzleGroups;
    }

    // Helper method to add puzzles to the same group
    private void addToPuzzleGroup(Map<String, Set<String>> puzzleGroupMap, String puzzleA, String puzzleB) {
        // Check if puzzleA is already in a group
        Set<String> groupA = puzzleGroupMap.getOrDefault(puzzleA, new HashSet<>());
        // Check if puzzleB is already in a group
        Set<String> groupB = puzzleGroupMap.getOrDefault(puzzleB, new HashSet<>());

        // Merge groupA and groupB
        groupA.addAll(groupB);
        groupA.add(puzzleA);
        groupA.add(puzzleB);

        // Update puzzleGroupMap for each puzzle in the merged group
        for (String puzzle : groupA) {
            puzzleGroupMap.put(puzzle, groupA);
        }
    }

}
