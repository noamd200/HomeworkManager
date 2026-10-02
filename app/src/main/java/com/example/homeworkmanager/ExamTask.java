package com.example.homeworkmanager;

public class ExamTask extends Task{
    private int topics;

    public ExamTask(int topics, int id, String title, Subject subject, Priority priority, String dueDate){
        super(id, title, subject, priority, dueDate);
        this.topics = topics;
    }

    @Override
    public String getType() {
        return "Exam";
    }

    @Override
    public int getExtra() { return this.topics; }

    @Override
    public String toString() {
        return "[" + getType() + "] ID: " + getId() + " | " + getTitle() + " (" + getSubject() + ") | Topics: " + topics + " | " +
                "Priority: " + getPriority() + " | Due: " + getDueDate() + " | Done: " + isDone() + " | Points: " + getPoints();
    }

    @Override
    public int getPoints(){
        return 10 + this.topics*5 + this.getPriorityBonus();
    }

    @Override
    public int getPriorityBonus(){
        return this.getPriority().getValue();
    }
}
