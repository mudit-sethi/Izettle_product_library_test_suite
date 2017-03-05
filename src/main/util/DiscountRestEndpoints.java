import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;


public class DiscountRestEndpoints {

    public String token;

    public DiscountRestEndpoints(){
        RestAssured.baseURI = "https://products.izettletest.com/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828/discounts";
        this.token = GetToken();
    }

    private String GetToken(){
        return
            given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=refresh_token&client_id=0bb91e02-7e53-4742-9bcc-7388d694e1fe&" +
                        "refresh_token=IZSEC0cddedec-c4d6-474d-b215-63d753e8a906").
            when()
                .post("https://oauth.izettletest.com/token/").
            then()
                .statusCode(HttpStatus.SC_OK).
            extract()
                .path("access_token");
    }

    public Response GetListOfDiscounts(int expectedResponse) {
        return
            given()
                .header("Authorization", "Bearer " + token).
            when()
                .get().
            then()
                .statusCode(expectedResponse)
                .contentType(ContentType.JSON).
            extract()
                .response();
    }

    public Response GetDiscountDetails(String DiscountId, int expectedResponse) {

        return
            given()
                .header("Authorization", "Bearer " + token).
              when()
                .get("/" + DiscountId).
            then()
                .statusCode(expectedResponse).
            extract()
                .response();

        }

    public Response GetDiscountDetailsWithIfMatchHeader(String DiscountId, int expectedResponse, String ETag) {

        return
            given()
                .header("Authorization", "Bearer " + token)
                .header("If-None-Match", ETag).
            when()
                .get("/" + DiscountId).
            then()
                .statusCode(expectedResponse).
            extract()
                .response();
    }


    public void DeleteDiscount(String DiscountId, int expectedResponse) {

        given()
            .header("Authorization", "Bearer " + token).
        when()
            .delete("/" + DiscountId).
        then()
            .statusCode(expectedResponse);
    }

    public void UpdateDiscountDetails(String DiscountId, String Payload, int ExpectedResponse) {

        given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(Payload).
        when()
            .put("/" + DiscountId).
        then()
            .statusCode(ExpectedResponse);
    }

    public void UpdateDiscountDetailsWithCondition(String DiscountId, String Payload, String ETag, int ExpectedResponse){

        given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .header("If-Match", "\"" + ETag + "\"" )
            .body(Payload).
        when()
            .put("/" + DiscountId).
        then()
            .statusCode(ExpectedResponse);
        }

    public String CreateDiscount(String Payload, int ExpectedResponse){

        String Location =
            given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Payload).
            when()
                .post().
            then()
                .statusCode(ExpectedResponse).
            extract()
                .header("Location");

        return StringUtils.substringAfter(Location, "/discounts/");
    }

}
