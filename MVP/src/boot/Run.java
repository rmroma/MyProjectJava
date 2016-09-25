package boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import model.MyModel;
import presenter.MyPresenter;
import view.CLI;
import view.ObservableCLIView;


public class Run {

	public static void main(String[] args) {
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		//PrintWriter out = new PrintWriter(System.out);
		MyModel model = new MyModel();
		ObservableCLIView view = new ObservableCLIView();
		MyPresenter presenter = new MyPresenter(model, view);
		CLI client = new CLI(new BufferedReader(new InputStreamReader(System.in)),new PrintWriter(System.out,true));
		view.setCli(client);
		client.addObserver(view);
		view.addObserver(presenter);
		model.addObserver(presenter );
		view.run();
	}

}
