package com.example.lab_5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView todoListView;
    private Button addBtn;
    private SwitchCompat urgentSwitch;
    private TodoDBHelper todoDbHelper;
    ArrayList<String> msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoListView = findViewById(R.id.listView);
        addBtn = findViewById(R.id.button);
        urgentSwitch = findViewById(R.id.switch4);
        todoDbHelper = new TodoDBHelper(MainActivity.this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        getTodos();
        todoDbHelper.printCursor();

        setUpListViewListener();
        urgentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    addBtn.setBackgroundColor(getResources().getColor(R.color.red));
                else
                    addBtn.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
            }
        });

    }

    private void setUpListViewListener() {
        todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Do you want to delete this?");
                alertDialogBuilder.setMessage("The selected row is: " + pos);

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Delete Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });

                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        todoDbHelper.deleteTodo((String) msgList.get(pos));
                        getTodos();

                    }
                });

                alertDialogBuilder.show();
                return true;
            }
        });
    }

    private void addItem() {
        EditText input = findViewById(R.id.todo_input);
        String itemText = input.getText().toString();
        int urgency = 0;
        if (!(itemText.equals(""))) {

            if (urgentSwitch.isChecked()) {
                urgency = 1;
            }

            todoDbHelper.addNewTodo(itemText, urgency);
            getTodos();
            input.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Type Here", Toast.LENGTH_LONG).show();
        }
    }

    private void getTodos() {

        ArrayList<TodoModel> todos = todoDbHelper.readTodos();
        msgList = new ArrayList<>();
        for (int i=0;i<todos.size();i++) {
            msgList.add(todos.get(i).getTodoMsg());
        }

        todoListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowItem = super.getView(position, convertView, parent);
                TextView textView=(TextView) rowItem.findViewById(android.R.id.text1);
                if(todos.get(position).getUrgency() == 1)
                {
                    rowItem.setBackgroundColor (Color.RED);
                    textView.setTextColor(Color.WHITE);

                }
                else
                {

                    rowItem.setBackgroundColor (Color.WHITE);
                }
                return rowItem;
            }
        });

    }
}