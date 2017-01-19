package com.example.teodora.employeetaskmanager.Models;

import java.io.Serializable;

/**
 * Created by viktor on 18.1.17.
 */

public class TaskModel implements Serializable{

    private String taskAssignedBy;
    private String taskAssignedById;
    private String taskAssignee;
    private String taskAssigneeId;
    private String taskDescription;
    private String taskDueDate;
    private String taskLocation;
    private String taskName;
    private String taskPercentage;
    private String taskPriority;
    private String taskProject;

    public TaskModel() {
    }

    public TaskModel(String taskAssignedBy, String taskAssignee, String taskDescription, String taskDueDate, String taskLocation, String taskName, String taskPercentage, String taskPriority, String taskProject) {
        this.taskAssignedBy = taskAssignedBy;
        this.taskAssignee = taskAssignee;
        this.taskDescription = taskDescription;
        this.taskDueDate = taskDueDate;
        this.taskLocation = taskLocation;
        this.taskName = taskName;
        this.taskPercentage = taskPercentage;
        this.taskPriority = taskPriority;
        this.taskProject = taskProject;
    }

    public TaskModel(String taskAssignedBy, String taskProject, String taskPriority, String taskPercentage, String taskDueDate, String taskLocation, String taskName, String taskDescription, String taskAssigneeId, String taskAssignee, String taskAssignedById) {
        this.taskAssignedBy = taskAssignedBy;
        this.taskProject = taskProject;
        this.taskPriority = taskPriority;
        this.taskPercentage = taskPercentage;
        this.taskDueDate = taskDueDate;
        this.taskLocation = taskLocation;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskAssigneeId = taskAssigneeId;
        this.taskAssignee = taskAssignee;
        this.taskAssignedById = taskAssignedById;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskAssignedById() {
        return taskAssignedById;
    }

    public void setTaskAssignedById(String taskAssignedById) {
        this.taskAssignedById = taskAssignedById;
    }

    public String getTaskAssigneeId() {
        return taskAssigneeId;
    }

    public void setTaskAssigneeId(String taskAssigneeId) {
        this.taskAssigneeId = taskAssigneeId;
    }

    public String getTaskAssignedBy() {
        return taskAssignedBy;
    }

    public void setTaskAssignedBy(String taskAssignedBy) {
        this.taskAssignedBy = taskAssignedBy;
    }

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(String taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }

    public String getTaskPercentage() {
        return taskPercentage;
    }

    public void setTaskPercentage(String taskPercentage) {
        this.taskPercentage = taskPercentage;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskProject() {
        return taskProject;
    }

    public void setTaskProject(String taskProject) {
        this.taskProject = taskProject;
    }
}
