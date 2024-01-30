package peaksoft.dao.impl;

import peaksoft.config.JdbcConfig;
import peaksoft.dao.JobDao;
import peaksoft.models.Job;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class JobDaoImpl implements JobDao {
    private final Connection connection = JdbcConfig.getConnection();
    @Override
    public void createJobTable() {
        try {
            String query = """
                    create table if not exists jobs(
                    id serial primary key,
                    position varchar,
                    profession varchar,
                    description varchar,
                    experience int
                    );
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJob(Job job) {
        try {
            String query = """
                    insert into jobs(position, profession, description, experience)
                    values (?, ?, ?, ?);
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, job.getPosition());
                preparedStatement.setString(2, job.getProfession());
                preparedStatement.setString(3, job.getDescription());
                preparedStatement.setInt(4, job.getExperience());
                int executed = preparedStatement.executeUpdate();
                System.out.println(executed != 0 ?  "Error":"Successfully saved"+executed);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Job getJobById(Long jobId) {
        try {
            String query = "select * from jobs where id = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setLong(1, jobId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    Job job = new Job();
                    job.setId(resultSet.getLong("id"));
                    job.setPosition(resultSet.getString("position"));
                    job.setProfession(resultSet.getString("profession"));
                    job.setDescription(resultSet.getString("description"));
                    job.setExperience(resultSet.getInt("experience"));
                    return job;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        List<Job> jobs = new ArrayList<>();
        String query = null;
                if (ascOrDesc.equalsIgnoreCase("asc")) {
                   query = "select * from jobs order by jobs.experience";
                } else if (ascOrDesc.equalsIgnoreCase("desc")) {
                    query = "select * from jobs order by jobs.experience desc";}
                else System.out.println("incorrect choise");
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Job job = new Job();
                        job.setId(resultSet.getLong("id"));
                        job.setPosition(resultSet.getString("position"));
                        job.setProfession(resultSet.getString("profession"));
                        job.setDescription(resultSet.getString("description"));
                        job.setExperience(resultSet.getInt("experience"));
                        jobs.add(job);
                    }
                } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        return jobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        Job job = new Job();
        try {
            String query = "select j.* from jobs j inner join employees e on e.job_id = j.id where  e.id  = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setLong(1, employeeId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        job.setId(resultSet.getLong("id"));
                        job.setPosition(resultSet.getString("position"));
                        job.setProfession(resultSet.getString("profession"));
                        job.setDescription(resultSet.getString("description"));
                        job.setExperience(resultSet.getInt("experience"));
                    }
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return job;
    }

    @Override
    public void deleteDescriptionColumn() {
        try {
            String query = "alter table jobs drop column description";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                int executed = preparedStatement.executeUpdate();
                System.out.println(executed != 0 ? "Error":"Success dropped column"+executed);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
