import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TaskFactory {
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm");

    public static Task createTask(int id, String description, String startStr, String endStr, String priorityStr) throws InvalidTaskException {
        try {
            LocalTime start = LocalTime.parse(startStr.trim(), TF);
            LocalTime end = LocalTime.parse(endStr.trim(), TF);
            if (!end.isAfter(start)) {
                throw new InvalidTaskException("End time must be after start time.");
            }
            Priority p = Priority.fromString(priorityStr);
            return new Task(id, description.trim(), start, end, p);
        } catch (DateTimeParseException e) {
            throw new InvalidTaskException("Invalid time format. Use HH:mm (24-hour).");
        } catch (IllegalArgumentException ia) {
            throw new InvalidTaskException("Invalid priority. Use High/Medium/Low.");
        }
    }
}