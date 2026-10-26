package org.Lab5;


//import java.awt.event.*;
import javax.media.opengl.*;


class CirclePolarCoordinateGLEventListener implements GLEventListener {

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

        double x,y;

        double shiftXPosition = 300;
        double shiftYPosition = 150;

        double radius = 100;
        float red = 0.0f;
        float green = 1.0f;
        float blue = 0.0f;

        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(red, green, blue);
        gl.glBegin(GL.GL_LINE_LOOP);         //GL.GL_LINE_LOOP  Outline circle  //GL.GL_POLYGON Filled circle
        // angle is
        // x = radius * (cosine of angle)
        // y = radius * (sine of angle)
        for (double a=0; a<THREE_SIXTY; a+=ONE_DEGREE) {
            x = radius * Math.cos(a) + shiftXPosition;
            y = radius * Math.sin(a) + shiftYPosition;
            gl.glVertex2d(x, y);
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


