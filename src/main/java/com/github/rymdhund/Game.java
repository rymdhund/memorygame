package com.github.rymdhund;

import java.util.*;

import static java.lang.Math.*;

/**
 * The Game model. Keeps track of the game state.
 */
public class Game {
    public static final int ROWS = 4;
    public static final int COLUMNS = 4;
    private Position currentPos;
    private int score;
    private final List<Color> cardColors;

    /**
     * The currently selected cards
     */
    private final List<Position> selected;

    /**
     * The cards that have been found
     */
    private final Set<Position> found;

    public Game() {
        this(randomColors());
    }

    public Game(List<Color> cardColors) {
        if (cardColors.size() != ROWS * COLUMNS) {
            throw new IllegalArgumentException("Expected "+ (ROWS * COLUMNS)  + " colors");
        }
        this.currentPos = new Position(0, 0);
        this.found = new HashSet<>();
        this.cardColors = cardColors;
        this.selected = new ArrayList<>();
    }

    private static List<Color> randomColors() {
        ArrayList<Color> colors = new ArrayList<>(16);
        colors.add(Color.RED);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.MAGENTA);
        colors.add(Color.CYAN);
        colors.add(Color.CYAN);
        colors.add(Color.PURPLE);
        colors.add(Color.PURPLE);
        colors.add(Color.GOLD);
        colors.add(Color.GOLD);

        // Shuffle
        Random r = new Random();
        for (int i = 0; i < colors.size(); i++) {
            int rand = r.nextInt(i, colors.size());
            Color tmp = colors.get(i);
            colors.set(i, colors.get(rand));
            colors.set(rand, tmp);
        }

        return colors;
    }

    /**
     * Get the color of a card at a position
     */
    public Color cardColor(Position position) {
        return cardColors.get(position.row() * 4 + position.col());
    }

    /**
     * Check whether we have selected two cards of the same color
     * @return true if the selected cards have the same color
     */
    private boolean isCorrectGuess() {
        return selected.size() == 2 && cardColor(selected.get(0)).equals(cardColor(selected.get(1)));
    }


    /**
     * Move currentPos left while making sure it stays within the game board
     */
    public void moveLeft() {
        int newCol = max(currentPos.col() - 1, 0);
        currentPos = new Position(currentPos.row(), newCol);
    }

    /**
     * Move currentPos right while making sure it stays within the game board
     */
    public void moveRight() {
        int newCol = min(currentPos.col() + 1, COLUMNS - 1);
        currentPos = new Position(currentPos.row(), newCol);
    }

    /**
     * Move currentPos up while making sure it stays within the game board
     */
    public void moveUp() {
        int newRow = max(currentPos.row() - 1, 0);
        currentPos = new Position(newRow, currentPos.col());
    }

    /**
     * Move currentPos down while making sure it stays within the game board
     */
    public void moveDown() {
        int newRow = min(currentPos.row() + 1, ROWS - 1);
        currentPos = new Position(newRow, currentPos.col());
    }

    /**
     * Show the current card if available. Updates the score and the set of found cards when two cards have been selected.
     * @return the number of shown cards
     */
    public int selectCurrentCard() {
        if (!selected.contains(currentPos) && !found.contains(currentPos)) {
            selected.add(currentPos);

            if (selected.size() == 2) {
                if (isCorrectGuess()) {
                    found.addAll(selected);
                    score++;
                } else {
                    score--;
                }
            }
        }
        return selected.size();
    }

    /**
     * Unselect all selected cards. Call this method after two cards have been selected.
     */
    public void clearSelected() {
        selected.clear();
    }

    public boolean isSelected(Position pos) {
        return selected.contains(pos);
    }

    public boolean isCurrentPos(Position pos) {
        return currentPos.equals(pos);
    }

    public boolean isFound(Position pos) {
        return found.contains(pos);
    }

    public int getScore() {
        return score;
    }

    /**
     * Check whether the game is finished
     */
    public boolean isFinished() {
        return found.size() == ROWS * COLUMNS;
    }
}
