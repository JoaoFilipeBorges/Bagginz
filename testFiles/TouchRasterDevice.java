package core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import main.SampleApp;

public class TouchRasterDevice extends Canvas implements ITouchRasterDevice {

	private static final long serialVersionUID = -1583834384008011722L;
	private boolean initialized = false;
	
	private List<IRenderListener> renderListeners = new LinkedList<IRenderListener>();
	private List<ITouchListener> touchListeners = new LinkedList<ITouchListener>();
	
	public static List<Point> pixels = new LinkedList<Point>();
	
	private int pixelWidth, pixelHeight ,nX ,nY;

	private final Point mousePosition = new Point();
	private Graphics currentGraphics;

	
	@Override
	public Component getComponent() { return this; }
	
	@Override
	public void setPixel(int x, int y, Color color) {
		Point pos = new Point(x, y);
		currentGraphics.setColor(color);
		currentGraphics.fillRect(pos.x, pos.y, pixelWidth, pixelHeight);
	}

	@Override
	public void initialize(int zoomInFactor) 
	{
		if(initialized) throw new java.lang.IllegalStateException("The raster device is already initialized");
		
		pixelWidth = zoomInFactor;
		pixelHeight = zoomInFactor;
		
		final UpdateLoop updateLoop = new UpdateLoop(new RenderCallback() {
			@Override
			public void render(Graphics g) 
			{
				currentGraphics = g;
				paint(g);
				for (IRenderListener renderListener : renderListeners) {
					renderListener.render(TouchRasterDevice.this);
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				mousePosition.setLocation(me.getPoint());
				for (ITouchListener touchListener : touchListeners) {
					touchListener.onMove((me.getPoint().x/pixelWidth)-(SampleApp.pX/pixelWidth)/2,((me.getPoint().y/pixelHeight)-(SampleApp.pY/pixelHeight)/2)*-1);//Acert X-Y
				}
			}
			@Override
			public void mouseDragged(MouseEvent me) {
				mousePosition.setLocation(me.getPoint());
				for (ITouchListener touchListener : touchListeners) {
					touchListener.onDrag((me.getPoint().x/pixelWidth)-(SampleApp.pX/pixelWidth)/2,((me.getPoint().y/pixelHeight)-(SampleApp.pY/pixelHeight)/2)*-1, me.getButton());
					pixels.add(new Point(me.getPoint().x,me.getPoint().y));
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				for (ITouchListener touchListener : touchListeners) {
					touchListener.onClick(me.getPoint().x, me.getPoint().y, me.getButton());
				}
			}
		});
		
		final Thread updateThread = new Thread(updateLoop);
		updateThread.setPriority(Thread.MIN_PRIORITY);
		updateThread.start(); 
		initialized = true;
	}
	
	@Override
	public void addTouchListener(ITouchListener touchListener) {
		touchListeners.add(touchListener);
	}
	@Override
	public void addRenderListener(IRenderListener renderListener) {
		renderListeners.add(renderListener);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		

		// Draw pixel in the mouse position
		g.setColor(Color.GREEN);
		Point devicePoint = mousePosition;
		g.fillRect(devicePoint.x/pixelWidth*pixelWidth, devicePoint.y/pixelHeight*pixelHeight, pixelWidth, pixelHeight);
		for(Point p : pixels)
			setPixel(p.x/pixelWidth*pixelWidth, p.y/pixelHeight*pixelHeight, Color.red);
		drawGrid(g);
	
		
	}

	private void drawGrid(Graphics g) 
	{
		// Draw squares (pixels)
		g.setColor(Color.GRAY);
		for (int y = 0; y < getHeight(); y += pixelHeight) {
			for (int x = 0; x < getWidth(); x += pixelWidth) 
			{
				g.drawRect(x, y, pixelWidth, pixelHeight);
			}
		}	
		//Draw axes x-y
		g.setColor(Color.black);
				int xPixelMed=SampleApp.pX/2+pixelWidth/2;
				int yPixelMed=SampleApp.pY/2+pixelHeight/2;
				g.drawLine(xPixelMed, 0, xPixelMed, SampleApp.pY+pixelHeight);
				g.drawLine(0, yPixelMed, SampleApp.pX+pixelWidth, yPixelMed);
	}


	private class UpdateLoop implements Runnable 
	{
		private static final int FRAME_DELAY = 40; // 40ms. implies 25fps (1000/40) = 25

		boolean isRunning;
		long cycleTime;
		private final RenderCallback renderCallback;
		
		public UpdateLoop(RenderCallback renderCallback) {
			this.renderCallback = renderCallback;
			isRunning = true;
		}
 
		public void run() {
			cycleTime = System.currentTimeMillis();
			createBufferStrategy(2);
			final BufferStrategy strategy = getBufferStrategy();
 
			// Application Loop
			while (isRunning) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							updateGUI(strategy);
						}
					});
				} catch (InvocationTargetException e) {
					System.exit(1);
				} catch (InterruptedException e) {
					System.exit(1);
				}
 				synchFramerate();
			}
		}
		

		private void updateGUI(BufferStrategy strategy) {
			Graphics g = strategy.getDrawGraphics();
 
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.BLACK);
			renderCallback.render(g);

			g.dispose();
			strategy.show();
		}
 
		private void synchFramerate() {
			cycleTime = cycleTime + FRAME_DELAY;
			long difference = cycleTime - System.currentTimeMillis();
			try {
				Thread.sleep(Math.max(0, difference));
			}
			catch(InterruptedException e) {
				isRunning = false;
			}
		}
 
	}

}

interface RenderCallback 
{
	void render(Graphics graphics);
}

