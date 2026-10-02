package com.example.homeworkmanager;

public class HomeworkTask extends Task{
    private int exercises;

    public HomeworkTask(int exercises, int id, String title, Subject subject, Priority priority, String dueDate){
        super(id, title, subject, priority, dueDate);
        this.exercises = exercises;
    }

    @Override
    public String getType() {
        return "Homework";
    }

    @Override
    public int getExtra() { return this.exercises; }

    @Override
    public String toString() {
        return "[" + getType() + "] ID: " + getId() + " | " + getTitle() + " (" + getSubject() + ") | Exercises: " + exercises + " | " +
                "Priority: " + getPriority() + " | Due: " + getDueDate() + " | Done: " + isDone() + " | Points: " + getPoints();
    }

    @Override
    public int getPoints(){
        return this.exercises*2 + this.getPriorityBonus();
    }

    @Override
    public int getPriorityBonus(){
        return this.getPriority().getValue();
    }
}
