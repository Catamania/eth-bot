package tmp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

/*	
TODO...
 */

public class KrakenPublicRequest {
	public JsonObject queryPublic(String a_sMethod, String props) {

		String address = String.format("%s/%d/public/%s", getMarketDataApiUrl(), 0, a_sMethod);

		URL url = null;
		HttpsURLConnection con = null;
		try {
			url = new URL(address);
			con = (HttpsURLConnection) url.openConnection();
			
			con.setConnectTimeout(30_000);//10 secondess
			
			con.setRequestProperty("ContentType", "application/x-www-form-urlencoded");
			con.setRequestMethod("POST");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		con.setDoOutput(true);
		try (OutputStream outputStream = con.getOutputStream();
				DataOutputStream wr = new DataOutputStream(outputStream);) {
			wr.writeBytes(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder response = new StringBuilder();
		try (InputStream inputStream = con.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));) {
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		String result = response.toString();
		
		JsonObject jsonObject = null;
		try(JsonReader reader = Json.createReader(new StringReader(result));) {
			jsonObject = reader.readObject(); 
			
		}
		/* TODO check que c'est null lorsque timeout Kraken */
		return jsonObject;
	}

	/**
	 * Return API URL of the MarketDATA provider defined on the config file
	 */
	public String getMarketDataApiUrl() {
		Properties prop = new Properties();
		InputStream input = null;
		String market_data_api_url = null;
		try {

			input = this.getClass().getResourceAsStream("/config.properties");
			prop.load(input);
			market_data_api_url = prop.getProperty("market_data_api_url");
			input.close();

			return market_data_api_url;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
