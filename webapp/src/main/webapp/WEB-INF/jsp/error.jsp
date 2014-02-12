<%@page import="java.io.*"%>
<h2>Error while processing page <%=request.getAttribute("javax.servlet.error.request_uri") %></h2>
<button id="button" onclick="document.getElementById('details').style.visibility='visible';document.getElementById('button').style.visibility='hidden'">details</button>
<div id="details" >
<pre>
<%

Throwable e = (Throwable)request.getAttribute("javax.servlet.error.exception");
Writer result = new StringWriter();
PrintWriter printWriter = new PrintWriter(result);
e.printStackTrace(printWriter);
out.print(result.toString());

%>
</pre>
</div>
<script>
document.getElementById('details').style.visibility="hidden";
</script>