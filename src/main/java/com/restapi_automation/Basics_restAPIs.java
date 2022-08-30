package com.restapi_automation;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.nio.file.Files;
import java.nio.file.Paths;


import org.testng.Assert;



public class Basics_restAPIs {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub

		// Given - All input details - Base URL, Query params, header, body
		// When - Action/Submit the details - HTTPS method, Resource data.
		// Then - Response validation
		
		// Line no. 36 -->> How to handle static Json file using File path -->> Convert Json to Byte -->> Convert Byte to String

// Problem Statement - Add Place >> Get Place Id as string to be reusable >> update address >> get place ID >> validate whether address is updated or not

		// Add Place
		
		//Users/kunalukande/Desktop/Eclipse/

		// Give Base URI
		RestAssured.baseURI = "https://rahulshettyacademy.com";

		// Provide details to add place and extract teh response as string
		String response = given().log().all().queryParam("key", "qaclick123").header("content-type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get("addPlace.json")))).when().post("/maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)").extract().response()
				.asString();

		System.out.println(response);

		// Parse the string Json response to get place ID
		JsonPath js = new JsonPath(response);
		String placeID = js.getString("place_id");

		System.out.println(placeID);

		// Update Place

		String newAddress = "Maphar Pristine, Hyderabad";

		// Provide details to update new address & validate the successful update for
		// address using inbuilt method
		given().log().all().queryParam("key", "qaclick123").header("content-type", "application/json")
				.body("{\r\n" + "    \"place_id\": \"" + placeID + "\",\r\n" + "    \"address\": \"" + newAddress
						+ "\",\r\n" + "    \"key\": \"qaclick123\"\r\n" + "}")
				.when().put("maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));

		// Get Place

		// Provide details to get the updated address and extract the response as string
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID)
				.when().get("maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract()
				.response().asString();

		// Parse the string Json response to get address
		JsonPath js1 = new JsonPath(getPlaceResponse);
		String updatedAddress = js1.getString("address");

		// System.out.println(updatedAddress);

		// Validated Updated address using TestNG assertions
		Assert.assertEquals(updatedAddress, newAddress);

	}

}
