
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Creator extends JFrame {
    private static final long serialVersionUID = 1L;
    Timer timer;
    Toolkit kit;
    CreatePanel panel;
    public String BP = (new File(".")).getAbsolutePath();

    public Creator() {
	BP = BP.substring(0, BP.length() - 1);
	getContentPane().requestFocus();
	showScreen();
	timer = new javax.swing.Timer(20, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		repaintWindow();
	    }
	});
	timer.start();
	setVisible(true);
    }

    public void repaintWindow() {
	getContentPane().repaint();
    }

    public void showScreen() {
	kit = Toolkit.getDefaultToolkit();
	Image img = kit.getImage(BP + "/zombieicon.png");
	setIconImage(img);
	setSize(750, 393);
	setUndecorated(true);
	setLocation(250, 300);
	setTitle("Brains, Blood, and Zombies");
	Container contentPane = getContentPane();
	panel = new CreatePanel(this);
	contentPane.add(panel);
    }

    public void stop() {
	timer.stop();
	this.dispose();
    }
}
