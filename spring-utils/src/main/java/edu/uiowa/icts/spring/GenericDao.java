package edu.uiowa.icts.spring;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.uiowa.icts.spring.SessionFactoryUtil;



@Transactional
public class GenericDao<Type> implements GenericDaoInterface<Type> {

	private static final Log log =LogFactory.getLog(GenericDao.class);

	protected SessionFactory sessionFactory;
	
	
	public void setSessionFactory(boolean usesf)
	{
			if(usesf)
		sessionFactory = SessionFactoryUtil.getInstance();
	}
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	private String domainName;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}


	@Transactional
	public void  saveOrUpdate(Type obj)
	{	
		log.debug("Saving/Updating Type:"+ obj.toString());
		getSession().saveOrUpdate(obj);
		
	}
	
	@Transactional
	public void  save(Type obj)
	{	
		log.debug("Saving Type:"+ obj.toString());
		getSession().save(obj);
		
	}
	@Transactional
	public void  justSave(Type obj)
	{	
		log.debug("Saving Type:"+ obj.toString());
		getSession().save(obj);
		
	}
	
	
	@Transactional
	public void  persist(Type obj)
	{	
		log.debug("persisting "+ obj.toString());
		getSession().persist(obj);
		
	}
	
	@Transactional
	public void  merge(Type obj)
	{	
		log.debug("Merging Type:"+ obj.toString());
		getSession().merge(obj);
		
	}
	
	
	@Transactional
	public void  flush()
	{	
		log.debug("Flushing");
		getSession().flush();
		
	}
	
	@Transactional
	public void  refresh(Object obj)
	{	
		log.debug("refreshing");
		getSession().refresh(obj);
		
	}
	
	@Transactional
	public long count()
	{	
		long val = (Long)getSession().createQuery("select count(*) from "+domainName).uniqueResult();
		return val;
		
	}
	
	@Transactional
	public void  delete(int id)
	{	
		
		
		Type obj = (Type)getSession().get(domainName, id);
		getSession().delete(obj);
		
	}
	
	
	@Transactional
	public void  delete(long id)
	{	
	
		Type obj = (Type)getSession().get(domainName, id);
		getSession().delete(obj);
		
	}
	public void  delete(Type obj)
	{	
		getSession().delete(obj);
		
	}
	
	public String getDomainName() {
		return domainName;
	}


	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Type findByCriteria(String s)
	{
		
		return (Type)this.sessionFactory.getCurrentSession().createQuery("from " + getDomainName() + " where "+s).uniqueResult();

		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Type findByQuery(String s)
	{
		
		return (Type)this.sessionFactory.getCurrentSession().createQuery(s).uniqueResult();

		
	}
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Type> listByQuery(String s)
	{
		
		
		return this.sessionFactory.getCurrentSession().createQuery(s).list();

		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Type>  list()
	{
	
		List<Type> list = this.sessionFactory.getCurrentSession().createQuery("from " + getDomainName()).list();

		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Type>  listOrdered(String order, String direction)
	{
		
		List<Type> list = this.sessionFactory.getCurrentSession().createQuery("from " + getDomainName() + " order by " + order + " " + direction).list();

		return list;
	}
	

	
	
	@Transactional
	public List<Type[]> query(String st) {
		return this.sessionFactory.getCurrentSession().createQuery(st).list();
		
	}
	
	@Transactional
	public Integer  maxOf(String s)
	{
		
		return (Integer) this.sessionFactory.getCurrentSession().createQuery("select max("+s+") from " + getDomainName() + " ").uniqueResult();

	
	}
	

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]>  listSearchPaged(
			int numResults, 
			int firstResult,  
			String searchst, List<String[]> orderData, 
			List<String> colNames, 
			List<String> searchCols,
			List<String> colType
			)
	{
		searchst = "%"+searchst+"%";
		String alias ="i";
		String prefix =alias+".";
		
		
		/*
		 * Build the WHERE clause
		 * 
		 */
		StringBuffer where = new StringBuffer();		
		int count = 0;
		for(String att:searchCols)
		{
			String type = colType.get(count);
			if("string".equalsIgnoreCase(type))
				where.append(prefix+att+ "  LIKE ? ");
			else 
				where.append("str("+prefix+att+ ")  LIKE ? ");
				
				
			if(count<searchCols.size()-1)
				where.append(" OR ");
				
			count++;
		}
		
		
		/**
		 * Build the SELECT clause
		 * 
		 */
		StringBuffer select = new StringBuffer();
		 count = 0;
		for(String col: colNames)
		{
			select.append(" "+prefix+col+" ");
			
			if(count<colNames.size()-1)
				select.append(", ");
			
			count++;
		}
		
		/**
		 * Build the ORDER BY clause
		 * 
		 */
		StringBuffer orderby = new StringBuffer();
		count = 0;
		for(String[] order: orderData)
		{
			if(order.length<2)
				continue;
			
			String col = order[0];
			String dir = order[1];
			if("desc".equalsIgnoreCase(dir))
				orderby.append(" "+prefix+col+" DESC" );
			else
				orderby.append(" "+prefix+col+" ASC" );
			
			if(count<orderData.size()-1)
				orderby.append(", ");
			count++;
			
		}
		
		
		/**
		 * Create Query
		 * 
		 * 
		 */
		String qstring = "SELECT "+select+" FROM " + getDomainName() + " as "+alias+" WHERE " + where+ " ORDER BY "+orderby +"";
		log.debug("FULL QUERY:"+qstring);
		Query q = this.sessionFactory.getCurrentSession().createQuery(qstring);
		q.setMaxResults(numResults);
		q.setFirstResult(firstResult);
		
		
		/**
		 * Add search parameters
		 * 
		 */
		count = 0;
		for(String att:searchCols)
		{
			//String type = colType.get(count);
			q.setString(count, searchst);

			
			count++;
		}	
		
		List<Object[]> list = q.list();
		log.debug("result Size:"+list.size());

		return list;
	}

	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Long  countSearch(
			String searchst,
			List<String> searchCols,
			List<String> colType
			)
	{
		searchst = "%"+searchst+"%";
		String alias ="i";
		String prefix =alias+".";
		
		
		StringBuffer where = new StringBuffer();		
		int count = 0;
		for(String att:searchCols)
		{
			String type = colType.get(count);
			if("string".equalsIgnoreCase(type))
				where.append(prefix+att+ "  LIKE ? ");
			else 
				where.append("str("+prefix+att+ ")  LIKE ? ");
				
				
			if(count<searchCols.size()-1)
				where.append(" OR ");
				
			count++;
		}
		
		String select = "count(i)";
		

		log.debug("select:"+select.toString());
		log.debug("where:"+where.toString());
		
		
		String qstring = "SELECT "+select+" FROM " + getDomainName() + " as "+alias+" WHERE " + where+"";
		log.debug("FULL QUERY:"+qstring);
		
		Query q = this.sessionFactory.getCurrentSession().createQuery(qstring);
		
	
		 count = 0;
		for(String att:searchCols)
		{
		
			q.setString(count, searchst);
			count++;
		}
		

		Long size = (Long)q.uniqueResult();
		log.debug("result Size:"+size);

		return size;
	}
	
	@Override
	public void update(Type obj) {
		getSession().update(obj);
		
	}




}
