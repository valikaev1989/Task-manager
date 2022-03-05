package ru.yandex.praktikum.Task;

import java.util.Objects;

public  class SubTask extends Task {
    private long idEpicTask;

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

    @Override
    public String toString() {
        return "SubTask{" +
                "nameTask='" + getNameTask() + '\'' +
                ", status=" + getStatus() +
                ", idSubTask=" + getId() +
                ", idEpicTask=" + idEpicTask +
                '}';
    }
}