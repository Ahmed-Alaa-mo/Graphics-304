package Lab7.Ball_labTask;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class BallGLEventListener implements GLEventListener {
// 0=up, 1=up-right, 2=up-left, 3=down, 4=down-right, 5=down-left, 6=right, 7=left
    // up -> 3, 4, 5
    // up-right -> 3, 4, 5
    // up-left -> 3, 4, 5

    // down -> 0, 1, 2
    // down-right -> 0, 1, 2
    // down-left -> 0, 1, 2

    // right -> 7, 2, 5

    // left -> 6, 1, 4
    private final int[] OPP_UP = {3, 4, 5};
    private final int[] OPP_DOWN = {0, 1, 2};
    private final int[] OPP_RIGHT = {2, 5, 7};
    private final int[] OPP_LEFT = {1, 4, 6};

    private final int[] OPP_UP_RIGHT = {2, 5, 7};
    private final int[] OPP_UP_LEFT = {1, 4, 6};
    private final int[] OPP_DOWN_RIGHT = {2, 5, 7};
    private final int[] OPP_DOWN_LEFT = {1, 4, 6};

    private final double X_MIN = -350.0;
    private final double X_MAX = 350.0;
    private final double Y_MIN = -350.0;
    private final double Y_MAX = 350.0;
    private final int NUMBER_OF_DIRECTIONS = 4;
    private final int MAX_STEPS = 30;
    private final int MAX_BALLS = 10;
    private final double ONE_DEGREE = (Math.PI / 180);
    private final double THREE_SIXTY = 2 * Math.PI;
    private double ballXPosition[] = new double[MAX_BALLS];
    private double ballYPosition[] = new double[MAX_BALLS];
    private double ballRadius;
    // 0=up, 1=up-right, 2=up-left, 3=down, 4=down-right, 5=down-left, 6=right, 7=left
    private int[] direction = new int[MAX_BALLS];
    private int steps;
    private float[] ballR = new float[MAX_BALLS];
    private float[] ballG = new float[MAX_BALLS];
    private float[] ballB = new float[MAX_BALLS];


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(X_MIN, X_MAX, Y_MIN, Y_MAX, -1.0, 1.0);

        for(int i = 0; i < MAX_BALLS; i++) {
            ballXPosition[i] = randNum(X_MIN, X_MAX);
            ballYPosition[i] = randNum(Y_MIN, Y_MAX);
            direction[i] = (int) (Math.random() * NUMBER_OF_DIRECTIONS);
            ballR[i] = (float) Math.random();
            ballG[i] = (float) Math.random();
            ballB[i] = (float) Math.random();

        }
        ballRadius = 30;


        steps = 1 + (int) (Math.random() * MAX_STEPS);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        System.err.println("**********************");
//    System.out.println("direction " + direction);
//    System.out.println("x pos " + ballXPosition + " " + "y pos " + ballYPosition);
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//    updateDirection();

        for(int i = 0; i < MAX_BALLS; i++) {
            updateBallPosition(i);

            drawBall(gl, ballXPosition[i], ballYPosition[i],i);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    /**
     * Draw a ball in the center position (ballXPosition, ballYPosition) with radius=ballRadius.
     *
     * @param gl the object of class GL
     */
    private void drawBall(GL gl, double xBall, double yBall,int idx) {
       // System.out.println("----" + xBall + " " + yBall);
//    gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glBegin(GL.GL_POLYGON);
        for (double a = 0; a < THREE_SIXTY; a += ONE_DEGREE) {
            gl.glColor3f(ballR[idx], ballG[idx], ballB[idx]);
            double x = xBall + ballRadius * (Math.cos(a));
            double y = yBall + ballRadius * (Math.sin(a));
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    /**
     * Update the direction of the ball if the steps number equals to zero and reset the value of the
     * steps.
     */
//  private void updateDirection() {
//    steps--;
//    if (steps != 0) {
//      return;
//    }
//    steps = 1 + (int) (Math.random() * MAX_STEPS);
//    int newDirection = direction;
//    while (newDirection == direction) {
//      newDirection = (int) (Math.random() * NUMBER_OF_DIRECTIONS);
//    }
//    direction = newDirection;
//  }

    /**
     * Update the position of the ball depending on the direction.
     */
    private void updateBallPosition(int idx) {
        // 1) التحريك حسب الاتجاه
        switch (direction[idx]) {
            case 0: ballYPosition[idx]++; break;                         // up
            case 1: ballXPosition[idx]++; ballYPosition[idx]++; break;   // up-right
            case 2: ballXPosition[idx]--; ballYPosition[idx]++; break;   // up-left
            case 3: ballYPosition[idx]--; break;                         // down
            case 4: ballXPosition[idx]++; ballYPosition[idx]--; break;   // down-right
            case 5: ballXPosition[idx]--; ballYPosition[idx]--; break;   // down-left
            case 6: ballXPosition[idx]++; break;                         // right
            default: ballXPosition[idx]--; break;                        // left
        }

        // 2) تحديد الحيطان اللي الكرة لمستها (مع حساب نصف القطر)
        boolean top    = ballYPosition[idx] + ballRadius >= Y_MAX;
        boolean bottom = ballYPosition[idx] - ballRadius <= Y_MIN;
        boolean right  = ballXPosition[idx] + ballRadius >= X_MAX;
        boolean left   = ballXPosition[idx] - ballRadius <= X_MIN;

        if (!top && !bottom && !right && !left) {
            return; // مفيش اصطدام
        }

        // 3) إرجاع الكرة جوه الحدود عشان ما تعلقش بره
        if (top)    ballYPosition[idx] = Y_MAX - ballRadius;
        if (bottom) ballYPosition[idx] = Y_MIN + ballRadius;
        if (right)  ballXPosition[idx] = X_MAX - ballRadius;
        if (left)   ballXPosition[idx] = X_MIN + ballRadius;

        // 4) اختيار اتجاه جديد
        if (top && right)         direction[idx] = 5;  // الركن فوق-يمين -> تحت-شمال
        else if (top && left)     direction[idx] = 4;  // الركن فوق-شمال -> تحت-يمين
        else if (bottom && right) direction[idx] = 2;  // الركن تحت-يمين -> فوق-شمال
        else if (bottom && left)  direction[idx] = 1;  // الركن تحت-شمال -> فوق-يمين
        else if (top)             direction[idx] = OPP_UP[randNum(0, 3)];
        else if (bottom)          direction[idx] = OPP_DOWN[randNum(0, 3)];
        else if (right)           direction[idx] = OPP_RIGHT[randNum(0, 3)];
        else                      direction[idx] = OPP_LEFT[randNum(0, 3)];
    }

    private int randNum(double a, double b) {
        return (int) (a + (int) (Math.random() * (b - a)));
    }

    public static void main(String[] args) {
        BallGLEventListener x = new BallGLEventListener();

    }

}
