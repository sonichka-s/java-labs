/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.awt.geom.Rectangle2D;
/**
 *
 * @author Администратор
 */
public class BurningShip extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double range){
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }
    public int numIterations(double x, double y){
        double z = 0;
        double zi = 0;
        for(int i = 0;i < MAX_ITERATIONS; i++){
            double zrealup = z * z - zi * zi + x;
            double zimup = 2 * Math.abs(z * zi) + y;
            z = zrealup;
            zi = zimup;
            if(z * z + zi * zi > 4){
                return i;
            }
        }
        return -1;
    }
    @Override
    public String toString() {
        return "BurningShip";
    }
}