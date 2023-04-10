package org.exampleR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exampleM.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightDBRepository implements IFlightRepository{

    private JdbcUtils dbUtils;

    private static final Logger log= LogManager.getLogger();

    public FlightDBRepository() {
        log.info("Initializing FlightDBRepository");
        dbUtils=new JdbcUtils();
    }

    @Override
    public List<Flight> findFlightByDestinationAndDatetime(String destination, LocalDateTime date) {
        log.traceEntry(" parameters {} {}",destination, date);
        String query = "select * from flights where destination=? and departure_date > ? and departure_date < ?";
        List<Flight> flightList = new ArrayList<>();

        try(Connection connection = dbUtils.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,destination);
            statement.setTimestamp(2, Timestamp.valueOf(date));
            LocalDateTime nextDay = date.plusDays(1);
            statement.setTimestamp(3, Timestamp.valueOf(nextDay));
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String airport = resultSet.getString("airport");
                LocalDateTime dateTime = resultSet.getTimestamp("departure_date").toLocalDateTime();
                int seats_left = resultSet.getInt("seats_left");

                Flight flight = new Flight(destination,dateTime,airport,seats_left);
                flight.setId(id);

                flightList.add(flight);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(flightList);
    }

    @Override
    public List<Flight> findAvailableFlights() {
        log.traceEntry();
        String query = "select * from flights where seats_left > 0";
        List<Flight> flightList = new ArrayList<>();

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String destination = resultSet.getString("destination");
                LocalDateTime departure_date = resultSet.getTimestamp("departure_date").toLocalDateTime();
                String airport = resultSet.getString("airport");
                int seats_left = resultSet.getInt("seats_left");

                Flight flight = new Flight(destination,departure_date,airport,seats_left);
                flight.setId(id);

                flightList.add(flight);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(flightList);
    }

    @Override
    public Flight findOne(Integer id) {
        log.traceEntry(" parameters {}",id);
        String query = "select * from flights where id=?";
        Flight flight = null;

        try(Connection connection = dbUtils.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                String destination = resultSet.getString("destination");
                LocalDateTime departure_date = resultSet.getTimestamp("departure_date").toLocalDateTime();
                String airport = resultSet.getString("airport");
                int seats_left = resultSet.getInt("seats_left");

                flight = new Flight(destination,departure_date,airport,seats_left);
                flight.setId(id);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(flight);
    }

    @Override
    public Iterable<Flight> findAll() {
        log.traceEntry();
        String query = "select * from flights";
        List<Flight> flightList = new ArrayList<>();

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String destination = resultSet.getString("destination");
                LocalDateTime departure_date = resultSet.getTimestamp("departure_date").toLocalDateTime();
                String airport = resultSet.getString("airport");
                int seats_left = resultSet.getInt("seats_left");

                Flight flight = new Flight(destination,departure_date,airport,seats_left);
                flight.setId(id);

                flightList.add(flight);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(flightList);
    }

    @Override
    public void save(Flight entity) {
        log.traceEntry(" parameters {}",entity);
        String query = "insert into flights(destination,departure_date,airport,seats_left) values(?,?,?,?)";

        try(Connection connection = dbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1,entity.getDestination());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3,entity.getAirportName());
            statement.setInt(4,entity.getNoOfSeatsLeft());

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
        String query = "delete from flights where id=?";

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
    public void update(Flight entity) {
        log.traceEntry(" parameters {}",entity);
        String query = "update flights set destination=?,departure_date=?,airport=?,seats_left=? where id = ?";

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1,entity.getDestination());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3,entity.getAirportName());
            statement.setInt(4,entity.getNoOfSeatsLeft());
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
