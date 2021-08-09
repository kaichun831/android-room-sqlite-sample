package www.bizpro.com.tw.app.roomsql.sqlite;

public class SqlThread extends  Thread{
    private SqlThread sqlThread;
    private Runnable runnable;
    public SqlThread getInstance(){
        if(sqlThread ==null){
            sqlThread = new SqlThread();
        }
        return sqlThread;
    }
    public void startRunnable(Runnable runnable){
        this.runnable = runnable;
        start();
    }
    @Override
    public void run() {
        runnable.run();
    }
}
