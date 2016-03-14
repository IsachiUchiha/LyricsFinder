package sample;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by sachin on 8/3/16.
 */
public class LyricsInfo {
    SimpleStringProperty link;
    SimpleStringProperty artist;
    SimpleStringProperty title;
    SimpleStringProperty album;
    SimpleStringProperty uploader;
    SimpleStringProperty rate;
    SimpleStringProperty ratecount;
    SimpleStringProperty downloads;
    SimpleStringProperty timelength;

    public LyricsInfo(String link, String artist, String title, String album, String uploader, Double rate, Integer ratecount, Integer downloads, String timelength) {
        this.link = new SimpleStringProperty(link);
        this.artist = new SimpleStringProperty(artist);
        this.title = new SimpleStringProperty(title);
        this.album = new SimpleStringProperty(album);
        this.uploader = new SimpleStringProperty(uploader);
        this.rate = new SimpleStringProperty(String.valueOf(rate));
        this.ratecount = new SimpleStringProperty(String.valueOf(ratecount));
        this.downloads = new SimpleStringProperty(String.valueOf(downloads));
        this.timelength = new SimpleStringProperty(timelength);
    }

    public String getLink() {
        return link.get();
    }

    public SimpleStringProperty linkProperty() {
        return link;
    }

    public void setLink(String link) {
        this.link.set(link);
    }

    public String getArtist() {
        return artist.get();
    }

    public SimpleStringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAlbum() {
        return album.get();
    }

    public SimpleStringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public String getUploader() {
        return uploader.get();
    }

    public SimpleStringProperty uploaderProperty() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader.set(uploader);
    }

    public String getRate() {
        return rate.get();
    }

    public SimpleStringProperty rateProperty() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate.set(rate);
    }

    public String getRatecount() {
        return ratecount.get();
    }

    public SimpleStringProperty ratecountProperty() {
        return ratecount;
    }

    public void setRatecount(String ratecount) {
        this.ratecount.set(ratecount);
    }

    public String getDownloads() {
        return downloads.get();
    }

    public SimpleStringProperty downloadsProperty() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads.set(downloads);
    }

    public String getTimelength() {
        return timelength.get();
    }

    public SimpleStringProperty timelengthProperty() {
        return timelength;
    }

    public void setTimelength(String timelength) {
        this.timelength.set(timelength);
    }
}
