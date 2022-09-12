package com.restapi_automation;



import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.payloads.Payload;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test(dataProvider = "BookData")
	public void addBook(String isbn, String aisle) {

		RestAssured.baseURI = "http://216.10.245.166";
		String addBook_Response = given().log().all().header("Content-Type", "application/json")
				.body(Payload.addBook_Body(isbn, aisle)).when().post("/Library/Addbook.php").then().log().all()
				.assertThat().statusCode(200).extract().response().asString();

		JsonPath js = new JsonPath(addBook_Response);
		String ID = js.getString("ID");
		System.out.println(ID);

	}

	@Test(dataProvider = "BookData")
	public void deleteBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String deleteResponse = given().header("Content-Type", "application/json")
				.body(Payload.deleteBook_Body(isbn, aisle)).when().post("/Library/DeleteBook.php").then().log().all()
				.assertThat().statusCode(200).extract().response().asString();

		JsonPath js = new JsonPath(deleteResponse);
		String deleteMessage = js.getString("msg");
		System.out.println(deleteMessage);

	}

	@DataProvider(name = "BookData")

	public Object[][] get_bookData() {

		// Unidirectional Arrays = Collection of elements
		// Multidirectional Arrays = Collection of Arrays

		return new Object[][] { { "qweo", "8795" }, { "qwhr", "3782" }, { "dwer", "6552" } };
	}

}
