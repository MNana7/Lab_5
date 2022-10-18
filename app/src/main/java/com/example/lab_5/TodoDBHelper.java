package com.example.lab_5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class TodoDBHelper extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "todosdb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "todoList";

    // below variable is for our id column.
    private static final String ID_COL = "ID";

    // below variable is for our course name column
    private static final String TODO_COL = "TODO";

    // below variable id for our course duration column.
    private static final String URGENCY_COL = "URGENCY";

    // creating a constructor for our database handler.
    public TodoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    Cursor cursorTodos;

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TODO_COL + " TEXT,"
                + URGENCY_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewTodo(String todo, int urgency) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(TODO_COL, todo);
        values.put(URGENCY_COL, urgency);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<TodoModel> readTodos() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        cursorTodos = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<TodoModel> todoModelArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTodos.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                todoModelArrayList.add(new TodoModel(cursorTodos.getInt(0),
                        cursorTodos.getString(1),
                        cursorTodos.getInt(2)));
            } while (cursorTodos.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.

        return todoModelArrayList;
    }

    public void deleteTodo(String todo) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "TODO=?", new String[]{todo});
        db.close();
    }

    public void printCursor() {

        String[] nameCursorColumns = cursorTodos.getColumnNames();
        int numberCursorResults = cursorTodos.getCount();
        ArrayList<TodoModel> cursorRowValuesList = new ArrayList<>();
        String cursorRowValues = "";

        if (cursorTodos.moveToFirst()) {

            while(!cursorTodos.isAfterLast()) {
                int id = cursorTodos.getColumnIndex(TodoDBHelper.ID_COL);
                int todo = cursorTodos.getColumnIndex(TodoDBHelper.TODO_COL);
                int urgency = cursorTodos.getColumnIndex(TodoDBHelper.URGENCY_COL);

                int idValue = cursorTodos.getInt(id);
                String todoValue = cursorTodos.getString(todo);
                int UrgencyValue = cursorTodos.getInt(urgency);

                cursorRowValuesList.add(new TodoModel(idValue, todoValue, UrgencyValue));
                cursorRowValues = TextUtils.join(",", cursorRowValuesList);
                cursorTodos.moveToNext();
            }

        }

        Log.i("DATABASE VERSION", Integer.toString(DB_VERSION));
        Log.i("NUMBER OF COLUMNS", Integer.toString(cursorTodos.getColumnCount()));
        Log.i("COLUMN NAMES", Arrays.toString(nameCursorColumns));
        Log.i("NUMBER OF RESULTS", Integer.toString(numberCursorResults));
        Log.i("ROW VALUES", cursorRowValues);
        cursorTodos.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
