package peaksoft.dao.impl;


import peaksoft.config.JdbcConfig;
import peaksoft.dao.EmployeeDao;
import peaksoft.models.Employee;
import peaksoft.models.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    private final Connection connection = JdbcConfig.getConnection();

    @Override
    public void createEmployee() {
        try {
            String query = """
                    create table if not exists employees(
                    id serial primary key,
                    first_name varchar,
                    last_name varchar,
                    age int,
                    email varchar,
                    job_id int references jobs(id)
                    );
                    """;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void addEmployee(Employee employee) {
        try {
            String query = """
                    insert into employees(first_name, last_name, age, email,job_id)
                    values (?, ?, ?, ?, ?);
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setInt(3, employee.getAge());
                preparedStatement.setString(4, employee.getEmail());
                preparedStatement.setLong(5,employee.getJobId());
                int executed = preparedStatement.executeUpdate();
                System.out.println(executed != 0 ?  "Error":"Successfully saved!" + executed);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        try {
            String query = "drop table if  exists employees";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                int executed = preparedStatement.executeUpdate();
                System.out.println(executed != 0 ?"Successfully dropped!":"Error" );
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cleanTable() {
        try {
            String query = "truncate table employees";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        try {
            String query = """
                    update employees set first_name = ?,
                    last_name = ?,
                    age = ?,
                    email = ?,
                    job_id = ? where id = ? ;""";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setInt(3, employee.getAge());
                preparedStatement.setString(4, employee.getEmail());
                preparedStatement.setLong(5,employee.getJobId());
                preparedStatement.setLong(6, id);

                int executed = preparedStatement.executeUpdate();
                System.out.println(executed != 0 ?"Successfully updated!":"Error");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try {
            String query = "select * from employees";
            try( PreparedStatement preparedStatement = connection.prepareStatement(query)){
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Employee employee = new Employee();
                    employee.setId(resultSet.getLong("id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setAge(resultSet.getInt("age"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setJobId(resultSet.getLong("job_id"));
                    employees.add(employee);
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employees;
    }
    @Override
    public Employee findByEmail(String email) {
        try {
            String query = "select * from employees where email = ? ";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    Employee employee = new Employee();
                    employee.setId(resultSet.getLong("id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setAge(resultSet.getInt("age"));
                    employee.setEmail(resultSet.getString("email"));
                    return employee;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee, Job> map = new HashMap<>();
        try {
            String query = """
                    select e.*, j.* from employees e join jobs j  on e.job_id = j.id 
                     where e.id = ?; """;
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setLong(1, employeeId);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while (resultSet.next()){
                        Job job = new Job();
                        job.setId(resultSet.getLong("id"));
                        job.setPosition(resultSet.getString("position"));
                        job.setProfession(resultSet.getString("profession"));
                        job.setDescription(resultSet.getString("description"));
                        job.setExperience(resultSet.getInt("experience"));
                        Employee employee = new Employee();
                        employee.setId(resultSet.getLong("id"));
                        employee.setFirstName(resultSet.getString("first_name"));
                        employee.setLastName(resultSet.getString("last_name"));
                        employee.setAge(resultSet.getInt("age"));
                        employee.setEmail(resultSet.getString("email"));
                        employee.setJobId(resultSet.getLong("job_id"));

                        map.put(employee, job);
                    }
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return map;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        try {
            String query = "select * from employees where job_id in (select id from jobs" +
                    " where position = ?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1, position);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Employee employee = new Employee();
                    employee.setId(resultSet.getLong("id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setAge(resultSet.getInt("age"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setJobId(resultSet.getLong("job_id"));
                    employees.add(employee);
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employees;
    }
}