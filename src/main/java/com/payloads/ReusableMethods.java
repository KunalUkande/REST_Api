package com.payloads;
import io.restassured.path.json.JsonPath;

public class ReusableMethods {

		public static void stringToJson(String response) {
			JsonPath js = new JsonPath(response);
		}
		
		

}
