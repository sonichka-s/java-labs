import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        double newWidth = range.width;
        double newHeight = range.height;

        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    @Override
    public int numIterations(double x, double y) {
        int iterator = 0;
        double real = 0;
        double imaginary = 0;

        while (iterator < MAX_ITERATIONS && real * real + imaginary * imaginary < 4) {
            real = real * real - imaginary * imaginary + x;
            imaginary = 2 * real * imaginary + y;
            iterator++;
        }

        if (iterator == MAX_ITERATIONS) {
            return -1;
        }
        return iterator;
    }
}