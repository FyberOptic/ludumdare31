
public class Bullet extends Sprite 
{




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
		this.remove = true;
	}
	
}
