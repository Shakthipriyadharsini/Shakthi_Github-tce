import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;
import java.time.LocalTime;

public class ScheduleManager {
    private static ScheduleManager instance;
    private final List<Task> tasks = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final Logger logger = Logger.getLogger("ScheduleLogger");
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm");

    private ScheduleManager() { configureLogger(); }

    public static synchronized ScheduleManager getInstance() {
        if (instance == null) instance = new ScheduleManager();
        return instance;
    }

    private void configureLogger() {
        try {
            logger.setUseParentHandlers(false);
            Handler fh = new FileHandler("app.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new SimpleFormatter());
            logger.addHandler(ch);
            logger.setLevel(Level.INFO);
        } catch (Exception e) {
            System.err.println("Logger configuration failed: " + e.getMessage());
        }
    }

    public synchronized void addObserver(Observer o) { observers.add(o); }
    public synchronized void removeObserver(Observer o) { observers.remove(o); }
    private synchronized void notifyObservers(String msg) {
        for (Observer o : observers) {
            try { o.update(msg); } catch (Exception ex) { logger.warning("Observer failed: " + ex.getMessage()); }
        }
    }

    public synchronized Task addTask(String description, String startStr, String endStr, String priorityStr)
            throws InvalidTaskException, TaskConflictException {
        int id = idGenerator.incrementAndGet();
        Task newTask = TaskFactory.createTask(id, description, startStr, endStr, priorityStr);

        for (Task t : tasks) {
            if (isOverlap(t, newTask)) {
                String msg = "Task conflicts with existing task \"" + t.getDescription() + "\" (ID: " + t.getId() + ")";
                logger.warning(msg);
                notifyObservers("CONFLICT: " + msg);
                throw new TaskConflictException(msg);
            }
        }
        tasks.add(newTask);
        logger.info("Task added: " + newTask);
        notifyObservers("Task added: " + newTask);
        return newTask;
    }

    private boolean isOverlap(Task a, Task b) {
        return b.getStartTime().isBefore(a.getEndTime()) && b.getEndTime().isAfter(a.getStartTime());
    }

    public synchronized boolean removeTaskByDescription(String desc) throws TaskNotFoundException {
        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            Task t = it.next();
            if (t.getDescription().equalsIgnoreCase(desc.trim())) {
                it.remove();
                logger.info("Task removed: " + t);
                notifyObservers("Task removed: " + t);
                return true;
            }
        }
        throw new TaskNotFoundException("Task not found: " + desc);
    }

    public synchronized boolean removeTaskById(int id) throws TaskNotFoundException {
        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            Task t = it.next();
            if (t.getId() == id) {
                it.remove();
                logger.info("Task removed: " + t);
                notifyObservers("Task removed: " + t);
                return true;
            }
        }
        throw new TaskNotFoundException("Task not found with id: " + id);
    }

    public synchronized List<Task> viewTasksSorted() {
        List<Task> copy = new ArrayList<>(tasks);
        copy.sort(Comparator.comparing(Task::getStartTime));
        return copy;
    }

    public synchronized Task editTask(int id, String newDesc, String newStartStr, String newEndStr, String newPriorityStr)
            throws TaskNotFoundException, InvalidTaskException, TaskConflictException {
        Task target = null;
        for (Task t : tasks) if (t.getId() == id) { target = t; break; }
        if (target == null) throw new TaskNotFoundException("Task not found with id: " + id);

        String desc = newDesc != null ? newDesc : target.getDescription();
        String start = newStartStr != null ? newStartStr : target.getStartTime().format(TF);
        String end = newEndStr != null ? newEndStr : target.getEndTime().format(TF);
        String priority = newPriorityStr != null ? newPriorityStr : target.getPriority().name();

        Task temp = TaskFactory.createTask(id, desc, start, end, priority);

        for (Task t : tasks) {
            if (t.getId() == id) continue;
            if (isOverlap(t, temp)) {
                String msg = "Edited task conflicts with existing task \"" + t.getDescription() + "\" (ID: " + t.getId() + ")";
                logger.warning(msg);
                notifyObservers("CONFLICT: " + msg);
                throw new TaskConflictException(msg);
            }
        }

        target.setDescription(temp.getDescription());
        target.setStartTime(temp.getStartTime());
        target.setEndTime(temp.getEndTime());
        target.setPriority(temp.getPriority());
        logger.info("Task edited: " + target);
        notifyObservers("Task edited: " + target);
        return target;
    }

    public synchronized Task markTaskCompleted(int id) throws TaskNotFoundException {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setCompleted(true);
                logger.info("Task marked completed: " + t);
                notifyObservers("Task completed: " + t);
                return t;
            }
        }
        throw new TaskNotFoundException("Task not found with id: " + id);
    }

    public synchronized List<Task> viewTasksByPriority(Priority priority) {
        List<Task> res = new ArrayList<>();
        for (Task t : tasks) if (t.getPriority() == priority) res.add(t);
        res.sort(Comparator.comparing(Task::getStartTime));
        return res;
    }
}