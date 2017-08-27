import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * Bouncing balls :)
 * 
 * @author Thomas Edwards
 *
 */
public class Main {

	private final int boxX = 50;
	private final int boxY = 50;
	private final int boxWidth = 400;
	private final int boxHeight = 400;
	private final int MAXBALLS = 25;// the maximum amount of balls you are allowed to have in the box

	private List<Ball> balls = new ArrayList<Ball>();
	private boolean gravity = false;

	public Main() {
		addStartingBalls();
		JComponent drawingPane = setupGUI();
		while (true) {
			drawingPane.repaint();
			this.updateBalls();
		}
	}

	/**
	 * Setups the gui.
	 * 
	 * @return the JComponent which is responsible for drawing the balls.
	 */
	public JComponent setupGUI() {
		JFrame frame = new JFrame();
		// Graphics Panel
		@SuppressWarnings("serial")
		JComponent drawingPane = new JComponent() {
			protected void paintComponent(Graphics g2) {
				Image i = createImage(500, 500);
				Graphics g = i.getGraphics();
				g.drawRect(boxX - 2, boxY - 2, boxWidth + 4, boxHeight + 4);
				g.clearRect(boxX - 1, boxY - 1, boxWidth + 2, boxHeight + 2);
				for (Ball b : balls) {
					b.drawNew(g);
				}
				g2.drawImage(i, 0, 0, null);
			}
		};

		// Buttons
		JComponent buttonPanel = new JPanel();
		addButton(buttonPanel, "Add Ball", (e) -> addBall());
		addButton(buttonPanel, "Gravity", (e) -> {
			toggleGravity();
			switchGravityBackground((JButton) buttonPanel.getComponent(1));
		});
		buttonPanel.getComponent(1).setBackground(Color.orange);
		addButton(buttonPanel, "Remove Ball", (e) -> removeBall());
		addButton(buttonPanel, "Print Balls", (e) -> printBalls());

		buttonPanel.setBackground(Color.gray);
		buttonPanel.setBorder(new LineBorder(Color.BLACK, 2));

		// Frame
		frame.add(buttonPanel, BorderLayout.PAGE_END);
		frame.add(drawingPane, BorderLayout.CENTER);
		frame.setSize(boxX * 2 + 18 + boxWidth, boxY * 2 + 8 + boxHeight + 80);// +100 for the buttons
		frame.setResizable(false);
		frame.setTitle("Bouncing Balls");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		return drawingPane;
	}

	private void addButton(JComponent buttonPanel, String name, ActionListener action) {
		JButton button = new JButton(name);
		button.addActionListener(action);
		button.setFocusable(false);
		button.setPreferredSize(new Dimension(120, 30));
		buttonPanel.add(button);
	}

	/**
	 * Adds the initial balls, which adds according to the evenballs() method.
	 */
	public void addStartingBalls() {
		addEvenBalls();
	}

	/**
	 * Adds a single ball. The ball constructor ensures that the ball is added to a location which doesnt overlap with
	 * another ball
	 */
	public void addBall() {
		if (balls.size() >= MAXBALLS)// can only add up to the maxBalls
			return;
		Ball b1 = new Ball(balls);
		balls.add(b1);
	}

	/**
	 * Removes the latest ball from the list
	 */
	public void removeBall() {
		if (balls.size() <= 0)
			return;
		balls.remove(balls.size() - 1);
	}

	public void toggleGravity() {
		gravity = !gravity;
	}

	public void switchGravityBackground(JButton button) {
		if (button.getBackground() == Color.orange)
			button.setBackground(Color.GREEN);
		else
			button.setBackground(Color.orange);
	}

