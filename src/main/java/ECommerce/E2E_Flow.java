package ECommerce;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class E2E_Flow {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RequestSpecification Login_reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

// Login

		LoginRequest_POJO loginReq = new LoginRequest_POJO();
		loginReq.setUserEmail("kunalukande@gmail.com");
		loginReq.setUserPassword("Test@123");

		RequestSpecification login_Given = given().spec(Login_reqSpec).body(loginReq);

		LoginResponse_POJO loginResponse = login_Given.when().post("/api/ecom/auth/login").then().log().all()
				.assertThat().statusCode(200).extract().response().as(LoginResponse_POJO.class);

		String token = loginResponse.getToken();
		String userID = loginResponse.getUserId();

		/*
		 * JsonPath login_JP= new JsonPath(login_Response); String token =
		 * login_JP.get("token"); String userID = login_JP.get("userId")
		 */

// Create Product

		RequestSpecification product_reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();

		ResponseSpecification product_resSpec = new ResponseSpecBuilder().expectStatusCode(201).build();

		RequestSpecification createProduct_Given = given().spec(product_reqSpec).param("productName", "iPhone")
				.param("productAddedBy", userID).param("productCategory", "Mobile")
				.param("productSubCategory", "SmartPhone").param("productPrice", "300")
				.param("productDescription", "iPhone 4s").param("productFor", "UniSex")
				.multiPart("productImage", new File(
						"iPhone.jpg"));

		String createProduct_Response = createProduct_Given.when().post("/api/ecom/product/add-product").then()
				.spec(product_resSpec).log().all().extract().response().asString();

		JsonPath createProductJP = new JsonPath(createProduct_Response);

		String productID = createProductJP.getString("productId");


// PlaceOrder

		PlaceOrderRequest_Sub_POJO placeOrder_childReq = new PlaceOrderRequest_Sub_POJO();

		placeOrder_childReq.setCountry("India");
		placeOrder_childReq.setProductOrderedId(productID);

		List<PlaceOrderRequest_Sub_POJO> placeOrderReqList = new ArrayList<PlaceOrderRequest_Sub_POJO>();
		placeOrderReqList.add(placeOrder_childReq);

		PlaceOrderRequest_POJO placeOrderReq = new PlaceOrderRequest_POJO();

		placeOrderReq.setOrders(placeOrderReqList);

		RequestSpecification placeOrder_Given = given().spec(product_reqSpec).header("Content-Type", "application/json")
				.body(placeOrderReq);

		PlaceOrderResponse_POJO placeOrderRes = placeOrder_Given.when().post("/api/ecom/order/create-order").then()
				.log().all().spec(product_resSpec).extract().response().as(PlaceOrderResponse_POJO.class);

		List<String> orderID_List = placeOrderRes.getOrders();
		String orderID = "";
		for (int i = 0; i < orderID_List.size(); i++) {
			orderID = orderID_List.get(i);
		}


// View Order Details

		RequestSpecification ViewOrder_Given = given().spec(product_reqSpec).queryParam("id", orderID);
		OrderDetailsResponse_POJO orderDetails = ViewOrder_Given.when().get("/api/ecom/order/get-orders-details").then()
				.assertThat().statusCode(200).extract().response().as(OrderDetailsResponse_POJO.class);
		System.out.println("Success Message : " + orderDetails.getMessage());
		System.out.println(orderDetails.getData().getProductImage());


// Delete Order

		RequestSpecification deleteOrder_Given = given().spec(product_reqSpec).pathParam("orderID", orderID);

		String deleteOrderResponse = deleteOrder_Given.when().delete("/api/ecom/order/delete-order/{orderID}").then()
				.extract().response().asString();
		System.out.println(deleteOrderResponse);

		
		// Delete Product

		RequestSpecification deleteProduct_Given = given().spec(product_reqSpec).pathParam("productID", productID);

		String deleteProductResponse = deleteProduct_Given.when().delete("/api/ecom/product/delete-product/{productID}")
				.then().extract().response().asString();
		System.out.println(deleteProductResponse);

	}

}
