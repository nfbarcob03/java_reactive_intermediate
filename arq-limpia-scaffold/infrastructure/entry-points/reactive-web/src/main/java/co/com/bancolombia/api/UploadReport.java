package co.com.bancolombia.api;

public class UploadReport {
    private int size;
    private String boxId;

    public UploadReport(){

    }
    public UploadReport(int size, String boxId){
        this.size = size;
        this.boxId = boxId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }
}
