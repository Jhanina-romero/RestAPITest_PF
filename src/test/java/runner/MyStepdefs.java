package runner;

import config.Configuration;
import factoryRequest.FactoryRequest;
import factoryRequest.RequestInfo;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;

public class MyStepdefs {
    Response response;
    RequestInfo requestInfo=new RequestInfo();
    Map<String,String> variables = new HashMap<>();

    @Given("using token in todo.ly")
    public void usingTokenInTodoLy() {
        String credentials = Base64.getEncoder()
                .encodeToString((Configuration.email + ":" + Configuration.password).getBytes());

        requestInfo.setUrl(Configuration.host + "/api/authentication/token.json")
                .setHeader("Authorization","Basic " + credentials);
        response = FactoryRequest.make("get").send(requestInfo);

        String token = response.then().extract().path("TokenString");
        requestInfo = new RequestInfo();
        requestInfo.setHeader("Token", token);
    }

    @When("send the {} request {string} with body")
    public void sendThePOSTRequestWithBody(String method,String url,String body) {
        String email= generatedEmail();
        requestInfo.setUrl(Configuration.host + this.replaceValues(url))
                .setBody(body.replace("<random_email>", email));
        response = FactoryRequest.make(method).send(requestInfo);
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @And("the attribute for {string} should be {string}")
    public void theAttributeForShouldBe(String attribute, String expectedValue) {
        response.then().body(attribute, equalTo(expectedValue));
    }

    @And("save the {string} in the variable {string}")
    public void saveTheInTheVariable(String attribute, String nameVariable) {
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
