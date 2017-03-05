import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class GetDiscountLists {

    private static DiscountRestEndpoints DiscountEndpoints;


    @Before
    public void setUp() {
        DiscountEndpoints = new DiscountRestEndpoints();
    }

    @Test
    public void getDiscountsList() {
        DiscountEndpoints.GetListOfDiscounts(HttpStatus.SC_OK);
    }

    @Test
    public void getDiscountListWithInvalidToken() {
        DiscountEndpoints.token = "dummytoken";
        DiscountEndpoints.GetListOfDiscounts(HttpStatus.SC_UNAUTHORIZED);
    }

}
    