package edu.uiowa.icts.safeseed.dao;

import edu.uiowa.icts.spring.*;
import edu.uiowa.icts.safeseed.domain.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/* Generated by Protogen
 *
 *
*/

@Repository("edu_uiowa_icts_safeseed_dao_GlobalSeqFragHome")
@Transactional
public class GlobalSeqFragHome extends GenericDao<GlobalSeqFrag> implements GlobalSeqFragService {


    private static final Log log =LogFactory.getLog(GlobalSeqFragHome.class);

    public GlobalSeqFragHome()
    {        setDomainName("edu.uiowa.icts.safeseed.domain.GlobalSeqFrag");

    }


    public GlobalSeqFrag  findById(Integer id)

    {
        return (GlobalSeqFrag)this.sessionFactory.getCurrentSession().get(getDomainName(), id);

    }




}