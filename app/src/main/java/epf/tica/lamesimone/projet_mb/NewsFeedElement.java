package epf.tica.lamesimone.projet_mb;

/**
 * Created by paullame on 07/06/2017.
 */

public class NewsFeedElement {
    public String thumbnail;
    public String date;
    public String gps;
    public String fileName;
    public String fileSize;

    public NewsFeedElement(String thumbnail, String date, String gps, String fileName, String fileSize){
        this.thumbnail=thumbnail;
        this.date=date;
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

    public  String getDate() {
        return date;
    }

    public void setData(String data) {
        this.date = date;
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
