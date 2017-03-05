
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;

public class UpdateDiscount {

    private static String DiscountName;
    private static String DiscountPercentage;
    private static String DiscountAmount;
    private static String AmountCurrencyId;
    private static String DiscountReference;
    private static DiscountRestEndpoints TestHelp;
    private static DiscountJsonBuilder JsonHelper;
    private static String DiscountToDelete;
    private String JsonPayload;
    private String DiscountId;
    private Response ResponseDetails;
    private String DiscountIdToUpdate;

    @Before
    public void setUp() {
        TestHelp = new DiscountRestEndpoints();
        DiscountName = RandomStringUtils.randomAlphabetic(10);
        DiscountPercentage = RandomStringUtils.randomNumeric(2);
        DiscountReference = RandomStringUtils.randomAlphabetic(10);
        DiscountAmount = RandomStringUtils.randomNumeric(5);
        AmountCurrencyId = "AED";
        JsonHelper = new DiscountJsonBuilder(DiscountName, DiscountPercentage, DiscountReference, DiscountAmount,
                AmountCurrencyId, null, null);

    }

    @Test
    public void UpdateDiscountWithNewInformation(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        DiscountIdToUpdate = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);

        Response response = TestHelp.GetDiscountDetails(DiscountIdToUpdate, HttpStatus.SC_OK);
        JsonPath bodyJson = response.body().jsonPath();
        String ETag = bodyJson.getString("etag");

        String NewDiscountName = RandomStringUtils.randomAlphabetic(10);
        JsonHelper = new DiscountJsonBuilder(NewDiscountName, DiscountPercentage, DiscountReference, DiscountAmount,
                AmountCurrencyId, DiscountIdToUpdate, null);
        String NewJsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        ResponseBody body = TestHelp.UpdateDiscountDetails(DiscountIdToUpdate, NewJsonPayload, HttpStatus.SC_BAD_REQUEST,
                ETag);
    }

    @Test
    public void UpdateDiscountWithInvalidIfMatchHeader(){

    }

}
