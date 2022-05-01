package ru.yandex.praktikum.task;


import java.util.Objects;

import static ru.yandex.praktikum.readAndWriteTasks.CSVutil.splitter;

public class SubTask extends Task {
    private long idEpicTask;

    public SubTask(String nameTask, String description, long idEpicTask) {
        super(nameTask, description);
        this.idEpicTask = idEpicTask;
    }

    public SubTask(String nameTask, String description, TaskStatus status, Long id, long idEpicTask) {
        super(nameTask, description, status, id);
        this.idEpicTask = idEpicTask;
    }

    public SubTask() {
    }

    public long getIdEpicTask() {
        return idEpicTask;
    }

    public void setIdEpicTask(long idEpicTask) {
        this.idEpicTask = idEpicTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return idEpicTask == subTask.idEpicTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpicTask);
    }

    //id,type,name,status,description,epic
    @Override
    public String toString() {
        return id.toString() + splitter + TypeTasks.SubTask + splitter + nameTask + splitter + status + splitter + description + splitter + idEpicTask;
    }
}