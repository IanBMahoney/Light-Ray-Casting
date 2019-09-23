/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package literaytracing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @authors Ian Mahoney, Adam Ohls, Andrew Thomas
 */
public class Input extends KeyAdapter implements Runnable{

    LiteRayTracing driver;
    boolean w, s, a, d;
    
    @Override
    public void run() {
    }
    
    public Input(LiteRayTracing driver) {
        this.driver = driver;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                w = true;
                break;
            case KeyEvent.VK_S:
                s = true;
                break;
            case KeyEvent.VK_A:
                a = true;
                break;
            case KeyEvent.VK_D:
                d = true;
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                w = false;
                break;
            case KeyEvent.VK_S:
                s = false;
                break;
            case KeyEvent.VK_A:
                a = false;
                break;
            case KeyEvent.VK_D:
                d = false;
                break;
        }
    } 
}
