package edu.uiowa.icts.safeseed.dao;


import edu.uiowa.icts.safeseed.domain.SiteContent;
import edu.uiowa.icts.spring.GenericDaoInterface;

public interface SiteContentService  extends GenericDaoInterface<SiteContent>{

	public SiteContent  findById(Integer id);
	public SiteContent  findByLabel(String label);

}
