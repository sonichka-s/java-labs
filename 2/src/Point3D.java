import java.util.Scanner;
public class Point3D {
    double x, y, z;

    Point3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    Point3D(double user_x, double user_y, double user_z) {
        x = user_x;
        y = user_y;
        z = user_z;
    }

    Point3D(Scanner scan) {
        x = scan.nextDouble();
        y = scan.nextDouble();
        z = scan.nextDouble();
    }

    boolean equals(Point3D point) {
        return (x == point.x) && (y == point.y) && (z == point.z);
    }

    double distanceTo(Point3D point) {
        return Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2) + Math.pow(z - point.z, 2));
    }
}
