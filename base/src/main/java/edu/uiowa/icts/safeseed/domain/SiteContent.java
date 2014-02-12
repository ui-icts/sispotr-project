package edu.uiowa.icts.safeseed.domain;

import java.util.Set;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Table;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.*;
import javax.persistence.CascadeType;
import edu.uiowa.icts.*;


@Entity(name="edu_uiowa_icts_safeseed_domain_site_content")
@Table(name = "site_content", schema="safeseed")
public class SiteContent
{
        private Integer siteContentId;
        private String label;
        private String content;
        private String page;


    public SiteContent()    {
        this.siteContentId = null;
        this.label = "";
        this.content = "";

    }


    /*****personId*****/
    @javax.persistence.SequenceGenerator(  name="gen",  sequenceName="safeseed.seqnum",allocationSize=1)
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO,generator="gen")
    @Column(name = "site_content_id", unique = true, nullable = false)
    public Integer getSiteContentId()
    {
        return siteContentId;
    }

    public void setSiteContentId(Integer siteContentId)
    {
        this.siteContentId = siteContentId;
    }

    /*****label*****/
    @Column(name = "label")
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

    /*****content*****/
    @Column(name = "content")
	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

	   /*****page*****/
    @Column(name = "page")
	public String getPage() {
		return page;
	}


	public void setPage(String page) {
		this.page = page;
	}






}
