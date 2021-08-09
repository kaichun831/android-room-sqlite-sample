package www.bizpro.com.tw.app.roomsql;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.bizpro.com.tw.app.roomsql.sqlite.SqlThread;
import www.bizpro.com.tw.app.roomsql.sqlite.UserDao;
import www.bizpro.com.tw.app.roomsql.sqlite.UserDatabase;
import www.bizpro.com.tw.app.roomsql.sqlite.models.SqlResponseCallback;
import www.bizpro.com.tw.app.roomsql.sqlite.models.User;

public class MainActivity extends AppCompatActivity {
    UserDao userDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User("Kai", 25);
        deleteRxUser(user, "Kai", new SqlResponseCallback() {
            @Override
            public void onResponseReceived(Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailedToGetResponse(Throwable t) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(t.getMessage());
                            builder.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void insertUser(User user) {
        new SqlThread().startRunnable(() -> {
            userDataBase = UserDatabase
                    .getInstance(MainActivity.this)
                    .getUserDao();
            userDataBase.insert(user);
        });
    }

    private void deleteRxUser(User user, String name, SqlResponseCallback callback) {
        userDataBase = UserDatabase
                .getInstance(MainActivity.this)
                .getUserDao();
        userDataBase.deleteByName(name).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .flatMap(new Function<Integer, SingleSource<?>>() {
                    @Override
                    public SingleSource<?> apply(Integer count) throws Throwable {
                        Log.d("KAI", "Delete Id " + count);
                        if (count > 0) {
                            return userDataBase.insert(user);
                        } else {
                            throw new SQLException("刪除失敗-不存在此用戶");
                        }
                    }
                }).subscribe(new SingleObserver<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
            @Override
            public void onSuccess(@NonNull Object id) {
                callback.onResponseReceived(id);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.onFailedToGetResponse(e);
            }
        });
    }

    private void selectAllUser() {
        new SqlThread().startRunnable(() -> {
            userDataBase = UserDatabase
                    .getInstance(MainActivity.this)
                    .getUserDao();
            List<User> users = userDataBase.getAllUsers();
            for (User item : users) {
                Log.d("", item.getId() + "\t" + item.getName() + "\t" + item.getAge());
            }
        });
    }

    private void deleteByName(String name) {
        new SqlThread().startRunnable(() -> {
            userDataBase = UserDatabase
                    .getInstance(MainActivity.this)
                    .getUserDao();
            userDataBase.deleteByName(name);
        });
    }

    private boolean deleteUser(User user) {
        AtomicBoolean isSuccess = new AtomicBoolean(false);
        new SqlThread().startRunnable(() -> {
            userDataBase = UserDatabase
                    .getInstance(MainActivity.this)
                    .getUserDao();
            userDataBase.deleteUser(user);
            if (userDataBase.getUser(user.getName(), user.getAge()).size() == 0) {
                isSuccess.set(true);
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isSuccess.get();
    }

    private void updateUser(User user) {
        new SqlThread().startRunnable(() -> {
            userDataBase = UserDatabase
                    .getInstance(MainActivity.this)
                    .getUserDao();
            userDataBase.update(user);
        });
    }
}