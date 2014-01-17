package view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class StartScreen extends JFrame {

	public StartScreen(ActionListener al) {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init(al);
		pack();
		setSize(200, 200);
		setVisible(true);

	}

	private void init(ActionListener al) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));
		panel.add(new ESButton("Random", al));
		panel.add(new ESButton("Simon Says", al));
		// panel.add(new ESButton("Challange Mode", al));
		add(panel);
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Scores");
		bar.add(menu);
		JMenuItem save = new JMenuItem("Save Scores");
		save.addActionListener(al);
		menu.add(save);
		JMenuItem openScores = new JMenuItem("High Scores");
		openScores.addActionListener(al);
		menu.add(openScores);
		setJMenuBar(bar);q

	}
}
