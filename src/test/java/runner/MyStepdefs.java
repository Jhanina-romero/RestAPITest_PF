package runner;

import config.Configuration;
import factoryRequest.FactoryRequest;
import factoryRequest.RequestInfo;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;

public class MyStepdefs {
    Response response;
    RequestInfo requestInfo=new RequestInfo();
    Map<String,String> variables = new HashMap<>();
    String email;

    @Given("a user created")
    public void aUserCreated() {
        email= generatedEmail();
    }

    @When("send {} request {string} with body")
    public void sendRequestWithBody(String method,String url,String body) {
        requestInfo.setUrl(Configuration.host + this.replaceValues(url))
                .setBody(body.replace("<random_email>", email));
        response = FactoryRequest.make(method).send(requestInfo);
    }

    @When("authenticate with user created")
    public void authenticateWithUserCreated() {
        String credentials = Base64.getEncoder()
                .encodeToString((email + ":" + Configuration.password).getBytes());

        requestInfo.setUrl(Configuration.host + "/api/authentication/token.json")
                .setHeader("Authorization","Basic " + credentials);
        response = FactoryRequest.make("get").send(requestInfo);

        String token = response.then().extract().path("TokenString");
        requestInfo = new RequestInfo();
        requestInfo.setHeader("Token", token);
    }

    @Then("response code should be {int}")
    public void responseCodeShouldBe(int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @And("attribute for {string} should be {string}")
    public void attributeForShouldBe(String attribute, String expectedValue) {
        response.then().body(attribute, equalTo(expectedValue));
    }

    @And("save {string} in the variable {string}")
    public void saveInTheVariable(String attribute, String nameVariable) {
        variables.put(nameVariable,response.then().extract().path(attribute) + "");
    }

    private String replaceValues(String value){
        for (String key:variables.keySet() ) {
            value=value.replace(key,variables.get(key));
        }
        return value;
    }

    private String generatedEmail() {
        return "user" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }
}
