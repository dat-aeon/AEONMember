package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;
import java.util.List;

public class CityTownshipResBean implements Serializable {

    private Integer cityId;
    private String name;
    private List<TownshipListBean> townshipInfoList;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TownshipListBean> getTownshipInfoList() {
        return townshipInfoList;
    }

    public void setTownshipInfoList(List<TownshipListBean> townshipInfoList) {
        this.townshipInfoList = townshipInfoList;
    }
}
