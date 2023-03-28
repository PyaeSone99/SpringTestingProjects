package com.example.sampleapi.controller;

import com.example.sampleapi.Dao.EmployeeDao;
import com.example.sampleapi.entity.Employee;
import com.example.sampleapi.exception.EntityNotFoundResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final EmployeeDao employeeDao;

    public ApiController(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @GetMapping("/employees")
    public List<EmployeeResponse> listAllEmployee(){
        return employeeDao.findAll().stream().map(e -> { return new EmployeeResponse(
            e.getId(),e.getFirstName(),e.getLastName(),e.getEmail(),e.getSalary()
        );
        }).collect(Collectors.toList());
    }

//    curl "http://localhost:8080/api/employees/employee?id=1"

    record EmployeeResponse(int id, @JsonProperty("first_name") String firstName,@JsonProperty("last_name") String lastName,
                            String email, double salary){}
    @GetMapping("/employees/employee")
    public EmployeeResponse getEmployeeById(@RequestParam("id")int id){
        Employee employee = employeeDao.findById(id).orElseThrow(EntityNotFoundResponse::new);
        return new EmployeeResponse(
                employee.getId(),employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getSalary()
        );
    }

    @GetMapping("/creation")
    public String init(){
        List.of(
                new Employee("Dean","Winchester","deanImpala@gmail.com",20000.00),
                new Employee("Sam","Winchester","Samwinchester@gmail.com",15000.00),
                new Employee("Marry","Winchester","marryFagt@gmail.com",25000.00)
        )
                .forEach(employeeDao::save);
        return "Successfully";
    }

    record EmployeeRequest(@JsonProperty("first_name") String firstName,@JsonProperty("last_name") String lastName,
                           String email,double salary){}
//    -X POST -H "Content-Type: Application/json" -d '{"first_name":"pyaesone","last_name":"aung","email":"pyaesone@gmail.com","salary":20000}' http://localhost:8080/api/employees
//
    @PostMapping(value = "/employees",consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest request){
        Employee employee = employeeDao.save(
                new Employee(request.firstName,request.lastName,request.email,request.salary)
        );
        return new EmployeeResponse(
                employee.getId(),employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getSalary()
        );
    }
}
