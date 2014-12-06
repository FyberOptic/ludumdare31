import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
 
public class LD31main {
    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800,600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
         
        // init OpenGL here
         
        while (!Display.isCloseRequested()) {
             
            // render OpenGL here
             
            Display.update();
        }
         
        Display.destroy();
    }
     
    public static void main(String[] argv) {
    	LD31main ld31main = new LD31main();
        ld31main.start();
    }
}