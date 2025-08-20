package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        model.Task data;
        Node next;
        Node prev;

        Node(Node prev, model.Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;
    private int size = 0;

    @Override
    public void add(model.Task task) {
        if (task == null) {
            return;
        }

        int id = task.getId();
        // Удаляем существующий узел, если задача уже есть в истории
        if (nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
        }

        // Добавляем задачу в конец списка
        linkLast(task);

        // Обновляем мапу
        nodeMap.put(id, last);
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
            nodeMap.remove(id);
        }
    }

    @Override
    public List<model.Task> getHistory() {
        return getTasks();
    }

    private void linkLast(model.Task task) {
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;

        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }

        size++;
    }

    private List<model.Task> getTasks() {
        List<model.Task> tasks = new ArrayList<>();
        Node current = first;

        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }

        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
        size--;
    }

    // Метод для тестирования
    public int size() {
        return size;
    }
}