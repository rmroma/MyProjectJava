package view.widgets;


import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class ImageGameCharacter extends GameCharacter {
	
	private Image image;
	
	
	
	public ImageGameCharacter(Composite parent, int style, Image image) {
		super(parent, style);
		
		this.image =image;
		
		addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				image.dispose();
			}
		});
		
	}

	
	public Image getImage() {
		return image;
	}


	public void setImage(Image image) {
		this.image = image;
	}


	public int getZ() {
		return z;
	}


	public void setZ(int z) {
		this.z = z;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getW() {
		return w;
	}


	public void setW(int w) {
		this.w = w;
	}


	public int getH() {
		return h;
	}


	public void setH(int h) {
		this.h = h;
	}
	
	public void paint(PaintEvent e,int y,int x,int w, int h){

		e.gc.drawImage(image, 0, 0, 591,1008,y, x, (int)Math.round(w*1.2), (int)Math.round(h*1.3));


	}


	
	
	
}

	