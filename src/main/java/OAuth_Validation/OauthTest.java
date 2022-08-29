package OAuth_Validation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;

import POJO_serlztn_dserlztn.Complete_Json;
import POJO_serlztn_dserlztn.WebAutomation_Json;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.opentelemetry.exporter.logging.SystemOutLogExporter;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.util.List;

public class OauthTest {

	public static void main(String[] args) {

//		Due to new update in google we won't be able to sign so commenting below code

		/*
		 * // WebDriverManager.chromedriver().setup(); // ChromeOptions option = new
		 * ChromeOptions(); // option.addArguments("incognito"); // // WebDriver driver
		 * = new ChromeDriver(option); // // driver.get( //
		 * "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=veifyfjdss"
		 * ); // //
		 * driver.findElement(By.xpath("//input[@name='identifier']")).sendKeys(
		 * "kunalukande"); //
		 * driver.findElement(By.xpath("//span[text()='Next']")).click(); // //
		 * driver.findElement(By.xpath("//input[@name='password']")).sendKeys(
		 * "killerdrama"); //
		 * driver.findElement(By.xpath("//span[text()='Next']")).click();
		 */
		
		
		String actualURL = "https://rahulshettyacademy.com/getCourse.php?state=veifyfjdss&code=4%2F0AdQt8qjf7fAxDadty4Eb7s7XsPiTmIqUVva0xyg4B7gy8Y3qZtLAdO2jU6t_0NlzdmbZWw&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none ";

		String[] URL = actualURL.split("&");

		String[] code_URL = URL[1].split("code=");

		String code = code_URL[1];

		System.out.println(code);

		String tokenResponse = given().log().all().urlEncodingEnabled(false).queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		JsonPath js = new JsonPath(tokenResponse);
		String access_token = js.get("access_token");

		Complete_Json jsonResponse = given().log().all().queryParam("access_token", access_token).expect()
				.defaultParser(Parser.JSON).when().get("https://rahulshettyacademy.com/getCourse.php")
				.as(Complete_Json.class);

		System.out.println(jsonResponse.getLinkedIn());

		for (int i = 0; i < jsonResponse.getCourses().getApi().size(); i++) {

			String courseTitle = jsonResponse.getCourses().getApi().get(i).getCourseTitle();

			if (courseTitle.contains("SoapUI")) {
				System.out.println(jsonResponse.getCourses().getWebAutomation().get(i).getPrice());
			}

		}

	}

}
