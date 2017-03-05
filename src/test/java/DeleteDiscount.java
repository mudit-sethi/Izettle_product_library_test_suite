
import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.apache.commons.lang3.RandomStringUtils;

public class DeleteDiscount {

    private static String Name;
    private static String Percentage;
    private static String Amount;
    private static String CurrencyId;
    private static String Reference;
    private static DiscountRestEndpoints TestHelp;
    private static DiscountJsonBuilder JsonHelper;
    private static String DiscountToDelete;

    @Before
    public void setUp() {
        TestHelp = new DiscountRestEndpoints();
        Name = RandomStringUtils.randomAlphabetic(10);
        Percentage = RandomStringUtils.randomNumeric(2);
        Reference = RandomStringUtils.randomAlphabetic(10);
        Amount = RandomStringUtils.randomNumeric(5);
        CurrencyId = "AED";
        JsonHelper = new DiscountJsonBuilder(Name, Percentage, Reference, Amount,
                CurrencyId, null, null);
    }


    @Test
    public void DeleteDiscountThatExists(){
        String JsonPayload = JsonHelper.DiscountPayloadGenerator(false, true);
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
