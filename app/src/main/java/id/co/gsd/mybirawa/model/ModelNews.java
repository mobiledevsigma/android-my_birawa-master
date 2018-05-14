package id.co.gsd.mybirawa.model;

/**
 * Created by Biting on 1/16/2018.
 */

public class ModelNews {

    private String news_id;
    private String news_judul;
    private String news_isi;
    private String news_tgl;
    private String news_status;

    public ModelNews(String news_id, String news_judul, String news_isi, String news_tgl, String news_status) {
        this.news_id = news_id;
        this.news_judul = news_judul;
        this.news_isi = news_isi;
        this.news_tgl = news_tgl;
        this.news_status = news_status;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_judul() {
        return news_judul;
    }

    public void setNews_judul(String news_judul) {
        this.news_judul = news_judul;
    }

    public String getNews_isi() {
        return news_isi;
    }

    public void setNews_isi(String news_isi) {
        this.news_isi = news_isi;
    }

    public String getNews_tgl() {
        return news_tgl;
    }

    public void setNews_tgl(String news_tgl) {
        this.news_tgl = news_tgl;
    }

    public String getNews_status() {
        return news_status;
    }

    public void setNews_status(String news_status) {
        this.news_status = news_status;
    }
}
