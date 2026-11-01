package org.Lab6;



import java.awt.BorderLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;

public class Car extends JFrame implements  KeyListener {

    private GLCanvas glcanvas;
    private CarEventListener listener = new CarEventListener();

    public static void main(String[] args) {
        new org.Lab6.Car();
    }

    public Car() {
        super("Car Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(this);



        add(glcanvas, BorderLayout.CENTER);

        setSize(500, 300);
        setLocationRelativeTo(this);
        setVisible(true);
        // give keyboard focus to the canvas so the arrow keys work immediately
        glcanvas.requestFocus();
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            listener.translateXValue -= 5;
        } else if (key == KeyEvent.VK_RIGHT) {
            listener.translateXValue += 5;
        } else if (key == KeyEvent.VK_UP) {
            listener.scaleXValue += 0.1;
            listener.scaleYValue += 0.1;
        } else if (key == KeyEvent.VK_DOWN) {
            listener.scaleXValue -= 0.1;
            listener.scaleYValue -= 0.1;
        }

        // بنظبط اخرك فين فى التكبير والتصغير عشان العربيه متختفيش او تتقلب
        if (listener.scaleXValue < 0.3) listener.scaleXValue = 0.3;
        if (listener.scaleXValue > 3)   listener.scaleXValue = 3;
        if (listener.scaleYValue < 0.3) listener.scaleYValue = 0.3;
        if (listener.scaleYValue > 3)   listener.scaleYValue = 3;

        //بنظبط الليميت عشان العربيه متخرجش برا الاطار بتاعنا
        //right limit
        //متظبطه على جسم العربيه عشان هو ابرز حته فى العربيه من ناحيه اليمين
        if (listener.translateXValue > 250 - 80 * listener.scaleXValue)
            listener.translateXValue = 250 - 80 * listener.scaleXValue;
        //left limit
        //مظبوطه على كشاف العربيه عشان هو ابرز حته من ناحيه الشمال
        if (listener.translateXValue < -250 + 6 * listener.scaleXValue)
            listener.translateXValue = -250 + 6 * listener.scaleXValue;
        /////////////
        glcanvas.repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}