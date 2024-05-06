package com.taskmanager.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Document(collection = "tasks")

public class Task {

 @Id    //The @Id annotation on the id field tells Spring Data MongoDB that the id attribue is unique 
 @NotNull(message = "id is required")   //it ensures that the object reference itself is not null.
 @Positive(message = "id must be a positive integer")
 private Integer id;
 

    @NotBlank(message = "Title is required")  
     /*Checks if the annotated string is not null 
     and if the trimmed length of the annotated string is greater than zero */ 
    private String title;

@NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "duedate is required")
    @Pattern(regexp = "^202[4-9]-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "Due date must be in yyyy-MM-dd format and in the future")
    // @Patern:used for validating the format of string fields.
    // regexp:defines the regular expression pattern to be matched.
    // "^":start  /    "$":end
    private String dueDate;

    @NotBlank(message = "status is required")
    private String status;


    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getDueDate() {
        return dueDate;
    }
    public String getStatus() {
        return status;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public void setStatus(String status) {
        this.status = status;
    }



}

