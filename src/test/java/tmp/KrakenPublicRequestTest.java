package tmp;

import static org.junit.Assert.assertTrue;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.junit.Test;

public class KrakenPublicRequestTest {

	@Test
	public void test() {
		KrakenPublicRequest krakenPublicRequest = new KrakenPublicRequest();
		try {
			JsonArray arr = krakenPublicRequest.queryPublic("fetchOHLCV", "ETH/EUR", 240);

			//JsonArray arr = jsonObject.getJsonObject("result")
			//		 .getJsonArray("XETHZEUR");
//			System.out.println(arr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);//TODO
	}

}
