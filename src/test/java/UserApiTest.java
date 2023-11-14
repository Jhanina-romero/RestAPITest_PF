import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserApiTest {

    @Test
    @DisplayName("Verify the CRUD the project")
    public void verifyCreateUpdateDeleteProject(){

        //create
        JSONObject body = new JSONObject();
        body.put("Email","user88@gmail.com");
        body.put("FullName","Jhanina Romero");
        body.put("Password","pASswoRd");

        JsonSchemaFactory schemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(SchemaVersion.DRAFTV4)
                        .freeze()).freeze();


        Response response =given()
                .auth()
                .preemptive()
                .basic("jhanina.romero@gmail.com","test123*")
                .body(body.toString())
                .log().all()
                .when()
                .post("https://todo.ly/api/user.json");

        System.out.println(">>>>>>>>>>>> Response <<<<<<<<<<<<<");

        response.then()
                .statusCode(200)
                .body("FullName",equalTo("Jhanina Romero"))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemaCreateUserResponse.json").using(schemaFactory))
                .log().all();

        // extraer una valor
        int userId= response.then().extract().path("Id");

        // update
        body.put("FullName","updated");
        response =given()
                .auth()
                .preemptive()
                .basic("jhanina.romero@gmail.com","test123*")
                .body(body.toString())
                .log().all()
                .when()
                .put("https://todo.ly/api/user/" + userId + ".json");

        System.out.println(">>>>>>>>>>>> Response <<<<<<<<<<<<<");

        response.then()
                .statusCode(200)
                //.body("FullName",equalTo("updated"))
                .log().all();


        //delete
        response=given()
                .auth()
                .preemptive()
                .basic("jhanina.romero@gmail.com","test123*")
                .log().all()
                .when()
                .delete("https://todo.ly/api/user/" + userId + ".json");

        System.out.println(">>>>>>>>>>>> Response <<<<<<<<<<<<<");

        response.then()
                .statusCode(200)
                //.body("FullName",equalTo("updated"))
                //.body("Id",equalTo(userId))
                .log().all();
    }
}
