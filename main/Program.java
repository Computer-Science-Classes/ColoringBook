package ColoringBook.main;

import java.awt.Color;
import java.io.IOException;
import java.util.Queue;
import java.util.Stack;

import ColoringBook.graphics.DrawingFrame;
import ColoringBook.graphics.Drawing;

public class Program {
    private class WorkItem {
    int x, y;

    WorkItem(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

    /**
     * Global static fields for the Drawing object being worked on
     * and the DrawingFrame containing and displaying it.
     */
    private static Drawing _drawing;
    private static DrawingFrame _frame;

    /**
     * Demonstrates a simple alteration to the drawing:
     * On a square section of the image, from top-left: (40,30) to bottom-right
     * (140, 130)
     * replace the dark pixels with yellow and the bright pixels with yellow.
     */
    public static void paint() throws InterruptedException {
        for (int x = 40; x < 140; x++) {
            for (int y = 30; y < 130; y++) {
                _frame.step(1);
                if (_drawing.isDarkPixel(x, y)) {
                    _drawing.setPixel(x, y, Color.yellow);
                } else {
                    _drawing.setPixel(x, y, Color.red);
                }
            }
        }
    }

    public static void stackFloodFill(int startX, int startY, Color targetColor, Color replacementColor) {
        if (_drawing.getPixel(startX, startY).equals(replacementColor)) {
            return;
        }

        Stack<WorkItem> s = new Stack<>();
        s.push(new WorkItem(startX, startY));

        while (!s.isEmpty()) {
            WorkItem curr = s.pop();
            int x = curr.x;
            int y = curr.y;

            if (!_drawing.isValidPixel(x, y)) {
                continue;
            }

            Color currColor = _drawing.getPixel(x, y);

            if (currColor.equals(targetColor)) {
                _drawing.setPixel(x, y, replacementColor);

                s.push(new WorkItem(x + 1, y)); // right
                s.push(new WorkItem(x - 1, y)); // left
                s.push(new WorkItem(x, y + 1)); // down
                s.push(new WorkItem(x, y - 1)); // up
            }
        }
    }

    public static void queueFloodFill(int startX, int startY, Color targetColor, Color replacementColor) {
        if (_drawing.getPixel(startX, startY).equals(replacementColor)) {
            return;
        }

        Queue<WorkItem> q = new LinkedList<>();
        q.add(new WorkItem(startX, startY));

        while (!q.isEmpty()) {
            WorkItem curr = q.remove();
            int x = curr.x;
            int y = curr.y;

            if (!_drawing.isValidPixel(x, y)) {
                continue;
            }

            Color currColor = _drawing.getPixel(x, y);

            if (currColor.equals(targetColor)) {
                _drawing.setPixel(x, y, replacementColor);

                q.add(new WorkItem(x + 1, y)); // right
                q.add(new WorkItem(x - 1, y)); // left
                q.add(new WorkItem(x, y + 1)); // down
                q.add(new WorkItem(x, y - 1)); // up
            }
        }
    }

    public static void recursiveFloodFill(int x, int y, Color targetColor, Color replacementColor) {
        if (!_drawing.isValidPixel(x, y)) {
            return;
        }

        Color color = _drawing.getPixel(x, y);
        if (!color.equals(targetColor)) {
            return;
        }

        _drawing.setPixel(x, y, replacementColor);

        recursiveFloodFill(x + 1, y, targetColor, replacementColor); // right
        recursiveFloodFill(x - 1, y, targetColor, replacementColor); // left
        recursiveFloodFill(x, y + 1, targetColor, replacementColor); // down
        recursiveFloodFill(x, y - 1, targetColor, replacementColor); // up

    }

    /**
     * Main entry point in the program:
     * Initializes the static Drawing (_drawing) with an image of your choice,
     * then initializes the static DrawingFrame (_frame) loading into it the new
     * drawing.
     * Subsequently the frame is opened on the screen then the drawing is painted
     * upon
     * and displayed as it is being modified before the program terminates.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Welcome to the Coloring Festival!");

        // pick a drawing
        _drawing = new Drawing("ColoringBook/drawings/bird.jpg");

        // put it in a frame
        _frame = new DrawingFrame(_drawing);

        // put the frame on display and stop to admire it.
        _frame.open();
        _frame.step();

        // make some change to the drawing, then stop for applause.
        paint();
        stackFloodFill(0, 0, Color.BLUE, Color.RED);

        _frame.stop();

        // the show is over.
        _frame.close();

        System.out.println("Well done, goodbye!");
    }
}
