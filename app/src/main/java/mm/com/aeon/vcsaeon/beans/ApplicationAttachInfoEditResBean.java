package mm.com.aeon.vcsaeon.beans;

public class ApplicationAttachInfoEditResBean {

    private int daApplicationInfoAttachmentId;
    private int fileType;
    private String filePath;
    private boolean editFlag;
    private boolean isEdited;

    public int getDaApplicationInfoAttachmentId() {
        return daApplicationInfoAttachmentId;
    }

    public void setDaApplicationInfoAttachmentId(int daApplicationInfoAttachmentId) {
        this.daApplicationInfoAttachmentId = daApplicationInfoAttachmentId;
    }

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

    public boolean isEditFlag() {
        return editFlag;
    }

    public void setEditFlag(boolean editFlag) {
        this.editFlag = editFlag;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
