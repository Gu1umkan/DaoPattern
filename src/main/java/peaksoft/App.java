package peaksoft;

import peaksoft.config.JdbcConfig;
import peaksoft.models.Employee;
import peaksoft.models.Job;
import peaksoft.service.EmployeeService;
import peaksoft.service.JobService;
import peaksoft.service.impl.EmployeeServiceImpl;
import peaksoft.service.impl.JobServiceImpl;

import java.sql.Connection;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {


        EmployeeService employeeService = new EmployeeServiceImpl();
        JobService jobService = new JobServiceImpl();
//        employeeService.createEmployee();
//        jobService.createJobTable();
//        jobService.addJob(new Job("Instructor", "IT", "backend", 10 ));
//        employeeService.addEmployee(new Employee("Gulumkan","Uson kyzy",21,"guli@gmail.com",1L));
//        employeeService.cleanTable();
//        employeeService.dropTable();
//        System.out.println(employeeService.getEmployeeById(2L));
//        System.out.println(employeeService.getAllEmployees());
//        System.out.println(jobService.getJobByEmployeeId(1L));
//        System.out.println(jobService.getJobById(1L));
//        System.out.println(employeeService.getAllEmployees());
//        System.out.println(employeeService.getEmployeeByPosition("Instructor"));
//        System.out.println(jobService.getJobById(2L));
//        System.out.println(jobService.sortByExperience("asc"));
//        System.out.println(jobService.sortByExperience("desc"));
//
       // employeeService.updateEmployee(1L, new Employee("ZARA","Akmat",22,"z@gmail.com",2L));

//        System.out.println(employeeService.getAllEmployees());
//
      //  System.out.println(employeeService.findByEmail("z@gmail.com"));
    }
}
