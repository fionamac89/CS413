package view;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ESButton extends JButton {

	/**
	 * @param args
	 */
	public ESButton(String label, ActionListener al) {
		super(label);
		addActionListener(al);
	}
}
