package www.bizpro.com.tw.app.roomsql.sqlite.models;

public interface SqlResponseCallback {
    void onResponseReceived( Object result);
    void onFailedToGetResponse(Throwable t);
}
