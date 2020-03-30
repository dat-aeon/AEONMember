package mm.com.aeon.vcsaeon.beans;

import java.io.File;

import okhttp3.MultipartBody;

public class ApplicationInfoPhotoBean {

    private int fileType;
    private String filePath;
    private String fileName;
    private MultipartBody.Part file;

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartBody.Part getFile() {
        return file;
    }

    public void setFile(MultipartBody.Part file) {
        this.file = file;
    }
}
