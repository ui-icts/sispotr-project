package edu.uiowa.icts.spring;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	HsqlCreateSchema test = new HsqlCreateSchema();

    	List<String> schemas = new ArrayList<String>();
    	schemas.add("thetest");
    	schemas.add("thetest2");
    	test.setTest(true);
    	test.setUrl("jdbc:hsqldb:file:target/unit_testdb");
    	test.setSchemas(schemas);
    	test.create();
        assertTrue( true );
    }
}
