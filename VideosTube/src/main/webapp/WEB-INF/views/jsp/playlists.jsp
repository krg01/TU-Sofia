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
<script>
</script>
	<c:if test="${playlists.size() == 0}">
		<h3 style="color:red;">No Playlists!</h3>
	</c:if>
	<h1 style="color: red; text-align: center; font-size: x-large;" >${message}</h1>
	<div class="my-playlists" id="my-playlists">
		<c:set var="playlists" value="${playlists}" />                	
         <c:forEach items="${playlists}" var="playlist">
			<div class="playlists-frame">
				<c:if test="${playlist.count == 0}">
					<a href="#">							
					<img src="img/play.png"  onclick="printMessage()" class="video-list-image" id="edit-but"></img>
				</a>
				</c:if>
				<c:if test="${playlist.count > 0}">
				<a href="video?name=${playlist.name}&username=${playlist.user}">							
					<img src="img/play.png" class="video-list-image" id="edit-but"></img>
				</a>
				</c:if>
				<h2 class="list-video-title">${playlist.name}</h2>
				<a class="list-video-uploader" href="userProfile?name=${playlist.user}">By: ${playlist.user}</a>					
				<h4 class="list-video-views">Videos: ${playlist.count}</h4>
			</div>
		</c:forEach>
		<div class="playlists-frame" id="new-playlist" style="display:none;">
			<a href="#">								
				<img src="img/play.png" class="video-list-image" id="edit-but"></img>
			</a>
			<h2 class="list-video-title" id="new-paylist-name"></h2>
			<h4 class="list-video-views" id="new-playlist-count"></h4>
		</div>
	</div>
</body>
</html>