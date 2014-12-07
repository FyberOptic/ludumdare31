import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class LD31main {

	public static final int TILE_DIM = 16;	
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	
	static Texture textureAtlas = null;
	
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
	    return System.nanoTime();// / 1000000;
	}
	
	
	/**
	 * 
	 * @param tilenum
	 * @param x
	 * @param y
	 */
	public void drawTile(int tilenum, float x, float y)
	{
		textureAtlas.bind();
		
		float pixelWidth = 1.0f / textureAtlas.getImageWidth();
		float pixelHeight = 1.0f / textureAtlas.getImageHeight();
		float tileWidth = pixelWidth * TILE_DIM;
		float tileHeight = pixelHeight * TILE_DIM;
		
		int tileX = tilenum % 16;
		int tileY = tilenum / 16;
		
		float tileXpix = tileX * (pixelWidth * 16);
		float tileYpix = tileY * (pixelHeight * 16);
				
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tileXpix, tileYpix);
			GL11.glVertex2f(x, y);
			
			GL11.glTexCoord2f(tileXpix, tileYpix + tileHeight);
			GL11.glVertex2f(x, y + 16);
			
			GL11.glTexCoord2f(tileXpix + tileWidth, tileYpix + tileHeight);			
			GL11.glVertex2f(x + 16, y + 16);
			
			GL11.glTexCoord2f(tileXpix + tileWidth, tileYpix);
			GL11.glVertex2f(x + 16, y);
		GL11.glEnd();
	}
	
	
	/**
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {      
		
		try {
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
		
        GL11.glClearColor(0,0,0,0);        
         
        // Enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);    
        
        // Setup ortho mode
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 160, 120, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        // Setup viewport
        GL11.glViewport(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		
        
		textureAtlas = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/atlas.png"));
		textureAtlas.setTextureFilter(GL11.GL_NEAREST);
		
		GL11.glColor3f(1, 1, 1);;
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		
		long lastTime = getTime();
		int tickCounter = 0;
		int fpscount = 0;
		
		Planet planet = new Planet();
		Ship ship = new Ship();
		Asteroid asteroid = new Asteroid();
		
		List<Sprite> sprites = new ArrayList<Sprite>();
		
		sprites.add(planet);		
		sprites.add(asteroid);
		sprites.add(ship);
		
		ship.xVel = 1;
		ship.yVel = 2;
		
		asteroid.xVel = 1;
		asteroid.yVel = 1;
		asteroid.rotVel = 10;
		
        boolean gameLoop = true;
		while (gameLoop) 
        {
        	long thisTime = getTime();
        	int deltaTicks = (int) (thisTime - lastTime);
        	double deltaf = deltaTicks / 1000000000.0D;
        	
        	lastTime = thisTime;
        	tickCounter += deltaTicks;
        	
        	if (tickCounter >= 1000000000)
        	{
        		tickCounter -= 1000000000;
        		System.out.println("FPS: " + fpscount);
        		fpscount = 0;
        	}
        	
        	
        	fpscount++;
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
        	while (Keyboard.next()) 
        	{
        	    if (Keyboard.getEventKeyState()) 
        	    {
        	    	if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) gameLoop = false; 
        	    	
        	    	if (Keyboard.getEventKey() == Keyboard.KEY_SPACE)
        	    	{
        	    		Bullet bullet = new Bullet();
        	    		bullet.xPos = ship.xPos;
        	    		bullet.yPos = ship.yPos;
        	    		bullet.rot = ship.rot;
        	    		bullet.xVel = (float) (Math.cos(Math.toRadians(ship.rot - 90)) * 80);
        	    		bullet.yVel = (float) (Math.sin(Math.toRadians(ship.rot - 90)) * 80);
        	    		sprites.add(bullet);
        	    	}
        	    }
        	}
        	
        	float turnSpeed = 100;
        	float accelSpeed = 20;
        	
        	if (Keyboard.isKeyDown(Keyboard.KEY_A)) ship.rot -= turnSpeed * deltaf;
        	if (Keyboard.isKeyDown(Keyboard.KEY_D)) ship.rot += turnSpeed * deltaf;
        	if (Keyboard.isKeyDown(Keyboard.KEY_W))
        	{
        		ship.xVel += Math.cos(Math.toRadians(ship.rot - 90)) * accelSpeed * deltaf;
        		ship.yVel += Math.sin(Math.toRadians(ship.rot - 90)) * accelSpeed * deltaf;
        	}
        	
        	for (Sprite s : sprites) s.update(deltaTicks);
        	
        	for (Sprite s : sprites) s.render();
        	
        	//drawTile(1,0,0);
        	//drawTile(2,0,0);
        	
        	//drawTile((int)(Math.random() * 2), (float)Math.random() * SCREEN_WIDTH, (float)Math.random() * SCREEN_HEIGHT);
        	
            Display.update();
            if (Display.isCloseRequested()) gameLoop = false;
        }
         
        Display.destroy();
    }
    
	
	/**
	 * 
	 * @param argv
	 * @throws IOException
	 */
    public static void main(String[] argv) throws IOException {
    	LD31main ld31main = new LD31main();
        ld31main.start();
    }
}