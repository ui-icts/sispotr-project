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

/* Generated by Protogen - www.icts.uiowa.edu/protogen
 * @date 2011/04/26 15:23:46
*/ 

@Entity(name="edu_uiowa_icts_safeseed_domain_searchStatus")
@Table(name = "search_status", schema="safeseed")
public class SearchStatus
{
        private String searchSeq;
        private String searchSeqFull;
        private Integer status;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date entryTime;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkoutTime;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finishTime;
        private Integer readCount;
        private Set<SearchResult> searchResults = new HashSet<SearchResult>(0);

    public SearchStatus()    {
        this.searchSeq = "";
        this.searchSeqFull = "";
        this.status = null;
        this.entryTime = null;
        this.checkoutTime = null;
        this.finishTime = null;
        this.readCount = null;
        this.searchResults = new HashSet<SearchResult>(0);
    }

    public SearchStatus(String searchSeq, String searchSeqFull, Integer status, Date entryTime, Date checkoutTime, Date finishTime, Integer readCount, Set<SearchResult> searchResults)
    {
        this.searchSeq = searchSeq;
        this.searchSeqFull = searchSeqFull;
        this.status = status;
        this.entryTime = entryTime;
        this.checkoutTime = checkoutTime;
        this.finishTime = finishTime;
        this.readCount = readCount;
        this.searchResults = searchResults;
    }

    /*****searchSeq*****/
    @Id
    @Column(name = "search_seq", unique = true, nullable = false)
    public String getSearchSeq()
    {
        return searchSeq;
    }

    public void setSearchSeq(String searchSeq)
    {
        this.searchSeq = searchSeq;
    }

    /*****searchSeqFull*****/
    @Column(name = "search_seq_full")
    public String getSearchSeqFull()
    {
        return searchSeqFull;
    }

    public void setSearchSeqFull(String searchSeqFull)
    {
        this.searchSeqFull = searchSeqFull;
    }

    /*****status*****/
    @Column(name = "status")
    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /*****entryTime*****/
    @Column(name = "entry_time")
    public Date getEntryTime()
    {
        return entryTime;
    }

    public void setEntryTime(Date entryTime)
    {
        this.entryTime = entryTime;
    }

    public void setEntryTime(String entryTime)
    {
        try{
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setLenient(true);        this.entryTime = formatter.parse(entryTime);
        } catch (ParseException e) {e.printStackTrace();}
    }

    /*****checkoutTime*****/
    @Column(name = "checkout_time")
    public Date getCheckoutTime()
    {
        return checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime)
    {
        this.checkoutTime = checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime)
    {
        try{
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setLenient(true);        this.checkoutTime = formatter.parse(checkoutTime);
        } catch (ParseException e) {e.printStackTrace();}
    }

    /*****finishTime*****/
    @Column(name = "finish_time")
    public Date getFinishTime()
    {
        return finishTime;
    }

    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }

    public void setFinishTime(String finishTime)
    {
        try{
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setLenient(true);        this.finishTime = formatter.parse(finishTime);
        } catch (ParseException e) {e.printStackTrace();}
    }

    /*****readCount*****/
    @Column(name = "read_count")
    public Integer getReadCount()
    {
        return readCount;
    }

    public void setReadCount(Integer readCount)
    {
        this.readCount = readCount;
    }

    /*****searchResults*****/
    @OneToMany(fetch = FetchType.LAZY,   mappedBy = "searchStatus", targetEntity=SearchResult.class, cascade=CascadeType.REMOVE)
    public Set<SearchResult> getSearchResults()
    {
        return searchResults;
    }

    public void setSearchResults(Set<SearchResult> searchResults)
    {
        this.searchResults = searchResults;
    }


}