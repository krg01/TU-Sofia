<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Channels</title>
</head>
<body>
	<c:if test="${channels.size() == 0}">
		<h3 style="color:red;">No Channels!</h3>
	</c:if>
	<c:set var="channelsList" value="${channels}" />                	
	<c:forEach items="${channelsList}" var="user">
		<div class="grid">
			<h3>${user.username}</h3>
			<a href="userProfile?name=${user.username}">
				<img style="width:274px;height:178px;" src="myChannel/${user.username}" title="${user.username }" />
			</a>
    		<div class="grid-info">
				<div class="video-share"></div>
				<div class="clear"> </div>
			</div>
		</div>
	</c:forEach>
</body>
</html>