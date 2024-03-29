package ru.yandex.praktikum.task;


import org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.yandex.praktikum.utils.CSVutil.splitter;

public class Task implements Comparable<Task> {
    private String nameTask;
    private String description;
    private TaskStatus status;
    private Long id;
    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TypeTasks type = TypeTasks.TASK;

    public Task(String nameTask, String description, TaskStatus status, Long id, int duration, LocalDateTime startTime) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String nameTask, String description, int duration, LocalDateTime startTime) {
        this.nameTask = nameTask;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        status = TaskStatus.NEW;
    }

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String nameTask, String description, TaskStatus status) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
    }

    public Task(String nameTask, String description, TaskStatus status, Long id) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task() {
        status = TaskStatus.NEW;
    }

    public TypeTasks getType() {
        return type;
    }

    public void setType(TypeTasks type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getNameTask() {
        return nameTask;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        this.endTime = startTime.plusMinutes(duration);
        return endTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && Objects.equals(nameTask, task.nameTask)
                && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, description, status, id);
    }

    @Override
    public String toString() {
        String endTime = startTime == null ? null : getEndTime().toString();
        return id.toString() + splitter + type + splitter + nameTask + splitter
                + status + splitter + description + splitter + startTime + splitter + duration + splitter + endTime;
    }

    @Override
    public int compareTo(Task o) {
        if (this.startTime == null) {
            return 1;
        }
        if (o.startTime == null) {
            return -1;
        }
        return (this.startTime.compareTo(o.startTime));
    }
}