package service;

import model.Task;
import util.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> customHistory = new CustomLinkedList<>();
    private final Map<Long, Node<Task>> taskIdToNode = new HashMap<>();

    @Override
    public void add(Task task) {
        long taskId = task.getId();

        if (taskIdToNode.containsKey(taskId)) {
            customHistory.removeNode(taskIdToNode.get(taskId));
        }

        Node<Task> node = customHistory.linkLast(task);
        taskIdToNode.put(taskId, node);
    }

    @Override
    public List<Task> getHistory() {
        return customHistory.getTasks();
    }

    @Override
    public void remove(long taskId) {
        Node<Task> node = taskIdToNode.get(taskId);
        customHistory.removeNode(node);

        taskIdToNode.remove(taskId);
    }

    private static class CustomLinkedList<T> {
        Node<T> head;
        Node<T> tail;

        private int size = 0;

        public Node<T> linkLast(T task) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<T>(oldTail, task, null);

            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode);
            }

            size++;

            return newNode;
        }

        public int size() {
            return this.size;
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node<T> node = head;

            for (int i = 0; i < size; i++) {
                tasks.add((Task) node.getData());
                node = node.getNext();
            }

            return tasks;
        }

        public void removeNode(Node<T> node) {
            Node<T> prevNode = node.getPrev();
            Node<T> nextNode = node.getNext();

            if (prevNode != null) {
                prevNode.setNext(nextNode);
            } else {
                head = nextNode;
            }

            if (nextNode != null) {
                nextNode.setPrev(prevNode);
            } else {
                tail = prevNode;
            }

            size--;
        }
    }
}
