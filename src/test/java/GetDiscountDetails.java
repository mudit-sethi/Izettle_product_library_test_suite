
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.UUID;

public class GetDiscountDetails {

    private static String DiscountName;
    private static String DiscountPercentage;
    private static String DiscountAmount;
    private static String AmountCurrencyId;
    private static String DiscountReference;
    private static DiscountRestEndpoints TestHelp;
    private static DiscountJsonBuilder JsonHelper;
    private String JsonPayload;
    private String DiscountId;

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
    public void DiscountDetailsWithIfNoneMatchETag(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountId = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        Response response = TestHelp.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
        String ETag = response.header("Etag");
        response = TestHelp.GetDiscountDetailsWithIfMatchHeader(DiscountId, HttpStatus.SC_NOT_MODIFIED, ETag);
    }

    /*@Test
    public void DiscountDetailsWithIfNoneNotMatchEtag(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountId = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        Response response = TestHelp.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
        String ETag = "A78341E49291B8B5174AC97057662BD1--gzip";
        TestHelp.GetDiscountDetailsWithIfMatchHeader(DiscountId, HttpStatus.SC_OK, ETag);
    }*/

    @Test
    public void DiscountDetailsForInvalidDiscount(){
        UUID InvalidDiscountId = UUID.randomUUID();
        TestHelp.GetDiscountDetails(InvalidDiscountId.toString(), HttpStatus.SC_NOT_FOUND);
    }

}
