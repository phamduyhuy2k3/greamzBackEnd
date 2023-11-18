package com.greamz.backend.checkout.paypal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class PaypalCreateOrderRequest {
    private String intent;

    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;

    // Getters and setters

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<PurchaseUnit> getPurchaseUnits() {
        return purchaseUnits;
    }

    public void setPurchaseUnits(List<PurchaseUnit> purchaseUnits) {
        this.purchaseUnits = purchaseUnits;
    }
}

class PurchaseUnit {
    private Amount amount;
    private List<Item> items;

    // Getters and setters

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

class Amount {
    @JsonProperty("currency_code")
    private String currencyCode;

    private String value;

    private Breakdown breakdown;

    // Getters and setters

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Breakdown getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(Breakdown breakdown) {
        this.breakdown = breakdown;
    }
}

class Breakdown {
    @JsonProperty("item_total")
    private ItemTotal itemTotal;

    // Getters and setters

    public ItemTotal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(ItemTotal itemTotal) {
        this.itemTotal = itemTotal;
    }
}

class ItemTotal {
    @JsonProperty("currency_code")
    private String currencyCode;

    private String value;

    // Getters and setters

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class Item {
    private String name;
    private int quantity;
    private UnitAmount unitAmount;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UnitAmount getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(UnitAmount unitAmount) {
        this.unitAmount = unitAmount;
    }
}

class UnitAmount {
    @JsonProperty("currency_code")
    private String currencyCode;

    private String value;

    // Getters and setters

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}