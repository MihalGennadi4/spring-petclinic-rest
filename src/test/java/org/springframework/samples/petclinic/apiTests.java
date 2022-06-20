package org.springframework.samples.petclinic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class apiTests {
	private static Connection connection;
	private Integer ownerId;
	private Integer visitId;
	private PreparedStatement sql;
	private ResultSet queryResult;

	private static final Integer VISIT_PET_ID = 1;
	private static final Date VISIT_DATE = new Date(2302715081L); //1970
	private static final String VISIT_DESC = "resurrects";

	private static final String OWNER_FIRST_NAME = "John";
	private static final String OWNER_SECOND_NAME = "Cena";
	private static final String OWNER_ADDRESS = "1241, East Main Street";
	private static final String OWNER_CITY = "Stamford";
	private static final String OWNER_TELEPHONE = "6085551023";

	@BeforeAll
	public static void connect() throws SQLException {
		connection = DriverManager.getConnection(
			"jdbc:postgresql://localhost/petclinic",
			"petclinic",
			"petclinic"
		);
	}

	@AfterAll
	public static void disconnect() throws SQLException {
		connection.close();
	}

	@Test
	void ownerCreate() throws SQLException {
		sql = connection.prepareStatement(
			"INSERT INTO owners(first_name, last_name, address, city, telephone) VALUES(?,?,?,?,?)",
			Statement.RETURN_GENERATED_KEYS
		);
		sql.setString(1, OWNER_FIRST_NAME);
		sql.setString(2, OWNER_SECOND_NAME);
		sql.setString(3, OWNER_ADDRESS);
		sql.setString(4, OWNER_CITY);
		sql.setString(5, OWNER_TELEPHONE);

		sql.executeUpdate();
		queryResult = sql.getGeneratedKeys();
		queryResult.next();
		ownerId = queryResult.getInt("id");
	}

	@Test
	void ownerDelete() throws SQLException {
		PreparedStatement sql = connection.prepareStatement(
			"DELETE FROM owners WHERE id = ?"
		);
		sql.setInt(1, ownerId);
		sql.executeUpdate();
	}

	@Test
	void ownerFindQuery() throws SQLException {
		sql = connection.prepareStatement("SELECT * FROM owners WHERE id = ?");
		sql.setInt(1, ownerId);
		queryResult = sql.executeQuery();
	}

	@Test
	void visitCreate() throws SQLException {
		sql = connection.prepareStatement(
			"INSERT INTO visits(pet_id, visit_date, description) VALUES(?,?,?)",
			Statement.RETURN_GENERATED_KEYS
		);
		sql.setInt(1, VISIT_PET_ID);
		sql.setDate(2, VISIT_DATE);
		sql.setString(3, VISIT_DESC);

		sql.executeUpdate();
		queryResult = sql.getGeneratedKeys();
		queryResult.next();
		visitId = queryResult.getInt("id");
	}

	@Test
	void visitDelete() throws SQLException {
		PreparedStatement sql = connection.prepareStatement(
			"DELETE FROM visits WHERE id = ?"
		);
		sql.setInt(1, visitId);
		sql.executeUpdate();
	}

	@Test
	void visitQuery() throws SQLException {
		sql = connection.prepareStatement("SELECT * FROM visits WHERE id = ?");
		sql.setInt(1, visitId);
		queryResult = sql.executeQuery();
	}
}
