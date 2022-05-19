package ru.yandex.praktikum.task;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static ru.yandex.praktikum.utils.CSVutil.splitter;

public class EpicTask extends Task {
    private LocalDateTime epicEndTime;
    private final ArrayList<Long> idSubTasks = new ArrayList<>();

    public EpicTask(String nameTask, String description, TaskStatus status, Long id, int duration, LocalDateTime startTime, LocalDateTime epicEndTime) {
        super(nameTask, description, status, id, duration, startTime);
        this.epicEndTime = epicEndTime;
        setType(TypeTasks.EPICTASK);
    }

    public EpicTask(String nameTask, String description, TaskStatus status, Long id) {
        super(nameTask, description, status, id);
        setType(TypeTasks.EPICTASK);
    }

    public EpicTask(String nameTask, String description) {
        super(nameTask, description);
        setType(TypeTasks.EPICTASK);
    }

    public EpicTask() {
        setType(TypeTasks.EPICTASK);
    }

    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public ArrayList<Long> getIdSubTasks() {
        return idSubTasks;
    }

    @Override
    public LocalDateTime getEndTime() {
        return epicEndTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    public void setIdSubTasks(Long idSubTasks) {
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
        return getId().toString() + splitter + getType() + splitter + getNameTask() +
                splitter + getStatus() + splitter + getDescription() + splitter +
                getStartTime() + splitter + getDuration() + splitter + epicEndTime;
    }
}