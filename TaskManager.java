import java.util.*;
import java.io.*;

class Task implements Comparable<Task> {
    private int id;
    private String description;
    private int priority;
    private boolean completed;
    private String deadline;
    private String category;

    public Task(int id, String description, int priority, String deadline, String category) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.completed = false;
        this.deadline = deadline;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getCategory() {
        return category;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void updateTask(String newDescription, int newPriority, String newDeadline, String newCategory) {
        this.description = newDescription;
        this.priority = newPriority;
        this.deadline = newDeadline;
        this.category = newCategory;
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", description='" + description + "', priority=" + priority +
               ", completed=" + completed + ", deadline='" + deadline + "', category='" + category + "'}";
    }

    public static Task fromString(String taskString) {
        // Parse the task string to create a Task object
        String[] parts = taskString.split(", ");
        int id = Integer.parseInt(parts[0].split("=")[1]);
        String description = parts[1].split("=")[1].replace("'", "");
        int priority = Integer.parseInt(parts[2].split("=")[1]);
        boolean completed = Boolean.parseBoolean(parts[3].split("=")[1]);
        String deadline = parts[4].split("=")[1].replace("'", "");
        String category = parts[5].split("=")[1].replace("'", "").replace("}", "");
        return new Task(id, description, priority, deadline, category);
    }
}

public class TaskManager {
    private LinkedList<Task> taskList;
    private Stack<Task> undoStack;
    private PriorityQueue<Integer> availableIds;
    private int idCounter;

    public TaskManager() {
        taskList = new LinkedList<>();
        undoStack = new Stack<>();
        availableIds = new PriorityQueue<>();
        idCounter = 1;
    }

    private int getNextId() {
        if (!availableIds.isEmpty()) {
            return availableIds.poll();
        }
        return idCounter++;
    }

    public void addTask(String description, int priority, String deadline, String category) {
        int id = getNextId();
        Task task = new Task(id, description, priority, deadline, category);
        taskList.add(task);
        System.out.println("Task added: " + task);
    }

