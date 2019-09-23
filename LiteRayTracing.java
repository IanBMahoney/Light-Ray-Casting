/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package literaytracing;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @authors Ian Mahoney, Adam Ohls, Andrew Thomas
 */
public class LiteRayTracing extends Canvas {

    Input input;
    boolean running = true;
    int[] origin = new int[3]; //x, y, theta
    int vel = 2, vela = 1, xVar, yVar;
    int time, timeAtLastCheck;
    int frames, fps;
    int width = 1000, height = 800;
    final int HORIZON = 400;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    BufferedImage minimap = new BufferedImage(230, 230, BufferedImage.TYPE_INT_ARGB);
    int[][] output = new int[width][2];//h, color;
    final int LIMIT = 100;
    Line[] lines = {new Line(-LIMIT, -LIMIT, LIMIT, -LIMIT), new Line(LIMIT, -LIMIT, LIMIT, LIMIT), new Line(LIMIT, LIMIT, -LIMIT, LIMIT), new Line(-LIMIT, LIMIT, -LIMIT, -LIMIT)};
    double m, b, x, y, d;
    
    
    public LiteRayTracing() {
        JFrame frame = new JFrame("Ray Casting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        input = new Input(this);
        Thread t1 = new Thread(input);
        t1.start();
        this.addKeyListener(input);
        this.requestFocus();
        timeAtLastCheck = (int) System.currentTimeMillis();
        origin[0] = 25;
        origin[1] = 25;
        origin[2] = 90;
        run();
    }
    
    
    public void run() {
        while (running) {
            time = (int) (System.currentTimeMillis());
            if ((time - timeAtLastCheck) >= 1000) {
                fps = frames / ((time - timeAtLastCheck) / 1000);
                System.out.println("FPS: " + fps);
                frames = 0;
                timeAtLastCheck = time;
            }
            tick();
            render();
            keyInput();
            frames += 1;
        }
    }
    
    
    public void tick() {
        for (int i = 0; i < width; i += 1) {
            m = (Math.tan(Math.toRadians(origin[2] + (i * (45 / (double) width)))));
            b = (double) ((double) origin[1] - m * (double) origin[0]);
            for (int j = 0; j < lines.length; j += 1) {
                try {
                    if (lines[j].x1 == lines[j].x2) {
                        x = lines[j].x1;
                        y = m * x + b;

                        if ((x >= -LIMIT) && (x <= LIMIT) && (y >= -LIMIT) && (y <= LIMIT)) {
                            d = Math.sqrt(((x - origin[0]) * (x - origin[0])) + ((y - origin[1]) * (y - origin[1])));
                            //d = d * Math.cos(Math.toRadians(90 - origin[2]));
                            int color;
                            if (d > 255) {
                                color = 0;
                            } else if (d < 0) {
                                color = 200;
                            } else {
                                color = 255 - (int) d;
                            }
                            output[i] = new int[]{(7000 / (int) (d)), color};
                        }
                    } else {
                        y = lines[j].y1;
                        x = (y - b) / m;

                        if ((x >= -LIMIT) && (x <= LIMIT) && (y >= -LIMIT) && (y <= LIMIT)) {
                            d = Math.sqrt(((x - origin[0]) * (x - origin[0])) + ((y - origin[1]) * (y - origin[1])));
                            //d = d * Math.cos(Math.toRadians(origin[2]));
                            int color;
                            if (d > 255) {
                                color = 0;
                            } else if (d < 0) {
                                color = 200;
                            } else {
                                color = 255 - (int) d;
                            }
                            output[i] = new int[]{(7000 / (int) (d)), color};
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
    }
    
    
    public void keyInput() {
        if (input.d) {
            origin[2] += vela;
        }
        if (input.a) {
            origin[2] -= vela;
        }
        if (origin[2] >= 360)
            origin[2] -= 360;
        if (origin[2] < 0)
            origin[2] += 360;
        
        int[][] signs = {{1,1},{1,-1},{-1,-1},{-1,1}};
        if (input.w) {
            origin[0] += signs[((origin[2]-45) / 90)][0] * vel * Math.cos(Math.toRadians(origin[2]));
            origin[1] += signs[((origin[2]-45) / 90)][1] *vel * Math.sin(Math.toRadians(origin[2]));
        }
        if (input.s) {
            origin[0] -= signs[((origin[2]-45) / 90)][0] * vel * Math.cos(Math.toRadians(origin[2]));
            origin[1] -= signs[((origin[2]-45) / 90)][1] *vel * Math.sin(Math.toRadians(origin[2]));
        }
    }
    
    
    public void render() {
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(0, height / 2, width, height / 2);
        g.drawString(" X: " + origin[0] + " Y: " + origin[1] + " Angle: " + origin[2] + " FPS: " + fps, 10, 20);
        for (int i = 0; i < output.length; i += 1) {
            g.setColor(new Color(0, 0, output[i][1]));
            g.drawLine(i, HORIZON + output[i][0], i, HORIZON - output[i][0]);
        }
        g.drawImage(minimap,width-230,0,this);
        g = minimap.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,230,230);
        g.setColor(Color.BLUE);
        for (int i = 0; i < lines.length; i += 1) {
            g.drawLine(lines[i].x1+115,lines[i].y1+115,lines[i].x2+115,lines[i].y2+115);
        }
        g.setColor(Color.red);
        g.drawOval(origin[0]+115,origin[1]+115,5,5);

        g = this.getGraphics();
        g.drawImage(image, 0, 0, this);

        g.dispose();
        output = new int[width][2];
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new LiteRayTracing();
    }

}
