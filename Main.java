import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ScheduleManager manager = ScheduleManager.getInstance();

    public static void main(String[] args) {
        manager.addObserver(new ConsoleNotifier());
        System.out.println("=== Astronaut Daily Schedule Organizer ===");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": handleAdd(); break;
                case "2": handleRemove(); break;
                case "3": handleViewAll(); break;
                case "4": handleEdit(); break;
                case "5": handleMarkComplete(); break;
                case "6": handleViewByPriority(); break;
                case "7": running = false; System.out.println("Exiting..."); break;
                default: System.out.println("Invalid option. Choose 1..7"); break;
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) Add Task");
        System.out.println("2) Remove Task (by id or description)");
        System.out.println("3) View All Tasks (sorted)");
        System.out.println("4) Edit Task (by id)");
        System.out.println("5) Mark Task Completed (by id)");
        System.out.println("6) View Tasks by Priority");
        System.out.println("7) Exit");
        System.out.print("Choose: ");
    }

    private static void handleAdd() {
        try {
            System.out.print("Description: "); String desc = scanner.nextLine();
            System.out.print("Start (HH:mm): "); String start = scanner.nextLine();
            System.out.print("End (HH:mm): "); String end = scanner.nextLine();
            System.out.print("Priority (High/Medium/Low): "); String pr = scanner.nextLine();
            Task t = manager.addTask(desc, start, end, pr);
            System.out.println("Added (ID=" + t.getId() + "): " + t);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleRemove() {
        try {
            System.out.print("Remove by (1) id or (2) description? Enter 1 or 2: ");
            String opt = scanner.nextLine().trim();
            if ("1".equals(opt)) {
                System.out.print("Enter id: "); int id = Integer.parseInt(scanner.nextLine().trim());
                manager.removeTaskById(id);
                System.out.println("Removed task id " + id);
            } else {
                System.out.print("Enter exact description: "); String desc = scanner.nextLine();
                manager.removeTaskByDescription(desc);
                System.out.println("Removed task with description \"" + desc + "\"");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleViewAll() {
        List<Task> list = manager.viewTasksSorted();
        if (list.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
            return;
        }
        System.out.println("Tasks:");
        for (Task t : list) System.out.println("ID " + t.getId() + " | " + t);
    }

    private static void handleEdit() {
        try {
            System.out.print("Enter task id to edit: "); int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("New description (or press Enter to keep): "); String d = scanner.nextLine();
            d = d.isEmpty() ? null : d;
            System.out.print("New start HH:mm (or press Enter to keep): "); String s = scanner.nextLine();
            s = s.isEmpty() ? null : s;
            System.out.print("New end HH:mm (or press Enter to keep): "); String e = scanner.nextLine();
            e = e.isEmpty() ? null : e;
            System.out.print("New priority (High/Medium/Low) (or press Enter to keep): "); String p = scanner.nextLine();
            p = p.isEmpty() ? null : p;
            Task edited = manager.editTask(id, d, s, e, p);
            System.out.println("Edited: " + edited);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void handleMarkComplete() {
        try {
            System.out.print("Enter task id to mark completed: "); int id = Integer.parseInt(scanner.nextLine().trim());
            Task t = manager.markTaskCompleted(id);
            System.out.println("Marked completed: " + t);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleViewByPriority() {
        try {
            System.out.print("Priority (High/Medium/Low): "); String p = scanner.nextLine().trim();
            Priority pr = Priority.fromString(p);
            List<Task> list = manager.viewTasksByPriority(pr);
            if (list.isEmpty()) { System.out.println("No tasks with priority " + pr); return; }
            for (Task t : list) System.out.println("ID " + t.getId() + " | " + t);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}