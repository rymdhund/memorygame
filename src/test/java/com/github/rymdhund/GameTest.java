package com.github.rymdhund;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    @BeforeEach
    void setUp() {
        // Setup a game with a known set of cards
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
        game = new Game(colors);
    }

    @Test
    void move() {
        game.moveRight();
        assertTrue(game.isCurrentPos(new Position(0, 1)));
        game.moveDown();
        assertTrue(game.isCurrentPos(new Position(1, 1)));
        game.moveLeft();
        assertTrue(game.isCurrentPos(new Position(1, 0)));
        game.moveUp();
        assertTrue(game.isCurrentPos(new Position(0, 0)));
    }

    @Test
    void moveStaysWithinBoard() {
        game.moveLeft();
        assertTrue(game.isCurrentPos(new Position(0, 0)));

        game.moveUp();
        assertTrue(game.isCurrentPos(new Position(0, 0)));

        game.moveRight();
        game.moveRight();
        game.moveRight();
        game.moveRight();
        assertTrue(game.isCurrentPos(new Position(0, Game.COLUMNS-1)));

        game.moveDown();
        game.moveDown();
        game.moveDown();
        game.moveDown();
        assertTrue(game.isCurrentPos(new Position(Game.ROWS-1, Game.COLUMNS-1)));
    }

    @Test
    void selectCurrentCard() {
        game.selectCurrentCard();
        assertTrue(game.isSelected(new Position(0, 0)));

        game.moveRight();
        game.moveDown();
        game.selectCurrentCard();
        assertTrue(game.isSelected(new Position(0, 0)));
        assertTrue(game.isSelected(new Position(1, 1)));
    }

    @Test
    void selectAlreadySelected() {
        assertEquals(1, game.selectCurrentCard());
        assertEquals(1, game.selectCurrentCard());
    }

    @Test
    void selectAlreadyFound() {
        game.selectCurrentCard();
        game.moveRight();
        game.selectCurrentCard();
        game.clearSelected();
        int numSelected = game.selectCurrentCard();
        assertEquals(0, numSelected);
    }


    @Test
    void getScore() {
        assertEquals(0, game.getScore());

        // Select two similar cards
        game.selectCurrentCard();
        game.moveRight();
        game.selectCurrentCard();
        game.clearSelected();
        assertEquals(1, game.getScore());

        // Select two dissimilar cards
        game.moveRight();
        game.selectCurrentCard();
        game.moveDown();
        game.selectCurrentCard();
        game.clearSelected();
        assertEquals(0, game.getScore());
    }
}