<%@ include file="/WEB-INF/include.jsp"%>
    <div id="lookupHelpAccordion">
      <h3><a href="#">Input Order</a></h3>
      <div>
<p>The order the sequences were entered.
          </p>
    </div>


    

     
  
      <h3><a href="#">POTS (Probability of Off-Targeting Score)</a></h3>
       <div>
       <p>This is
      a representation of the off-targeting potential of a given
      sequence that combines seed complement frequencies as well as
      estimates of their probability of being off-targeted if an
      experiment overexpressing that siRNA. Probabilities are assigned
      by first categorizing seed matches based on rules used by the
      Targetscan algorithm used for predicting targets for endogenous
      miRNAs:
      </p>
      In increasing order of potency:
    
        <ul>
          <li>6mer: Seed match only to nt 2-7</li>
          <li>7mer-1A: In addition to a seed match to nt 2-7, an
            "A" nt is found in the target across from nt1.</li>
          <li>7mer-M8: Seed match extends to nt 2-8.</li>
          <li>8mer: A combination of a 1A and M8 site</li>
        </ul>
    <p>Microarray data were used to estimate the probability that a
      gene containing a seed match of a particular type would be
      down-regulated at least 1.5 fold relative to the control
      conditions. These probabilities are applied to each predicted
      target, and used to calculate an overall score for a particular
      seed sequence.</p>
    <p>See the Targetscan documentation and literature for more
      information.</p>
          </div>

    <h3><a href="#">Percentile (% worse)</a></h3>
    <div>
    <p> The percent of all 7mer
      sequences with a worse (higher) POTS value
   </p>
          </div>



    <h3><a href="#">Seed Sequence</a></h3>
    <div>
    <p>This is the seed sequence for the siRNA
      used for calculating POTS.<br />
</p>
          </div>


    <h3><a href="#">miRNA seed</a></h3>
    <div>
    <p>If the seed of the siRNA matches a
      known endogenous miRNA, the matching miRNA family is presented
      here. We suggest avoiding sequences with miRNA seeds, as many
      predicted miRNA binding sites have evolved to facilitate miRNA
      accessibility. Consequently, work developing miRNA seed targeting
      rules demonstrated a much higher probability of being targeted
      than we have predicted from artificial siRNA expression data.
</p>
          </div>
    <h3><a href="#">miRNA Conservation</a></h3>
    <div>
    <p> This represents the
      conservation of the miRNA with a matching seed sequence. H(uman),
      M(ouse) and D(og) are given as representative species. Sequences
      with only one of the three letters shows that the miRNA is
      specific to that species or evolutionary lineage. H,M says that
      orthologus miRNAs are found in both human and mouse genomes. H,M,D
      means that the miRNA is at the very least conserved in those 3
      species, and are often much more conserved than that. The more
      conserved a miRNA is, the more likely conserved, functional target
      sites exist.
      </p>
   </div>

  </div>
    <script>
  $(function() {
    $( "#lookupHelpAccordion" ).accordion({
      autoHeight: false,
      navigation: true
    });
  });

  </script>