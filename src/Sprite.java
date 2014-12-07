import org.lwjgl.opengl.GL11;


public class Sprite 
{
	public int tileNum = 0;
	public double xPos = 0;
	public double yPos = 0;
	public float xVel = 0;
	public float yVel = 0;
	public double rot = 0;
	public double rotVel = 0;
	
	public float radius = 0;
	
	public float tileWidth = 16;
	public float tileHeight = 16;
	
	public boolean remove = false;
	
	public void render()
	{
		LD31main.textureAtlas.bind();
		
		float pixelWidth = 1.0f / LD31main.textureAtlas.getImageWidth();
		float pixelHeight = 1.0f / LD31main.textureAtlas.getImageHeight();
		float tileUwidth = pixelWidth * tileWidth;
		float tileVheight = pixelHeight * tileHeight;
		
		int tileX = tileNum % 16;
		int tileY = tileNum / 16;
		
		float tileXpix = tileX * (pixelWidth * 16);
		float tileYpix = tileY * (pixelHeight * 16);
		
		GL11.glPushMatrix();
		GL11.glTranslated(xPos,  yPos,  0);
		GL11.glRotated(rot,  0, 0,  1);
		
		float halfWidth = this.tileWidth / 2.0f;
		float halfHeight = this.tileHeight / 2.0f;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tileXpix, tileYpix);
			GL11.glVertex2d(0 - halfWidth, 0 - halfHeight);
			
			GL11.glTexCoord2f(tileXpix, tileYpix + tileVheight);
			GL11.glVertex2d(0 - halfWidth, 0 + halfHeight);
			
			GL11.glTexCoord2f(tileXpix + tileUwidth, tileYpix + tileVheight);			
			GL11.glVertex2d(0 + halfWidth, 0 + halfHeight);
			
			GL11.glTexCoord2f(tileXpix + tileUwidth, tileYpix);
			GL11.glVertex2d(0 + halfWidth, 0 - halfHeight);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	
	public void onCollision(Sprite sprite)
	{
		
	}
	
	public void update(int delta)
	{
		double deltaf = delta / 1000000000.0D;
		
		xPos += xVel * deltaf;
		yPos += yVel * deltaf;	
		rot += rotVel * deltaf;
		
		if (this != LD31main.planet)
		{
			double dist = LD31main.getDistance(this, LD31main.planet);
			double deltaX = (this.xPos - LD31main.planet.xPos) / (dist / 15f);
			double deltaY = (this.yPos - LD31main.planet.yPos) / (dist / 15f);
			
			xVel += -deltaX * deltaf;
			yVel += -deltaY * deltaf;
		}
		
		for (Sprite sprite : LD31main.sprites)
		{
			if (sprite == this) continue;
			double dist = LD31main.getDistance(this,  sprite);
			if (dist <= sprite.radius + this.radius) this.onCollision(sprite); 
		}
		
		if (xPos - radius < 0 && xVel < 0) xVel = 0;
		if (yPos - radius < 0 && yVel < 0) yVel = 0;
		if (xPos + radius >= 160 && xVel > 0) xVel = 0;
		if (yPos + radius >= 120 && yVel > 0) yVel = 0;
	}
	
}
