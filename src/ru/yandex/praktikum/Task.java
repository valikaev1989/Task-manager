package ru.yandex.praktikum;

import java.util.ArrayList;
import java.util.Objects;

public class Task {
    private String nameTask;
    private String status = "New";
    private int id;

    public int getId() {
        return id;
    }

    public void setIdTask(int id) {
        this.id = id;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача: '" + nameTask + "'" + System.lineSeparator()
                + "id = " + id + System.lineSeparator()
                + "status: '" + status + "'.";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(nameTask, task.nameTask) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, status, id);
    }

    public static class EpicTask {
        private String nameEpicTask;
        private String statusEpicTask = "New";
        private int idEpicTask;
        ArrayList<Integer> idUnderTasks = new ArrayList<>();

        public ArrayList<Integer> getIdUnderTasks() {
            return idUnderTasks;
        }

        public void setIdUnderTasks(int idUnderTasks) {
            this.idUnderTasks.add(idUnderTasks);
        }

        public int getId() {
            return idEpicTask;
        }

        public void setIdEpicTask(int idEpicTask) {
            this.idEpicTask = idEpicTask;
        }

        public void setNameEpicTask(String nameEpicTask) {
            this.nameEpicTask = nameEpicTask;
        }

        public String getNameEpicTask() {
            return nameEpicTask;
        }

        public void setStatus(String statusEpicTask) {
            this.statusEpicTask = statusEpicTask;
        }

        @Override
        public String toString() {
            return "Сложная задача: '" + nameEpicTask + "'" + System.lineSeparator()
                    + "id = " + idEpicTask + System.lineSeparator()
                    + "status: '" + statusEpicTask + "'.";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EpicTask epicTask = (EpicTask) o;
            return idEpicTask == epicTask.idEpicTask
                    && Objects.equals(nameEpicTask, epicTask.nameEpicTask)
                    && Objects.equals(statusEpicTask, epicTask.statusEpicTask);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nameEpicTask, statusEpicTask, idEpicTask);
        }
    }

    public static class UnderTask {
        private String nameUnderTask;
        private String statusUnderTask = "New";
        private int idUnderTask;
        private int idEpicTask;

        public int getIdEpicTask() {
            return idEpicTask;
        }

        public void setIdEpicTask(int idEpicTask) {
            this.idEpicTask = idEpicTask;
        }

        public int getId() {
            return idUnderTask;
        }

        public void setIdUnderTask(int idUnderTask) {
            this.idUnderTask = idUnderTask;
        }

        public void setNameUnderTask(String nameUnderTask) {
            this.nameUnderTask = nameUnderTask;
        }

        public String getNameUnderTask() {
            return nameUnderTask;
        }

        public void setStatus(String statusUnderTask) {
            this.statusUnderTask = statusUnderTask;
        }

        public String getStatus() {
            return statusUnderTask;
        }

        @Override
        public String toString() {
            return "Подзадача: '" + nameUnderTask + "'" + System.lineSeparator()
                    + "id = " + idUnderTask + System.lineSeparator()
                    + "status: '" + statusUnderTask + "'.";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UnderTask underTask = (UnderTask) o;
            return idUnderTask == underTask.idUnderTask
                    && Objects.equals(nameUnderTask, underTask.nameUnderTask)
                    && Objects.equals(statusUnderTask, underTask.statusUnderTask);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nameUnderTask, statusUnderTask, idEpicTask);
        }
    }
}
