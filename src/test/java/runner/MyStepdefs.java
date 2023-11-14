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

public class MyStepdefs {
    Response response;
    RequestInfo requestInfo=new RequestInfo();
    Map<String,String> variables = new HashMap<>();

    @Given("using basic authentication in todo.ly")
    public void usingBasicAuthenticationInTodoLy() {
        String credentials = Base64.getEncoder()
                .encodeToString((Configuration.email + ":" + Configuration.password).getBytes());

        requestInfo.setUrl(Configuration.host+"/api/authentication/token.json")
                .setHeader("Authorization","Basic "+credentials);
        response = FactoryRequest.make("get").send(requestInfo);
        // get token
        String token = response.then().extract().path("TokenString");
        requestInfo = new RequestInfo();
        requestInfo.setHeader("Token",token);
    }

    @When("send POST request {string} with body")
    public void sendPOSTRequestWithBody(String arg0) {
    }

    @Then("response code should be {int}")
    public void responseCodeShouldBe(int arg0) {
    }

    @And("the attribute {string} should be {string}")
    public void theAttributeShouldBe(String arg0, String arg1) {
    }

    @And("save {string} in the variable {string}")
    public void saveInTheVariable(String arg0, String arg1) {
    }

    @When("send PUT request {string} with body")
    public void sendPUTRequestWithBody(String arg0) {
    }

    @When("send DELETE request {string} with body")
    public void sendDELETERequestWithBody(String arg0) {
    }
}
