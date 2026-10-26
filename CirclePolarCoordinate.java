package org.Lab5;


import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class CirclePolarCoordinate extends JFrame {

    public static void main(String[] args) {

        new CirclePolarCoordinate();
    }

    //private final JButton jButton1 ;

    public CirclePolarCoordinate() {
        super("First Circle ");
//        jButton1 = new JButton("jButton1");
//        add(jButton1);

        //set the JFrame title
        //super("Ahmad Alaa JFrame ");


        //kill the process when the JFrame is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //only three JOGL lines of code ... and here they are
        GLCanvas glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(new CirclePolarCoordinateGLEventListener());
        add(glcanvas, BorderLayout.CENTER);


        setSize(600,300);




        //center the JFrame on the screen
        setLocationRelativeTo(this);
        setVisible(true);
    }


}




