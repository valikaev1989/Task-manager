package ru.yandex.praktikum.tests;

import ru.yandex.praktikum.taskmanager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }
}
