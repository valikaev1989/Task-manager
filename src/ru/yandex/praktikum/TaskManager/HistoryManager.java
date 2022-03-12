package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.Task.Task;

import java.util.List;

public interface HistoryManager extends Managers {
    void add(Task task);

    List<Task> getHistory();
}