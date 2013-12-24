package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

	/**
	 * @param args
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action " + e.getActionCommand()
				+ " Not Implemented");
	}

}
