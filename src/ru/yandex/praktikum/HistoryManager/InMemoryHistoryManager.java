package ru.yandex.praktikum.HistoryManager;

import ru.yandex.praktikum.Interface.HistoryManager;
import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Node.Node;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Long, Node> mapHistory = new HashMap<>();
    private LinkedListHistory linkedHistory = new LinkedListHistory();

    public void add(Task task) { //добавление задачи в список истории просмотров
        long idTask = task.getId();
        if (mapHistory.containsKey(idTask)) { //проверка на наличие дубликатов
            Node node = mapHistory.get(idTask);
            linkedHistory.removeNode(node); //удаление дубликата
            linkedHistory.linkLast(task);//добавление в конец списка истории просмотра
        } else {
            linkedHistory.linkLast(task);
        }
    }

    @Override
    public void remove(long idTask) { // удаление из истории просмотров
        if (mapHistory.containsKey(idTask)) {
            Node node = mapHistory.get(idTask);
            Task task = node.getTask();
            EpicTask epicTask = new EpicTask();
            if (task.getClass() == epicTask.getClass()) {
                epicTask = (EpicTask) task;
                for (Long idSubTask : epicTask.getIdSubTasks()) {
                    Node nodeSubTask = mapHistory.get(idSubTask);
                    linkedHistory.removeNode(nodeSubTask);
                    mapHistory.remove(idSubTask);
                }
                linkedHistory.removeNode(node);
                mapHistory.remove(idTask);
            } else {
                linkedHistory.removeNode(node);
                mapHistory.remove(idTask);
            }
        } else {
            System.out.println("Нет такой задачи в истории");
        }
    }

    @Override
    public List<Task> getHistory() {
        return linkedHistory.getTasks();
    }

    private class LinkedListHistory {
        private Node firstNode;
        private Node lastNode;
        private int size = 0;

        public void linkLast(Task task) {
            Node node = new Node(null, task, null);
            if (size == 0) {
                addFirst(task);
            } else {
                lastNode.setNext(node);
                node.setPrev(lastNode);
                lastNode = node;
                mapHistory.put(task.getId(), lastNode);
                size++;
            }
        }

        public void addFirst(Task task) {
            Node node = new Node(null, task, null);

            if (size == 0) {
                firstNode = node;
                lastNode = node;
            } else {
                firstNode.setPrev(node);
                node.setNext(firstNode);
                firstNode = node;
            }
            mapHistory.put(task.getId(), node);
            size++;
        }

        public void removeNode(Node taskNode) {
            if (size == 0) {
                return;
            }
            if (size == 1) {
                firstNode = lastNode = null;
            } else {
                if (taskNode.getPrev() == null) {
                    firstNode = firstNode.getNext();
                    firstNode.setPrev(null);
                } else if (taskNode.getNext() == null) {
                    lastNode = lastNode.getPrev();
                    lastNode.setNext(null);
                } else {
                    Node prevTaskNode = taskNode.getPrev();
                    Node nextTaskNode = taskNode.getNext();
                    prevTaskNode.setNext(nextTaskNode);
                    nextTaskNode.setPrev(prevTaskNode);
                }
            }
            size--;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> listTask = new ArrayList<>();
            Node current = firstNode;
            while (current != null) {
                listTask.add(current.getTask());
                current = current.getNext();
            }
            return listTask;
        }
    }
}