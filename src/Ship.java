
public class Ship extends Sprite
{

	public boolean thrusting = false;
	private int thrustcounter = 0;
	
	public Ship()
	{
		tileNum = 3;
		radius = 2;
		xPos = 10;
		yPos = 10;
		
		this.tileWidth = 5;
		this.tileHeight = 7;
	}
	
	
	public void update(int delta)
	{
		super.update(delta);
		
		/*if (xPos - radius < 0 && xVel < 0) xVel = 0;
		if (yPos - radius < 0 && yVel < 0) yVel = 0;
		if (xPos + radius >= 160 && xVel > 0) xVel = 0;
		if (yPos + radius >= 120 && yVel > 0) yVel = 0;*/
		
		
		double speed = Math.sqrt(xVel * xVel + yVel * yVel);
		double maxspeed = 100;
		if (speed > maxspeed)
		{
			xVel /= speed;
			xVel *= maxspeed;
			
			yVel /= speed;
			yVel *= maxspeed;
		}
		
		
		if (thrusting)
		{
			thrustcounter += delta;
			if (thrustcounter > 50000000)
			{
				thrustcounter -= 50000000;
				if (tileNum == 3) tileNum = 6; else tileNum = 3;
					
			}
		}
		else tileNum = 3;
	}
	
	
	public void onCollision(Sprite sprite)
	{
		if (sprite instanceof Bullet) return;
		
		if (sprite instanceof Asteroid)
		{
			float mass = 1;
			if (((Asteroid)sprite).large) mass = 2;
			
			sprite.xVel = this.xVel = (this.xVel + (sprite.xVel * mass)) / (1f + mass);
			sprite.yVel = this.yVel = (this.yVel + (sprite.yVel * mass)) / (1f + mass);
			
			//this.xVel += (1 - (Math.random() * 2));
			//this.yVel += (1 - (Math.random() * 2));
		}
		
		if (sprite instanceof Planet)
		{
			this.xVel = 0;
			this.yVel = 0;
		}
		
	}
	
}
