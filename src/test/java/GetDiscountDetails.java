import com.google.gson.Gson;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GetDiscountDetails {

    private static String Name;
    private static String Percentage;
    private static String Amount;
    private static String CurrencyId;
    private static String Reference;
    private static DiscountRestEndpoints DiscountEndpoints;
    private static DiscountJsonBuilder JsonHelper;

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

    }

    @Test
    public void DiscountDetailsWhenEtagMatch() {
        String JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        String DiscountId = DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        Response response = DiscountEndpoints.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
        String ETag = response.header("Etag");
        DiscountEndpoints.GetDiscountDetailsWithIfMatchHeader(DiscountId, HttpStatus.SC_NOT_MODIFIED, ETag);
    }

    @Test
    public void DiscountDetailsWhenEtagNotMatch(){
        String JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        String DiscountId = DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        Response response = DiscountEndpoints.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
        String ETag1 = response.header("Etag");
        String NewDiscountName = RandomStringUtils.randomAlphabetic(10);
        Map NewPutMap = new HashMap();
        NewPutMap.put("name", NewDiscountName);
        Gson gson = new Gson();
        String NewNameJson = gson.toJson(NewPutMap);
        DiscountEndpoints.UpdateDiscountDetails(DiscountId,  NewNameJson, HttpStatus.SC_NO_CONTENT);
        DiscountEndpoints.GetDiscountDetailsWithIfMatchHeader(DiscountId, HttpStatus.SC_OK, ETag1);
    }

    @Test
    public void DiscountDetailsForInvalidDiscount() {
        UUID InvalidDiscountId = UUID.randomUUID();
        DiscountEndpoints.GetDiscountDetails(InvalidDiscountId.toString(), HttpStatus.SC_NOT_FOUND);
    }

}
