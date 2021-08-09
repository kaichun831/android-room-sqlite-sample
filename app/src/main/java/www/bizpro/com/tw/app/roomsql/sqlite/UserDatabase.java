package www.bizpro.com.tw.app.roomsql.sqlite;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import www.bizpro.com.tw.app.roomsql.sqlite.models.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static final String DB_NAME = "UserDatabase.db";
    private static volatile UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static UserDatabase create(final Context context) {
        //Migration(舊版本,新版本) void 執行sql語法
        final Migration migration = new Migration(1, 2) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE User ADD COLUMN birthday TEXT");
                //當14行 版本更新為2時 ,必須更新model 添加 birthday變數 才有作用
            }
        };
        //db加上遷移
        UserDatabase database = Room.databaseBuilder(
                context,
                UserDatabase.class, DB_NAME).addMigrations(migration).build();
        return database;

    }
    public abstract UserDao getUserDao();
}