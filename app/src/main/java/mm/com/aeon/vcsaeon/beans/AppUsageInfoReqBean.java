package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class AppUsageInfoReqBean implements Serializable {

    private String phoneModel;
    private String manufacture;
    private String sdk;
    private String osType;
    private String osVersion;
    private String resolution;
    private String instructionSet;
    private String cpuArchitecture;

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setInstructionSet(String instructionSet) {
        this.instructionSet = instructionSet;
    }

    public void setCpuArchitecture(String cpuArchitecture) {
        this.cpuArchitecture = cpuArchitecture;
    }
}
