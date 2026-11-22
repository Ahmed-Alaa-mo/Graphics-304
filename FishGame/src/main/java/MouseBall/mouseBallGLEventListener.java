package MouseBall;


import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.*;

public class mouseBallGLEventListener implements GLEventListener {
    // 0=up, 1=up-right, 2=up-left, 3=down, 4=down-right, 5=down-left, 6=right, 7=left

    // الاتجاهات اللي بتبعد الكرة عن كل حيطة
    private final int[] OPP_UP = {3, 4, 5};
    private final int[] OPP_DOWN = {0, 1, 2};
    private final int[] OPP_RIGHT = {2, 5, 7};
    private final int[] OPP_LEFT = {1, 4, 6};

    private final double X_MIN = -350.0;
    private final double X_MAX = 350.0;
    private final double Y_MIN = -350.0;
    private final double Y_MAX = 350.0;
    private final int NUMBER_OF_DIRECTIONS = 8;
    private final int MAX_BALLS = 5;
    private final int FISH_SIZE = 25;
    private final double BALL_SPEED = 3;
    private final double ONE_DEGREE = (Math.PI / 180);
    private final double THREE_SIXTY = 2 * Math.PI;

    private double ballRadius = 30;

    // الكرة رقم i: بياناتها في الخانة i من المصفوفات
    private double[] ballXPosition = new double[MAX_BALLS];
    private double[] ballYPosition = new double[MAX_BALLS];
    private int[] direction = new int[MAX_BALLS];
    private boolean[] alive = new boolean[MAX_BALLS];   // الكرة لسه موجودة ولا اتاكلت
    private int ballsLeft;                              // عدد الكور الباقية

    // مكان السمكة (الزاوية السفلية اليسرى للمربع)
    private double fishX = 0;
    private double fishY = 0;

    // مكان الماوس بإحداثيات العالم
    private volatile double mouseX = 0;
    private volatile double mouseY = 0;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(X_MIN, X_MAX, Y_MIN, Y_MAX, -1.0, 1.0);

        play();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        if (checkWin()) {
            String[] options = {"Play", "Exit"};

            int choice = JOptionPane.showOptionDialog(null,
                    "Do you want to play again?",
                    "Play again?",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                play();
            } else {
                System.exit(0);   // Exit أو قفل النافذة بـ X
            }
        }

        for (int i = 0; i < MAX_BALLS; i++) {
            if (alive[i]) {
                updateBallPosition(i);
                drawBall(gl, ballXPosition[i], ballYPosition[i]);
            }
        }

        updateFishPosition();
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        Square(gl, fishX, fishY, FISH_SIZE);

