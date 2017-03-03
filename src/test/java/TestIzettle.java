import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TestIzettle {

    public static Response response;
    public static String JsonAsString;

    @BeforeClass
    public static void initialization(){
        baseURI = "https://products.izettletest.com/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828/discounts";
    }

    @Test
    public void getDiscountsList(){

        baseURI = "https://products.izettletest.com/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828/discounts";
        String token = "eyJraWQiOiIxNDg4NDg1ODU5NTMyIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNDg4NTY4NDE1LCJzdWIiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJzY29wZSI6WyJBTEw6SU5URVJOQUwiLCJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIiwiUkVBRDpVU0VSSU5GTyIsIldSSVRFOkZJTkFOQ0UiLCJXUklURTpQUk9EVUNUIiwiV1JJVEU6UFVSQ0hBU0UiLCJXUklURTpVU0VSSU5GTyJdLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJvcmdVdWlkIjoiMWY4NWYxYzAtZmYyYS0xMWU2LTlmZWEtZTgzMTQ2ZmIwODI4IiwidXNlclJvbGUiOiJPV05FUiJ9LCJjbGllbnRfaWQiOiIwYmI5MWUwMi03ZTUzLTQ3NDItOWJjYy03Mzg4ZDY5NGUxZmUifQ.Alen_I-8C0RT6n1BhqHSWxYkT7aQ12GGuecUz21d9OHb3UF5LaTrWjy_c2KlrYumsDde_qEFahdYInNLIBaaOW0uE3Xsx2s_7V5XDUN_T8pyJF7JZbgA7TRfvXlvk6ToR5hfv3QwD-Dzel5galmAshxQ7M7ib8Zk5cpuHf-IN0oHb4-NGr0wyEIjIkdFyBLjwwKKy9BrDfYHB2UgGp4Xk_VK242-LGKlceAMOaDNxqionujwn15LqlLhV2yU4vre6KCxKIz2UjaHn7FnEL9xCVu_IWWmf8n7pO4H3CkJy3_J1gOztFaVE7OguliMsUu0lvNQoEpXVdzdeSSRC__q9Q";
        given()
                .header("Authorization", "Bearer " + token).
        when()
                .get().
        then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON);

    }
    
    @Test
    public void getDiscountsWhenInvalidToken(){
        baseURI = "https://products.izettletest.com/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828/discounts";
        String invalidToken = "dummytoken"
        response =
                given()
                   .header("Authorization", "Bearer " + invalidToken).
                when()
                    .get().
                then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED).
                extract()
                    .response();
        JsonAsString = response.asString();


    }
    



}
