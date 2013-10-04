<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.adnan.Stream" %>
<%@ page import="com.adnan.OfyService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>

<%@ page import="com.adnan.OfyService" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

<body>
 
<h1>
<a href="CreateStream.html">Create</a> 	
<a href="ViewAllStreams.jsp">View</a> 
<a href="SearchStreams.jsp">Search</a>  
</h1>
        
<table>    
<%
		List<Stream> th = OfyService.ofy().load().type(Stream.class).list();
		Collections.sort(th);
		int i = 0;
		for (Stream s : th ) {
		  if ( i % 4 == 0 ) {
		    %> <tr> <%
		  }
		  i++;
		  // APT: calls to System.out.println go to the console, calls to out.println go to the html returned to browser
		  // the line below is useful when debugging (jsp or servlet)
		  System.out.println("s = " + s);
		  %>
		  <td> <img width="100" height="100" src="<%= s.coverImageUrl %>"> <br>
		  <a href="ShowStream.jsp?streamId=<%= s.id %>&streamName=<%= s.name %>"> <%= s.name %></a></td>
		  <%if ( i % 4 == 0 ) {
		    %>
		    </tr>
		    <%
		  }%>

<% } %>
		
</table>
</body>
</html>