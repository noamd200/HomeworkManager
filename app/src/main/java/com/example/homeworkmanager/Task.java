package com.example.homeworkmanager;

public abstract class Task implements Rewardable{
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

    public enum Subject {
        MATH("Math"),
        ENGLISH("English"),
        COMPUTER_SCIENCE("Computer Science"),
        PHYSICS("Physics"),
        HISTORY("History");

        private final String displayName;

        Subject(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // Helper method to convert a display name back to the Enum
        public static Subject fromDisplayName(String text) {
            for (Subject s : Subject.values()) {
                if (s.displayName.equalsIgnoreCase(text)) {
                    return s;
                }
            }
            return null;
        }
    }

    private final int id;
    private String title;
    private Subject subject;
    private Priority priority;
    private String dueDate;
    private boolean done;

    public Task(int id, String title, Subject subject, Priority priority, String dueDate) {
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

    public int getExtra() { return -1; }

    @Override
    public String toString() {
        return "[" + getType() + "] ID: " + id + " | " + title + " (" + subject + ") | Priority: "
                + priority + " | Due: " + dueDate + " | Done: " + done + " | Points: " + getPoints();
    }

    @Override
    public int getPoints(){ return 0; }

    public int getPriorityBonus(){ return 0; }

    // Getters and Setters
    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

}
