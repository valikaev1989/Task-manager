package ru.yandex.praktikum.Task;

import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {

    ArrayList<Long> idSubTasks = new ArrayList<>();

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
        return "EpicTask{" +
                "nameTask='" + getNameTask() + '\''
                + ", status=" + getStatus() +
                ", idEpicTask=" + getId() +
                ", idSubTasks=" + idSubTasks +
                '}';
    }
}