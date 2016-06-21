import java.awt.Graphics;
import java.util.ArrayList;


public class Trap {
    private final int x;
    private final int y;
    private final char type;
    Main main;

    public Trap(int x, int y, char type, Main main) {
	this.x = x;
	this.y = y;
	this.type = type;
	this.main = main;
	for (ArrayList<Tile> tileset : main.gamepanel.tiles) {
	    for (Tile tile : tileset) {
		if (tile.getX() == x * 64 && tile.getY() == y * 64) {
		    tile.containsTrap = true;
		}
	    }
	}
    }

    public int getX() {
	return x * 64;
    }

    public int getY() {
	return y * 64;
    }

    public void draw(Graphics g) {
	int drawx = (x * 64) - (int) main.player.getX();
	int drawy = (y * 64) - (int) main.player.getY();
	g.drawImage(main.kit.getImage(main.BP + "/spikes.png"), drawx, drawy,
		64, 64, null);
    }

}
