package edu.uiowa.icts.spring;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

public interface GenericDaoInterface<Type> {


	public void setSessionFactory(SessionFactory sessionFactory) ;
	public Session getSession();
	public void  saveOrUpdate(Type obj);
	public void  update(Type obj);
	public void  save(Type obj);
	public void  persist(Type obj);
	public void  merge(Type obj);	
	public long count();
	public void  delete(int id);
	public void  delete(long id);
	public void  delete(Type obj);
	public Type  findByCriteria(String s);
	public String getDomainName() ;

	public List<Type[]> query(String st); 
	public void setDomainName(String domainName) ;
	public Integer  maxOf(String s);
	public void  justSave(Type obj);
	public List<Type>  list();
	public List<Type>  listOrdered(String order, String direction);
	public void  flush();
	public void  refresh(Object obj);
	
	
	
	//used for serverside pagination on a table
	public Long  countSearch(
			String searchst,
			List<String> searchCols,
			List<String> colType
			);
	public List<Object[]>  listSearchPaged(
			int numResults, 
			int firstResult,  
			String searchst, List<String[]> orderData, 
			List<String> colNames, 
			List<String> searchCols,
			List<String> colType
			);
	
}
