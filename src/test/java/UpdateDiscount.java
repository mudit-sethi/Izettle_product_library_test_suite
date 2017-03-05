import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertThat;

public class UpdateDiscount {

    private static String Name;
    private static String Percentage;
    private static String Amount;
    private static String CurrencyId;
    private static String Reference;
    private static DiscountRestEndpoints DiscountEndpoints;
    private static DiscountJsonBuilder JsonHelper;
    private String JsonPayload;
    private String DiscountIdToUpdate;

    @Before
    public void setUp() {
        DiscountEndpoints = new DiscountRestEndpoints();
        Name = RandomStringUtils.randomAlphabetic(10);
        Percentage = RandomStringUtils.randomNumeric(2);
        Reference = RandomStringUtils.randomAlphabetic(10);
        Amount = RandomStringUtils.randomNumeric(5);
        CurrencyId = "AED";
        JsonHelper = new DiscountJsonBuilder(Name, Percentage, Reference, Amount,
                CurrencyId, null, null);
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        DiscountIdToUpdate = DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);

    }

    @Test
    public void UpdateDiscountWithNewName() {
        String NewDiscountName = RandomStringUtils.randomAlphabetic(10);
        Map NewPutMap = new HashMap();
        NewPutMap.put("name", NewDiscountName);
        Gson gson = new Gson();
        String NewNameJson = gson.toJson(NewPutMap);
        DiscountEndpoints.UpdateDiscountDetails(DiscountIdToUpdate, NewNameJson, HttpStatus.SC_NO_CONTENT);
        Response response = DiscountEndpoints.GetDiscountDetails(DiscountIdToUpdate, HttpStatus.SC_OK);
        JsonPath bodyJson = response.body().jsonPath();
        assertThat(NewDiscountName, IsEqual.equalTo(bodyJson.getString("name"))); //assert new name updated
        assertThat(Reference, IsEqual.equalTo(bodyJson.getString("externalReference"))); //assert rest of the fields unchanged
    }

    @Test
    public void UpdateDiscountForDiscountThatNotExists() {
        String NewDiscountName = RandomStringUtils.randomAlphabetic(10);
        Map NewPutMap = new HashMap();
        NewPutMap.put("name", NewDiscountName);
        Gson gson = new Gson();
        String NewNameJson = gson.toJson(NewPutMap);
        UUID DummyDiscountId = UUID.randomUUID();
        DiscountEndpoints.UpdateDiscountDetails(DummyDiscountId.toString(), NewNameJson, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void UpdateDiscountWithBodyFieldThatIsNotAllowed() {
        Map NewPutMap = new HashMap();
        NewPutMap.put("Random", "Random");  // Will update the Discount with this Field which is not in the contract
        Gson gson = new Gson();
        String NewNameJson = gson.toJson(NewPutMap);
        DiscountEndpoints.UpdateDiscountDetails(DiscountIdToUpdate, NewNameJson, HttpStatus.SC_BAD_REQUEST);
        // This test fails as it gives 204 code
    }

    @Test
    public void UpdateDiscountWithNullValueForName() {
        Map NewPutMap = new HashMap();
        NewPutMap.put("name", null);
        NewPutMap.put("Description", "Description");
        GsonBuilder gb = new GsonBuilder();
        gb.serializeNulls();
        Gson gson = gb.create();
        String NewNameJson = gson.toJson(NewPutMap);
        DiscountEndpoints.UpdateDiscountDetails(DiscountIdToUpdate, NewNameJson, HttpStatus.SC_NO_CONTENT);
        Response response = DiscountEndpoints.GetDiscountDetails(DiscountIdToUpdate, HttpStatus.SC_OK);
        JsonPath bodyJson = response.body().jsonPath();
        assertThat(bodyJson.getString("name"), IsEqual.equalTo(null)); //assert name updated with null
    }

    @Test
    public void UpdateDiscountNameWithCondition() {
        String NewDiscountName = RandomStringUtils.randomAlphabetic(10);
        Response response = DiscountEndpoints.GetDiscountDetails(DiscountIdToUpdate, HttpStatus.SC_OK);
        JsonPath bodyJson = response.body().jsonPath();
        String ETag = bodyJson.getString("etag");
        Map NewPutMap = new HashMap();
        NewPutMap.put("name", NewDiscountName);
        Gson gson = new Gson();
        String NewNameJson = gson.toJson(NewPutMap);
        DiscountEndpoints.UpdateDiscountDetailsWithCondition(DiscountIdToUpdate, NewNameJson, ETag, HttpStatus.SC_NO_CONTENT);
        response = DiscountEndpoints.GetDiscountDetails(DiscountIdToUpdate, HttpStatus.SC_OK);
        bodyJson = response.body().jsonPath();
        assertThat(bodyJson.getString("name"), IsEqual.equalTo(NewDiscountName)); //assert name updated
    }

    @Test
    public void UpdateDiscountNameWithPreConditionFail() {
        String NewDiscountName = RandomStringUtils.randomAlphabetic(10);
        Map NewPutMap = new HashMap();
        NewPutMap.put("name", NewDiscountName);
        Gson gson = new Gson();
        String NewNameJson = gson.toJson(NewPutMap);
        String Etag = RandomStringUtils.randomAlphanumeric(10);
        DiscountEndpoints.UpdateDiscountDetailsWithCondition(DiscountIdToUpdate, NewNameJson, Etag, HttpStatus.SC_PRECONDITION_FAILED);
    }


}
