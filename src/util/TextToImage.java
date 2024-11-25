package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import _res.values.StringValues;

public class TextToImage {

	public static String addTextToImage(String address, double latitude, double longitude, String imgAbsPath,

			String imgTitle) {
		String inputImagePath = imgAbsPath + imgTitle;

		String dstPath = imgAbsPath + Constant.PATH_COMPLEMENT_TEMP;

		String outputImagePath = dstPath + imgTitle;

		// Exemplo de dados que você quer adicionar à imagem

		try {
			// Carregar a imagem
			BufferedImage image = ImageIO.read(new File(inputImagePath));

			if (image == null) {
				System.err.println("Erro: Imagem não pôde ser carregada.");
				return "Erro: Imagem não pôde ser carregada.";
			}

			// Obter a extensão do arquivo (png ou jpg)
			String fileExtension = getFileExtension(imgTitle);

			// Verificar se a extensão é suportada
			if (!fileExtension.equalsIgnoreCase(Constant.IMG_EXTENSION_JPEG)
					&& !fileExtension.equalsIgnoreCase(Constant.IMG_EXTENSION_JPG)
					&& !fileExtension.equalsIgnoreCase(Constant.IMG_EXTENSION_PNG)) {
				System.err.println("Formato de imagem não suportado: " + fileExtension);
				return "Formato de imagem não suportado: " + fileExtension;
			}

			// Escrever o texto na imagem
			BufferedImage modifiedImage = formatTextToImage(image, address, latitude, longitude);

			// Criar diretório se não existir
			File outputDir = new File(dstPath);
			if (!outputDir.exists()) {
				outputDir.mkdirs();
			}

			// Salvar a nova imagem no diretório de destino
			ImageIO.write(modifiedImage, fileExtension, new File(outputImagePath));
			System.out.println("Imagem salva em: " + outputImagePath);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputImagePath;
	}

	// Método para obter a extensão do arquivo
	private static String getFileExtension(String fileName) {
		int lastIndexOfDot = fileName.lastIndexOf(".");
		if (lastIndexOfDot == -1) {
			return ""; // Sem extensão encontrada
		}
		return fileName.substring(lastIndexOfDot + 1);
	}

	private static BufferedImage formatTextToImage(BufferedImage image, String address, double latitude,
			double longitude) {
		Graphics2D g2d = image.createGraphics();

		// Configurar a fonte e a cor do texto
		g2d.setFont(new Font("Arial", Font.BOLD, 48));
		g2d.setColor(Color.WHITE);

		// Posição inicial para desenhar o texto
		int x = 10;
		int y = 40;
		int lineHeight = g2d.getFontMetrics().getHeight();

		// Texto para adicionar
		String text1 = address;
		String text2 = StringValues.LAT_ + ": " + latitude;
		String text3 = StringValues.LONG_ + ": " + longitude;

		// Adicionar o texto à imagem, respeitando as quebras de linha
		drawStringWithLineBreaks(g2d, text1, x, y, lineHeight);
		g2d.drawString(text2, x, y + 230); // Posição abaixo do endereço
		g2d.drawString(text3, x, y + 280); // Posição abaixo da latitude

		g2d.dispose(); // Liberar o objeto Graphics2D
		System.out.println("Formatted Address:\n" + address);
		return image;
	}

	// Método auxiliar para desenhar texto com quebras de linha
	private static void drawStringWithLineBreaks(Graphics2D g2d, String text, int x, int y, int lineHeight) {
		String[] lines = text.split("\n");
		for (String line : lines) {
			g2d.drawString(line, x, y);
			y += lineHeight;
		}
	}

}
