package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import _res.values.StringValues;
import model.ImgCoordenate;

public class ReadXlsx {
	private static JLabel txtInsertFile;

	public ReadXlsx(JLabel txtInsertFile) {
		// per();
		ReadXlsx.txtInsertFile = txtInsertFile;
	}

	public static void readXlsx(String pathXlsxString) {
		ArrayList<ImgCoordenate> listToAddMetadaData = new ArrayList<ImgCoordenate>();

		File file = new File(pathXlsxString);

		try {
			FileInputStream fs = new FileInputStream(file);
			Workbook wb = WorkbookFactory.create(fs);

			Sheet sheet = wb.getSheetAt(0);

			// readAllRowAndColumn(sheet);

			// Percorre todas as linhas da planilha
			for (Row row : sheet) {
				if (row != null) {
					if (row.getCell(0) != null) {
						String title = row.getCell(0).toString();
						String lat_ = row.getCell(1).toString();
						String long_ = row.getCell(2).toString();

						String address = "";
						try {
							txtInsertFile.setText("" + title);

							double latitude = Double.parseDouble(lat_);
							double longitude = Double.parseDouble(long_);
							// new GeoCodeService();
							address = GeoCodeService.getAddress(latitude, longitude);

							// add text To image after get address

							System.out.println(file.getParent() + "/absPath*****");
							String filesAbsPath = file.getParent() + "/";
							System.out.println("*****absPhoto" + filesAbsPath + title);
							TextToImage.addTextToImage(address, latitude, longitude, filesAbsPath, title);

							// Add geom in meta data image
							String dstPath = filesAbsPath + Constant.PATH_COMPLEMENT_TEMP;
							String outputImagePath = dstPath + title;
							ImgCoordenate imageMetadata = new ImgCoordenate(outputImagePath, latitude, longitude);

							listToAddMetadaData.add(imageMetadata);

						} catch (Exception e) {
							System.out.println("****Error: " + e.getMessage());
						}

						System.out.println(
								"title: " + title + "----lat: " + lat_ + "----long: " + long_ + "\n" + address + "\n");
					}
				}

			}
			fs.close();

			String pathToSaveImageWithMetadata = file.getParent() + Constant.PATH_COMPLEMENT_CONVERTED;
			addMetadaGeom(listToAddMetadaData, pathToSaveImageWithMetadata);

		} catch (Exception e) { // TODO Auto-generated catch block
			System.out.println("READ EXCEL catch block");
			// edtInsertFile.setText("INSERT A VALIDATE PATH xlsx");
			e.printStackTrace();
			txtInsertFile.setText(StringValues.ERROR + ": " + e);
		}

	}

	private static void addMetadaGeom(ArrayList<ImgCoordenate> listToAddMetadaData,
			String pathToSaveImageWithMetadata) {

		if (listToAddMetadaData.size() > 0) {

			File pathTempToDeleteFile = null;

			for (int i = 0; i < listToAddMetadaData.size(); i++) {
				File imgFileTemp = new File(listToAddMetadaData.get(i).getImgPath());
				// ImageMetadataEditor.setExifGPSTag(testFileIn, testFileOut);

				if (pathTempToDeleteFile == null) {
					pathTempToDeleteFile = new File(imgFileTemp.getParent());
				}

				File outFile = new File(pathToSaveImageWithMetadata);
				if (!outFile.exists()) {
					outFile.mkdirs();
				}

				// File imgFileOut = new File(out + "img" + i + 4 + ".jpeg");
				File img = new File(listToAddMetadaData.get(i).getImgPath());
				File imgFileOut = new File(pathToSaveImageWithMetadata + img.getName());

				if (imgFileTemp.isFile()) {
					txtInsertFile.setText(StringValues.WAIT_WE_ARE_EDITING_METADATA + ": " + img.getName());
					System.out.println("********************FILE TO OUT: " + imgFileOut.getAbsolutePath());
					ImageMetadataEditor.changeExifMetadata(imgFileTemp, imgFileOut,
							listToAddMetadaData.get(i).getLatitude(), listToAddMetadaData.get(i).getLongitude());
				} else {
					System.out
							.println("********************FILE TO OUT IS NOT A FILE: " + imgFileOut.getAbsolutePath());
				}

			}

			// TODO REMOVE PATH TEMP
			if (pathTempToDeleteFile != null) {
				String msg = DeleteRecursive.deleteRecursive(pathTempToDeleteFile);
				txtInsertFile.setText(msg);
			}
		}

	}

	/*
	 * private static void readAllRowAndColumn(Sheet sheet) { // Percorre todas as
	 * linhas da planilha for (Row row : sheet) { // Limita a leitura às 3 primeiras
	 * colunas for (int colIndex = 0; colIndex < 3; colIndex++) { Cell cell =
	 * row.getCell(colIndex); if (cell != null) { // Verifica o tipo de célula e
	 * obtém o valor switch (cell.getCellType()) { case STRING:
	 * System.out.print(cell.getStringCellValue() + "\t"); break; case NUMERIC:
	 * System.out.print(cell.getNumericCellValue() + "\t"); break; case BOOLEAN:
	 * System.out.print(cell.getBooleanCellValue() + "\t"); break; case FORMULA:
	 * System.out.print(cell.getCellFormula() + "\t"); break; default:
	 * System.out.print("UNKNOWN\t"); break; } } else { System.out.print("EMPTY\t");
	 * } } System.out.println("\n"); // Quebra de linha após cada linha } }
	 */

}
