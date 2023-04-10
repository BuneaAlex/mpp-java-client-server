package org.exampleR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exampleM.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDBRepository implements IClientRepository{

    private JdbcUtils dbUtils;

    private static final Logger log= LogManager.getLogger();

    public ClientDBRepository() {
        log.info("Initializing ClientDBRepository");
        dbUtils=new JdbcUtils();
    }

    @Override
    public List<Client> findClientsByNoOfSeatsReserved(int noOfSeats) {
        return null;
    }

    @Override
    public Client findOne(Integer id) {
        log.traceEntry(" parameters {}",id);
        String query = "select * from clients where id=?";
        Client client = null;

        try(Connection connection = dbUtils.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                int seats_reserved = resultSet.getInt("seats_reserved");
                String tourists_names = resultSet.getString("tourists_names");

                client = new Client(name,tourists_names,address,seats_reserved);
                client.setId(id);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(client);
    }

    @Override
    public Iterable<Client> findAll() {
        log.traceEntry();
        String query = "select * from clients";
        List<Client> clientList = new ArrayList<>();

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                int seats_reserved = resultSet.getInt("seats_reserved");
                String tourists_names = resultSet.getString("tourists_names");

                Client client = new Client(name,tourists_names,address,seats_reserved);
                client.setId(id);

                clientList.add(client);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(clientList);
    }

    @Override
    public void save(Client entity) {
        log.traceEntry(" parameters {}",entity);
        String query = "insert into clients(name,address,seats_reserved,tourists_names) values(?,?,?,?)";

        try(Connection connection = dbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1,entity.getName());
            statement.setString(2,entity.getAddress());
            statement.setInt(3,entity.getNoOfSeatsReserved());
            statement.setString(4,entity.getTouristNames());

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
        log.traceEntry(" parameters {}",id);
        String query = "delete from clients where id=?";

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
    public void update(Client entity) {
        log.traceEntry(" parameters {}",entity);
        String query = "update clients set name=?,address=?,seats_reserved=?,tourists_names=? where id = ?";

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1,entity.getName());
            statement.setString(2,entity.getAddress());
            statement.setInt(3,entity.getNoOfSeatsReserved());
            statement.setString(4,entity.getTouristNames());
            statement.setInt(5,entity.getID());

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        log.traceExit();
    }
}
