<%@ page session="true"%>

User '<%=request.getRemoteUser()%>' has been logged out.

<% session.invalidate(); %>

<%response.sendRedirect("index.jsp");%>

