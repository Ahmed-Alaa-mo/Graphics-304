package org.Lab6;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class CarEventListener implements GLEventListener {

    // this two lines for the circle
    final double ONE_DEGREE = (Math.PI/180);
    final double THREE_SIXTY = 2 * Math.PI;

    // default values(means do nothing), and it is changes when we press keys .there is change happen from keyPressed function in Car class  .
    public double scaleXValue = 1;
    public double scaleYValue = 1;
    public double translateXValue = 0;
    public double translateYValue = 0;


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

        //////الميثود دى بصمجها زى مهى كدا مش لازم تفهم

        GL gl = glAutoDrawable.getGL();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(-250.0, 250.0, -150.0, 150.0, -1, 1);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        System.err.println("display");

        GL gl = glAutoDrawable.getGL();

        drawGraph(gl);
        drawPolygon(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    private void drawGraph(GL gl) {
        ////// الميثود دى بتعمل الرسم البيانى مش مهم تفهمها برده يخويا
        float red;
        float green;
        float blue;
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        //drawing the grid
        red = 0.2f;
        green = 0.2f;
        blue = 0.2f;
        gl.glColor3f(red, green, blue);
        /////////////////////////////
        //You may notice I'm using GL_LINES here.
        //Details of glBegin() will be discussed latter.
        gl.glBegin(GL.GL_LINES);
        //draw the vertical lines
        for (int x = -250; x <= 250; x += 10) {
            gl.glVertex2d(x, -150);
            gl.glVertex2d(x, 150);
        }
        //draw the horizontal lines
        for (int y = -150; y <= 150; y += 10) {
            gl.glVertex2d(-250, y);
            gl.glVertex2d(250, y);
        }
        gl.glEnd();
        //////////////////////////////
        // draw the x-axis and y-axis
        red = 0.0f;
        green = 0.2f;
        blue = 0.4f;
        gl.glColor3f(red, green, blue);
        gl.glBegin(GL.GL_LINES);
        //line for y-axis
        gl.glVertex2d(0, 140);
        gl.glVertex2d(0, -140);
        //line for x-axis
        gl.glVertex2d(240, 0);
        gl.glVertex2d(-240, 0);
        gl.glEnd();
        /////////////////////
        // draw arrow heads
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex2d(0, 150);
        gl.glVertex2d(-5, 140);
        gl.glVertex2d(5, 140);
        gl.glVertex2d(0, -150);
        gl.glVertex2d(-5, -140);
        gl.glVertex2d(5, -140);
        gl.glVertex2d(250, 0);
        gl.glVertex2d(240, -5);
        gl.glVertex2d(240, 5);
        gl.glVertex2d(-250, 0);
        gl.glVertex2d(-240, -5);
        gl.glVertex2d(-240, 5);
        gl.glEnd();
    }

    private void drawPolygon(GL gl) {
        ////// دى اهم ميثود فيهم الشغل كله هنا

        gl.glPushMatrix();

        //move It adds a shift to every point. in x and y and z  Ax . 0 bcz it is Additive identity
        gl.glTranslated(translateXValue, translateYValue, 0);
        //resize It multiplies every point by a factor. 1 bcz it is Multiplicative identity
        gl.glScaled(scaleXValue, scaleYValue, 1);

        // upper and lower body of the car
        gl.glColor3f(1, 0, 0);
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2i(0,0);
        gl.glVertex2i(80,0);
        gl.glVertex2i(80,30);
        gl.glVertex2i(0,30);

        gl.glVertex2i(15,30);
        gl.glVertex2i(25,50);
        gl.glVertex2i(55,50);
        gl.glVertex2i(65,30);
        gl.glEnd();

        // the  windows of the car
        gl.glColor3f(0, 0, 1);
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2i(20,33);
        gl.glVertex2i(25,44);
        gl.glVertex2i(38,44);
        gl.glVertex2i(38,33);
        gl.glEnd();


        gl.glColor3f(0, 0, 1);
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2i(42,33);
        gl.glVertex2i(42,44);
        gl.glVertex2i(55,44);
        gl.glVertex2i(60,33);
        gl.glEnd();

        // الكشاف اللى ورا فى العربيه
        gl.glColor3f(1, 1, 0);
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2i(74,16);
        gl.glVertex2i(80,16);
        gl.glVertex2i(80,24);
        gl.glVertex2i(74,24);
        gl.glEnd();

        // الكوتشات والكشاف اللى قدام
        gl.glColor3f(0.4f, 0.2f, 0.2f);
        drawCircle(gl, 20, 0, 8);
        drawCircle(gl, 60, 0, 8);

        gl.glColor3f(0.0f, 0.0f, 0.0f);
        drawCircle(gl, 20, 0, 4);
        drawCircle(gl, 60, 0, 4);

        gl.glColor3f(1f, 1f, 0f);
        drawCircle(gl, 0, 20, 6);


        gl.glPopMatrix();

    }

    //خليت رسمه الدايره فى ميثود واستدعيها على طول احسن
    private void drawCircle(GL gl, double cx, double cy, double r) {
        double x, y;

        gl.glBegin(GL.GL_POLYGON);
        for (double a = 0; a < THREE_SIXTY; a += ONE_DEGREE) {
            x = r * Math.cos(a) + cx;
            y = r * Math.sin(a) + cy;
            gl.glVertex2d(x, y);
        }
        gl.glEnd();

    }
}