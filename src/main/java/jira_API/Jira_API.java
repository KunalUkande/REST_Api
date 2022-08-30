package jira_API;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Jira_API {

	public static void main(String[] args) throws Exception

	{
		// Create Session ID

		RestAssured.baseURI = "http://localhost:8080/";

		// If we need just a session ID for login request the we can use session filters
		// just to by pass HTTPS validations we can use relaxedHTTPSValidations() method in REST Assured libraries.

		SessionFilter session = new SessionFilter();

		given().relaxedHTTPSValidation().log().all().header("Content-Type", "application/json").body(new String(Files.readAllBytes(Paths.get(
				"createSession_Body.json"))))
				.filter(session).when().post("rest/auth/1/session").then().log().all().assertThat().statusCode(200);

		// Add Comment

		String addComment_response = given().log().all().pathParam("key", "RSA-3")
				.header("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get(
						"addComment_Body.json"))))
				.filter(session).when().post("rest/api/2/issue/{key}/comment").then().log().all().assertThat()
				.statusCode(201).extract().response().asString();

		JsonPath js = new JsonPath(addComment_response);
		String commentID = js.getString("id");
		String actualComment = js.getString("body");

		// Add Attachments
		
		//To add an attachment to automation script for sending it to the server we need to use multipart method in given.

		given().log().all().header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data")
				.filter(session).pathParam("key", "RSA-3")
				.multiPart("file", new File(
						"SampleAttachment.txt"))
				.when().post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

		// Get Issue Details
		
		// We can use path and query params together to filter out large json reponse for desired values.

		String getIssue_response = given().log().all().pathParam("key", "RSA-3").filter(session)
				.queryParam("fields", "comment").when().get("/rest/api/2/issue/{key}").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		JsonPath js1 = new JsonPath(getIssue_response);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentsCount; i++) {
			String commentID_runTime = js1.get("fields.comment.comments[" + i + "].id").toString();
			if (commentID_runTime.equalsIgnoreCase(commentID)) {
				String expected_Comment = js1.get("fields.comment.comments[" + i + "].body").toString();
				Assert.assertEquals(expected_Comment, actualComment);
			}

		}

	}

}
