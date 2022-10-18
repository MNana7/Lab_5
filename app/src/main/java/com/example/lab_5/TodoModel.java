package com.example.lab_5;

public class TodoModel {
    private int id;
    private String todoMsg;
    private int urgency;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // creating getter and setter methods
    public String getTodoMsg() {
        return todoMsg;
    }

    public void setTodoMsg(String todoMsg) {
        this.todoMsg = todoMsg;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    // constructor
    public TodoModel(int id, String todoMsg, int urgency) {
        this.todoMsg = todoMsg;
        this.urgency = urgency;
        this.id = id;
    }

    @Override
    public String toString() {
        return "TodoModel{" +
                "id=" + id +
                ", todoMsg='" + todoMsg + '\'' +
                ", urgency=" + urgency +
                '}';
    }
}
