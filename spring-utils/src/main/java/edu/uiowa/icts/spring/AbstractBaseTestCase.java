package edu.uiowa.icts.spring;


import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBaseTestCase extends TestCase {

	

		  protected final Log log = LogFactory.getLog(getClass().getName());

		  @Override
		  protected void setUp() throws Exception {
		    log.debug("=== starting "+getName()+" =============================");
		  }

		  @Override
		  protected void tearDown() throws Exception {
		    log.debug("=== ending "+getName()+" =============================\n");
		  }

		  public void assertTextPresent(String expected, String value) {
		    if (value == null || !value.contains(expected)) {
		      fail("expected presence of '"+expected+"' but was '"+value+"'");
		    }
		  }
	

}
