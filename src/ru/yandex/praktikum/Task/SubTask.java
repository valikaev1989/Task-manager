package ru.yandex.praktikum.Task;

import java.util.Objects;

public  class SubTask extends Task {
    private int idEpicTask;

    public SubTask() {
    }

    public int getIdEpicTask() {
        return idEpicTask;
    }

    public void setIdEpicTask(int idEpicTask) {
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
                "nameTask='" + nameTask + '\'' +
                ", status=" + status +
                ", idSubTask=" + id +
                ", idEpicTask=" + idEpicTask +
                '}';
    }
}