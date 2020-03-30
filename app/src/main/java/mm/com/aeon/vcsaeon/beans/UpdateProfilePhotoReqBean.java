package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class UpdateProfilePhotoReqBean implements Serializable {
    private String customerId;
    private byte[] photoByte;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public byte[] getPhotoByte() {
        return photoByte;
    }

    public void setPhotoByte(byte[] photoByte) {
        this.photoByte = photoByte;
    }
}
