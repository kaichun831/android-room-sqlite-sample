package www.bizpro.com.tw.app.roomsql.sqlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import www.bizpro.com.tw.app.roomsql.sqlite.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE name=:name AND age = :age")
    List<User> getUser(String name, int age);

    @Query("DELETE FROM user WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM user WHERE name = :name")
    Single<Integer> deleteByName(String name);

    @Insert
    Single<Long> insert(User user);
    @Update
    void update(User... users);

    @Delete
    void deleteUser(User... users);
}
