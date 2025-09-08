import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Поехали!");

        // Создаем две задачи
        Task task1 = new Task("Тестовая задача 1", "Описание тестовой задачи 1", Status.NEW);
        Task task2 = new Task("Тестовая задача 2", "Описание тестовой задачи 2", Status.IN_PROGRESS);
        Task createdTask1 = manager.createTask(task1);
        Task createdTask2 = manager.createTask(task2);

        // Создаем эпик с двумя подзадачами
        Epic epic1 = new Epic("Тестовый эпик 1", "Описание тестового эпика 1", Status.NEW);
        Epic createdEpic1 = manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "Описание тестовой подзадачи 1", Status.NEW, createdEpic1.getId());
        Subtask subtask2 = new Subtask("Тестовая подзадача 2", "Описание тестовой подзадачи 2", Status.DONE, createdEpic1.getId());
        Subtask createdSubtask1 = manager.createSubtask(subtask1);
        Subtask createdSubtask2 = manager.createSubtask(subtask2);

        // Создаем эпик без подзадач
        Epic epic2 = new Epic("Тестовый эпик 2", "Описание тестового эпика 2", Status.NEW);
        Epic createdEpic2 = manager.createEpic(epic2);

        // Печатаем списки задач, эпиков и подзадач
        System.out.println("\nВсе задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nВсе эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        // Изменяем статусы созданных объектов
        Task updatedTask2 = new Task(createdTask2.getId(), "Обновленная задача 2", "Новое описание", Status.DONE);
        manager.updateTask(updatedTask2);

        Subtask updatedSubtask1 = new Subtask(createdSubtask1.getId(), "Обновленная подзадача 1", "Новое описание", Status.IN_PROGRESS, createdEpic1.getId());
        manager.updateSubtask(updatedSubtask1);

        // Печатаем обновленные списки
        System.out.println("\nПосле обновления:");
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        // Имитируем просмотр задач для истории
        System.out.println("\nИмитация просмотра задач:");
        manager.getTask(createdTask1.getId());
        manager.getEpic(createdEpic1.getId());
        manager.getSubtask(createdSubtask1.getId());
        manager.getTask(createdTask2.getId());
        manager.getEpic(createdEpic2.getId());

        // Печатаем историю просмотров
        System.out.println("\nИстория просмотров:");
        List<Task> history = manager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }

        // Удаляем одну задачу и проверяем историю
        System.out.println("\nПосле удаления задачи " + createdTask1.getId() + ":");
        manager.deleteTask(createdTask1.getId());

        history = manager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }

        // Удаляем эпик и проверяем, что удалились подзадачи
        System.out.println("\nПосле удаления эпика " + createdEpic1.getId() + ":");
        manager.deleteEpic(createdEpic1.getId());

        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История просмотров:");
        history = manager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
    }
}