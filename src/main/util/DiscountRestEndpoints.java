import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;


public class DiscountRestEndpoints {

    private String token;
    private String Location;

    public DiscountRestEndpoints(){
        RestAssured.baseURI = "https://products.izettletest.com/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828/discounts";
        this.token = GetToken();
    }

    private String GetToken(){
        String AccessToken =
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
        return AccessToken;
    }

    public Response GetListOfDiscounts(int expectedResponse) {

        Response response =
            given()
                .header("Authorization", "Bearer " + token).
            when()
                .get().
            then()
                .statusCode(expectedResponse)
                .contentType(ContentType.JSON).
            extract()
                .response();
        return response;
    }

    public Response GetDiscountDetails(String DiscountId, int expectedResponse) {

        Response response =
            given()
                .header("Authorization", "Bearer " + token).
              when()
                .get("/" + DiscountId).
            then()
                .statusCode(expectedResponse).
            extract()
                .response();

        return response;
    }

    public Response GetDiscountDetailsWithIfMatchHeader(String DiscountId, int expectedResponse, String ETag) {

        Response response =
            given()
                .header("Authorization", "Bearer " + token)
                .header("If-None-Match", ETag).
            when()
                .get("/" + DiscountId).
            then()
                .statusCode(expectedResponse).
            extract()
                .response();
        return response;
    }


    public void DeleteDiscount(String DiscountId, int expectedResponse) {

        given()
            .header("Authorization", "Bearer " + token).
        when()
            .delete("/" + DiscountId).
        then()
            .statusCode(expectedResponse);
    }

    public ResponseBody UpdateDiscountDetails(String DiscountId, String Payload, int ExpectedResponse, String ETag){
        ResponseBody body =
            given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("If-Match", ETag)
                .body(Payload).
            when()
                .put("/" + DiscountId).
            then()
                .statusCode(ExpectedResponse).
            extract()
                .response();
        return body;

    }

    public String CreateDiscount(String Payload, int ExpectedResponse){

        Location =
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

        String DiscountId = StringUtils.substringAfter(Location, "/discounts/");
        return DiscountId;
    }

    public String GetEtagFromHeader(Response response){
        String Etag = response.header("ETag");
        return Etag;
    }
}
