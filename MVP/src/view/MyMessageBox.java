package view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MyMessageBox {
	
	private MessageBox msgBox;
	
	private volatile boolean closed = false;
	
	public MyMessageBox(Shell shell, int style) {
		msgBox = new MessageBox(shell,style);
	}
	
	public void showMessage(Display display, String title, String msg) {
	
		display.syncExec(new Runnable() {

			@Override
			public void run() {

				if (msgBox != null && !closed) {
					msgBox.setText(title);
					msgBox.setMessage(msg);
					msgBox.open();
				}
			}
		});
		
		

			
		
	}

}
