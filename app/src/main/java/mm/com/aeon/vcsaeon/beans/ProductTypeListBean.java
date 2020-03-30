package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class ProductTypeListBean implements Serializable {

    private Integer productTypeId;
    private String name;

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
