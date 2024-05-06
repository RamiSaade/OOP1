package com.taskmanager.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;  

import com.taskmanager.Model.Task;
import com.taskmanager.Repository.TaskRepository;

import jakarta.validation.Valid;


/*Controller classes (e.g., TaskController) that handle incoming HTTP requests and map
them to appropriate methods for CRUD operations on tasks*/
@RestController
public class TaskController {

   @Autowired
TaskRepository taskrepo;  //since TaskRepositiory extends MongoRepository,and we need to use the functions of mongo


   @PostMapping("/addTask")                //When a POST request is made to "/addTask", this method is invoked
public ResponseEntity<String> addTask(@Valid @RequestBody Task task, BindingResult bindingResult)
{
   if(bindingResult.hasErrors())
   {
StringBuilder errorMessage= new StringBuilder();
bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
return ResponseEntity.badRequest().body(errorMessage.toString());
   }
taskrepo.save(task);
return ResponseEntity.status(HttpStatus.CREATED).body("\nTask created successfully!");
}
/*This method returns a ResponseEntity object, which represents the entire HTTP response,
 including headers, status code, and body. The response body is of type String*/

//BindingResult bindingResult:captures any validation errors that occur during the validation process.
/* StringBuilder errorMessage = new StringBuilder(): to construct error messages if validation fails bad request response*/ 


@PutMapping("/updateTask")               
public ResponseEntity<String> updateTask(@Valid @RequestBody Task task)
{
   //  1 retrieve the task using id
Task existingTask=taskrepo.findById(task.getId()).orElse(null);

if(existingTask!=null)  //2 check if null
{
existingTask.setTitle(task.getTitle());
existingTask.setDescription(task.getDescription());
existingTask.setDueDate(task.getDueDate());
existingTask.setStatus(task.getStatus());

taskrepo.save(existingTask);
return ResponseEntity.status(HttpStatus.OK).body("\nTask updated successfully!");
}
else{
return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\n The id you are looking for does not exist");
}
}


@Autowired
private MongoTemplate mongoTemplate;

@GetMapping("/retrieveTask")    // it is optional to add the page nb nd limit eg: http://localhost:8081/retrieveTask?page=2&limit=3
public ResponseEntity<?> retrieveTask(@RequestParam(defaultValue = "1") int page,    //def page number
                                    @RequestParam(defaultValue = "20") int limit,   //def tasks limit number
                                    @RequestParam(defaultValue = "id") String sortBy, //def sort by id
                                    @RequestParam(defaultValue = "asc") String sortOrder,   //def ascending oder
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) String description,    // false= not required
                                    @RequestParam(required = false) String status,
                                    @RequestParam(required = false) String dueDate) {

// Create Criteria object for filtering and searching
    Criteria criteria = new Criteria();

       // Add filters for each filterable field
       if (title != null) {
         criteria.and("title").is(title);
     }
     if (description != null) {
         criteria.and("description").is(description);
     }
     if (status != null) {
         criteria.and("status").is(status);
     }
     if (dueDate != null) {
         criteria.and("dueDate").is(dueDate);
     }

     Query query = new Query(criteria);

// Define sorting direction
Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

// Create Sort object based on sortBy and sortOrder parameters
Sort sort = Sort.by(direction, sortBy);

 // Apply pagination and sorting to the query
query.with(PageRequest.of(page - 1, limit, sort));

// Fetch data based on the query
List<Task> tasks = mongoTemplate.find(query, Task.class);

// Total count of tasks
long totalCount = mongoTemplate.count(query, Task.class);

    // response using hashmap
    Map<String, Object> response = new HashMap<>();
    response.put("page", page);
    response.put("total_pages", (int) Math.ceil((double) totalCount / limit)); //calculates total pages
    response.put("total_items", totalCount);
    response.put("data", tasks);
    
    return ResponseEntity.ok(response);
}




@GetMapping("/retrieveTaskById/{id}")               // to return based on id
public ResponseEntity<String> getTaskById(@PathVariable Integer id)
{
Task task =taskrepo.findById(id).orElse(null);
if(task!=null)
{
String taskAsString = "ID: " + task.getId() + ", Title: " + task.getTitle() + ", Description: " + task.getDescription()+
  ", duedate: " + task.getDueDate()+", Status: " + task.getStatus();
return ResponseEntity.status(HttpStatus.OK).body(taskAsString);
}
else{
   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\nThe id you are looking for does not exist");
}
}



@DeleteMapping("/deleteTask/{id}")                //delete a task
public ResponseEntity<String> deleteTask(@PathVariable Integer id)
{
   if(taskrepo.existsById(id)) {
      taskrepo.deleteById(id);
      return ResponseEntity.ok("\nTask deleted successfully!");
}
else{
   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("\nTask not found with this id");
}
}


}
