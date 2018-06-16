<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${likedVideos.size() == 0}">
		<h3 style="color:red;">No Liked Videos!</h3>
	</c:if>
	<c:set var="videosList" value="${likedVideos}" />                	
	<c:forEach items="${videosList}" var="video">
		<div class="grid">
			<h3> ${video.name}</h3>
			<a href="video?name=${video.name}"><img src="videoPoster/${video.name}" title= "${video.name}" /></a>
			<div class="time">
				<span>Views<c:out value="${ video.view}"/></span>
			</div>
			<div class="grid-info">
				<div class="clear"> </div>
				<div class="lables">
					<p>Uploader:<a href="userProfile?name=${video.uploader}">${video.uploader}</a></p>
				</div>
			</div>
		</div>
	</c:forEach>
</body>
</html>