package org.exampleR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exampleM.Client;
import org.exampleM.Flight;
import org.exampleM.FlightBooking;
import org.exampleM.utils.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightBookingDBRepository implements IFlightBookingRepository{

    private JdbcUtils dbUtils;

    private static final Logger log= LogManager.getLogger();

    public FlightBookingDBRepository() {
        log.info("Initializing FlightBookingDBRepository");
        dbUtils=new JdbcUtils();
    }

    @Override
    public FlightBooking findOne(Pair<Integer, Integer> key) {
        log.traceEntry(" parameters {} {}",key.first,key.second);
        String query = "select * from flight_bookings where id_client=? and id_flight=?";
        FlightBooking booking = null;

        try(Connection connection = dbUtils.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,key.first);
            statement.setInt(2,key.second);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                int client_id = resultSet.getInt("client_id");
                int flight_id = resultSet.getInt("flight_id");

                Pair<Integer,Integer> id = new Pair<>(client_id,flight_id);

                ClientDBRepository repoClient = new ClientDBRepository();
                Client client = repoClient.findOne(client_id);

                FlightDBRepository repoFlight = new FlightDBRepository();
                Flight flight = repoFlight.findOne(flight_id);

                booking = new FlightBooking(flight, client);
                booking.setID(id);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(booking);
    }

    @Override
    public Iterable<FlightBooking> findAll() {
        log.traceEntry();
        String query = "select * from flight_bookings";
        List<FlightBooking> bookingsList = new ArrayList<>();

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next())
            {
                int client_id = resultSet.getInt("id_client");
                int flight_id = resultSet.getInt("id_flight");

                Pair<Integer,Integer> id = new Pair<>(client_id,flight_id);

                ClientDBRepository repoClient = new ClientDBRepository();
                Client client = repoClient.findOne(client_id);

                FlightDBRepository repoFlight = new FlightDBRepository();
                Flight flight = repoFlight.findOne(flight_id);

                FlightBooking booking = new FlightBooking(flight, client);
                booking.setID(id);

                bookingsList.add(booking);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return log.traceExit(bookingsList);
    }

    @Override
    public void save(FlightBooking entity) {
        log.traceEntry(" parameters {}",entity);
        String query = "insert into flight_bookings(id_client,id_flight) values(?,?)";

        try(Connection connection = dbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1,entity.getID().first);
            statement.setInt(2,entity.getID().second);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        log.traceExit();
    }

    @Override
    public void delete(Pair<Integer, Integer> key) {
        log.traceEntry(" parameters {}",key);
        String query = "delete from flight_bookings where id_client=? and id_flight=?";

        try(Connection connection = dbUtils.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1,key.first);
            statement.setInt(2,key.second);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        log.traceExit();
    }

    @Override
    public void update(FlightBooking entity) {

    }
}
