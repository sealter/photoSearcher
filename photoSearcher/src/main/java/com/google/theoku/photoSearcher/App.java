package com.google.theoku.photoSearcher;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.theoku.photoSearcher.control.PhotoSearcherControl;
import com.google.theoku.photoSearcher.model.PhotoSearcherModel;
import com.google.theoku.photoSearcher.view.PhotoSearcherView;


/**
 * Hello world!
 *
 */
public class App {
	
	private static final Log LOGGER = LogFactory.getLog(App.class);
	
	public static void createAndShowGUI() throws UnknownHostException {
		JFrame aFrame = new JFrame();

		PhotoSearcherModel aModel = new PhotoSearcherModel();
		final PhotoSearcherControl aControl = new PhotoSearcherControl(aModel);

		
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		PhotoSearcherView aView = new PhotoSearcherView(aControl);
		
		JMenuBar aMenuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("FILE");
		
		JMenuItem loadImageFromDB = new JMenuItem("Load from DB");
		loadImageFromDB.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent pArg0) {
				aControl.loadImageFromDB();
			}
		});
		
		fileMenu.add(loadImageFromDB);
		aMenuBar.add(fileMenu);
		
		contentPanel.add(aMenuBar, BorderLayout.NORTH);
		contentPanel.add(aView.getView(), BorderLayout.CENTER);

		aFrame.setContentPane(contentPanel);
		aFrame.pack();
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Starting application!");
		}
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					createAndShowGUI();
				} catch (UnknownHostException e) {
					if(LOGGER.isErrorEnabled()) {
						LOGGER.error("Unable to start application; unable to connect to DB: ", e);
					}
				}
			}
		});
		
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Finished Main Thread");
		}
	}
}
