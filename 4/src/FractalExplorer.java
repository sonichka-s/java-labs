import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private final int windowSize;

    private final JImageDisplay display;
    private final FractalGenerator fractal;
    private final Rectangle2D.Double rect;

    public FractalExplorer(int size) {
        windowSize = size;

        fractal = new Mandelbrot();
        rect = new Rectangle2D.Double();
        fractal.getInitialRange(rect);
        display = new JImageDisplay(windowSize, windowSize);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal");
        frame.add(display, BorderLayout.CENTER);

        JButton button = new JButton("Reset Display");

        ButtonHandler resetHandler = new ButtonHandler();
        button.addActionListener(resetHandler);

        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(button);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal() {
        for (int x = 0; x < windowSize; x++) {
            for (int y = 0; y < windowSize; y++) {

                double xCoord = fractal.getCoord(rect.x, rect.x + rect.width, windowSize, x);
                double yCoord = fractal.getCoord(rect.y, rect.y + rect.height, windowSize, y);

                int iteration = fractal.numIterations(xCoord, yCoord);

                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }

                else {
                    float hue = 0.6f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }

            }
        }

        display.repaint();
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Reset Display")) {
                fractal.getInitialRange(rect);
                drawFractal();
            }

        }
    }

    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = fractal.getCoord(rect.x,
                    rect.x + rect.width, windowSize, x);

            int y = e.getY();
            double yCoord = fractal.getCoord(rect.y,
                    rect.y + rect.height, windowSize, y);

            fractal.recenterAndZoomRange(rect, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }

    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