        checkCollision();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    private void drawBall(GL gl, double xBall, double yBall) {
        gl.glColor3f(0.5f, 0.0f, 0.5f);
        gl.glBegin(GL.GL_POLYGON);
        for (double a = 0; a < THREE_SIXTY; a += ONE_DEGREE) {
            double x = xBall + ballRadius * (Math.cos(a));
            double y = yBall + ballRadius * (Math.sin(a));
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    private void Square(GL gl, double startX, double startY, int size) {
        gl.glBegin(GL.GL_POLYGON);

        gl.glVertex2d(startX, startY);
        gl.glVertex2d(startX + size, startY);
        gl.glVertex2d(startX + size, startY + size);
        gl.glVertex2d(startX, startY + size);

        gl.glEnd();
    }

    private void updateBallPosition(int idx) {
        double s = BALL_SPEED;

        // 1) الحركة
        if (direction[idx] == 0) {
            ballYPosition[idx] += s;
        } else if (direction[idx] == 1) {
            ballXPosition[idx] += s;
            ballYPosition[idx] += s;
        } else if (direction[idx] == 2) {
            ballXPosition[idx] -= s;
            ballYPosition[idx] += s;
        } else if (direction[idx] == 3) {
            ballYPosition[idx] -= s;
        } else if (direction[idx] == 4) {
            ballXPosition[idx] += s;
            ballYPosition[idx] -= s;
        } else if (direction[idx] == 5) {
            ballXPosition[idx] -= s;
            ballYPosition[idx] -= s;
        } else if (direction[idx] == 6) {
            ballXPosition[idx] += s;
        } else {
            ballXPosition[idx] -= s;
        }

        // 2) الحيطان اللي الكرة لمستها (الحافة هي اللي بتلمس)
        boolean top = ballYPosition[idx] + ballRadius >= Y_MAX;
        boolean bottom = ballYPosition[idx] - ballRadius <= Y_MIN;
        boolean right = ballXPosition[idx] + ballRadius >= X_MAX;
        boolean left = ballXPosition[idx] - ballRadius <= X_MIN;

        if (!top && !bottom && !right && !left) {
            return;   // مفيش اصطدام
        }

        // 3) رجّع الكرة جوه الحدود
        if (top) ballYPosition[idx] = Y_MAX - ballRadius;
        if (bottom) ballYPosition[idx] = Y_MIN + ballRadius;
        if (right) ballXPosition[idx] = X_MAX - ballRadius;
        if (left) ballXPosition[idx] = X_MIN + ballRadius;

        // 4) اتجاه جديد
        if (top && right) direction[idx] = 5;          // ركن: اعكس الاتجاه
        else if (top && left) direction[idx] = 4;
        else if (bottom && right) direction[idx] = 2;
        else if (bottom && left) direction[idx] = 1;
        else if (top) direction[idx] = OPP_UP[randNum(0, 3)];
        else if (bottom) direction[idx] = OPP_DOWN[randNum(0, 3)];
        else if (right) direction[idx] = OPP_RIGHT[randNum(0, 3)];
        else direction[idx] = OPP_LEFT[randNum(0, 3)];
    }

    private void updateFishPosition() {
        // السمكة تلحق الماوس (مركزها عند الماوس)
        double targetX = mouseX - FISH_SIZE / 2.0;
        double targetY = mouseY - FISH_SIZE / 2.0;

        fishX += (targetX - fishX) * 0.15;   // 15% من المسافة كل frame
        fishY += (targetY - fishY) * 0.15;

        // متخرجش بره الحدود
        fishX = Math.max(X_MIN, Math.min(fishX, X_MAX - FISH_SIZE));
        fishY = Math.max(Y_MIN, Math.min(fishY, Y_MAX - FISH_SIZE));
    }

    private void checkCollision() {
        double cx = fishX + FISH_SIZE / 2.0;   // مركز السمكة
        double cy = fishY + FISH_SIZE / 2.0;

        for (int i = 0; i < MAX_BALLS; i++) {
            if (!alive[i]) continue;

            double dx = ballXPosition[i] - cx;
            double dy = ballYPosition[i] - cy;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < FISH_SIZE / 2.0 + ballRadius) {
                alive[i] = false;   // الكرة اتاكلت
                ballsLeft--;
            }
        }
    }

    private boolean checkWin() {
        return ballsLeft == 0;
    }

    private void play() {
        for (int i = 0; i < MAX_BALLS; i++) {
            ballXPosition[i] = randNum(X_MIN + ballRadius, X_MAX - ballRadius);
            ballYPosition[i] = randNum(Y_MIN + ballRadius, Y_MAX - ballRadius);
            direction[i] = (int) (Math.random() * NUMBER_OF_DIRECTIONS);
            alive[i] = true;
        }
        ballsLeft = MAX_BALLS;

        fishX = 0;
        fishY = 0;
        mouseX = 0;
        mouseY = 0;
    }

    private int randNum(double a, double b) {
        return (int) (a + (int) (Math.random() * (b - a)));
    }

    // بيتنادى من BallApp لما الماوس يتحرك
    public void setMouse(int px, int py, int width, int height) {
        mouseX = X_MIN + ((double) px / width) * (X_MAX - X_MIN);
        mouseY = Y_MAX - ((double) py / height) * (Y_MAX - Y_MIN);
    }
}