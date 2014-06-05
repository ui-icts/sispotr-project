package edu.uiowa.icts.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.util.search.SearchException;

public class DatasetBuilder {

	private static Connection jdbcConnection;
	private static IDatabaseConnection connection;
	
	private static final Log log = LogFactory.getLog(DatasetBuilder.class);
	
	public DatasetBuilder(Properties properties) throws ClassNotFoundException, SQLException, DatabaseUnitException{
		String schema = properties.getProperty("test.db.schema");
		String url = properties.getProperty("test.db.url");
		String user = properties.getProperty("test.db.username");
		String pwrd = properties.getProperty("test.db.password");
		String driver_class = properties.getProperty("test.db.driver.class", "org.postgresql.Driver");
		
		Class.forName(driver_class);
		jdbcConnection = DriverManager.getConnection(url, user, pwrd);

		connection = new DatabaseConnection(jdbcConnection, schema);
		connection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
	}
	
	public DatasetBuilder(String schema, Properties properties) throws ClassNotFoundException, SQLException, DatabaseUnitException{
		String url = properties.getProperty("test.db.url");
		String user = properties.getProperty("test.db.username");
		String pwrd = properties.getProperty("test.db.password");
		String driver_class = properties.getProperty("test.db.driver.class", "org.postgresql.Driver");
		
		Class.forName(driver_class);
		jdbcConnection = DriverManager.getConnection(url, user, pwrd);

		connection = new DatabaseConnection(jdbcConnection, schema);
		connection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
	}
	
	public void buildPartial(String output_directory, String file_name, QueryDataSet partialDataSet) throws DataSetException, FileNotFoundException, IOException{
      	FlatXmlDataSet.write(partialDataSet, new FileOutputStream(output_directory+"/"+file_name));
	}
	
	public void buildAll(String output_directory, String file_name) throws SQLException, DataSetException, FileNotFoundException, IOException{
		IDataSet fullDataSet = connection.createDataSet();
		FlatXmlDataSet.write(fullDataSet, new FileOutputStream(output_directory+"/"+file_name));
	}
	
	public void buildByDependency(String table, String file_name, String output_directory) throws SearchException, DataSetException, SQLException, FileNotFoundException, IOException{
        String[] depTableNames = TablesDependencyHelper.getAllDependentTables(connection, table);
        IDataSet depDataset = connection.createDataSet(depTableNames);
        FlatXmlDataSet.write(depDataset, new FileOutputStream(output_directory+"/"+file_name)); 
	}
	
	public void buildByDependency(String[] tables, String file_name, String output_directory) throws SearchException, DataSetException, SQLException, FileNotFoundException, IOException{
        String[] depTableNames = TablesDependencyHelper.getAllDependentTables(connection, tables);
        IDataSet depDataset = connection.createDataSet(depTableNames);
        FlatXmlDataSet.write(depDataset, new FileOutputStream(output_directory+"/"+file_name)); 
	}
	
	public IDatabaseConnection getConnection(){
		return connection;
	}
	
	public void closeConnection(){
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("SQL exception thrown while closing IDatabaseConnection connection", e);
			}
		}
		
		if(jdbcConnection != null){
			try {
				jdbcConnection.close();
			} catch (SQLException e) {
				log.error("SQL exception thrown while closing DatabaseConnection jdbcConnection", e);
			}
		}
		connection = null;
		jdbcConnection = null;
	}
}
