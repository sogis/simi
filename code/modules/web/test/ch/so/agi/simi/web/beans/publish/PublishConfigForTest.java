package ch.so.agi.simi.web.beans.publish;

public class PublishConfigForTest implements PublishConfig{

    private String baseUrl = null;
    private int timeOutMillis = -1;
    private String secToken;

    public PublishConfigForTest(String baseUrl, int timeOutMillis, String secToken){
        this.baseUrl = baseUrl;
        this.timeOutMillis = timeOutMillis;
        this.secToken = secToken;
    }

    @Override
    public String getJobBaseUrl() {
        return baseUrl;
    }

    @Override
    public int getTimeOutMillis() {
        return timeOutMillis;
    }

    @Override
    public String getSecToken() {
        return secToken;
    }
}
