package www.bizpro.com.tw.app.roomsql.sqlite;

public class SqlException extends  Exception{
    private String message;
    public SqlException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
