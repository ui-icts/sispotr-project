package edu.uiowa.icts.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
@Transactional
public abstract class IctsTestCase extends BaseIctsTestCase {
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
