package org.Lab5;



//import java.awt.event.*;
import javax.media.opengl.*;


class CircleCartesianCoordinateGLEventListener implements GLEventListener {

    final double ONE_DEGREE = (Math.PI/180);
    final double THREE_SIXTY = 2 * Math.PI;

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //gl.glViewport(0, 0, 600, 300);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, 600.0, 0.0, 300.0, -1.0, 1.0);



    }

    public void display(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glColor3f(0.0f, 0.5f, 0.5f);

        double centerX = 300;
        double centerY = 150;
        double radius = 100;

        gl.glBegin(GL.GL_POINTS);

        for (double x = -radius; x <= radius; x += 1) {

            double y = Math.sqrt(
                    radius * radius - x * x
            );

            gl.glVertex2d(centerX + x, centerY + y);
            gl.glVertex2d(centerX + x, centerY - y);
        }
        gl.glEnd();

    }

    public void reshape(
            GLAutoDrawable drawable,
            int x,
            int y,
            int width,
            int height
    ) {}

    public void displayChanged(
            GLAutoDrawable drawable,
            boolean modeChanged,
            boolean deviceChanged
    ) {}


    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

}



