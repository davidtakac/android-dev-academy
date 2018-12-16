package ada.osc.taskie.util;

import java.util.Comparator;

import ada.osc.taskie.model.Task;

public class CompareByPriority implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        return task1.getPriority().compareTo(task2.getPriority());
    }
}
