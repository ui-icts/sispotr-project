package edu.uiowa.icts.spring;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-application-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
@Transactional
public abstract class AbstractSpringTestCase extends AbstractBaseTestCase {
	
	  @Override
	  protected void setUp() throws Exception {
	    super.setUp();
	  }
	  
	  
	  @Override
	  protected void tearDown() throws Exception {
		  super.tearDown();
		  
	  }
	
	
	

}
