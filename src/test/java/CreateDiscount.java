
import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;

public class CreateDiscount {

    private static DiscountRestEndpoints DiscountEndpoints;
    private static String Name;
    private static String Percentage;
    private static String Amount;
    private static String CurrencyId;
    private static String Reference;
    private static DiscountJsonBuilder JsonHelper;
    private String JsonPayload;
    private String DiscountId;

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
    public void CreateDiscountWithPercentage() {
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountId = DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        DiscountEndpoints.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
    }

    @Test
    public void CreateDiscountWithAmount() {
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        DiscountId = DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        DiscountEndpoints.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
    }

    @Test
    public void CreateDiscountWithPercentageAndAmount() {
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, true);
        DiscountId = DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void CreateDiscountWithoutPercentageAndAmount() {
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, false);
        DiscountId = DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void CreateDiscountWithPercentageMoreThan100() {
        JsonHelper = new DiscountJsonBuilder(Name, "200", Reference, Amount,
                CurrencyId, null, null);
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY);

    }

    @Test
    public void CreateDiscountWithPercentageLessThan0() {
        JsonHelper = new DiscountJsonBuilder(Name, "-5", Reference, Amount,
                CurrencyId, null, null);
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void CreateDiscountWithPercentageIs100() {
        JsonHelper = new DiscountJsonBuilder(Name, "100", Reference, Amount,
                CurrencyId, null, null);
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);

    }

    @Test
    public void CreateDiscountWithPercentageIs0() {
        JsonHelper = new DiscountJsonBuilder(Name, "0", Reference, Amount,
                CurrencyId, null, null);
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);

    }

    @Test
    public void CreateDiscountWithPercentageAsAlphaNumeric() {
        JsonHelper = new DiscountJsonBuilder(Name, "abc123", Reference, Amount,
                CurrencyId, null, null);
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY);

    }

    @Test
    public void CreateDiscountWithAmountAsAlphaNumeric() {
        JsonHelper = new DiscountJsonBuilder(Name, Percentage, Reference, "abc123",
                CurrencyId, null, null);
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY); // Inconsistent to Percentage alpha numeric

    }

    @Test
    public void CreateMultipleDiscountWithSameInformation() {
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        DiscountEndpoints.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED); //No check on duplicate objects
    }


}
