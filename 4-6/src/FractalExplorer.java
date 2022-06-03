import javax.swing.SwingWorker;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author Администратор
 */
public class FractalExplorer {
    private int screenSize;
    private JImageDisplay myScreen;
    private FractalGenerator freak;
    private Rectangle2D.Double area;
    private JComboBox boxList;
    private JFrame frame;
    private JButton buttonSave;
    private JButton button;
    private int stroke;
    public FractalExplorer(int screensize){
        this.screenSize=screensize;
        area = new Rectangle2D.Double();
        freak=new Mandelbrot();
        myScreen=new JImageDisplay(screenSize,screenSize);
        freak.getInitialRange(area);
    }
    public void createAndShowGUI(){
        frame = new JFrame("Fractal Explorer");
        boxList = new JComboBox();
        boxList.addItem(new Mandelbrot());
        boxList.addItem(new Tricorn());
        boxList.addItem(new BurningShip());
        JLabel label = new JLabel("Выпадающий список");
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(boxList);
        boxList.addActionListener(new ButtonHand());
        frame.add(panel, BorderLayout.NORTH);

        buttonSave=new JButton ("Save");
        button = new JButton("Reset");
        JPanel buttons = new JPanel();
        buttons.add(button);
        buttons.add(buttonSave);
        frame.add(buttons, BorderLayout.SOUTH);
        frame.add(myScreen,BorderLayout.CENTER);
        frame.setTitle("Window");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        button.addActionListener(new ButtonHand());
        buttonSave.addActionListener(new ButtonHand());
        MouseHand click = new MouseHand();
        myScreen.addMouseListener(click);
    }
    public void drawFractal(){
        enableUI(false);
        stroke = screenSize;
        for (int x=0;x<screenSize;x++){
            SwingWorker en = new FractalWorker(x);
            en.execute();
        }
    }
    private class ButtonHand implements java.awt.event.ActionListener{
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            if(command.equals("Reset")){
                freak.getInitialRange(area);
                drawFractal();
            }
            if(command.equals("Save")){
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                FileFilter filter1 = new FileNameExtensionFilter("jpg", "jpg");
                FileFilter filter2 = new FileNameExtensionFilter("gif", "gif");
                chooser.addChoosableFileFilter(filter);
                chooser.addChoosableFileFilter(filter1);
                chooser.addChoosableFileFilter(filter2);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.showSaveDialog(frame);
                System.out.println(chooser.getFileFilter().toString());
                try{
                    ImageIO.write(myScreen.image,"png", chooser.getSelectedFile());
                }
                catch(Exception u){
                    JOptionPane.showMessageDialog(frame, u.getMessage(),"ОшибкаСохраненияФайла",JOptionPane.ERROR_MESSAGE);
                }
            }
            if(e.getSource()==boxList){
                freak = (FractalGenerator)boxList.getSelectedItem();
                freak.getInitialRange(area);
                drawFractal();
            }
        }
    }
    private class MouseHand extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e){
            if (stroke!=0)return;
            int x=e.getX();
            int y=e.getY();
            double xCoord = freak.getCoord(area.x,area.x+area.width,screenSize,x);
            double yCoord = freak.getCoord(area.y,area.y+area.height,screenSize,y);
            freak.recenterAndZoomRange(area, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
    private class FractalWorker extends SwingWorker<Object, Object> {
        int cord;
        ArrayList<Integer> mass;
        FractalWorker(int co){
            cord=co;
        }
        @Override
        public Object doInBackground(){
            mass=new ArrayList();
            for (int y=0;y<screenSize;y++){
                double xCoord=FractalGenerator.getCoord(area.x,area.x+area.width,screenSize,cord);
                double yCoord=FractalGenerator.getCoord(area.y,area.y+area.height,screenSize,y);
                int numIters = freak.numIterations(xCoord, yCoord);
                if (numIters ==-1){
                    mass.add(0);
                }
                else{
                    float hue = 0.7f+(float)numIters/200f;
                    int rgbColor=Color.HSBtoRGB(hue, 1f, 1f);
                    mass.add(rgbColor);
                }
            }
            return null;
        }
        @Override
        public void done(){
            for (int y=0;y<screenSize;y++){
                myScreen.drawPixel(cord,y,mass.get(y));
            }
            myScreen.repaint(cord,0,1,screenSize);
            stroke-=1;
            if (stroke==0)enableUI(true);
        }
    }
    void enableUI(boolean val){
        buttonSave.setEnabled(val);
        button.setEnabled(val);
        buttonSave.setEnabled(val);
        boxList.setEnabled(val);

    }
    public static void main(String[] args) {
        FractalExplorer frak=new FractalExplorer(700);
        frak.createAndShowGUI();
        frak.drawFractal();
    }
}