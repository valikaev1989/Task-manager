package ru.yandex.praktikum.task;


import java.util.ArrayList;
import java.util.Objects;

import static ru.yandex.praktikum.utils.CSVutil.splitter;

public class EpicTask extends Task {
    ArrayList<Long> idSubTasks = new ArrayList<>();

    public EpicTask(String nameTask, String description, TaskStatus status, Long id) {
        super(nameTask, description, status, id);
    }

    public EpicTask(String nameTask, String description) {
        super(nameTask, description);
    }

    public EpicTask() {
    }

    public ArrayList<Long> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(long idSubTasks) {
        this.idSubTasks.add(idSubTasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(idSubTasks, epicTask.idSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTasks);
    }

    @Override
    public String toString() {
        return id.toString() + splitter + TypeTasks.EpicTask + splitter + nameTask + splitter + status + splitter + description;
    }
}