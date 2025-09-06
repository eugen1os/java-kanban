package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;
    private int size = 0;

    @Override
    public void add(Task task) {
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
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;

        if (l == null) {
            first = newNode;
        } else {
            l.setNext(newNode);
        }

        size++;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = first;

        while (current != null) {
            tasks.add(current.getData());
            current = current.getNext();
        }

        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        final Node next = node.getNext();
        final Node prev = node.getPrev();

        if (prev == null) {
            first = next;
        } else {
            prev.setNext(next);
            node.setPrev(null);
        }

        if (next == null) {
            last = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }

        node.setData(null);
        size--;
    }

    // Метод для тестирования
    public int size() {
        return size;
    }
}