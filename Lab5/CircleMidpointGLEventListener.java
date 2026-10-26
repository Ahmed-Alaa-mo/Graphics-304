package org.Lab5;


//import java.awt.event.*;
import javax.media.opengl.*;


class CircleMidpointGLEventListener implements GLEventListener {

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
        gl.glColor3f(0.5f, 0.5f, 0.5f);

        int centerX = 300;
        int centerY = 150;
        int radius = 100;

        int x = 0;
        int y = radius;

        int p = 1 - radius;

        gl.glPointSize(3.0f);
        gl.glBegin(GL.GL_POINTS);  //GL.GL_POLYGON   VS.   //GL.GL_POINTS

        while (x <= y) {

            // Eight-way symmetry
            gl.glVertex2i(centerX + x, centerY + y);
            gl.glVertex2i(centerX - x, centerY + y);
            gl.glVertex2i(centerX + x, centerY - y);
            gl.glVertex2i(centerX - x, centerY - y);

            gl.glVertex2i(centerX + y, centerY + x);
            gl.glVertex2i(centerX - y, centerY + x);
            gl.glVertex2i(centerX + y, centerY - x);
            gl.glVertex2i(centerX - y, centerY - x);

            x++;

            if (p < 0) {
                p = p + 2 * x + 1;
            } else {
                y--;
                p = p + 2 * (x - y) + 1;
            }
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


