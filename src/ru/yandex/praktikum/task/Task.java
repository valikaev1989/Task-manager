package ru.yandex.praktikum.task;


import java.util.Objects;

import static ru.yandex.praktikum.readAndWriteTasks.CSVutil.splitter;

public class Task {
     String nameTask;
     String description;
     TaskStatus status;
     Long id;

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
        status = TaskStatus.NEW;
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

    public long getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(nameTask, task.nameTask)
                && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, description, status, id);
    }

//    @Override
//    public String toString() {
//        return "Task{" +
//                "nameTask='" + nameTask + '\'' +
//                ", description='" + description + '\'' +
//                ", status=" + status +
//                ", id=" + id +
//                '}';
//    }

    @Override
    public String toString() {
        return id.toString() + splitter + TypeTasks.Task + splitter + nameTask + splitter + status + splitter + description;
    }
}