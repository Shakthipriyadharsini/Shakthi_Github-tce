import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private final int id;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Priority priority;
    private boolean completed;

    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm");

    public Task(int id, String description, LocalTime startTime, LocalTime endTime, Priority priority) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.completed = false;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setDescription(String description) { this.description = description; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        String done = completed ? " (Completed)" : "";
        return String.format("%s - %s: %s [%s]%s",
                startTime.format(TF),
                endTime.format(TF),
                description,
                priority,
                done);
    }
}