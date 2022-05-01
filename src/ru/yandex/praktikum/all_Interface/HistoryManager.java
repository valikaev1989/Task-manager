package ru.yandex.praktikum.all_Interface;

import ru.yandex.praktikum.task.Task;

import java.util.List;

public interface HistoryManager<T> extends Managers {
    void add(Task task);

    List<Task> getHistory();

    void remove(long id);
    }