import java.util.Scanner;
public class Lab1 {
    static double computeArea(Point3D point1, Point3D point2, Point3D point3) {
        double AB = point1.distanceTo(point2);
        double BC = point2.distanceTo(point3);
        double CA = point3.distanceTo(point1);
        double p = (AB + BC + CA) / 2;
        return Math.sqrt(p * (p - AB) * (p - BC) * (p - CA));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("First point: ");
        Point3D A = new Point3D(scanner);
        System.out.println("Second point: ");
        Point3D B = new Point3D(scanner);
        System.out.println("Third point: ");
        Point3D C = new Point3D(scanner);

        if ((A.equals(B)) && (A.equals(C))) {
            System.out.println("Points are EQUAL. Couldn't calculate area.");
            return;
        }

        double sqrt = computeArea(A, B, C);
        System.out.println("Computed area: " + sqrt);
    }
}
