package com.github.rymdhund;

public class Ui {
    private static final int BOX_SIZE = 5;

    private static int colorCode(Color color) {
        return switch(color) {
            case RED -> 1;
            case GREEN -> 2;
            case YELLOW -> 3;
            case BLUE -> 4;
            case MAGENTA -> 5;
            case CYAN -> 6;
            case PURPLE -> 99;
            case GOLD -> 11;
            case GRAY -> 7;
            case WHITE -> 15;
            case BLACK -> 16;
        };
    }
    private static void draw(Color color, int numPixels) {
        System.out.print("\033[48;5;" + colorCode(color) + "m");
        System.out.print(" ".repeat(numPixels));
        System.out.print("\033[0m");
    }

    private static void gotoPos(int col, int row) {
        // we add 1 since terminal coords start at (1, 1) instead of (0, 0)
        System.out.print("\033[" + (row + 1) + ";" + (col + 1) + "H");
    }

    public static void clear() {
        System.out.print("\033[2J");
    }

    public static void drawBox(int col, int row, BoxStyle style, int size) {
        gotoPos(col, row);
        draw(style.outline(), size);
        for(int i = 1; i < size-1; i++) {
            gotoPos(col, row+i);
            draw(style.outline(), 1);
            draw(style.filling(), size-2);
            draw(style.outline(), 1);
        }
        gotoPos(col, row + size - 1);
        draw(style.outline(), size);
    }

    public static void drawGame(Game game) {
        clear();
        for(int row = 0; row < Game.ROWS; row++) {
            for(int col = 0; col < Game.COLUMNS; col++) {
                BoxStyle style = getBoxStyle(game, new Position(row, col));
                drawBox(col * BOX_SIZE, row * BOX_SIZE, style, BOX_SIZE);
            }
        }
        gotoPos(0, BOX_SIZE * Game.ROWS);
        System.out.println("Score: " + game.getScore());
        System.out.println("");
        System.out.println("arrows: move    space: select      q: quit");
    }

    /**
     * Get the outline of a box at a specific coord
     */
    private static Color getBoxOutline(Game game, Position pos) {
        if(game.isSelected(pos)) {
            return Color.WHITE;
        } else if(game.isCurrentPos(pos)) {
            return Color.BLACK;
        } else {
            return Color.GRAY;
        }
    }

    /**
     * Get the filling of a box at a specific coord
     */
    private static Color getBoxFilling(Game game, Position pos) {
        if(game.isSelected(pos)) {
            return game.cardColor(pos);
        } else if(!game.isFound(pos)) {
            return Color.WHITE;
        } else if(game.isCurrentPos(pos)) {
            return Color.BLACK;
        } else {
            return Color.GRAY;
        }
    }

    private static BoxStyle getBoxStyle(Game game, Position pos) {
        return new BoxStyle(getBoxFilling(game, pos), getBoxOutline(game, pos));
    }

    public static void drawFinished(Game game) {
        clear();
        gotoPos(0, 0);
        System.out.println("You won!");
        System.out.println("Score: " + game.getScore());
        System.out.println("Play again? (y/n)");
    }
}
