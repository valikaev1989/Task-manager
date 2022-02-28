package ru.yandex.praktikum.Task;

import java.util.Objects;

public  class UnderTask extends Task {
    private int idEpicTask;

    public UnderTask() {
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
        UnderTask underTask = (UnderTask) o;
        return idEpicTask == underTask.idEpicTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpicTask);
    }

    @Override
    public String toString() {
        return "UnderTask{" +
                "nameTask='" + nameTask + '\'' +
                ", status=" + status +
                ", idUnderTask=" + id +
                ", idEpicTask=" + idEpicTask +
                '}';
    }
}