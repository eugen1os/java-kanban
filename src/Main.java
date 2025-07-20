import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        /* ===== БЛОК МЕНЮ (для тестирования) =====
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
                case 0 -> isRunning = false;
                default -> System.out.println("Ошибка: неверная команда!");
            }
        }
         ===== КОНЕЦ БЛОКА МЕНЮ ===== */
        /* Может сделать сердечник катушки деревянным? */
    }

    private static void printMainMenu() {
        System.out.println("\nГлавное меню:");
        System.out.println("1. Управление задачами");
        System.out.println("2. Управление эпиками");
        System.out.println("3. Управление подзадачами");
        System.out.println("4. Показать все данные");
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
                manager.createTask(new Task(0, name, desc, Status.NEW));
                System.out.println("Задача создана!");
            }
            case 2 -> {
                System.out.print("Введите ID задачи для удаления: ");
                int id = scanner.nextInt();
                manager.deleteTaskById(id);
                System.out.println("Задача удалена.");
            }
            case 3 -> {
                System.out.println("Все задачи:");
                manager.getAllTasks().forEach(System.out::println);
            }
            case 4 -> {
                System.out.print("Введите ID задачи: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Новый статус (NEW/IN_PROGRESS/DONE): ");
                Status status = Status.valueOf(scanner.nextLine().toUpperCase());

                Task task = manager.getTaskById(id);
                if (task != null) {
                    task.setStatus(status);
                    manager.updateTask(task);
                    System.out.println("Статус обновлён!");
                } else {
                    System.out.println("Задача не найдена!");
                }
            }
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
                manager.createEpic(new Epic(0, name, desc));
                System.out.println("Эпик создан!");
            }
            case 2 -> {
                System.out.print("Введите ID эпика для удаления: ");
                int id = scanner.nextInt();
                manager.deleteEpicById(id);
                System.out.println("Эпик удалён.");
            }
            case 3 -> {
                System.out.println("Все эпики:");
                manager.getAllEpics().forEach(System.out::println);
            }
            case 4 -> {
                System.out.print("Введите ID эпика: ");
                int epicId = scanner.nextInt();
                System.out.println("Подзадачи эпика " + epicId + ":");
                manager.getSubtasksByEpicId(epicId).forEach(System.out::println);
            }
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
                manager.createSubtask(new Subtask(0, name, desc, Status.NEW, epicId));
                System.out.println("Подзадача создана!");
            }
            case 2 -> {
                System.out.print("Введите ID подзадачи для удаления: ");
                int id = scanner.nextInt();
                manager.deleteSubtaskById(id);
                System.out.println("Подзадача удалена.");
            }
            case 3 -> {
                System.out.println("Все подзадачи:");
                manager.getAllSubtasks().forEach(System.out::println);
            }
            case 4 -> {
                System.out.print("Введите ID подзадачи: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Новый статус (NEW/IN_PROGRESS/DONE): ");
                Status status = Status.valueOf(scanner.nextLine().toUpperCase());

                Subtask subtask = manager.getSubtaskById(id);
                if (subtask != null) {
                    subtask.setStatus(status);
                    manager.updateSubtask(subtask);
                    System.out.println("Статус обновлён!");
                } else {
                    System.out.println("Подзадача не найдена!");
                }
            }
        }
    }

    private static void printAllData(TaskManager manager) {
        System.out.println("\n=== Все данные ===");
        System.out.println("Задачи:");
        manager.getAllTasks().forEach(System.out::println);

        System.out.println("\nЭпики:");
        manager.getAllEpics().forEach(System.out::println);

        System.out.println("\nПодзадачи:");
        manager.getAllSubtasks().forEach(System.out::println);
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