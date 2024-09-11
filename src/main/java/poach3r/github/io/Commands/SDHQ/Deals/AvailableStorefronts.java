package poach3r.github.io.Commands.SDHQ.Deals;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AvailableStorefronts {
    protected String id;
    List<Deal> deals;
}

class Deal {
    protected Shop shop;
    protected Price price;
    protected Price regular;
    protected int cut;
    protected String voucher;
    protected Price storeLow;
    protected Price historyLow;
    protected String flag;
    protected List<Platform> drm;
    protected List<Platform> platforms;
    protected String timestamp;
    protected String expiry;
    protected String url;
}

class Shop {
    protected int id;
    protected String name;
}

class Price {
    protected double amount;
    @SerializedName("amountInt")
    protected int amountInt;
    protected String currency;
}

class Platform {
    protected int id;
    protected String name;
}