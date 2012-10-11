<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>People Home</title>
</head>
<body>
	<c:forEach var="link" items="${links}">
		<s:url value="${link.href}" var="people_uri"></s:url>
		<ul><li><a href="${people_uri}" rel="${link.rel}">${link.rel}</a></li></ul>
	</c:forEach>
</body>
</html>