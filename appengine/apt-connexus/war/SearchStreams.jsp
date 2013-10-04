<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.adnan.Stream" %>
<%@ page import="com.adnan.OfyService" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <body>
 
  
<h1>
<a href="CreateStream.html">Create</a> 	
<a href="ViewAllStreams.jsp">View</a> 
<a href="SearchStreams.jsp">Search</a>  
</h1>
<p>  

        <form>
			<b>Search:</b> <input type="text" name="searchString"><br>
		</form>
		
    
 <%
 	String searchString = request.getParameter("searchString");
 	if ( searchString != null) {
	  List<Stream> allStreams = OfyService.ofy().load().type(Stream.class).list();
	  Collections.sort(allStreams);
	  int i = 0;
	  for (Stream s : allStreams ) {
	    if (s.name.contains(searchString)) {
	      %> <img width="100" height="100" src="<%= s.coverImageUrl %>"> <br><%= s.name %><br>
          <a href="ShowStream.jsp?streamId=<%= s.id%>&streamName=<%= s.name %>"</a> <%
        } 
     } 
   } else {
     %><h2>Need to provide a search string!</h2><%
   }
%>
  
  </body>
</html>