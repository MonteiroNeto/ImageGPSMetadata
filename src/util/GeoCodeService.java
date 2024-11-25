package util;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class GeoCodeService {


	public static String getAddress(double latitude, double longitude) {
		try {
			// Construir a URL da requisição Nominatim
			String url = "https://nominatim.openstreetmap.org/reverse?format=json" + "&lat=" + latitude + "&lon="
					+ longitude + "&addressdetails=1";

			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");

			// Ler a resposta da API
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				response.append(inputLine);

			}
			in.close();

			// Analisar o JSON usando org.json
			JSONObject jsonObject = new JSONObject(response.toString());
			if (jsonObject.has("display_name")) {
				JSONObject addressObject = jsonObject.getJSONObject("address");

				// Obter os campos específicos
				String street = addressObject.optString("road", "N/A");
				String town = addressObject.optString("town", "N/A");
				String houseNumber = addressObject.optString("house_number", "N/A");

				// Formatar o endereço
				String formattedAddress = "\nStreet: " + street + "\nHouse Number: " + houseNumber + "\nTown: " + town;

				System.out.println("******DISPLAY_NAME: " + jsonObject.getString("display_name"));
				System.out.println("******ADDRESS: " + formattedAddress);
				// return jsonObject.getString("display_name");
				return formattedAddress;
			}
			return "Endereço não encontrado";

		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao obter endereço";
		}
	}

}
