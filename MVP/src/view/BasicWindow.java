package view;



import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class BasicWindow.
 */
public abstract class BasicWindow extends AbstractObservableView implements Runnable{
	
	//------------------------------Data Members-------------------------//

	protected Display display;
	protected Shell shell;
	
	//------------------------------Constructors-------------------------//
	
	public BasicWindow(String title, int width, int height) {
		
		display = new Display();
		shell = new Shell(display);
		shell.setSize(width, height);
		shell.setText(title);
	}
	
	
	public Display getDisplay() {
		return display;
	}


	public void setDisplay(Display display) {
		this.display = display;
	}


	public Shell getShell() {
		return shell;
	}


	public void setShell(Shell shell) {
		this.shell = shell;
	}
	

	/**
	 * Inits the widgets.
	 */
	protected abstract void initWidgets();
	
	@Override
	public void run() {
		initWidgets();
		shell.open();

		while(!shell.isDisposed()){ 
			if(!display.readAndDispatch()){ 	
				display.sleep(); 			
			}
		} 

		display.dispose();
	}



	
	
}
