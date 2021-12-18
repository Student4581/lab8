package com.example.lab8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonAdd, buttonDelete, buttonClear, buttonRead, buttonUpdate;
    EditText etName, etAuthor, etID, etDate;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);

        buttonRead = (Button) findViewById(R.id.buttonRead);
        buttonRead.setOnClickListener(this);

        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(this);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);

        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);

        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etAuthor = (EditText) findViewById(R.id.etAuthor);
        etDate = (EditText) findViewById(R.id.etDate);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v)
    {
        String ID = etID.getText().toString();
        String name = etName.getText().toString();
        String author = etAuthor.getText().toString();
        String date = etDate.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); // класс для добавления новых строк в таблицу

        switch (v.getId())
        {
            case R.id.buttonAdd:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_AUTHOR, author);
                contentValues.put(DBHelper.KEY_DATE, date);
                database.insert(DBHelper.TABLE_PERSONS, null, contentValues);
                break;

            case R.id.buttonRead:
                Cursor cursor = database.query(DBHelper.TABLE_PERSONS, null, null, null,
                        null, null, null); // все поля без сортировки и группировки

                if (cursor.moveToFirst())
                {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int authorIndex = cursor.getColumnIndex(DBHelper.KEY_AUTHOR);
                    int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
                    do {
                        Log.d("mLog", "ID =" + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", author = " + cursor.getString(authorIndex) +
                                ", date = " + cursor.getString(dateIndex));

                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog", "0 rows");

                cursor.close(); // освобождение памяти
                break;

            case R.id.buttonClear:
                database.delete(DBHelper.TABLE_PERSONS, null, null);
                break;

            case R.id.buttonDelete:
                if (ID.equalsIgnoreCase(""))
                {
                    break;
                }
                int delCount = database.delete(DBHelper.TABLE_PERSONS, DBHelper.KEY_ID + "= " + ID, null);
                Log.d("mLog", "Удалено строк = " + delCount);

            case R.id.buttonUpdate:
                if (ID.equalsIgnoreCase(""))
                {
                    break;
                }
                contentValues.put(DBHelper.KEY_NAME, date);
                contentValues.put(DBHelper.KEY_AUTHOR, author);
                contentValues.put(DBHelper.KEY_DATE, name);
                int updCount = database.update(DBHelper.TABLE_PERSONS, contentValues, DBHelper.KEY_ID + "= ?", new String[] {ID});
                Log.d("mLog", "Обновлено строк = " + updCount);
        }
        dbHelper.close(); // закрываем соединение с БД
    }
}