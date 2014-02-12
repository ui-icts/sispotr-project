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

@Repository("edu_uiowa_icts_safeseed_dao_SeqRelationshipHome")
@Transactional
public class SeqRelationshipHome extends GenericDao<SeqRelationship> implements SeqRelationshipService {


    private static final Log log =LogFactory.getLog(SeqRelationshipHome.class);

    public SeqRelationshipHome()
    {        setDomainName("edu.uiowa.icts.safeseed.domain.SeqRelationship");

    }


    public SeqRelationship  findById(SeqRelationshipId id)

    {
        return (SeqRelationship)this.sessionFactory.getCurrentSession().get(getDomainName(), id);

    }




}