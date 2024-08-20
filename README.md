# Puzzle-Difficulty-Problem
In the puzzle problem, we are trying to compare the difficulty levels of various puzzles. As  this topic is subjective and it can vary from person to person, we take multiple inputs  sometimes and check for the support level that decides what population’s votes are needed to  be considered. The **Puzzle-Difficulty-Problem** is a Java-based application designed to compare and analyze the difficulty levels of various puzzles, such as wooden and wire puzzles. This project leverages crowd-sourced comparisons to determine relative puzzle difficulty while addressing the subjective nature of such evaluations through the concept of "support."

## Project Overview

Comparing puzzle difficulty is inherently subjective, varying from person to person. To manage this variability, the application collects multiple comparisons between puzzles and uses a support threshold to filter out low-consensus opinions. The "support" represents the proportion of participants who agree on a specific comparison. By setting a support level, the system ensures that only comparisons with sufficient agreement are considered valid.

The core of the project is the `PuzzleLibrary` class, which processes input comparisons, filters them based on the support level, and provides various functionalities such as identifying harder, hardest, and equivalent puzzles.

## Key Features

- **Support-Based Filtering**: Define and adjust the support level for considering comparisons, ensuring that only comparisons with adequate agreement are processed.
- **Flexible Input Handling**: Accept comparisons from an input stream and dynamically add or remove them based on the support level.
- **Puzzle Comparison**: Identify which puzzles are harder, hardest, or equivalent in difficulty relative to a given puzzle.
- **Group Identification**: Group puzzles based on their type, considering only those comparisons with sufficient support.

## System Modules

### 1. **PuzzleLibrary Class**
- Handles the main logic for puzzle comparisons, filtering based on support, and determining puzzle relationships.
- Methods include:
    - `PuzzleLibrary(int support)`: Initializes the library with a specified support level.
    - `setSupport(int support)`: Adjusts the support level and recalculates valid comparisons.
    - `comparePuzzle(BufferedReader streamOfComparisons)`: Reads and processes puzzle comparisons from an input stream.
    - `equivalentPuzzle(String puzzle)`: Identifies puzzles equivalent in difficulty to the given puzzle.
    - `harderPuzzle(String puzzle)`: Identifies puzzles that are harder than the given puzzle.
    - `hardestPuzzle()`: Identifies puzzles with no harder puzzles in the library.
    - `puzzleGroup()`: Groups puzzles of the same type based on valid comparisons.

### 2. **Input Stream File**
- Contains comparisons between puzzles in the format: `[Puzzle1]\t[Puzzle2]`, where `Puzzle1` is harder than `Puzzle2`.

## Data Flow

The project is divided into three main phases:

1. **Input Phase**: Reads and stores comparisons from an input stream into a temporary list.
2. **Filtering Phase**: Filters the comparisons based on the current support level.
3. **Analysis Phase**: Determines the relationships between puzzles (harder, hardest, equivalent) and groups them based on comparisons.

## Usage

- **Constructor**: Create a new `PuzzleLibrary` instance with a specified support level.
- **Setting Support**: Adjust the support level dynamically, recalculating valid comparisons.
- **Comparison Input**: Load comparisons from an input stream using the `comparePuzzle` method.
- **Analyzing Puzzles**: Use methods like `harderPuzzle`, `hardestPuzzle`, and `equivalentPuzzle` to explore puzzle relationships.

## Conclusion

The **Puzzle-Difficulty-Problem** project provides a systematic way to analyze puzzle difficulties using crowd-sourced comparisons, with an emphasis on ensuring that only high-consensus opinions influence the results. This tool is useful for gaining insights into the relative difficulty of various puzzles based on user comparisons.
