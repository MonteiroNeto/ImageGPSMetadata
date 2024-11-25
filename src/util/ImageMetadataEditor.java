package util;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata.GPSInfo;

import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;

import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;

import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

public class ImageMetadataEditor {

	// *****************************READ METADATA IN IMAGE
	// JPEG*************************************************
	public static void metadataRead(final File file) {
		// get all metadata stored in EXIF format (ie. from JPEG or TIFF).
		ImageMetadata metadata;
		try {

			System.out.println("*************************GPS Description: ******************************");
			metadata = Imaging.getMetadata(file);

			// System.out.println(metadata);

			if (metadata instanceof JpegImageMetadata) {
				final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

				// Jpeg EXIF metadata is stored in a TIFF-based directory structure
				// and is identified with TIFF tags.
				// Here we look for the "x resolution" tag, but
				// we could just as easily search for any other tag.
				//
				// see the TiffConstants file for a list of TIFF tags.

				System.out.println("file: " + file.getPath());

				// print out various interesting EXIF tags.
				printTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_XRESOLUTION);
				printTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_DATE_TIME);
				printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
				printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
				printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_ISO);
				printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
				printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
				printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_BRIGHTNESS_VALUE);
				printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
				printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE);
				printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
				printTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE);

				System.out.println();

				// simple interface to GPS data
				final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
				if (null != exifMetadata) {
					final GPSInfo gpsInfo = exifMetadata.getGPS();
					if (null != gpsInfo) {
						final String gpsDescription = gpsInfo.toString();
						final double longitude = gpsInfo.getLongitudeAsDegreesEast();
						final double latitude = gpsInfo.getLatitudeAsDegreesNorth();

						System.out.println("    " + "GPS Description: " + gpsDescription);
						System.out.println("    " + "GPS Longitude (Degrees East): " + longitude);
						System.out.println("    " + "GPS Latitude (Degrees North): " + latitude);
					}
				}

				// more specific example of how to manually access GPS values
				final TiffField gpsLatitudeRefField = jpegMetadata
						.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
				final TiffField gpsLatitudeField = jpegMetadata
						.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
				final TiffField gpsLongitudeRefField = jpegMetadata
						.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
				final TiffField gpsLongitudeField = jpegMetadata
						.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
				if (gpsLatitudeRefField != null && gpsLatitudeField != null && gpsLongitudeRefField != null
						&& gpsLongitudeField != null) {
					// all of these values are strings.
					final String gpsLatitudeRef = (String) gpsLatitudeRefField.getValue();
					final RationalNumber[] gpsLatitude = (RationalNumber[]) gpsLatitudeField.getValue();
					final String gpsLongitudeRef = (String) gpsLongitudeRefField.getValue();
					final RationalNumber[] gpsLongitude = (RationalNumber[]) gpsLongitudeField.getValue();

					final RationalNumber gpsLatitudeDegrees = gpsLatitude[0];
					final RationalNumber gpsLatitudeMinutes = gpsLatitude[1];
					final RationalNumber gpsLatitudeSeconds = gpsLatitude[2];

					final RationalNumber gpsLongitudeDegrees = gpsLongitude[0];
					final RationalNumber gpsLongitudeMinutes = gpsLongitude[1];
					final RationalNumber gpsLongitudeSeconds = gpsLongitude[2];

					// This will format the gps info like so:
					//
					// gpsLatitude: 8 degrees, 40 minutes, 42.2 seconds S
					// gpsLongitude: 115 degrees, 26 minutes, 21.8 seconds E

					System.out.println("    " + "GPS Latitude: " + gpsLatitudeDegrees.toDisplayString() + " degrees, "
							+ gpsLatitudeMinutes.toDisplayString() + " minutes, " + gpsLatitudeSeconds.toDisplayString()
							+ " seconds " + gpsLatitudeRef);
					System.out.println("    " + "GPS Longitude: " + gpsLongitudeDegrees.toDisplayString() + " degrees, "
							+ gpsLongitudeMinutes.toDisplayString() + " minutes, "
							+ gpsLongitudeSeconds.toDisplayString() + " seconds " + gpsLongitudeRef);

				}

				System.out.println();

				final List<ImageMetadataItem> items = jpegMetadata.getItems();
				for (final ImageMetadataItem item : items) {
					System.out.println("    " + "item: " + item);
				}

				System.out.println();
			}
		} catch (ImageReadException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printTagValue(final JpegImageMetadata jpegMetadata, final TagInfo tagInfo) {
		final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
		if (field == null) {
			System.out.println(tagInfo.name + ": " + "Not Found.");
		} else {
			System.out.println(tagInfo.name + ": " + field.getValueDescription());
		}
	}

	// ************************************IT IS NOT
	// USED*********************************
	public static void setExifGPSTag(final File jpegImageFile, final File dst) {
		try {

			try (FileOutputStream fos = new FileOutputStream(dst); OutputStream os = new BufferedOutputStream(fos)) {
				TiffOutputSet outputSet = null;

				// note that metadata might be null if no metadata is found.
				final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
				final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
				if (null != jpegMetadata) {
					// note that exif might be null if no Exif metadata is found.
					final TiffImageMetadata exif = jpegMetadata.getExif();

					if (null != exif) {
						// TiffImageMetadata class is immutable (read-only).
						// TiffOutputSet class represents the Exif data to write.
						//
						// Usually, we want to update existing Exif metadata by
						// changing
						// the values of a few fields, or adding a field.
						// In these cases, it is easiest to use getOutputSet() to
						// start with a "copy" of the fields read from the image.
						outputSet = exif.getOutputSet();
					}
				}

				// if file does not contain any exif metadata, we create an empty
				// set of exif metadata. Otherwise, we keep all of the other
				// existing tags.
				if (null == outputSet) {
					outputSet = new TiffOutputSet();
				}

				{
					// Example of how to add/update GPS info to output set.

					// New York City
					final double longitude = -74.0; // 74 degrees W (in Degrees East)
					final double latitude = 40 + 43 / 60.0; // 40 degrees N (in Degrees
					// North)

					outputSet.setGPSInDegrees(longitude, latitude);
				}

				new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
			}
		} catch (Exception e) {
			System.out.println("***********ERROR WRITER GPS: " + e.getMessage());
			// TODO: handle exception
		}

	}

	// *****************************SET METADATA IN IMAGE JPEG
	// *************************************************

	public static void changeExifMetadata(File jpegImageFile, File dst, double latitude, double longitude) {
		try {
			// Criar fluxo de saída para a nova imagem
			try (FileOutputStream fos = new FileOutputStream(dst); OutputStream os = new BufferedOutputStream(fos)) {
				TiffOutputSet outputSet = null;

				// Ler metadados da imagem original
				final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
				final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

				if (jpegMetadata != null) {
					final TiffImageMetadata exif = jpegMetadata.getExif();

					if (exif != null) {
						// Obter a configuração existente de EXIF, se houver
						outputSet = exif.getOutputSet();
					}
				}

				// Criar metadados EXIF se não existirem
				if (outputSet == null) {
					outputSet = new TiffOutputSet();
				}

				// Configurar ou criar informações de GPS
				setGpsMetadata(outputSet, latitude, longitude);

				// Escrever a nova imagem com metadados atualizados
				new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
				System.out.println("Metadados EXIF atualizados com sucesso!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualizar os metadados EXIF: " + e.getMessage());
		}
	}

	private static void setGpsMetadata(TiffOutputSet outputSet, double latitude, double longitude)
			throws ImageWriteException {
		// Converter latitude e longitude para formato EXIF
		RationalNumber[] latitudeComponents = toRationalNumbers(latitude);
		RationalNumber[] longitudeComponents = toRationalNumbers(longitude);

		String latitudeRef = latitude >= 0 ? "N" : "S";
		String longitudeRef = longitude >= 0 ? "E" : "W";

		// Obter ou criar o diretório GPS
		TiffOutputDirectory gpsDirectory = outputSet.getOrCreateGPSDirectory();

		// Atualizar ou adicionar as coordenadas de latitude
		gpsDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
		gpsDirectory.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF, latitudeRef);

		gpsDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
		gpsDirectory.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE, latitudeComponents);

		// Atualizar ou adicionar as coordenadas de longitude
		gpsDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
		gpsDirectory.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, longitudeRef);

		gpsDirectory.removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
		gpsDirectory.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE, longitudeComponents);

		System.out.println("Informações GPS configuradas: Lat: " + latitude + " " + latitudeRef + ", Lon: " + longitude
				+ " " + longitudeRef);
	}

	private static RationalNumber[] toRationalNumbers(double coord) {
		coord = Math.abs(coord);
		int degrees = (int) coord;
		double minutesRaw = (coord - degrees) * 60;
		int minutes = (int) minutesRaw;
		double seconds = (minutesRaw - minutes) * 60;

		return new RationalNumber[] { new RationalNumber(degrees, 1), new RationalNumber(minutes, 1),
				new RationalNumber((int) (seconds * 1000000), 1000000) // Segundos com precisão de 6 dígitos
		};
	}

}
