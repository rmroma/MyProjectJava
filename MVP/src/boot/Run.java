package boot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import model.MyModel;
import presenter.MyPresenter;
import presenter.Properties;
import presenter.PropertiesHandler;
import view.AbstractObservableView;
import view.CLI;
import view.GameWindow;
import view.ObservableCLIView;


public class Run2 {

	public static void main(String[] args) {
		initStart();
/*
		Properties prop;
		try{
			prop = PropertiesHandler.getInstance();
		}catch (FileNotFoundException e) {
			prop = new Properties();
		}
		
		CLI client = new CLI(new BufferedReader(new InputStreamReader(System.in)),new PrintWriter(System.out,true));
		MyModel model = new MyModel();
		
		AbstractObservableView view;
		
		if( prop.getGui())
			view = new GameWindow(prop);
		else
			view = new ObservableCLIView();
		
		MyPresenter p = new MyPresenter(model, view);
		view.setCli(client);
		client.addObserver(view);
		view.addObserver(p);
		model.addObserver(p);
		view.run();*/
		 
		 
		 
	
	}
	
	private static void initStart(){
		Display display  = new Display();
		Shell shell = new Shell(display);
		Properties prop=null;
		MyModel model = new MyModel();;
		AbstractObservableView view = null;
		CLI client = new CLI(new BufferedReader(new InputStreamReader(System.in)),new PrintWriter(System.out,true));;
		MyPresenter presenter = null;
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO  );
		messageBox.setText("GUI/CLI");
		messageBox.setMessage("Start The game with GUI/CLI?\nFor GUI press Yes\nFor CLI press No ");
		int buttonID = messageBox.open();
		switch (buttonID) {
		case SWT.YES:
			shell.dispose();
			display.dispose();
			try{
				prop = PropertiesHandler.getInstance();
			}catch (FileNotFoundException e) {
				prop = new Properties();
			}
			view =  new GameWindow(prop);
			presenter = new MyPresenter(model, view);
			view.setCli(client);
			client.addObserver(view);
			view.addObserver(presenter);
			model.addObserver(presenter);
			view.run();
			break;
		case SWT.NO:
			System.out.println("hjkhjk");
			shell.dispose();
			display.dispose();
			view =  new ObservableCLIView();
			presenter = new MyPresenter(model, view);
			view.setCli(client);
			client.addObserver(view);
			view.addObserver(presenter);
			model.addObserver(presenter);
			view.run();
			break;
			/*	default:
				shell.dispose();
				display.dispose();
				break;*/
		}


		//System.out.println(buttonID);
	}


}
	
	
	

