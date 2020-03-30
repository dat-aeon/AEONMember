package mm.com.aeon.vcsaeon.networking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import mm.com.aeon.vcsaeon.common_utils.CommonConstants;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> {

    private String status;
    private String messageCode;
    private String message;
    private String error;
    private T data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isResponseOk(){
        return this.status.equals(SUCCESS) && this.data != null;
    }
}

