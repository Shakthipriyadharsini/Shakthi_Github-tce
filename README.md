# Astronaut Daily Schedule Organizer

Java console application that helps astronauts organize their daily schedules.

## Features
- Add a new task with description, start time, end time, and priority level
- Remove an existing task (by id or description)
- View all tasks sorted by start time
- Validate that new tasks do not overlap with existing tasks
- Provide error messages for invalid operations
- Optional: Edit tasks, mark tasks completed, view tasks by priority level
- Logging (app.log) tracks application usage and errors

## Design Patterns Used
1. **Singleton Pattern** - `ScheduleManager` ensures a single instance manages all tasks.
2. **Factory Pattern** - `TaskFactory` creates validated `Task` objects from input.
3. **Observer Pattern** - `ConsoleNotifier` gets updates for conflicts, additions, edits, and removals.

## Requirements
- Java 8+ installed

## Compile
```bash
javac *.java
```

## Run
```bash
java Main
```

## Example Usage
- Add Task: `Morning Exercise, 07:00, 08:00, High` → Task added successfully.
- Add Task: `Team Meeting, 09:00, 10:00, Medium` → Task added successfully.
- Add Overlapping Task: `09:30 - 10:30` → Error: conflicts with Team Meeting.
- View Tasks → Tasks displayed sorted by start time.

