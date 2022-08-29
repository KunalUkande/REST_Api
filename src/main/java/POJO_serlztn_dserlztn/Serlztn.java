package POJO_serlztn_dserlztn;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;


import java.util.ArrayList;
import java.util.List;

public class Serlztn {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").build();
		ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200).build();

		Map_Json requestBody = new Map_Json();
		Location_Json loc = new Location_Json();
		loc.setLat(-38.383494);
		loc.setLng(33.427362);
		List<String> myList = new ArrayList<String>();
		myList.add("Shoe Park");
		myList.add("Shop");

		requestBody.setAccuracy(50);
		requestBody.setAddress("29, side layout, cohen 09");
		requestBody.setLanguage("French-IN");
		requestBody.setName("Frontline house");
		requestBody.setPhone_number("(+91) 983 893 3937");
		requestBody.setWebsite("http://google.com");
		requestBody.setLocation(loc);
		requestBody.setTypes(myList);

		RequestSpecification addplaceReqSpec_Given = given().spec(reqSpec).body(requestBody);

		String addPlace_Response = addplaceReqSpec_Given.when().post("/maps/api/place/add/json").then().spec(resSpec)
				.extract().response().asString();
		System.out.println(addPlace_Response);

		JsonPath json_Path = new JsonPath(addPlace_Response);
		String placeID = json_Path.getString("place_id");

		update_Json updateBody = new update_Json();
		List<String> updateList = new ArrayList<String>();
		updateBody.setKey("qaclick123");
		updateBody.setPlace_id(placeID);
		updateBody.setAddress("403, Maphar Pristine");
		updateBody.setName("Radha Bhoyar");
		updateList.add("New York city");
		updateList.add("Madhav Reddy Nagar");
		updateBody.setTypes(updateList);

		RequestSpecification updateplaceReqSpec_Given = given().spec(reqSpec).queryParam("place_id", placeID)
				.body(updateBody);

		String updatePlace_Response = updateplaceReqSpec_Given.when().put("/maps/api/place/update/json").then()
				.spec(resSpec).extract().response().asString();

		System.out.println(updatePlace_Response);

	}

}
