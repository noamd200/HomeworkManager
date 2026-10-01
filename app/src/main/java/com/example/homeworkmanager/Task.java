package com.example.homeworkmanager;

public abstract class Task {
    public enum Priority {
        LOW(1),
        MEDIUM(3),
        HIGH(5);

        private final int level;

        Priority(int level) {
            this.level = level;
        }

        public int getValue() {
            return level;
        }
    }

    private final int id;
    private String title;
    private String subject;
    private Priority priority;
    private String dueDate;
    private boolean done;

    public Task(int id, String title, String subject, Priority priority, String dueDate) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.priority = priority;
        this.dueDate = dueDate;
        this.done = false;
    }

    public String getType() {
        return "n/a";
    }

    @Override
    public String toString(){ return "TBA"; }

    public int getPoints(){ return 0; }

    public int getPriorityBonus(){ return 0; }

    // Getters and Setters
    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

}
