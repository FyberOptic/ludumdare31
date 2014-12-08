
public class Bullet extends Sprite 
{

	int lifetime = 0;


	public Bullet()
	{
		tileNum = 5;
		tileWidth = 1;
		tileHeight = 1;
		radius = 0.5f;
		
	}
	
	
	public void onCollision(Sprite sprite)
	{
		if (sprite instanceof Ship) return;
		
		if (sprite instanceof Asteroid)
		{
			/*double dist = LD31main.getDistance(this, sprite);
			double deltaX = (this.xPos - sprite.xPos) / (dist / 2f);
			double deltaY = (this.yPos - sprite.yPos) / (dist / 2f);
			
			sprite.xVel += -deltaX;
			sprite.yVel += -deltaY;*/
			
			if (((Asteroid)sprite).large)
			{
				Asteroid asteroids[] = new Asteroid[4];
				for (int n = 0; n < 4; n++)
				{
					asteroids[n] = new Asteroid(false);
					asteroids[n].xPos = sprite.xPos + (Math.random() * 2);
					asteroids[n].yPos = sprite.yPos + (Math.random() * 2);
					asteroids[n].xVel = (float) (sprite.xVel - 5 + (Math.random() * 10));
					asteroids[n].yVel = (float) (sprite.yVel - 5 + (Math.random() * 10));
					asteroids[n].rotVel = Math.random() * 20;
					LD31main.newsprites.add(asteroids[n]);
				}
			}
			
			sprite.remove = true;
			
			LD31main.breakSound.playAsSoundEffect((float) (0.9 + (Math.random() * 0.2)),  0.75f,  false);
			
		}
		
		this.remove = true;
	}
	
	
	public void update(int delta)
	{
		super.update(delta);
		
		lifetime += delta;
		
		if (lifetime > 1000000000) this.remove = true;
		
		//if (xPos < 0) this.remove = true;
		//if (yPos < 0) this.remove = true;
		//if (xPos >= 160) this.remove = true;
		//if (yPos >= 120) this.remove = true;
	}
	
}
