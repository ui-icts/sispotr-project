package edu.uiowa.icts.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;

import edu.uiowa.icts.service.PropertyLoader;

public abstract class BaseIctsTestCase extends DBTestCase {

	protected final Log log = LogFactory.getLog(getClass().getName());

	protected Properties properties;

	private String dataset = null;
	private boolean useAltData = false;
	private boolean buildSchema = false;
	private String schemaSqlFile = null;
	
	public BaseIctsTestCase(){
		log.debug("setting database properties");

		properties = PropertyLoader.loadProperties("test.properties");

		System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.hsqldb.jdbcDriver" );
		System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:hsqldb:mem:testdb" );
		System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "sa" );
		System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "" );
	}

	@Before
	public void setUp() throws Exception {
		log.debug("=== starting "+getName()+" =============================");
		
		if(isBuildSchema()){
			File f = new File(getSchemaSqlFile());
			
			Class.forName(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS));
			
			Properties props = new Properties();
	        props.setProperty("user", System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME));
	        props.setProperty("password", System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD));

	        Connection conn = DriverManager.getConnection(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL), props);
	        
	        assertNotNull(conn);
	        
	        StringWriter output = new StringWriter();
	        FileInputStream fis = new FileInputStream(f);
	        IOUtils.copy(fis, output);
	        
	        Statement stmt = conn.createStatement();
	        stmt.execute("SET DATABASE SQL SYNTAX PGS TRUE");
	        
	        stmt = conn.createStatement();
	        stmt.executeUpdate(output.toString());
	        stmt.close();	
		}
		
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		log.debug("=== ending "+getName()+" =============================\n");
		super.tearDown();
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		
		log.debug("setting data set");

		String file_name = getDatasetName(); 
		if(!isUseAltData()){
			file_name = properties.getProperty("dataset.file.name", "dataset.xml");
		}

		log.debug("loading "+file_name+" as data set");

		InputStream in = this.getClass().getClassLoader().getResourceAsStream(file_name);
		FlatXmlDataSetBuilder fxdsb = new FlatXmlDataSetBuilder();
		fxdsb.setCaseSensitiveTableNames(false);
		fxdsb.setColumnSensing(true);
		return fxdsb.build(in);
	}

	protected void setUpDatabaseConfig(DatabaseConfig config) {
		log.debug("setting database config");
		config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
		config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false); 
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
		setUseAltData(true);
	}

	public String getDatasetName() {
		if(dataset == null){
			setDataset("dataset.xml");
		}
		return dataset;
	}

	public void setUseAltData(boolean useAltData) {
		this.useAltData = useAltData;
	}

	public boolean isUseAltData() {
		return useAltData;
	}

	public boolean isBuildSchema() {
		return buildSchema;
	}

	public void setBuildSchema(boolean buildSchema) {
		this.buildSchema = buildSchema;
	}

	public String getSchemaSqlFile() {
		return schemaSqlFile;
	}

	public void setSchemaSqlFile(String schemaSqlFile) {
		this.schemaSqlFile = schemaSqlFile;
	}


}