    public void viewTasks() {
        if (taskList.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        System.out.println("Tasks in insertion order:");
        for (Task task : taskList) {
            System.out.println(task);
        }
    }

    public void viewTasksInPriorityOrder() {
        if (taskList.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        PriorityQueue<Task> tempQueue = new PriorityQueue<>(taskList);
        System.out.println("Tasks in priority order:");
        while (!tempQueue.isEmpty()) {
            System.out.println(tempQueue.poll());
        }
    }

    public void editTask(int id, String newDescription, int newPriority, String newDeadline, String newCategory) {
        for (Task task : taskList) {
            if (task.getId() == id) {
                task.updateTask(newDescription, newPriority, newDeadline, newCategory);
                System.out.println("Task edited: " + task);
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void deleteTask(int id) {
        for (Task task : taskList) {
            if (task.getId() == id) {
                undoStack.push(task);
                taskList.remove(task);
                availableIds.add(id);
                System.out.println("Task deleted: " + task);
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void undoDelete() {
        if (!undoStack.isEmpty()) {
            Task task = undoStack.pop();
            taskList.add(task);
            availableIds.remove(task.getId());
            System.out.println("Task restored: " + task);
        } else {
            System.out.println("No tasks to undo.");
        }
    }

    public void markTaskCompleted(int id) {
        for (Task task : taskList) {
            if (task.getId() == id) {
                task.setCompleted(true);
                System.out.println("Task marked as completed: " + task);
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void sortTasksByInsertionSort() {
        List<Task> sortedList = new ArrayList<>(taskList);
        insertionSort(sortedList);
        System.out.println("Tasks sorted by insertion sort:");
        for (Task task : sortedList) {
            System.out.println(task);
        }
    }

    private void insertionSort(List<Task> list) {
        System.out.println("Starting insertion sort...");
        for (int i = 1; i < list.size(); i++) {
            Task key = list.get(i);
            int j = i - 1;
            System.out.println("Inserting element: " + key);
            while (j >= 0 && list.get(j).getPriority() > key.getPriority()) {
                list.set(j + 1, list.get(j));
                j = j - 1;
                System.out.println("Shifting element: " + list.get(j + 1));
            }
            list.set(j + 1, key);
            System.out.println("Current state of the list: " + list);
        }
        System.out.println("Insertion sort completed.");
    }

    public void sortTasksByBubbleSort() {
        List<Task> sortedList = new ArrayList<>(taskList);
        bubbleSort(sortedList);
        System.out.println("Tasks sorted by bubble sort:");
        for (Task task : sortedList) {
            System.out.println(task);
        }
    }

    private void bubbleSort(List<Task> list) {
        System.out.println("Starting bubble sort...");
        int n = list.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                System.out.println("Comparing: " + list.get(j) + " and " + list.get(j + 1));
                if (list.get(j).getPriority() > list.get(j + 1).getPriority()) {
                    // Swap list.get(j) and list.get(j + 1)
                    Task temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                    System.out.println("Swapped: " + list.get(j) + " and " + list.get(j + 1));
                }
            }
            System.out.println("Current state of the list after pass " + (i + 1) + ": " + list);
            // If no two elements were swapped by inner loop, then break
            if (!swapped) break;
        }
        System.out.println("Bubble sort completed.");
    }

    public void sortTasksBySelectionSort() {
        List<Task> sortedList = new ArrayList<>(taskList);
        selectionSort(sortedList);
        System.out.println("Tasks sorted by selection sort:");
        for (Task task : sortedList) {
            System.out.println(task);
        }
    }

    private void selectionSort(List<Task> list) {
        System.out.println("Starting selection sort...");
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in remaining unsorted array
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                System.out.println("Comparing: " + list.get(minIdx) + " and " + list.get(j));
                if (list.get(j).getPriority() < list.get(minIdx).getPriority()) {
                    minIdx = j;
                }
            }
            // Swap the found minimum element with the first element
            Task temp = list.get(minIdx);
            list.set(minIdx, list.get(i));
            list.set(i, temp);
            System.out.println("Swapped: " + list.get(i) + " and " + list.get(minIdx));
            System.out.println("Current state of the list after pass " + (i + 1) + ": " + list);
        }
        System.out.println("Selection sort completed.");
    }

    public void sortTasksByMergesort() {
        List<Task> sortedList = new ArrayList<>(taskList);
        mergeSort(sortedList, 0, sortedList.size() - 1);
        System.out.println("Tasks sorted by mergesort:");
        for (Task task : sortedList) {
            System.out.println(task);
        }
    }

    private void mergeSort(List<Task> list, int l, int r) {
        System.out.println("Starting merge sort...");
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            System.out.println("Sorting first half: " + list.subList(l, m + 1));
            mergeSort(list, l, m);
            System.out.println("Sorting second half: " + list.subList(m + 1, r + 1));
            mergeSort(list, m + 1, r);

            // Merge the sorted halves
            System.out.println("Merging: " + list.subList(l, m + 1) + " and " + list.subList(m + 1, r + 1));
            merge(list, l, m, r);
            System.out.println("Merged list: " + list.subList(l, r + 1));
        }
        System.out.println("Merge sort completed.");
    }

    private void merge(List<Task> list, int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        List<Task> L = new ArrayList<>(list.subList(l, l + n1));
        List<Task> R = new ArrayList<>(list.subList(m + 1, m + 1 + n2));

        // Merge the temp arrays

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray
        int k = l;
        while (i < n1 && j < n2) {
            if (L.get(i).getPriority() <= R.get(j).getPriority()) {
                list.set(k, L.get(i));
                i++;
            } else {
                list.set(k, R.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            list.set(k, L.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            list.set(k, R.get(j));
            j++;
            k++;
        }
    }

    public void saveTasksToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Task task : taskList) {
                writer.write(task.toString());
                writer.newLine();
            }
            System.out.println("Tasks saved to file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasksFromFile(String filename) {
        taskList.clear();
        availableIds.clear();
        idCounter = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromString(line);
                taskList.add(task);
                if (task.getId() >= idCounter) {
                    idCounter = task.getId() + 1;
                }
            }
            System.out.println("Tasks loaded from file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTask Manager Menu:");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. View Tasks in Priority Order");
            System.out.println("4. Edit Task");
            System.out.println("5. Delete Task");
            System.out.println("6. Undo Delete");
            System.out.println("7. Mark Task Completed");
            System.out.println("8. Sort Tasks by Insertion Sort");
            System.out.println("9. Sort Tasks by Bubble Sort");
            System.out.println("10. Sort Tasks by Selection Sort");
            System.out.println("11. Sort Tasks by Mergesort");
            System.out.println("12. Save Tasks to File");
            System.out.println("13. Load Tasks from File");
            System.out.println("14. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter task priority: ");
                    int priority = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter task deadline: ");
                    String deadline = scanner.nextLine();
                    System.out.print("Enter task category: ");
                    String category = scanner.nextLine();
                    taskManager.addTask(description, priority, deadline, category);
                    break;
                case 2:
                    taskManager.viewTasks();
                    break;
                case 3:
                    taskManager.viewTasksInPriorityOrder();
                    break;
                case 4:
                    System.out.print("Enter task ID to edit: ");
                    int editId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new task description: ");
                    String newDescription = scanner.nextLine();
                    System.out.print("Enter new task priority: ");
                    int newPriority = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new task deadline: ");
                    String newDeadline = scanner.nextLine();
                    System.out.print("Enter new task category: ");
                    String newCategory = scanner.nextLine();
                    taskManager.editTask(editId, newDescription, newPriority, newDeadline, newCategory);
                    break;
                case 5:
                    System.out.print("Enter task ID to delete: ");
                    int deleteId = scanner.nextInt();
                    taskManager.deleteTask(deleteId);
                    break;
                case 6:
                    taskManager.undoDelete();
                    break;
                case 7:
                    System.out.print("Enter task ID to mark as completed: ");
                    int completeId = scanner.nextInt();
                    taskManager.markTaskCompleted(completeId);
                    break;
                case 8:
                    taskManager.sortTasksByInsertionSort();
                    break;
                case 9:
                    taskManager.sortTasksByBubbleSort();
                    break;
                case 10:
                    taskManager.sortTasksBySelectionSort();
                    break;
                case 11:
                    taskManager.sortTasksByMergesort();
                    break;
                case 12:
                    System.out.print("Enter filename to save tasks: ");
                    String saveFilename = scanner.nextLine();
                    taskManager.saveTasksToFile(saveFilename);
                    break;
                case 13:
                    System.out.print("Enter filename to load tasks: ");
                    String loadFilename = scanner.nextLine();
                    taskManager.loadTasksFromFile(loadFilename);
                    break;
                case 14:
                    System.out.println("Exiting Task Manager.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
