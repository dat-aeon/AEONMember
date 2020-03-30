package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class ProductInfoBean implements Serializable {

    private Integer daPurchaseInfoProductId;
    private Integer daPurchaseInfoId;
    private Integer daLoanTypeId;
    private Integer daProductTypeId;
    private Double price;
    private Double cashDownAmount;
    private String productDescription;
    private String brand;
    private String model;

    public Integer getDaPurchaseInfoProductId() {
        return daPurchaseInfoProductId;
    }

    public void setDaPurchaseInfoProductId(Integer daPurchaseInfoProductId) {
        this.daPurchaseInfoProductId = daPurchaseInfoProductId;
    }

    public Integer getDaPurchaseInfoId() {
        return daPurchaseInfoId;
    }

    public void setDaPurchaseInfoId(Integer daPurchaseInfoId) {
        this.daPurchaseInfoId = daPurchaseInfoId;
    }

    public Integer getDaLoanTypeId() {
        return daLoanTypeId;
    }

    public void setDaLoanTypeId(Integer daLoanTypeId) {
        this.daLoanTypeId = daLoanTypeId;
    }

    public Integer getDaProductTypeId() {
        return daProductTypeId;
    }

    public void setDaProductTypeId(Integer daProductTypeId) {
        this.daProductTypeId = daProductTypeId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCashDownAmount() {
        return cashDownAmount;
    }

    public void setCashDownAmount(Double cashDownAmount) {
        this.cashDownAmount = cashDownAmount;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
