package mm.com.aeon.vcsaeon.beans;

import java.util.List;

public class BuyCategory {

    private List<String> categories;
    private List<String> brands;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }
}
