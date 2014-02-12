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
 * @date 2011/03/03 16:15:53
*/ 

@Entity(name="edu_uiowa_icts_safeseed_domain_globalSeq")
@Table(name = "global_seq", schema="safeseed")
public class GlobalSeq
{
        private Integer globalSeqId;
        private String locus;
        private String sequence;
        private Integer giNumber;
        private String fromField;
        private String name1;
        private String name2;
        private String description;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateAdded;
        private Set<GlobalSeqFrag> globalSeqFrags = new HashSet<GlobalSeqFrag>(0);
        private Set<SeqRelationship> seqRelationships = new HashSet<SeqRelationship>(0);
        private Set<GlobalCollection> globalCollections = new HashSet<GlobalCollection>(0);
        private Set<GroupGlobalSeq> groupGlobalSeqs = new HashSet<GroupGlobalSeq>(0);

    public GlobalSeq()    {
        this.globalSeqId = null;
        this.locus = "";
        this.sequence = "";
        this.giNumber = null;
        this.fromField = "";
        this.name1 = "";
        this.name2 = "";
        this.description = "";
        this.dateAdded = null;
        this.globalSeqFrags = new HashSet<GlobalSeqFrag>(0);
        this.seqRelationships = new HashSet<SeqRelationship>(0);
        this.globalCollections = new HashSet<GlobalCollection>(0);
        this.groupGlobalSeqs = new HashSet<GroupGlobalSeq>(0);
    }

    public GlobalSeq(Integer globalSeqId, String locus, String sequence, Integer giNumber, String fromField, String name1, String name2, String description, Date dateAdded, Set<GlobalSeqFrag> globalSeqFrags, Set<SeqRelationship> seqRelationships, Set<GlobalCollection> globalCollections, Set<GroupGlobalSeq> groupGlobalSeqs)
    {
        this.globalSeqId = globalSeqId;
        this.locus = locus;
        this.sequence = sequence;
        this.giNumber = giNumber;
        this.fromField = fromField;
        this.name1 = name1;
        this.name2 = name2;
        this.description = description;
        this.dateAdded = dateAdded;
        this.globalSeqFrags = globalSeqFrags;
        this.seqRelationships = seqRelationships;
        this.globalCollections = globalCollections;
        this.groupGlobalSeqs = groupGlobalSeqs;
    }

    /*****globalSeqId*****/
    @javax.persistence.SequenceGenerator(  name="gen",  sequenceName="safeseed.seqnum",allocationSize=1)
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO,generator="gen")
    @Column(name = "global_seq_id", unique = true, nullable = false)
    public Integer getGlobalSeqId()
    {
        return globalSeqId;
    }

    public void setGlobalSeqId(Integer globalSeqId)
    {
        this.globalSeqId = globalSeqId;
    }

    /*****locus*****/
    @Column(name = "locus")
    public String getLocus()
    {
        return locus;
    }

    public void setLocus(String locus)
    {
        this.locus = locus;
    }

    /*****sequence*****/
    @Column(name = "sequence")
    public String getSequence()
    {
        return sequence;
    }

    public void setSequence(String sequence)
    {
        this.sequence = sequence;
    }

    /*****giNumber*****/
    @Column(name = "gi_number")
    public Integer getGiNumber()
    {
        return giNumber;
    }

    public void setGiNumber(Integer giNumber)
    {
        this.giNumber = giNumber;
    }

    /*****fromField*****/
    @Column(name = "from_field")
    public String getFromField()
    {
        return fromField;
    }

    public void setFromField(String fromField)
    {
        this.fromField = fromField;
    }

    /*****name1*****/
    @Column(name = "name1")
    public String getName1()
    {
        return name1;
    }

    public void setName1(String name1)
    {
        this.name1 = name1;
    }

    /*****name2*****/
    @Column(name = "name2")
    public String getName2()
    {
        return name2;
    }

    public void setName2(String name2)
    {
        this.name2 = name2;
    }

    /*****description*****/
    @Column(name = "description")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /*****dateAdded*****/
    @Column(name = "date_added")
    public Date getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public void setDateAdded(String dateAdded)
    {
        try{
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setLenient(true);        this.dateAdded = formatter.parse(dateAdded);
        } catch (ParseException e) {e.printStackTrace();}
    }

    /*****globalSeqFrags*****/
    @OneToMany(fetch = FetchType.LAZY,   mappedBy = "globalSeq", targetEntity=GlobalSeqFrag.class, cascade=CascadeType.REMOVE)
    public Set<GlobalSeqFrag> getGlobalSeqFrags()
    {
        return globalSeqFrags;
    }

    public void setGlobalSeqFrags(Set<GlobalSeqFrag> globalSeqFrags)
    {
        this.globalSeqFrags = globalSeqFrags;
    }

    /*****seqRelationships*****/
    @OneToMany(fetch = FetchType.LAZY,   mappedBy = "globalSeq", targetEntity=SeqRelationship.class, cascade=CascadeType.REMOVE)
    public Set<SeqRelationship> getSeqRelationships()
    {
        return seqRelationships;
    }

    public void setSeqRelationships(Set<SeqRelationship> seqRelationships)
    {
        this.seqRelationships = seqRelationships;
    }

    /*****globalCollections*****/
    @OneToMany(fetch = FetchType.LAZY,   mappedBy = "globalSeq", targetEntity=GlobalCollection.class, cascade=CascadeType.REMOVE)
    public Set<GlobalCollection> getGlobalCollections()
    {
        return globalCollections;
    }

    public void setGlobalCollections(Set<GlobalCollection> globalCollections)
    {
        this.globalCollections = globalCollections;
    }

    /*****groupGlobalSeqs*****/
    @OneToMany(fetch = FetchType.LAZY,   mappedBy = "globalSeq", targetEntity=GroupGlobalSeq.class, cascade=CascadeType.REMOVE)
    public Set<GroupGlobalSeq> getGroupGlobalSeqs()
    {
        return groupGlobalSeqs;
    }

    public void setGroupGlobalSeqs(Set<GroupGlobalSeq> groupGlobalSeqs)
    {
        this.groupGlobalSeqs = groupGlobalSeqs;
    }


}