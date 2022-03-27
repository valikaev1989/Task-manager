package ru.yandex.praktikum.HistoryManager;

import ru.yandex.praktikum.Interface.HistoryManager;
import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Node.Node;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyList = new ArrayList<>();
    private HashMap<Long, Node> mapHistory = new HashMap<>();
    private LinkedList<Node> linkedHistory = new LinkedList<>();
    Node firstNode;
    Node lastNode;


    public void add(Task task) { //добавление задачи в список истории просмотров
        long idTask = task.getId();
        if (mapHistory.containsKey(idTask)) { //проверка на наличие дубликатов
            Node node = mapHistory.get(idTask);
            removeNode(node); //удаление дубликата
            linkLast(task);//добавление в конец списка истории просмотра
        } else if (linkedHistory.size() == 10) {
            linkedHistory.removeFirst();
            linkLast(task);
        } else {
            linkLast(task);
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
                    removeNode(nodeSubTask);
                    mapHistory.remove(idSubTask);
                }
                removeNode(node);
                mapHistory.remove(idTask);
            } else {
                removeNode(node);
                mapHistory.remove(idTask);
            }
        } else {
            System.out.println("Нет такой задачи в истории");
        }
    }

    public void removeNode(Node taskNode) {
        if (linkedHistory.isEmpty()) {
            return;
        }
        if (linkedHistory.size() == 1) {
            firstNode = lastNode = null;
            linkedHistory.removeFirst();
        } else {
            if (taskNode.getPrev() == null) {
                linkedHistory.removeFirst();
                firstNode = firstNode.getNext();
                firstNode.setPrev(null);
            } else if (taskNode.getNext() == null) {
                linkedHistory.removeLast();
                lastNode = lastNode.getPrev();
                lastNode.setNext(null);
            } else {
                Node prevTaskNode = taskNode.getPrev();
                Node nextTaskNode = taskNode.getNext();
                prevTaskNode.setNext(nextTaskNode);
                nextTaskNode.setPrev(prevTaskNode);
                linkedHistory.remove(taskNode);
            }
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Node node : linkedHistory) {
            listTask.add(node.getTask());
        }
        return listTask;
    }

    @Override
    public List<Task> getHistory() {
        historyList = getTasks();
        return historyList;
    }

    public void linkLast(Task task) {
        Node node = new Node(null, task, null);
        if (linkedHistory.size() == 0) {
            addFirst(task);
        } else {
            lastNode.setNext(node);
            node.setPrev(lastNode);
            lastNode = node;
            linkedHistory.add(lastNode);
            mapHistory.put(task.getId(), lastNode);
        }
    }

    public void addFirst(Task task) {
        Node node = new Node(null, task, null);

        if (linkedHistory.size() == 0) {
            firstNode = node;
            lastNode = node;
            linkedHistory.add(node);
        } else {
            firstNode.setPrev(node);
            node.setNext(firstNode);
            firstNode = node;
            linkedHistory.add(firstNode);
        }
        mapHistory.put(task.getId(), node);
    }
}