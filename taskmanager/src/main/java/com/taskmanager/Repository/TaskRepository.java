package com.taskmanager.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.Model.Task;
//Extend the MongoRepository interface provided by Spring Data MongoDB.
//Pass the data model class (Task) and the type of its primary key as type arguments to MongoRepository.
//This interface will inherit basic CRUD operations (such as save, findById, findAll, deleteById, etc.) from MongoRepository.

public interface TaskRepository extends MongoRepository<Task,Integer> {

}
