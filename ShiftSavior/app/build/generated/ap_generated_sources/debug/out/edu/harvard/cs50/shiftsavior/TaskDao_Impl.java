package edu.harvard.cs50.shiftsavior;

import android.database.Cursor;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfCreate;

  private final SharedSQLiteStatement __preparedStmtOfSave;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public TaskDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfCreate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "INSERT INTO tasks (TaskName, Location, AlarmInMillis) VALUES ('New Task', 'No Location', 1000)";
        return _query;
      }
    };
    this.__preparedStmtOfSave = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE tasks SET TaskName = ?, Location = ?, AlarmInMillis = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM tasks WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void create() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfCreate.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeInsert();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfCreate.release(_stmt);
    }
  }

  @Override
  public void save(final String taskName, final String location, final long millis, final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSave.acquire();
    int _argIndex = 1;
    if (taskName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, taskName);
    }
    _argIndex = 2;
    if (location == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, location);
    }
    _argIndex = 3;
    _stmt.bindLong(_argIndex, millis);
    _argIndex = 4;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfSave.release(_stmt);
    }
  }

  @Override
  public void delete(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public List<Task> getAllTasks() {
    final String _sql = "SELECT * FROM tasks";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
      try {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "TaskName");
        final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "Location");
        final int _cursorIndexOfMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "AlarmInMillis");
        final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
        while(_cursor.moveToNext()) {
          final Task _item;
          _item = new Task();
          _item.id = _cursor.getInt(_cursorIndexOfId);
          _item.taskName = _cursor.getString(_cursorIndexOfTaskName);
          _item.location = _cursor.getString(_cursorIndexOfLocation);
          _item.millis = _cursor.getLong(_cursorIndexOfMillis);
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }
}
