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
import java.net.ProtocolException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
//import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;

/*	
TODO...
 */

public class KrakenPublicRequest {
	public void createExchange () {
		// Create an exchange instance
		try {
			String address = String.format("%s/%s/%s", getMarketDataApiUrl(), "exchanges", "kraken");
			URL url = new URL(address);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			String result = CCXTquery(con, "{\"id\":\"kraken\"}");
			//System.out.println(result);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JsonArray queryPublic(String a_sMethod, String pair, int grain) {

		String address = String.format("%s/%s/%s", getMarketDataApiUrl(), "exchanges", "kraken");
		String result = null;

		try {
			result = this.queryCCXTRest (address, a_sMethod, pair, grain);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonArray jsonObject = null;
		try(JsonReader reader = Json.createReader(new StringReader(result));) {
			jsonObject = reader.readArray(); 
		}
		/* TODO check que c'est null lorsque timeout Kraken */
		return jsonObject;
	}

	public String queryCCXTRest (String address, String a_sMethod, String pair, int grain) throws IOException, MalformedURLException {
		// Create an exchange instance
		/* URL url = new URL(address);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		this.CCXTquery(con, "{\"id\":\"kraken\"}"); */

		// Query OHLC data
		String result = null;
		URL url = new URL(address + "/kraken/" + a_sMethod);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		result = this.CCXTquery(con, "[\""+pair+"\", \""+grain+"m\"]");

		//System.out.println(result);
		return result;
	}

	private String CCXTquery (HttpURLConnection con, String props) throws ProtocolException {
		// Query configuration
		con.setConnectTimeout(30_000);//10 secondess
		con.setRequestProperty("ContentType", "application/x-www-form-urlencoded");
		con.setRequestMethod("POST");
		
		// Send query param
		con.setDoOutput(true);
		try (OutputStream outputStream = con.getOutputStream();
			 DataOutputStream wr = new DataOutputStream(outputStream);) {
			wr.writeBytes(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		// Get response
		StringBuilder response = new StringBuilder();
		try (InputStream inputStream = con.getInputStream();
			 BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));) {
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		return response.toString();
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
