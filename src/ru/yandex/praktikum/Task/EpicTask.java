package ru.yandex.praktikum.Task;

import java.util.ArrayList;
import java.util.Objects;

public class EpicTask  extends Task{

    ArrayList<Integer> idUnderTasks = new ArrayList<>();

    public ArrayList<Integer> getIdUnderTasks() {
        return idUnderTasks;
    }

    public void setIdUnderTasks(int idUnderTasks) {
        this.idUnderTasks.add(idUnderTasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(idUnderTasks, epicTask.idUnderTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idUnderTasks);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "nameTask='" + nameTask + '\'' +
                ", status=" + status +
                ", idEpicTask=" + id +
                '}';
    }
}