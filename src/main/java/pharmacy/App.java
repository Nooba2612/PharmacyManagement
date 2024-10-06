package pharmacy;

import java.awt.EventQueue;

import javafx.application.Application;
import pharmacy.gui.*;

public class App {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new App(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public App(String[] args) {
		initialize(args);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private void initialize(String[] args) {
		Application.launch(MainLayout_GUI.class, args);
	}

}
