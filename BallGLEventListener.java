package org.Ball;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class BallGLEventListener implements GLEventListener {

    private final double X_MIN = -350.0;
    private final double X_MAX = 350.0;
    private final double Y_MIN = -350.0;
    private final double Y_MAX = 350.0;
    private final int NUMBER_OF_DIRECTIONS = 8;

    private final double ONE_DEGREE = (Math.PI / 180);
    private final double THREE_SIXTY = 2 * Math.PI;

    private final int NUMBER_OF_BALLS = 10  ;
    private double ballXPosition[] = new double[NUMBER_OF_BALLS];
    private double ballYPosition[] = new double[NUMBER_OF_BALLS];
    private double ballRadius;

    private float red[] = new float[NUMBER_OF_BALLS];
    private float green[] = new float[NUMBER_OF_BALLS];
    private float blue[] = new float[NUMBER_OF_BALLS];

    // 0=up, 1=down, 2=right, 3=left, 4=up-right, 5=down-left, 6=up-left, 7=down-right
    private int direction[] = new int[NUMBER_OF_BALLS] ;


    int Opp_left[] ={2,4,7};
    int Opp_right[] ={3,5,6};
    int Opp_up[] ={1,5,7};
    int Opp_down[] ={0,5,6};
    int Opp_up_right_xMax[] ={3,5,6};
    int Opp_up_right_yMax[] ={1,5,7};
    int Opp_up_right_xMax_yMax[] ={1,3,5};
    int Opp_up_left_xMin[] ={2,4,7};
    int Opp_up_left_yMax[] ={1,5,7};
    int Opp_up_left_xMin_yMax[] ={1,2,7};
    int Opp_down_left_xMin[] ={2,4,7};
    int Opp_down_left_yMin[] ={0,4,6};
    int Opp_down_left_xMin_yMin[] ={0,2,4};
    int Opp_down_right_xMax[] ={3,5,6};
    int Opp_down_right_yMin[] ={0,4,6};
    int Opp_down_right_xMax_yMin[] ={0,3,6};



    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(X_MIN, X_MAX, Y_MIN, Y_MAX, -1.0, 1.0);

        for(int i =0 ; i< NUMBER_OF_BALLS  ; i++){
            ballXPosition[i] = random(X_MIN, X_MAX);
            ballYPosition[i] = random(Y_MIN,Y_MAX);
            direction[i] = (int) (Math.random() * NUMBER_OF_DIRECTIONS);

            red[i] = (float)Math.random();
            green[i] = (float)Math.random();
            blue[i] = (float)Math.random();
        }
        ballRadius = 30;



    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        System.err.println("Display is Running");
        GL gl = glAutoDrawable.getGL();
        //updateDirection();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        for(int i=0 ; i<NUMBER_OF_BALLS ; i++){
            updateBallPosition(i);
            drawBall(gl, ballXPosition[i], ballYPosition[i],red[i], green[i], blue[i]);
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
    private void drawBall(GL gl, double XBall, double YBall,float r, float g, float b){

        gl.glColor3f(r, g, b );
        gl.glBegin(GL.GL_POLYGON);
        for (double a = 0; a < THREE_SIXTY; a += ONE_DEGREE) {
            double x = XBall + ballRadius * (Math.cos(a));
            double y = YBall + ballRadius * (Math.sin(a));
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    /**
     * Update the direction of the ball if the steps number equals to zero and reset the value of the
     * steps.
     */
//    private void updateDirection() {
//        steps--;
//        if (steps != 0) {
//            return;
//        }
//        steps = 1 + (int) (Math.random() * MAX_STEPS);
//        int newDirection = direction;
//        while (newDirection == direction) {
//            newDirection = (int) (Math.random() * NUMBER_OF_DIRECTIONS);
//        }
//        direction = newDirection;
//    }

    /**
     * Update the position of the ball depending on the direction.
     */
    private void updateBallPosition(int idx) {

        // 0=up, 1=down, 2=right, 3=left, 4=up-right, 5=down-left, 6=up-left, 7=down-right
        // The Movies
        if (direction[idx] == 0) { // up
            ballYPosition[idx]++;
        } else if (direction[idx] == 1) { // down
            ballYPosition[idx]--;
        } else if (direction[idx] == 2) { // right
            ballXPosition[idx]++;
        } else if(direction[idx] == 3) { // left
            ballXPosition[idx]--;
        }else if (direction[idx] == 4) { //up-right
            ballYPosition[idx]++;
            ballXPosition[idx]++;
        } else if (direction[idx] == 5) { // down-left
            ballYPosition[idx]--;
            ballXPosition[idx]--;
        } else if (direction[idx] == 6) { // up-left
            ballYPosition[idx]++;
            ballXPosition[idx]--;
        } else { // down-right
            ballXPosition[idx]++;
            ballYPosition[idx]--;
        }

        // 0=up, 1=down, 2=right, 3=left, 4=up-right, 5=down-left, 6=up-left, 7=down-right
        // The Limits and how to change the direction in
        if(direction[idx]==0){//up
            if(ballYPosition[idx] >Y_MAX){
                direction [idx]= Opp_up[random(0,3)] ;
            }
        }
        if(direction[idx]==1){//down
            if(ballYPosition[idx] <Y_MIN){
                direction[idx] = Opp_down[random(0,3)] ;
            }
        }
        if(direction[idx]==2){//right
            if(ballXPosition[idx] >X_MAX){
                direction[idx] = Opp_right[random(0,3)] ;
            }
        }
        if(direction[idx]==3){//left
            if(ballXPosition[idx] <X_MIN){
                direction[idx] = Opp_left[random(0,3)] ;
            }
        }
        if(direction[idx]==4){//up_right
            if(ballYPosition[idx] >Y_MAX && ballXPosition[idx] >X_MAX ){
                direction [idx]= Opp_up_right_xMax_yMax[random(0,3)];
            } else if(ballYPosition[idx] >Y_MAX){
                direction[idx] = Opp_up_right_yMax[random(0,3)] ;
            } else if(ballXPosition[idx] >X_MAX){
                direction[idx] = Opp_up_right_xMax[random(0,3)] ;
            }

        }
        if(direction[idx]==5){//down_left
            if(ballYPosition[idx] < Y_MIN && ballXPosition[idx] <X_MIN){
                direction[idx] = Opp_down_left_xMin_yMin[random(0,3)] ;
            }else if(ballYPosition [idx]< Y_MIN){
                direction[idx] = Opp_down_left_yMin[random(0,3)] ;
            }else if(ballXPosition[idx] <X_MIN){
                direction[idx] = Opp_down_left_xMin[random(0,3)] ;
            }

        }
        if(direction[idx]==6){//up_left
            if(ballYPosition[idx] >Y_MAX && ballXPosition[idx] <X_MIN){
                direction[idx] = Opp_up_left_xMin_yMax[random(0,3)] ;
            }else if(ballYPosition[idx] >Y_MAX){
                direction[idx] = Opp_up_left_yMax[random(0,3)] ;
            }else if(ballXPosition[idx] <X_MIN){
                direction[idx] = Opp_up_left_xMin[random(0,3)] ;
            }

        }
        if(direction[idx]==7){//down_right
            if(ballYPosition[idx] <Y_MIN && ballXPosition [idx]>X_MAX){
                direction [idx]= Opp_down_right_xMax_yMin[random(0,3)] ;
            }else if(ballYPosition[idx] <Y_MIN){
                direction[idx] = Opp_down_right_yMin[random(0,3)] ;
            }else if(ballXPosition[idx] >X_MAX){
                direction[idx] = Opp_down_right_xMax[random(0,3)] ;
            }
        }



    }


    private int random (double x ,double y){
        return (int)(x + (int)(Math.random()*(y-x))) ;
    }

}
