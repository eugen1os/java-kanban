import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();
        Scanner scanner = new Scanner(System.in);

        boolean isRunning = true;
        while (isRunning) {
            printMainMenu();
            int command = readInt(scanner, "Введите команду: ");
            scanner.nextLine(); // Очистка буфера

            switch (command) {
                case 1 -> menuTasks(manager, scanner);
                case 2 -> menuEpics(manager, scanner);
                case 3 -> menuSubtasks(manager, scanner);
                case 4 -> printAllData(manager);
                case 5 -> printHistory(manager);
                case 0 -> isRunning = false;
                default -> System.out.println("Ошибка: неверная команда!");
            }
        }
    }

    private static void printStatusMenu() {
        System.out.println("Выберите статус:");
        System.out.println("1. NEW");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. DONE");
    }

    private static Status selectStatus(Scanner scanner) {
        printStatusMenu();
        int statusChoice = readInt(scanner, "Введите номер статуса: ");
        return switch (statusChoice) {
            case 1 -> Status.NEW;
            case 2 -> Status.IN_PROGRESS;
            case 3 -> Status.DONE;
            default -> {
                System.out.println("Неверный выбор, установлен статус NEW");
                yield Status.NEW;
            }
        };
    }

    private static void printHistory(TaskManager manager) {
        System.out.println("\n=== История просмотров ===");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    private static void printMainMenu() {
        System.out.println("\nГлавное мену:");
        System.out.println("1. Управление задачами");
        System.out.println("2. Управление эпиками");
        System.out.println("3. Управление подзадачами");
        System.out.println("4. Показать все данные");
        System.out.println("5. История просмотров");
        System.out.println("0. Выход");
    }

    private static void menuTasks(TaskManager manager, Scanner scanner) {
        System.out.println("\n=== Управление задачами ===");
        System.out.println("1. Создать задачу");
        System.out.println("2. Удалить задачу");
        System.out.println("3. Показать все задачи");
        System.out.println("4. Изменить статус задачи");
        System.out.println("5. Назад");
        System.out.print("Введите команду: ");

        int command = scanner.nextInt();
        scanner.nextLine();

        switch (command) {
            case 1 -> {
                System.out.print("Введите название задачи: ");
                String name = scanner.nextLine();
                System.out.print("Введите описание: ");
                String desc = scanner.nextLine();
                Task task = new Task(0, name, desc, Status.NEW);
                manager.createTask(task);
                System.out.println("Задача создана! ID: " + task.getId());
            }
            case 2 -> {
                System.out.print("Введите ID задачи для удаления: ");
                int id = scanner.nextInt();
                manager.deleteTaskById(id);
                System.out.println("Задача удалена.");
            }
            case 3 -> {
                System.out.println("Все задачи:");
                for (Task task : manager.getAllTasks()) {
                    System.out.println(task);
                }
            }
            case 4 -> {
                System.out.print("Введите ID задачи: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                Status status = selectStatus(scanner);
                Task task = manager.getTaskById(id);
                if (task != null) {
                    task.setStatus(status);
                    manager.updateTask(task);
                    System.out.println("Статус обновлён!");
                } else {
                    System.out.println("Задача не найдена!");
                }
            }
            case 5 -> {
                // Возврат в главное меню
            }
            default -> System.out.println("Ошибка: неверная команда!"); // ДОБАВЛЕНО default case
        }
    }

    private static void menuEpics(TaskManager manager, Scanner scanner) {
        System.out.println("\n=== Управление эпиками ===");
        System.out.println("1. Создать эпик");
        System.out.println("2. Удалить эпик");
        System.out.println("3. Показать все эпики");
        System.out.println("4. Показать подзадачи эпика");
        System.out.println("5. Назад");
        System.out.print("Введите команду: ");

        int command = scanner.nextInt();
        scanner.nextLine();

        switch (command) {
            case 1 -> {
                System.out.print("Введите название эпика: ");
                String name = scanner.nextLine();
                System.out.print("Введите описание: ");
                String desc = scanner.nextLine();
                Epic epic = new Epic(0, name, desc);
                manager.createEpic(epic);
                System.out.println("Эпик создан! ID: " + epic.getId());
            }
            case 2 -> {
                System.out.print("Введите ID эпика для удаления: ");
                int id = scanner.nextInt();
                manager.deleteEpicById(id);
                System.out.println("Эпик удалён.");
            }
            case 3 -> {
                System.out.println("Все эпики:");
                for (Epic epic : manager.getAllEpics()) {
                    System.out.println(epic);
                }
            }
            case 4 -> {
                System.out.print("Введите ID эпика: ");
                int epicId = scanner.nextInt();
                System.out.println("Подзадачи эпика " + epicId + ":");
                for (Subtask subtask : manager.getSubtasksByEpicId(epicId)) {
                    System.out.println(subtask);
                }
            }
            case 5 -> {
                // Возврат в главное меню
            }
            default -> System.out.println("Ошибка: неверная команда!"); // ДОБАВЛЕНО default case
        }
    }

    private static void menuSubtasks(TaskManager manager, Scanner scanner) {
        System.out.println("\n=== Управление подзадачами ===");
        System.out.println("1. Создать подзадачу");
        System.out.println("2. Удалить подзадачу");
        System.out.println("3. Показать все подзадачи");
        System.out.println("4. Изменить статус подзадачи");
        System.out.println("5. Назад");
        System.out.print("Введите команду: ");

        int command = scanner.nextInt();
        scanner.nextLine();

        switch (command) {
            case 1 -> {
                System.out.print("Введите ID эпика для подзадачи: ");
                int epicId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Введите название подзадачи: ");
                String name = scanner.nextLine();
                System.out.print("Введите описание: ");
                String desc = scanner.nextLine();
                Subtask subtask = new Subtask(0, name, desc, Status.NEW, epicId);
                manager.createSubtask(subtask);
                System.out.println("Подзадача создана! ID: " + subtask.getId());
            }
            case 2 -> {
                System.out.print("Введите ID подзадачи для удаления: ");
                int id = scanner.nextInt();
                manager.deleteSubtaskById(id);
                System.out.println("Подзадача удалена.");
            }
            case 3 -> {
                System.out.println("Все подзадачи:");
                for (Subtask subtask : manager.getAllSubtasks()) {
                    System.out.println(subtask);
                }
            }
            case 4 -> {
                System.out.print("Введите ID подзадачи: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                Status status = selectStatus(scanner);
                Subtask subtask = manager.getSubtaskById(id);
                if (subtask != null) {
                    subtask.setStatus(status);
                    manager.updateSubtask(subtask);
                    System.out.println("Статус обновлён!");
                } else {
                    System.out.println("Подзадача не найдена!");
                }
            }
            case 5 -> {
                // Возврат в главное меню
            }
            default -> System.out.println("Ошибка: неверная команда!"); // ДОБАВЛЕНО default case
        }
    }

    private static void printAllData(TaskManager manager) {
        System.out.println("\n=== Все данные ===");
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nЭпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nПодзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите число!");
            scanner.next();
        }
        return scanner.nextInt();
    }
}