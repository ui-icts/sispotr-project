<%@ include file="/WEB-INF/include.jsp"%>

       <div align="center" >
                <fieldset>      
                    	<input type="hidden" name="htmlFormName" value="recompute"/>
                    		<input type="hidden" name="lengthOfXmers" id="lengthOfXmers" value="<c:out value="${main.lengthOfXmers}"/>"/>
           
                            <div class="fieldOptions" style="font-size: 12px; font-family: Trebuchet MS, Arial, Helvetica, Verdana, sans-serif;">
                                <input type="hidden" name="accession" id="accession" value=""/>
                                <table border="0" >
                                    <tr>
                                    	<td>
                                                                                   </td>
                                        <td align="right">
                                         
                                        </td>
                                        <!--<td>
                                            <strong>Select Species 3'UTR Hexamer Profile </strong>
                                        </td>
                                        <td align="right">
                                        	<c:choose>
                                        		<c:when test="${list.species == 'human'}">
		                                            <select name="species" id="speciess" disabled="disabled">
		                                                <option value="human" selected="selected">Human</option>
		                                                <option value="mouse">Mouse</option>
		                                            </select>
	                                            </c:when>
	                                            <c:when test="${list.species == 'mouse'}">
		                                            <select name="species" id="speciess" disabled="disabled">
		                                                <option value="human">Human</option>
		                                                <option value="mouse" selected="selected">Mouse</option>
		                                            </select>
	                                            </c:when>
                                            </c:choose>
                                        </td>-->
                                        <td style="padding-left: 20px">
                                            <strong>G/C Content% Min</strong>
                                        </td>
                                        <td align="right">
                                            <input type="text" name="gcMin" id="gcMin" value="<c:out value="${main.gcMin*100}"/>" size="8" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <strong>Number of Results</strong>
                                        </td>
                                        <td align="right">
                                            <input type="text" name="numberOfReturnElements" id="numberOfReturnElements" value="<c:out value="${main.numberOfReturnElements}"/>" size="8"/>
                                        </td>
                                        <td style="padding-left: 20px">
                                            <strong>G/C Content% Max</strong>
                                        </td>
                                        <td align="right">
                                            <input type="text" name="gcMax" id="gcMax" value="<c:out value="${main.gcMax*100}"/>" size="8" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <strong>How many G/Cs required at 5' end of Passenger?</strong>
                                        </td>
                                        <td align="right">
                                            <input type="text" name="gcRequired" id="gcRequired" value="<c:out value="${main.gcRequired}"/>" size="8" />
                                        </td>
                                        <td style="padding-left: 20px">
                                            <strong>Allow "G" at position 2 of guide?</strong>
                                        </td>
                                        <td align="right">
                              					<input type="radio" name="gAt2" value="true" id="gAt2_true" >Yes
												<input type="radio" name="gAt2" value="false" id="gAt2_false" >No
		                 

                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <strong>Allow G:U Wobble in Seed</strong>
                                        </td>
                                        <td align="right">
                                    	    <input type="radio" name="ensureBestPots" id="ensureBestPots_true" value="true" >Yes
											<input type="radio" name="ensureBestPots" id="ensureBestPots_false"  value="false" >No
                                   
                                        </td>
                                        <td style="padding-left: 20px">
                                            <strong>Allow "G" at position 3 of guide?</strong>
                                        </td>
                                        <td align="right">
                                                    <input type="radio" name="gAt3" id="gAt3_true" value="true" >Yes
													<input type="radio" name="gAt3" id="gAt3_false" value="false" >No
                                         
                                        </td>
                                	<tr>
                                		<td colspan="2"></td>
                                		<td style="text-align:right;"><span id="statusMessage"></span></td>
                                    	<td>
                                    		<button type="button" id="recomputeButton">Ok</button>
                                    	</td>
                                    </tr>
                                </table>
                            </div>

                 
                </fieldset>
            </div>
    
        <script>
	    $('#numberOfReturnElements').spinner({step: 1, min: 1, max: 10000000 });
	    $('#gcMin').spinner({step: 1, min: 0, max: 100});
	    $('#gcMax').spinner({step: 1, min: 0, max: 100});
	    $('#gcRequired').spinner({step: 1, min: 0, max: 2});
	    $('input:radio[name=gAt2]').val(["true"]);
	    $('input:radio[name=gAt3]').val(["true"]);
	    $('input:radio[name=ensureBestPots]').val(['false']);
		</script>
        

