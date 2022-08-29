package com.restapi_automation;

import org.testng.Assert;

import com.payloads.Payload;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Whenever in agile if the API is not developed we need to create a mock
		// response and test with that
		/*
		 * 1. Print No of courses returned by API
		 * 
		 * 2. Print Purchase Amount
		 * 
		 * 3. Print Title of the first course
		 * 
		 * 4. Print All course titles and their respective Prices
		 * 
		 * 5. Print no of copies sold by RPA Course
		 * 
		 * 6. Verify if Sum of all Course prices matches with Purchase Amount
		 */

		JsonPath js = new JsonPath(Payload.mockResponse());

		// 1. Print No of courses returned by API

		System.out.println("Number of Course Listed : " + js.getInt("courses.size()"));

		// 2. Print Purchase Amount

		System.out.println("Purchase Amount : " + js.getInt("dashboard.purchaseAmount"));

		// 3. Print Title of the first course

		System.out.println("Title of the First Course : " + js.getString("courses[0].title"));

		// 4. Print All course titles and their respective Prices

		for (int i = 0; i < js.getInt("courses.size()"); i++) {
			System.out.println("Title of the Course : " + js.getString("courses[" + i + "].title"));
			System.out.println("Price of the Course : " + js.getInt("courses[" + i + "].price"));
			System.out.println();
		}

		// 5. Print no of copies sold by RPA Course

		for (int i = 0; i < js.getInt("courses.size()"); i++) {
			if (js.getString("courses[" + i + "].title").equalsIgnoreCase("RPA")) {
				System.out.println("Copies sold for RPA Course : " + js.getInt("courses[" + i + "].copies"));
				break;
			}
		}

		// 6. Verify if Sum of all Course prices matches with Purchase Amount

		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		int actualPurchaseAmount = 0;

		for (int i = 0; i < js.getInt("courses.size()"); i++) {
			int eachCoursePrice = js.getInt("courses[" + i + "].price");
			int eachCourseSoldCopies = js.getInt("courses[" + i + "].copies");

			actualPurchaseAmount = actualPurchaseAmount + (eachCoursePrice * eachCourseSoldCopies);

		}
		Assert.assertEquals(purchaseAmount, actualPurchaseAmount);

	}

}
