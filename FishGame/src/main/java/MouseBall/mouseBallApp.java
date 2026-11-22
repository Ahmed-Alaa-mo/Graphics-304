package MouseBall;




import com.sun.opengl.util.FPSAnimator;
import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class mouseBallApp extends JFrame implements MouseMotionListener {

    private FPSAnimator animator;
    private GLCanvas glcanvas;
    private mouseBallGLEventListener listener = new mouseBallGLEventListener();

    public static void main(String[] args) {
        new mouseBallApp().animator.start();
    }

    public mouseBallApp() {
        super("Ball Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addMouseMotionListener(this);
        animator = new FPSAnimator(glcanvas, 60);

        add(glcanvas, BorderLayout.CENTER);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        listener.setMouse(e.getX(), e.getY(), glcanvas.getWidth(), glcanvas.getHeight());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);   // نفس التصرف لو الزرار ضاغط
    }
}