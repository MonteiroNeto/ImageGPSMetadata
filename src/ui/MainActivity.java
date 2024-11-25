package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import _res.layout.ActivityMain;
import _res.values.StringValues;
import util.ReadXlsx;

@SuppressWarnings("serial")
public class MainActivity extends ActivityMain {
	private static JButton btnAddInfoAtImage;
	private static JTextField edtInsertFile;
	private static JLabel txtInsertFile;

	public static void main(String[] args) {
		ActivityMain activity = new ActivityMain();
		activity.setVisible(true);

		initComponents();

	}

	private static void initComponents() {
		btnAddInfoAtImage = getBtnInitProcessMainAct();
		edtInsertFile = getEdtSelectFileMainAct();
		txtInsertFile = getTxtHeadMainAct();

		onClick();

	}

	private static void onClick() {
		btnAddInfoAtImage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String pathXlsxString = edtInsertFile.getText().toString();
				if (!pathXlsxString.isEmpty()) {
					txtInsertFile.setText(StringValues.WAIT_WE_ARE_EDITING_IMAGE);

					// readXlsx(pathXlsxString);

					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							// Adicionar um delay de 2 segundos
							Thread.sleep(2000);
							// Executar o método readXlsx após o delay
							new ReadXlsx(txtInsertFile).readXlsx(pathXlsxString);

							return null;
						}

						@Override
						protected void done() {
							// Atualizar a interface após a conclusão
							txtInsertFile.setText(StringValues.EDITING_COMPLETED);

						}
					};

					worker.execute(); // Iniciar a tarefa

				} else {
					edtInsertFile.setText(StringValues.INSERT_FILE_VALIDATE_XLS);
				}

			}

		});

	}

}
