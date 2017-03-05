
import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;

public class DeleteDiscount {

    private static String DiscountName;
    private static String DiscountPercentage;
    private static String DiscountAmount;
    private static String AmountCurrencyId;
    private static String DiscountReference;
    private static DiscountRestEndpoints TestHelp;
    private static DiscountJsonBuilder JsonHelper;
    private static String DiscountToDelete;
    private String JsonPayload;

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
    public void DeleteDiscountThatExists(){
        JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
        DiscountToDelete = TestHelp.CreateDiscount(JsonPayload, HttpStatus.SC_CREATED);
        TestHelp.DeleteDiscount(DiscountToDelete, HttpStatus.SC_NO_CONTENT);
        TestHelp.GetDiscountDetails(DiscountToDelete, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void DeleteDiscountThatNotExists(){
        DiscountToDelete = "RandomDiscountId";
        TestHelp.DeleteDiscount(DiscountToDelete, HttpStatus.SC_NOT_FOUND);
    }


}
