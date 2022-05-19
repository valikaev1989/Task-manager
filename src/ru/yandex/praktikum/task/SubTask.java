package ru.yandex.praktikum.task;


import java.time.LocalDateTime;
import java.util.Objects;

import static ru.yandex.praktikum.utils.CSVutil.splitter;

public class SubTask extends Task {
    private Long idEpicTask;

    public SubTask(String nameTask, String description, TaskStatus status, Long id, int duration, LocalDateTime startTime, Long idEpicTask) {
        super(nameTask, description, status, id, duration, startTime);
        this.idEpicTask = idEpicTask;
        setType(TypeTasks.SUBTASK);
    }

    public SubTask(String nameTask, String description, int duration, LocalDateTime startTime, Long idEpicTask) {
        super(nameTask, description, duration, startTime);
        this.idEpicTask = idEpicTask;
        setType(TypeTasks.SUBTASK);
    }

    public SubTask(String nameTask, String description, Long idEpicTask) {
        super(nameTask, description);
        this.idEpicTask = idEpicTask;
        setType(TypeTasks.SUBTASK);
    }

    public SubTask(String nameTask, String description, TaskStatus status, Long idEpicTask) {
        super(nameTask, description, status);
        this.idEpicTask = idEpicTask;
        setType(TypeTasks.SUBTASK);
    }

    public SubTask(String nameTask, String description, TaskStatus status, Long id, Long idEpicTask) {
        super(nameTask, description, status, id);
        this.idEpicTask = idEpicTask;
        setType(TypeTasks.SUBTASK);
    }

    public SubTask() {
        setType(TypeTasks.SUBTASK);
    }

    public long getIdEpicTask() {
        if (idEpicTask != null) {
            return idEpicTask;
        } else {
            throw new NullPointerException("idEpicTask = null");
        }

    }

    public void setIdEpicTask(Long idEpicTask) {
        if (idEpicTask != null) {
            if (idEpicTask > 0) {
                this.idEpicTask = idEpicTask;
            } else {
                throw new IllegalArgumentException("idEpicTask имеет отрицательное значение");
            }
        } else {
            throw new NullPointerException("переданный idEpicTask = null");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return idEpicTask.equals(subTask.idEpicTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpicTask);
    }

    //id,type,name,status,description,epic
    @Override
    public String toString() {
        String endTime = getStartTime() == null ? null : getEndTime().toString();

        return getId().toString() + splitter + getType() + splitter + getNameTask() +
                splitter + getStatus() + splitter + getDescription() + splitter +
                getStartTime() + splitter + getDuration() + splitter + endTime + splitter + idEpicTask;

    }
}