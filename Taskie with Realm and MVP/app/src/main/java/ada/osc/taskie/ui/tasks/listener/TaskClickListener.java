package ada.osc.taskie.ui.tasks.listener;

import ada.osc.taskie.model.Task;

public interface TaskClickListener {
	void onClick(Task task);
	void onLongClick(Task task);
	void onToggleButtonClick(Task task);
	void onPriorityClick(Task task);
}
