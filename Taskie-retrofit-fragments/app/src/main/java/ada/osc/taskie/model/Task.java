package ada.osc.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable{

	@Expose
	@SerializedName("id")
	private String mId;
	@Expose
	@SerializedName("title")
	private String mTitle;
	@Expose
	@SerializedName("content")
	private String mDescription;

	private boolean mCompleted;

	@Expose
	@SerializedName("taskPriority")
	private int mPriority;

	@Expose
	@SerializedName("isFavorite")
	private boolean mFavorite = false;

	public Task(String title, String description, int priority) {
		mId = UUID.randomUUID().toString();
		mTitle = title;
		mDescription = description;
		mCompleted = false;
		mPriority = priority;
	}


	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public boolean isCompleted() {
		return mCompleted;
	}

	public void setCompleted(boolean completed) {
		mCompleted = completed;
	}

	public int getPriority() {
		return mPriority;
	}

	public TaskPriority getPriorityEnum() {
		return TaskPriority.values()[mPriority - 1];
	}

	public void setPriority(int priority) {
		mPriority = priority;
	}

	public boolean isFavorite() {
		return mFavorite;
	}

	public void toggleFavorite(){
		setFavorite(!mFavorite);
	}

	public void setFavorite(boolean isFavorite) {
		mFavorite = isFavorite;
	}
}
