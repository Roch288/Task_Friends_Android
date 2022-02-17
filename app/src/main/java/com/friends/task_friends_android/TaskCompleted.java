
package com.friends.task_friends_android;

        import java.util.ArrayList;

public class TaskCompleted {

    private static ArrayList<TaskCompleted> taskCompletedArrayList = new ArrayList<>();
    private String comID;
    private  String Completed;

    public TaskCompleted(String comID, String completed) {
        this.comID = comID;
        Completed = completed;
    }

    public String getComID() {
        return comID;
    }

    public void setComID(String comID) {
        this.comID = comID;
    }

    public String getCompleted() {
        return Completed;
    }

    public void setCompleted(String completed) {
        Completed = completed;
    }

    public static void initCompleted(){
        taskCompletedArrayList.clear();
        taskCompletedArrayList.add(0,new TaskCompleted("-1", "Mark work"));
        TaskCompleted InComplete = new TaskCompleted("0","InComplete");
        taskCompletedArrayList.add(InComplete);
        TaskCompleted Complete = new TaskCompleted("1","Completed");
        taskCompletedArrayList.add(Complete);
    }

    public static ArrayList<TaskCompleted> getTaskCompletedArrayList() {
        return taskCompletedArrayList;
    }
}
