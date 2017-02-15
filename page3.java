/* Completed Paint Application having
 * -Line
 * -Ellipse
 * -Rectangle
 * -Transparency
 * -Stroke Color
 * -Fill Color
 * -Clear Button
 * 
 */



import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;


@SuppressWarnings("serial")
public class page3 extends JFrame{
	
	
	JButton lineBut,ellBut,recBut,fillBut,strokeBut,select;
	JRadioButton fillrb,boundary;
	Color strokeColor=Color.CYAN,fillColor=Color.CYAN;
	JSlider tranSlider;
	JLabel transLabel;
	Point drawStart,drawEnd;
	float transPercent=1;
	int currentAction=1;
	int filler=1;
	
	public static void main(String[] args)
	{
		new page3();
	}
	DrawingArea obj1 = new DrawingArea();
	public page3()
	{
		this.setSize(800,600);
		this.setTitle("mPAINT");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(obj1,BorderLayout.CENTER);
		
		
		ListenForButton lForButton = new ListenForButton();
		lineBut = new JButton("Line");
		lineBut.addActionListener(lForButton);
		ellBut = new JButton("Ellipse");
		ellBut.addActionListener(lForButton);
		recBut = new JButton("Rectangle");
		recBut.addActionListener(lForButton);
		select = new JButton("Clear");
		select.addActionListener(lForButton);
		fillBut = new JButton("Fill Color");
		fillBut.addActionListener(lForButton);
		strokeBut = new JButton("Stroke Color");
		strokeBut.addActionListener(lForButton);
		
		fillrb =  new JRadioButton("Fill");
		boundary =  new JRadioButton("Boundary");
		fillrb.setSelected(true);
		ButtonGroup fillBound = new ButtonGroup();
		tranSlider = new JSlider(0,99,99);
		transLabel = new JLabel("Transparency %:");
		ListenForSlider lforSlider = new ListenForSlider();
		tranSlider.addChangeListener(lforSlider);
		Box box2 = Box.createHorizontalBox();
		Box box1 = Box.createVerticalBox();
		fillBound.add(fillrb);
		fillBound.add(boundary);
		box1.add(fillrb);
		box1.add(boundary);
		box2.add(box1);
		box2.add(select);
		
		Border operBorder = BorderFactory.createTitledBorder("Operation");
		Box theBox = Box.createHorizontalBox();
		theBox.setBorder(operBorder);
		theBox.add(box1);
		theBox.add(box2);
		theBox.add(lineBut);
		theBox.add(ellBut);
		theBox.add(recBut);
		theBox.add(fillBut);
		theBox.add(strokeBut);
		theBox.add(transLabel);
		theBox.add(tranSlider);
		
		
		this.add(theBox,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public class DrawingArea extends JComponent
	{
		
		ArrayList<Shape> shapeSet = new ArrayList<Shape>();
		ArrayList<Color> stroke = new ArrayList<Color>();
		ArrayList<Color> fill = new ArrayList<Color>();
		ArrayList<Float> transValues = new ArrayList<Float>();
		ArrayList<Integer> option = new ArrayList<Integer>();
		
		public DrawingArea()
		{
			this.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					drawStart = new Point(e.getX(),e.getY());
					drawEnd = drawStart;
					if(fillrb.isSelected())
					{
						filler=0;
					}
					else
					{
						filler=1;
					}
					
				}
				
				public void mouseReleased(MouseEvent e)
				{
					drawEnd = new Point(e.getX(),e.getY());
					Shape aShape = null;
					
					
					if(currentAction == 2)
					{
						aShape = drawRectangle(drawStart.x,drawStart.y,drawEnd.x,drawEnd.y);
					}
					else if(currentAction == 1)
					{
						aShape = drawEllipse(drawStart.x,drawStart.y,drawEnd.x,drawEnd.y);
					}
					else if(currentAction == 3)
					{
						aShape = drawLine(drawStart.x,drawStart.y,drawEnd.x,drawEnd.y);
					}
					
					shapeSet.add(aShape);
					option.add(filler);
					stroke.add(strokeColor);
					fill.add(fillColor);
					transValues.add(transPercent);
					
					drawEnd=null;
					drawStart=null;
					repaint();
				}
			});
			this.addMouseMotionListener(new MouseMotionAdapter(){
				
				public void mouseDragged(MouseEvent e)
				{
					drawEnd = new Point(e.getX(),e.getY());
					repaint();
				}
			}
			);
		}
		public void reset()
		{
			shapeSet.clear();
			option.clear();
			fill.clear();
			transValues.clear();
			stroke.clear();
		}
		public void paint(Graphics g)
		{
			Graphics2D graphicSetting = (Graphics2D)g;
			graphicSetting.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphicSetting.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0F));
			graphicSetting.setStroke(new BasicStroke(3));
			graphicSetting.setPaint(Color.lightGray);
			graphicSetting.fillRect(0,0,800,610);
			
			Iterator<Color> fillI = fill.iterator();
			Iterator<Color> strokeI = stroke.iterator();
			Iterator<Float> transI = transValues.iterator();
			Iterator<Integer> optionI = option.iterator();
			
			for(Shape s :shapeSet)
			{
					graphicSetting.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transI.next()));
					
					
					if(optionI.next()==1)
					{
						graphicSetting.setPaint(fillI.next());
						graphicSetting.setPaint(strokeI.next());
						graphicSetting.draw(s);
					}
					else
					{
						graphicSetting.setPaint(fillI.next());
						graphicSetting.fill(s);
						graphicSetting.setPaint(strokeI.next());
						graphicSetting.draw(s);
					}
			}
			
			Shape nShape=null;
			if(drawStart!=null && drawEnd != null)
			{
				graphicSetting.setPaint(Color.BLACK);
				if(currentAction == 2)
				{
					nShape = drawRectangle(drawStart.x,drawStart.y,drawEnd.x,drawEnd.y);
				}
				else if(currentAction == 1)
				{
					nShape = drawEllipse(drawStart.x,drawStart.y,drawEnd.x,drawEnd.y);
				}
				else if(currentAction == 3)
				{
					nShape = drawLine(drawStart.x,drawStart.y,drawEnd.x,drawEnd.y);
				}
					graphicSetting.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3F));
					graphicSetting.draw(nShape);		
			}
		}
	}
	
	private Rectangle2D.Float drawRectangle(int x1,int y1,int x2,int y2)
	{
		int x = Math.min(x1,x2);
		int y = Math.min(y1,y2);
		
		int width = Math.abs(x1-x2);
		int height = Math.abs(y1-y2);
		
		return new Rectangle2D.Float(x,y,width,height);
	}
	
	private Ellipse2D.Float drawEllipse(int x1,int y1,int x2,int y2)
	{
		int x = Math.min(x1,x2);
		int y = Math.min(y1,y2);
		
		int width = Math.abs(x1-x2);
		int height = Math.abs(y1-y2);
		
		return new Ellipse2D.Float(x,y,width,height);
	}
	
	private Line2D.Float drawLine(int x1,int y1,int x2,int y2)
	{
		return new Line2D.Float(x1,y1,x2,y2);
	}
	
	public class ListenForSlider implements ChangeListener
	{
		
		public void stateChanged(ChangeEvent e) {
			
			if(e.getSource()==tranSlider)
			{
				transLabel.setText("Transparency: " + tranSlider.getValue()*0.01);
				transPercent = (float) (tranSlider.getValue() * 0.01);
			}
		}
	}
	
	public class ListenForButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == select)
			{
				obj1.reset();
				obj1.repaint();
			}
			else if(e.getSource() == ellBut)
				currentAction=1;
			else if(e.getSource() == recBut)
				currentAction=2;
			else if(e.getSource() == lineBut)
				currentAction=3;
			else if(e.getSource() == strokeBut)
			{
				strokeColor = JColorChooser.showDialog(null, "Pick a Stroke",Color.BLACK);
			}
			else if(e.getSource() == fillBut)
			{
				fillColor = JColorChooser.showDialog(null, "Pick a Fill",Color.BLACK);
			}
		}
		
	}
	
	

}
