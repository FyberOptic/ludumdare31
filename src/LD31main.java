import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class LD31main {

	public static final int TILE_DIM = 16;	
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	static List<Sprite> sprites = new ArrayList<Sprite>();
	static List<Sprite> deadsprites = new ArrayList<Sprite>();
	static List<Sprite> newsprites = new ArrayList<Sprite>();
	
	static Texture textureAtlas = null;
	
	static Planet planet = null;
	Ship ship = null;
	
	static Audio shootSound = null;
	static Audio breakSound = null;
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
	    return System.nanoTime();// / 1000000;
	}
	
	
	public static double getDistance(Sprite s1, Sprite s2)
	{
		double x1 = s2.xPos - s1.xPos;
		double y1 = s2.yPos - s1.yPos;
				
		return Math.sqrt(x1 * x1 + y1 * y1);		
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
	
	
	public void fireBullet()
	{
		Bullet bullet = new Bullet();
		bullet.xPos = ship.xPos;
		bullet.yPos = ship.yPos;
		bullet.rot = ship.rot;
		bullet.xVel = (float) (Math.cos(Math.toRadians(ship.rot - 90)) * 100);
		bullet.yVel = (float) (Math.sin(Math.toRadians(ship.rot - 90)) * 100);
		sprites.add(bullet);
		 
		shootSound.playAsSoundEffect((float) (0.9 + (Math.random() * 0.2)),  0.75f,  false);
	}
	
	public void spawnAsteroid()
	{
		Asteroid a = new Asteroid(true);
		a.xPos = (Math.random() * 320) - 80;
		a.yPos = (Math.random() * 240) - 60;
		a.xVel = 10;
		a.yVel = -4;
		a.rotVel = 10;
		sprites.add(a);
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
		
        shootSound = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/shoot.wav"));
        breakSound = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/break.wav"));
        
		textureAtlas = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/atlas.png"));
		textureAtlas.setTextureFilter(GL11.GL_NEAREST);
		
		GL11.glColor3f(1, 1, 1);;
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		
		long lastTime = getTime();
		int tickCounter = 0;
		int fpscount = 0;
		
		planet = new Planet();
		ship = new Ship();
		Asteroid asteroid = new Asteroid(true);	
		
		sprites.add(planet);		
		//sprites.add(asteroid);
		sprites.add(ship);
		
		ship.xVel = 1;
		ship.yVel = 2;
		
		asteroid.xPos = 20;
		asteroid.xVel = 10;
		asteroid.yVel = -5;
		asteroid.rotVel = 10;
		
		spawnAsteroid();
		
		long asteroidTimer = 0;
		
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
        	    	
        	    	if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) fireBullet();
        	    	
        	    	if (Keyboard.getEventKey() == Keyboard.KEY_BACK)
        	    	{
        	    		ship.xVel = 0;
        	    		ship.yVel = 0;
        	    	}
        	    }
        	}
        	
        	while (Mouse.next())
        	{
        		if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) fireBullet();
        	}
        	
        	float turnSpeed = 160;
        	float accelSpeed = 25;
        	
        	if (Keyboard.isKeyDown(Keyboard.KEY_A)) ship.rot -= turnSpeed * deltaf;
        	if (Keyboard.isKeyDown(Keyboard.KEY_D)) ship.rot += turnSpeed * deltaf;
        	if (Keyboard.isKeyDown(Keyboard.KEY_W))
        	{
        		ship.xVel += Math.cos(Math.toRadians(ship.rot - 90)) * accelSpeed * deltaf;
        		ship.yVel += Math.sin(Math.toRadians(ship.rot - 90)) * accelSpeed * deltaf;
        		ship.thrusting = true;
        	}
        	else ship.thrusting = false;
        	
        	boolean hasAsteroids = false;
        	
        	for (Sprite s : sprites) { s.update(deltaTicks); if (s.remove) deadsprites.add(s); if (s instanceof Asteroid) hasAsteroids = true; }
        	
        	for (Sprite s : sprites) s.render();
        	
        	for (Sprite s : newsprites) sprites.add(s);
        	newsprites.clear();
        	for (Sprite s : deadsprites) sprites.remove(s);
        	deadsprites.clear();
        	
        	if (!hasAsteroids) spawnAsteroid();
        	asteroidTimer += deltaTicks;
        	if (asteroidTimer > 10000000000L)
        	{
        		asteroidTimer -= 10000000000L;
        		spawnAsteroid();
        	}
        	 
        	//drawTile(1,0,0);
        	//drawTile(2,0,0);
        	
        	//drawTile((int)(Math.random() * 2), (float)Math.random() * SCREEN_WIDTH, (float)Math.random() * SCREEN_HEIGHT);
        	
            Display.update();
            if (Display.isCloseRequested()) gameLoop = false;
        }
        
		AL.destroy();
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