
public class Asteroid extends Sprite
{
	public boolean large = true;

	public Asteroid(boolean large)
	{
		this.tileNum = 4;
		this.radius = 4;
		this.large = large;
		
		if (!large)
		{
			radius = 2;
			tileNum = 7;
		}
	}
	
	
	public void onCollision(Sprite sprite)
	{
		if (sprite instanceof Planet)
		{
			LD31main.breakSound.playAsSoundEffect((float) (0.9 + (Math.random() * 0.2)),  0.75f,  false);
			this.remove = true;
		}
	}
	
}
