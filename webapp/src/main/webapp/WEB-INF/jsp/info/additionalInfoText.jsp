<%@ include file="/WEB-INF/include.jsp"%>
 <br/><p>
          <strong>Details of POTS score:</strong><br/> This provides
          summary information on the total number of predicted
          off-targets and the distribution of the various "site-types"
          found in those predicted targets. The POTS value is calculated
          by analyzing the potential off-target genes for seed matches
          conforming to known endogenous microRNA targeting rules.
          "8mer" sites tend to be the most potent followed by (in
          decreasing order) 7mer-M8, 7mer-1A and 6mer.
           <br/>
             <br/>
          <strong>Potential off-target list:</strong><br/> A list of probable off-target genes
          rank-ordered by individual transcript Probability of
          Off-Target Score (tPOTS). tPOTS is calculated based on the
          number and type of seed matches found in that transcript. The
          number of 8mer, 7mer-M8, 7mer-1A and 6mer sites are also
          shown. The entire list of off-targets can be exported to a CSV
          file to analyze as a simple spreadsheet.
          <br/> 
            <br/>
         <strong>Run BLAST search:</strong><br/>
          This is a link to the NCBI nucleotide blast page. In addition
          to seed-mediated off-targets, siRNAs can have more extensive
          binding to mRNAs outside of the 3'UTR. The option to search
          the full 21-mer, or the core 18-mer are provided (p1 and
          p20/21 mismatches often show negligible impact on silencing
          efficacy). Previous studies have suggested avoiding hits with
          0 or 1 mismatches, although 2 and 3 mismatches can still
          mediate silencing. In addition, this feature can be used to
          look for potentially desirable "on-targets". Search for hits
          to known gene paralogs or to see if your sequence would likely
          target the orthologous transcript in another species.
          </p>