package com.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



//PROJECT IDEA:
/*MongoDB stores the data for your task manager application, your Spring Boot application serves as the backend that 
interacts with MongoDB to perform CRUD operations on tasks, and Postman allows you to test and validate your API endpoints
by sending HTTP requests and inspecting the responses.
*/
@SpringBootApplication
public class TaskmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}

}
