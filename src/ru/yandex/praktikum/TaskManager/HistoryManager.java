package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.Task.Task;

import java.util.ArrayList;

public interface HistoryManager extends Managers{
    void add(Task task);

    ArrayList<Task> getHistory();
}