	/**
	 * Sleeps for 0.003 seconds after updating
	 */
	public void updateBalls() {
		for (int i = 0; i < balls.size(); i++) {
			try {
				Ball b = balls.get(i);
				b.move();
				checkCollision(b, i);
			} catch (java.util.ConcurrentModificationException e) {
				continue;// happens occasionally if you add a ball while iterating
			}
		}
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param b1
	 *            the ball to check collisons for
	 * @param listProgress
	 *            only compares the ball to balls further up in the list, this ensures that balls are not compared twice
	 */
	public void checkCollision(Ball b1, int listProgress) {
		this.touchingTopBotWalls(b1);
		this.touchingLeftRightWalls(b1);
		for (int j = listProgress + 1; j < balls.size(); j++) {
			Ball b2 = balls.get(j);
			b1.compareBalls(b2); // if true, then we have a collision

		}
	}

	public void touchingTopBotWalls(Ball b) {
		if (b.y + b.radius >= boxHeight + boxY - 1 || b.y - b.radius <= boxY + 1) {
			b.yVelocity = -b.yVelocity;
		}
	}

	public void touchingLeftRightWalls(Ball b) {
		if (b.x - b.radius <= boxX + 1 || b.x + b.radius >= boxX + boxWidth - 1) {
			b.xVelocity = -b.xVelocity;
		}
	}

	/**
	 * Prints out each ball's position using a stream to do it. Just for practice using streams for exam.
	 */
	public void printBalls() {
		System.out.printf("\n\tSpeed\tX\ty\n");// title: Speed, x, y
		balls.stream().sorted((Ball b1, Ball b2) -> {
			return (int) (b1.x - b2.x);
		}).forEach(b -> System.out.printf("Ball:\t%.1f\t%.1f\t%.1f\n", Math.hypot(b.xVelocity, b.yVelocity), b.x, b.y));
	}

	public void addVariedBalls() {
		Ball b1 = new Ball(15, 10, 295, 230, 50, 300, Color.BLUE);
		balls.add(b1);
		Ball b2 = new Ball(20, 15, 250, 250, 300, 40, Color.RED);
		balls.add(b2);
		Ball b3 = new Ball(30, 8, 100, 150, 200, 300, Color.GREEN);
		balls.add(b3);
		Ball b4 = new Ball(40, 15, 100, 250, 400, 600, Color.pink);
		balls.add(b4);
		Ball b5 = new Ball(50, 20, 80, 90, -300, 0, Color.YELLOW);
		balls.add(b5);
		Ball b6 = new Ball(200, 20, 200, 90, 300, 0, Color.black);
		balls.add(b6);
		Ball b7 = new Ball(150, 10, 195, 230, -400, -300, Color.orange);
		balls.add(b7);
	}

	public void addRandomBalls(int amount) {
		for (int i = 0; i < amount; i++) {
			Ball b1 = new Ball(balls);
			balls.add(b1);
		}
	}

	public void addEvenBalls() {
		Ball b1 = new Ball(10, 15, 295, 330, 50, 300, Color.BLACK);
		balls.add(b1);
		Ball b2 = new Ball(10, 15, 250, 250, 300, 540, Color.RED);
		balls.add(b2);
		Ball b3 = new Ball(10, 15, 100, 170, 200, 300, Color.BLACK);
		balls.add(b3);
		Ball b4 = new Ball(10, 15, 100, 250, 400, 600, Color.RED);
		balls.add(b4);
		Ball b5 = new Ball(10, 15, 360, 90, -300, 650, Color.BLACK);
		balls.add(b5);
		Ball b6 = new Ball(10, 15, 200, 90, 300, -450, Color.RED);
		balls.add(b6);
		Ball b7 = new Ball(10, 15, 195, 230, -600, -300, Color.BLACK);
		balls.add(b7);
		Ball b8 = new Ball(10, 15, 300, 190, -300, -450, Color.RED);
		balls.add(b8);
		Ball b9 = new Ball(10, 15, 300, 150, -300, 50, Color.BLACK);
		balls.add(b9);
		Ball b10 = new Ball(10, 15, 200, 400, 300, -550, Color.RED);
		balls.add(b10);
		Ball b11 = new Ball(10, 15, 95, 330, -400, -300, Color.BLACK);
		balls.add(b11);
		Ball b12 = new Ball(10, 15, 100, 400, 700, -450, Color.RED);
		balls.add(b12);
	}

	public static void main(String[] args) {
		new Main();
	}

}
