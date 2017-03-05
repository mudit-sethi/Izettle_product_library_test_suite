
import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;

public class CreateDiscount {

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
    public void getDiscountsList(){
        TestHelp.GetListOfDiscounts(HttpStatus.SC_OK);
    }
    
    /*@Test
    public void getDiscountsWhenInvalidToken(){
        String invalidToken = "dummytoken";
        TestHelp.GetListOfDiscounts(invalidToken, HttpStatus.SC_UNAUTHORIZED);
    }*/

    @Test
    public void CreateDiscountWithPercentage(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountId = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
      //  ResponseDetails = TestHelp.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
    }

    @Test
    public void CreateDiscountWithAmount(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        DiscountId = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
     //   ResponseDetails = TestHelp.GetDiscountDetails(DiscountId, HttpStatus.SC_OK);
    }

    @Test
    public void CreateDiscountWithPercentageAndAmount(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, true);
        DiscountId = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }


    @Test
    public void CreateDiscountWithoutPercentageAndAmount(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, false);
        DiscountId = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void CreateMultipleDiscountWithSameInformation(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(true, false);
        TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED); //No check on duplicate objects
    }

}
