package ru.yandex.praktikum.historyManager;

import ru.yandex.praktikum.all_Interface.HistoryManager;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.node.Node;

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
                }
            }
            linkedHistory.removeNode(node);
        } else {
            System.out.println("задача с id:" + idTask + " отсутствует в истории");
        }
    }

    @Override
    public List<Task> getHistory() {
        return linkedHistory.getTasks();
    }

    private class LinkedListHistory {
        private Node firstNode;
        private Node lastNode;

        public void linkLast(Task task) {
            final Node oldLastNode = lastNode;
            final Node newNode = new Node(oldLastNode, task, null);
            lastNode = newNode;
            if (oldLastNode == null) {
                firstNode = newNode;
                mapHistory.put(task.getId(), newNode);
            } else {
                oldLastNode.setNext(newNode);
                mapHistory.put(task.getId(), newNode); //обновление мапы
            }
        }

        public void removeNode(Node taskNode) {
            if (firstNode == null && lastNode == null) {
                return;
            } else if (firstNode.getNext() == null && lastNode.getPrev() == null) {
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
            mapHistory.remove(taskNode.getTask().getId());
        }

        public List<Task> getTasks() {
            List<Task> listTask = new ArrayList<>();
            Node current = firstNode;
            while (current != null) {
                listTask.add(current.getTask());
                current = current.getNext();
            }
            return listTask;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Task task : getHistory()) {
            if (result.length() == 0) {
                result.append(task.getId());
            }else{
                result.append(",");
                result.append(task.getId());
            }
        }
        return result.toString();
    }
}