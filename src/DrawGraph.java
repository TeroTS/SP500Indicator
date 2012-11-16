/*
 * DrawGraph class, draws a 2D graph
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Point;

@SuppressWarnings("serial")
public class DrawGraph extends JPanel {
	
	private static final int P_WIDTH = 1000; 
	private static final int P_HEIGHT = 500;
	private static final int BORDER_GAP = 50;
	private static final int MAX_SCORE = 100;
	private static final int Y_HATCH_CNT = 10;
	private static final int GRAPH_POINT_WIDTH = 10;
	
	//moving average list to be drawn
	private ArrayList<MaItem> maList;
	
	public DrawGraph(ArrayList<MaItem> list) {
		this.maList = list;
	}
	
	/*
	 * the actual method used to draw the panel
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//x any y resolution
		double xScale = ((double) getWidth() - 2 * BORDER_GAP)/(maList.size());
		double yScale = ((double) getHeight() - 2 * BORDER_GAP)/(MAX_SCORE);
		
		//System.out.println("xscale: " + xScale + "" + " yscale: " + yScale);
		
		//create x and y axes 
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);

		//create resolution marks for y axis
		for (int i = 0; i < Y_HATCH_CNT; i++) {
			int x0 = BORDER_GAP;
			int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
			int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
			//draw the y axis labels
			g2.drawString(Integer.toString(10 + (i * 10)), x0 - BORDER_GAP/2, y0 + (int)yScale);
		}
		g2.drawString("%", BORDER_GAP - (int)(BORDER_GAP/1.25), BORDER_GAP + (getHeight() - 2*BORDER_GAP)/2);


		//create resolution marks for x axis
		for (int i = 0; i < maList.size() - 1; i++) {
			int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (maList.size() - 1) + BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = y0 - GRAPH_POINT_WIDTH;
			g2.drawLine(x0, y0, x1, y1);
			//draw the x axis labels (every 10th mark)
			if (i % 10 == 0) {
				g2.drawString(maList.get(i).getDate(), x0, y0 + BORDER_GAP/2);
			}
		}
		
		//create the list of points
		List<Point> graphPoints = new ArrayList<Point>();
		for (int i = 0; i < maList.size(); i++) {
			int x1 = (int) (i * xScale + BORDER_GAP);
			int y1 = (int) ((MAX_SCORE - maList.get(i).getUnderMa()) * yScale + BORDER_GAP);
			graphPoints.add(new Point(x1, y1));
			//System.out.println("x: " + x1 + " y: " + y1);
		}
	
		//draw the graph
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(3f));
		for (int i = 0; i < graphPoints.size() - 1; i++) {
			int x1 = graphPoints.get(i).x;
			int y1 = graphPoints.get(i).y;
			int x2 = graphPoints.get(i + 1).x;
			int y2 = graphPoints.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);         
		}
	}
	
	/*
	 * 
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(P_WIDTH, P_HEIGHT);
	}
	
	/*
	 * initialize the GUI
	 */
	public void init(DrawGraph drawPanel) {//(ArrayList<MaItem> list) {

		//new draw panel
		//DrawGraph drawPanel = panel; //= new DrawGraph(MaList);
		//drawPanel.setBounds(0, 0, 600, 500);
		//drawPanel.setLayout(null);
		//new frame
		JFrame frame = new JFrame("% of SP500 stocks under MA50");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		//frame.getContentPane().setLayout(null);
		frame.getContentPane().add(drawPanel);
		//frame.setSize(600,500);
	    frame.pack();
	    frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
	
}
