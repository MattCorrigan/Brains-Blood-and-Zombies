import java.awt.Graphics;


public class Tile {
    public static final int width = 64;
    public static final int height = 64;

    private final int x;
    private final int y;
    private final String type;
    private final Main main;
    private boolean solid = false;
    public boolean containsTrap = false;

    public Tile(int x, int y, String type, Main main) {
	this.x = x;
	this.y = y;
	this.type = type;
	this.main = main;
	if (type.equals("0")) {
	    solid = true;
	}
    }

    public boolean isSolid() {
	if (solid) {
	    return true;
	}
	return false;
    }

    public int getX() {
	return x * 64;
    }

    public int getY() {
	return y * 64;
    }

    public void draw(Graphics g) {
	int drawx = (x * width) - (int) main.player.getX();
	int drawy = (y * height) - (int) main.player.getY();
	int modifierx = 0, modifiery = 0;
	if (type.equals("0")) {
	    modifierx = 384;
	} else if (type.equals("g")) {
	    g.drawImage(main.kit.getImage(main.BP + "/tile.png"), drawx, drawy,
		    drawx + 64, drawy + 64, 192, 96, 192 + 32, 96 + 32, null);
	    modifierx = 256;
	} else {
	    modifierx = 224;
	}
	g.drawImage(main.kit.getImage(main.BP + "/tile.png"), drawx, drawy,
		drawx + 64,
		drawy + 64, modifierx, modifiery,
		modifierx + 32, modifiery + 32, null);
	if (type.equals("2")) {
	    modifierx = 256;
	    g.drawImage(main.kit.getImage(main.BP + "/tile.png"), drawx, drawy,
		    drawx + 64, drawy + 64, modifierx, modifiery,
		    modifierx + 32, modifiery + 32, null);
	}
    }
}
