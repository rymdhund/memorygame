package com.github.rymdhund;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory(System.out, System.in, Charset.defaultCharset());
        try(Terminal terminal = factory.createHeadlessTerminal()) {
            mainLoop(terminal);
        }
    }

    private static void mainLoop(Terminal terminal) throws IOException, InterruptedException {
        Game game = new Game();
        boolean quit = false;
        while (!quit) {
            Ui.drawGame(game);

            KeyStroke key;
            key = terminal.readInput();

            switch (key.getKeyType()) {
                case ArrowLeft -> game.moveLeft();
                case ArrowRight -> game.moveRight();
                case ArrowUp -> game.moveUp();
                case ArrowDown -> game.moveDown();
                case Character -> {
                    switch (key.getCharacter()) {
                        case ' ' -> {
                            int selected = game.selectCurrentCard();
                            if (selected == 2) {
                                // We've selected 2 cards. Show them for 2 seconds before we continue
                                Ui.drawGame(game);
                                Thread.sleep(2000);
                                game.clearSelected();
                            }
                        }
                        case 'q' -> quit = true;
                    }
                }
            }

            if (game.isFinished()) {
                Ui.drawFinished(game);
                if (terminal.readInput().getCharacter().equals('y')) {
                    game = new Game();
                } else {
                    quit = true;
                }
            }
        }
    }
}