package epf.tica.lamesimone.projet_mb;

/**
 * Created by paullame on 07/06/2017.
 */

public class NewsFeedElement {
    public String thumbnail;
    public String data;
    public String gps;
    public String fileName;



    public String fileSize;

    public NewsFeedElement(String thumbnail, String data, String gps, String fileName, String fileSize){
        this.thumbnail=thumbnail;
        this.data=data;
        this.gps=gps;
        this.fileName=fileName;
        this.fileSize=fileSize;

    }

    public  String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public  String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public  String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public  String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public  String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
