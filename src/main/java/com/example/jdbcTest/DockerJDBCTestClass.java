package com.example.jdbcTest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import java.sql.*;

@SpringBootApplication
public class DockerJDBCTestClass {
    @Autowired
	private Environment env;

	private static final String TARGET_TABLE = "ACCOUNT_HEADERS";

	public static void main(String[] args) {
		SpringApplication.run(DockerJDBCTestClass.class, args);
	}

	@PostConstruct
	public void init() {
		checkSpecificTable();
	}

	private void checkSpecificTable() {
		Connection connection = null;
		try {
			// Get database properties
			String url = env.getProperty("spring.datasource.url");
			String username = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");

			System.out.println("Connecting to database: " + url);

			// Load Oracle JDBC Driver
			Class.forName("oracle.jdbc.OracleDriver");

			// Establish connection
			connection = DriverManager.getConnection(url, username, password);

			// Check if table exists
			if (!tableExists(connection, TARGET_TABLE)) {
				System.out.println("Table '" + TARGET_TABLE + "' does not exist!");
				return;
			}

			// Get table structure (columns)
			System.out.println("\n=== Table Structure: " + TARGET_TABLE + " ===");
			displayTableStructure(connection, TARGET_TABLE);

			// Get table data (first 10 rows)
			System.out.println("\n=== Sample Data from: " + TARGET_TABLE + " (first 10 rows) ===");
			displayTableData(connection, TARGET_TABLE, 10);

			// Get row count
			System.out.println("\n=== Table Statistics ===");
			int rowCount = getTableRowCount(connection, TARGET_TABLE);
			System.out.println("Total rows in " + TARGET_TABLE + ": " + rowCount);

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
	}

	private boolean tableExists(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet tables = metaData.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"});
		return tables.next();
	}

	private void displayTableStructure(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet columns = metaData.getColumns(null, null, tableName.toUpperCase(), null);

		System.out.printf("%-20s %-15s %-10s %-10s%n", "Column Name", "Data Type", "Size", "Nullable");
		System.out.println("------------------------------------------------------------");

		while (columns.next()) {
			String columnName = columns.getString("COLUMN_NAME");
			String dataType = columns.getString("TYPE_NAME");
			int columnSize = columns.getInt("COLUMN_SIZE");
			String nullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable ? "YES" : "NO";

			System.out.printf("%-20s %-15s %-10d %-10s%n", columnName, dataType, columnSize, nullable);
		}
	}

	private void displayTableData(Connection connection, String tableName, int limit) throws SQLException {
		String query = "SELECT * FROM " + tableName + " WHERE ROWNUM <= ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, limit);
			ResultSet resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			// Print column headers
			for (int i = 1; i <= columnCount; i++) {
				System.out.printf("%-15s", metaData.getColumnName(i));
			}
			System.out.println();

			// Print separator
			for (int i = 1; i <= columnCount; i++) {
				System.out.printf("%-15s", "---------------");
			}
			System.out.println();

			// Print data rows
			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					Object value = resultSet.getObject(i);
					System.out.printf("%-15s", value != null ? value.toString() : "NULL");
				}
				System.out.println();
			}
		}
	}

	private int getTableRowCount(Connection connection, String tableName) {
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error getting row count: " + e.getMessage());
		}
		return -1;
	}

	private void closeConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("\nDatabase connection closed.");
			}
		} catch (SQLException e) {
			System.err.println("Error closing connection: " + e.getMessage());
		}
	}
}
