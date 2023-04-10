package org.exampleR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exampleM.Agent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgentDBRepository implements IAgentRepository{

    private JdbcUtils dbUtils;

    private static final Logger log= LogManager.getLogger();

    public AgentDBRepository() {
        log.info("Initializing AgentDBRepository");
        dbUtils=new JdbcUtils();
    }

    @Override
    public Agent findAgentByLoginInformation(String agencyName, String userName, String password) {
        log.traceEntry(" parameters {} {} {}",agencyName,userName,password);
        String query = "select * from agents where user_name=? and agency_name=? and password=?";
        Agent agent = null;

        try(Connection connection = dbUtils.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,userName);
            statement.setString(2,agencyName);
            statement.setString(3,password);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String user_name = resultSet.getString("user_name");
                String agency_name = resultSet.getString("agency_name");
                String password2 = resultSet.getString("password");

                agent = new Agent(user_name,agency_name,password2);
                agent.setId(id);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(agent);
    }

    @Override
    public Agent findOne(Integer id) {
        log.traceEntry(" parameters {} ",id);
        String query = "select * from agents where id=?";
        Agent agent = null;

        try(Connection connection = dbUtils.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                String user_name = resultSet.getString("user_name");
                String agency_name = resultSet.getString("agency_name");
                String password = resultSet.getString("password");

                agent = new Agent(user_name,agency_name,password);
                agent.setId(id);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(agent);
    }

    @Override
    public Iterable<Agent> findAll() {
        log.traceEntry();
        String query = "select * from agents";
        List<Agent> agentList = new ArrayList<>();

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String user_name = resultSet.getString("user_name");
                String agency_name = resultSet.getString("agency_name");
                String password = resultSet.getString("password");

                Agent agent = new Agent(user_name,agency_name,password);
                agent.setId(id);

                agentList.add(agent);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(agentList);
    }

    @Override
    public void save(Agent entity) {
        log.traceEntry(" parameters {} ",entity);
        String query = "insert into agents(user_name,agency_name,password) values(?,?,?)";

        try(Connection connection = dbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1,entity.getUserName());
            statement.setString(2,entity.getAgencyName());
            statement.setString(3,entity.getPassword());

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        log.traceExit();
    }

    @Override
    public void delete(Integer id) {
        log.traceEntry(" parameters {} ",id);
        String query = "delete from agents where id=?";

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1,id);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        log.traceExit();
    }

    @Override
    public void update(Agent entity) {
        log.traceEntry(" parameters {} ",entity);
        String query = "update agent set user_name=?,agency_name=?,password=? where id = ?";

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1,entity.getUserName());
            statement.setString(2,entity.getAgencyName());
            statement.setString(3,entity.getPassword());
            statement.setInt(4,entity.getID());

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        log.traceExit();
    }
}
