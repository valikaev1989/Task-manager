package ru.yandex.praktikum.Interface;

import ru.yandex.praktikum.Task.Task;

import java.util.List;

public interface HistoryManager<T> extends Managers {
    void add(Task task);

    List<Task> getHistory();

    void remove(long id);
    }