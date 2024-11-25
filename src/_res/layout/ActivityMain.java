package _res.layout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

import _res.values.StringValues;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Font;

public class ActivityMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JTextField edtSelectFileMainAct;
	private static JButton btnInitProcessMainAct;
	private static JLabel txtHeadMainAct;

	public static JLabel getTxtHeadMainAct() {
		return txtHeadMainAct;

	}

	public static JTextField getEdtSelectFileMainAct() {
		return edtSelectFileMainAct;
	}

	public static JButton getBtnInitProcessMainAct() {
		return btnInitProcessMainAct;
	}

	public ActivityMain() {
		getContentPane().setBackground(new Color(61, 56, 70));
		setTitle(StringValues.MAIN_ACTIVITY);
		setSize(500, 300);
		setForeground(Color.white);
		setResizable(false);
		setBackground(new Color(61, 56, 70));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panelMainAct = panelBackGround();
		// Painel principal com BoxLayout
		panelMainAct.setLayout(new BoxLayout(panelMainAct, BoxLayout.Y_AXIS));
		panelMainAct.setAlignmentX(Component.CENTER_ALIGNMENT);

		txtViewInsertFile(panelMainAct);

		edtTextInsertFile(panelMainAct);

		getBtnAddImage(panelMainAct);

		getContentPane().add(panelMainAct, BorderLayout.CENTER);

	}

	private JPanel panelBackGround() {

		JPanel panelMainAct = new JPanel();
		panelMainAct.setBackground(new Color(94, 92, 100));
		panelMainAct.setBounds(0, 0, 500, 263);
		getContentPane().add(panelMainAct);
		panelMainAct.setLayout(null);
		return panelMainAct;
	}

	private void txtViewInsertFile(JPanel panelMainAct) {
		txtHeadMainAct = new JLabel(StringValues.INSERT_FILE_XLS);
		txtHeadMainAct.setAlignmentX(Component.CENTER_ALIGNMENT);
		txtHeadMainAct.setFont(new Font("Dialog", Font.BOLD, 18));
		txtHeadMainAct.setForeground(new Color(255, 255, 255));
		txtHeadMainAct.setBounds(112, 25, 287, 15);
		panelMainAct.add(txtHeadMainAct);
	}

	private void getBtnAddImage(JPanel panelMainAct) {
		btnInitProcessMainAct = new JButton(StringValues.ADD_INFO_AT_IMAGE);
		btnInitProcessMainAct.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnInitProcessMainAct.setForeground(new Color(255, 255, 255));
		btnInitProcessMainAct.setBackground(new Color(255, 163, 72));
		btnInitProcessMainAct.setBounds(12, 199, 470, 35);
		panelMainAct.add(btnInitProcessMainAct);

	}

	private void edtTextInsertFile(JPanel panelMainAct) {

		edtSelectFileMainAct = new JTextField(StringValues.INSERT_FILE_XLS);
		edtSelectFileMainAct.setBackground(new Color(146, 146, 146));
		edtSelectFileMainAct.setForeground(new Color(75, 75, 75));
		edtSelectFileMainAct.setBounds(87, 68, 287, 86);
		panelMainAct.add(edtSelectFileMainAct);
		edtSelectFileMainAct.setColumns(10);
		edtSelectFileMainAct.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		Border lineBorder = new LineBorder(new Color(0, 0, 0), 2, true);
		edtSelectFileMainAct.setBorder(lineBorder);

	}

}
