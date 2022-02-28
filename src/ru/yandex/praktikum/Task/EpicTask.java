package ru.yandex.praktikum.Task;

import java.util.ArrayList;
import java.util.Objects;

public class EpicTask  extends Task{

    ArrayList<Integer> idSubTasks = new ArrayList<>();

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(int idSubTasks) {
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
                "idSubTasks=" + idSubTasks +
                ", nameTask='" + nameTask + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}