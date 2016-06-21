import java.awt.Graphics;
import java.util.ArrayList;

public class Bullet {
    private double x;
    private double y;
    private final double origx;
    private final double origy;

    private final String dir;

    public static final double MOVE_SPEED = 15;

    private final Main main;

    public Bullet(double x, double y, String dir, Main main) {
	this.x = x;
	this.y = y;
	origx = x;
	origy = y;
	this.dir = dir;
	this.main = main;
    }

    private void move() {
	double newx = x;
	double newy = y;
	if (dir == "left" && !collisionDetect(newx - MOVE_SPEED, newy)) {
	    x -= MOVE_SPEED;
	} else if (dir == "right" && !collisionDetect(newx + MOVE_SPEED, newy)) {
	    x += MOVE_SPEED;
	} else if (dir == "forward"
		&& !collisionDetect(newx, newy - MOVE_SPEED)) {
	    y -= MOVE_SPEED;
	} else if (dir == "backward"
		&& !collisionDetect(newx, newy + MOVE_SPEED)) {
	    y += MOVE_SPEED;
	}
    }

    public boolean collisionDetect(double bulletx, double bullety) {
	ArrayList<ArrayList<Tile>> tiles = main.gamepanel.tiles;
	if (tiles != null) {
	    for (ArrayList<Tile> tileset : tiles) {
		for (Tile tile : tileset) {
		    if (tile.isSolid()) {
			if (!((bullety + 10) < tile.getY()
				|| bullety > (tile.getY() + 64)
				|| (bulletx + 10) < tile.getX() || bulletx > (tile
					.getX() + 64))) {
			    main.player.disposeBullet(this);
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    public boolean checkKill(double bulletx, double bullety) {
	ArrayList<Zombie> zombies = main.zombies;
	if (zombies != null) {
	    for (Zombie zombie : zombies) {
		if (!((bullety + 10) < zombie.getY()
			|| bullety > (zombie.getY() + 64)
			|| (bulletx + 10) < zombie.getX() || bulletx > (zombie
				.getX() + 64))) {
		    main.player.disposeBullet(this);
		    zombie.damage((int) ((Math.random() * 5) + 1)); //deals damage between 1 and 6
		    return true;
		}
	    }
	}
	return false;
    }

    public void draw(Graphics g) {
	checkKill(x - main.player.getX(), y - main.player.getY());
	if (!main.player.DEAD) {
	    move();
	}
	int drawx = (int) (x - (main.player.getX()));
	int drawy = (int) (y - (main.player.getY()));
	g.drawImage(main.kit.getImage(main.BP + "/bullet.png"), drawx, drawy,
		null);
    }

}
