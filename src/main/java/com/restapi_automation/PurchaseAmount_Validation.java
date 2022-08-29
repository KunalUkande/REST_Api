package com.restapi_automation;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.payloads.Payload;

import io.restassured.path.json.JsonPath;

public class PurchaseAmount_Validation {
	
	@Test
	
	// 6. Verify if Sum of all Course prices matches with Purchase Amount
	
	public void validation_PA() {
		
		JsonPath js = new JsonPath(Payload.mockResponse());
		
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		int actualPurchaseAmount = 0;

		for (int i = 0; i < js.getInt("courses.size()"); i++) {
			int eachCoursePrice = js.getInt("courses[" + i + "].price");
			int eachCourseSoldCopies = js.getInt("courses[" + i + "].copies");

			actualPurchaseAmount = actualPurchaseAmount + (eachCoursePrice * eachCourseSoldCopies);

		}
		
		System.out.println("Given Purchase Amount : "+purchaseAmount);
		System.out.println("Actual Purchase Amount : "+actualPurchaseAmount);
		Assert.assertEquals(purchaseAmount, actualPurchaseAmount);
		
	}

}
