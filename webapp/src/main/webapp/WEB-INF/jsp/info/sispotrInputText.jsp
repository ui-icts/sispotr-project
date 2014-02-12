<%@ include file="/WEB-INF/include.jsp"%>
<ol>
  <li><strong>Input sequence(s) for siRNA design.</strong> Click
    the appropriate button to paste or upload the sequence(s). Single
    sequences can be in plain or FASTA format. Multiple sequences
    require FASTA formatting.</li>
  <li><strong>Select the number of candidates desired in
      the output.</strong>
  </li>
  <li><strong>(Advanced) Modify input parameters:</strong>
    <ul>
      <li><strong>GC Content (%Min/Max):</strong> Filters candidate
        sequences based on specified GC content range.</li>
      <li><strong>5' Passenger G/C Number:</strong> Forces the
        passenger strand to have high 5' end stability. This decreases
        likelihood of loading the unintended (passenger) strand.
        Additional parameters to promote guide strand loading are
        automatically included in the design.</li>
      <li><strong>Pol III Expression:</strong> Removes sequences
        with &ge;4 T/U's. Stretches of 4 or more U's can serve as a
        termination signal for RNA Pol III. Since many shRNA/artificial
        miRNA expression vectors use Pol III promoters (e.g. U6, H1) to
        drive expression, these sequences should be avoided to prevent
        pre-mature transcript termination.</li>
    </ul></li>
  <li><strong>Run siSPOTR</strong>
  </li>
  <li><strong>Browse additional off-target information
      (Highly Recommended): Click "Off-Target Details" to:</strong>
    <ul>
      <li>BLAST against transcript databases</li>
      <li>View the overall number of off-targets and the
        distribution of more potent seed site-types (7mer-1A, 7mer-M8,
        8mer)</li>
      <li>View/download list of all predicted off-targets with
        site-type information</li>
    </ul></li>
</ol>
