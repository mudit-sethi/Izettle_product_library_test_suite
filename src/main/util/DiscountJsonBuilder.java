import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;


public class DiscountJsonBuilder {

    private final String name;
    private final String description;
    private final String percentage;
    private final String externalReference;
    private final String amount;
    private final String CurrencyId;
    private String uuid;
    private String eTag;

    public DiscountJsonBuilder(String Name, String Percentage, String Reference, String Amount, String CurrencyId,
                               String uuid, String eTag) {
        this.uuid = uuid;
        this.name = Name;
        this.description = "";
        this.percentage = Percentage;
        this.externalReference = Reference;
        this.amount = Amount;
        this.CurrencyId = CurrencyId;
        this.eTag = eTag;
    }

    public String DiscountPayloadGenerator(Boolean WithAmount, Boolean WithPercentage) {

        if (uuid == null) {
            uuid = Generators.timeBasedGenerator().generate().toString();
        }

        HashMap<String, Object> DiscountPayload = new HashMap<String, Object>();
        DiscountPayload.put("uuid", uuid);
        DiscountPayload.put("name", name);
        DiscountPayload.put("description", description);
        DiscountPayload.put("externalReference", externalReference);
        DiscountPayload.put("imageLookupKeys", new ArrayList<String>());


        if (WithAmount) {
            HashMap<String, String> AmountPayload = new HashMap<String, String>();
            AmountPayload.put("amount", amount);
            AmountPayload.put("currencyId", CurrencyId);
            DiscountPayload.put("amount", AmountPayload);
        }

        if (WithPercentage) {
            DiscountPayload.put("percentage", percentage);
        }
        if (eTag != null) {
            DiscountPayload.put("eTag", eTag);
        }

        Gson gson = new Gson();
        return gson.toJson(DiscountPayload);
        }


}

