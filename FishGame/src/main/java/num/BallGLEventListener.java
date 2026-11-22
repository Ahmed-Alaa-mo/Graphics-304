package num;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BallGLEventListener implements GLEventListener {
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
    private final int NUMBER_OF_DIRECTIONS = 8;
    private final int MAX_STEPS = 30;
    private final int MAX_BALLS = 5;
    private final int FISH_SIZE = 25;
    private final double ONE_DEGREE = (Math.PI / 180);
    private final double THREE_SIXTY = 2 * Math.PI;

    private List<Double> ballX = new ArrayList<>(MAX_BALLS);
    private List<Double> ballY = new ArrayList<>(MAX_BALLS);

    private double ballRadius;
    private boolean[] fishMoveDirection = new boolean[NUMBER_OF_DIRECTIONS];
    private double fishX = 0;
    private double fishY = 0;
    private int[] direction = new int[MAX_BALLS];
    private int steps;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(X_MIN, X_MAX, Y_MIN, Y_MAX, -1.0, 1.0);

        ballRadius = 30;
        steps = 1 + (int) (Math.random() * MAX_STEPS);

        play();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        if (checkWin()) {
            String[] options = {"Play", "Exit"};

            int choice = JOptionPane.showOptionDialog(null,
                    "Do you want to play again",
                    "Play again?",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                play();
            } else if (choice == 1) {
                System.exit(0);
            }
        }

        for (int i = 0; i < ballX.size(); i++) {
            updateBallPosition(i);
            drawBall(gl, ballX.get(i), ballY.get(i));
        }

        Square(gl, fishX, fishY, FISH_SIZE);
        updateFishPosition();

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
        double x = ballX.get(idx);
        double y = ballY.get(idx);

        if (direction[idx] == 0) {
            y += 3;
        } else if (direction[idx] == 1) {
            x += 3;
            y += 3;
        } else if (direction[idx] == 2) {
            x -= 3;
            y += 3;
        } else if (direction[idx] == 3) {
            y -= 3;
        } else if (direction[idx] == 4) {
            x += 3;
            y -= 3;
        } else if (direction[idx] == 5) {
            x -= 3;
            y -= 3;
        } else if (direction[idx] == 6) {
            x += 3;
        } else {
            x -= 3;
        }

        ballX.set(idx, x);
        ballY.set(idx, y);

        if (direction[idx] == 0) {
            if (y > Y_MAX - ballRadius) {
                direction[idx] = OPP_UP[randNum(0, 3)];
            }
        }

        if (direction[idx] == 1) {
            if (x > X_MAX - ballRadius || y > Y_MAX - ballRadius) {
                direction[idx] = OPP_UP_RIGHT[randNum(0, 3)];
            }
        }

        if (direction[idx] == 2) {
            if (x < X_MIN + ballRadius || y > Y_MAX - ballRadius) {
                direction[idx] = OPP_UP_LEFT[randNum(0, 3)];
            }
        }

        if (direction[idx] == 3) {
            if (y < Y_MIN + ballRadius) {
                direction[idx] = OPP_DOWN[randNum(0, 3)];
            }
        }

        if (direction[idx] == 4) {
            if (y < Y_MIN + ballRadius || x > X_MAX - ballRadius) {
                direction[idx] = OPP_DOWN_RIGHT[randNum(0, 3)];
            }
        }

        if (direction[idx] == 5) {
            if (y < Y_MIN + ballRadius || x < X_MIN + ballRadius) {
                direction[idx] = OPP_DOWN_LEFT[randNum(0, 3)];
            }
        }

        if (direction[idx] == 6) {
            if (x > X_MAX - ballRadius) {
                direction[idx] = OPP_RIGHT[randNum(0, 3)];
            }
        }

        if (direction[idx] == 7) {
            if (x < X_MIN + ballRadius) {
                direction[idx] = OPP_LEFT[randNum(0, 3)];
            }
        }
    }

    private void updateFishPosition() {
        if (fishMoveDirection[0]) setFishY(3);
        if (fishMoveDirection[1]) setFishY(-3);
        if (fishMoveDirection[2]) setFishX(3);
        if (fishMoveDirection[3]) setFishX(-3);
    }

    private void checkCollision() {
        for (int idx = 0; idx < ballX.size(); idx++) {
            double bx = ballX.get(idx);
            double by = ballY.get(idx);
            double distance = Math.hypot(bx - fishX, by - fishY);

            if (distance < FISH_SIZE + ballRadius) {
                ballX.remove(idx);
                ballY.remove(idx);
                idx--;
            }
        }
    }

    private boolean checkWin() {
        return ballX.isEmpty();
    }

    private void play() {
        ballX.clear();
        ballY.clear();

        for (int i = 0; i < MAX_BALLS; i++) {
            ballX.add((double) randNum(X_MIN, X_MAX));
            ballY.add((double) randNum(Y_MIN, Y_MAX));
            direction[i] = (int) (Math.random() * NUMBER_OF_DIRECTIONS);
        }

        fishX = 0;
        fishY = 0;
        Arrays.fill(fishMoveDirection, false);
    }

    private int randNum(double a, double b) {
        return (int) (a + (int) (Math.random() * (b - a)));
    }

    private void setFishX(double fishX) {
        if (this.fishX + fishX < X_MAX - FISH_SIZE && this.fishX + fishX > X_MIN) {
            this.fishX += fishX;
        }
    }

    private void setFishY(double fishY) {
        if (this.fishY + fishY < Y_MAX - FISH_SIZE && this.fishY + fishY > Y_MIN) {
            this.fishY += fishY;
        }
    }

    public void setFishMoveDirection(int direction, boolean moving) {
        this.fishMoveDirection[direction] = moving;
    }
}