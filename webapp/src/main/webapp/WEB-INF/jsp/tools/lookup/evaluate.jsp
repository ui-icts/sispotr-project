<%@ include file="/WEB-INF/include.jsp"%>

<h1>siRNA POTS Lookup</h1>
<div class="row-fluid">
<div class="span5 well">
<form action="runEvaluate.html" method="POST">
<fieldset class="admin1">
<!-- <span class="field">siRNA:</span> -->
<!-- <input type="radio" name="radioVal" value="s"/>Sense (Passenger) -->
<!-- <input type="radio" name="radioVal" value="a" checked="checked"/>Antisense (Guide) -->
<input type="hidden" name="radioVal" value="a" />

<h4>
<label for="textVal">
siRNA <strong>antisense</strong> sequences (limit 20)
</label>
</h4>
	<c:if test="${not empty errorMessage }">
	<div class="error">
	${errorMessage}
	</div>
	</c:if>
NOTE: For multiple sequences, enter one per line.
<!--  <br/><br/>Example sequences have been entered for demonstration purposes.  -->
<br/><br/>
<label for="species">Species:</label> 
<select id="species" name="species">
<option>human</option>
<option>mouse</option>
</select>
<br/>
<br/>
<textarea name="textVal" id="textVal" class="input-xlarge" rows="10" cols="50"></textarea>
<br/>
<br/>

<button type="submit" class="btn btn-large btn-primary">Run POTS Lookup </button>

</fieldset>
</form> 
</div>
<div class="span6 well">
<h2>About</h2>
 <jsp:include page="../../info/evaluateSIRNAText.jsp"></jsp:include>
</div>

</div>
<script>
// $("#textVal").attr("readonly","readonly");
</script>