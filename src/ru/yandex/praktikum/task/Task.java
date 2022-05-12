package ru.yandex.praktikum.task;


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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return startTime.plusMinutes(duration);
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
        String result;
        try {
            String q = String.valueOf(startTime);
            String w = String.valueOf(getEndTime());
            result = id.toString() + splitter + TypeTasks.Task + splitter + nameTask + splitter
                    + status + splitter + description + splitter + q + splitter + duration + splitter + w;
        } catch (NullPointerException ex) {
            String q = String.valueOf(startTime);
            String w = "null";
            result = id.toString() + splitter + TypeTasks.Task + splitter + nameTask + splitter
                    + status + splitter + description + splitter + q + splitter + duration + splitter + w;
        }
        return result;
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