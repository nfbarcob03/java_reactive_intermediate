package co.com.bancolombia.model.box;

import java.time.LocalDateTime;

public class UploadBoxReport {
    private String boxId;
    private int total;
    private int success;
    private int failed;
    private String uploadedAt;
    private String uploadedBy;

    public UploadBoxReport() {
    }

    public UploadBoxReport(String boxId, int total, int success, int failed, String uploadedAt, String uploadedBy) {
        this.boxId = boxId;
        this.total = total;
        this.success = success;
        this.failed = failed;
        this.uploadedAt = uploadedAt;
        this.uploadedBy = uploadedBy;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}