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
	<c:set var="cannels" value="${subscribers}"></c:set>
	<c:forEach var="channel" items="${channels}">
		    <div class="grid">
	        	<h3>channel.user</h3>
	        	<a href="video?name=teodor"><img style="width:234px;height:178px;" src="#" title= "NAME" /></a>
	        	<div class="grid-info">
					<div class="video-share">
					</div>
					<div class="clear"> </div>
				</div>
        	</div>
    </c:forEach>    	
    
    	<c:set var="videosList" value="${userVideos}" />                	
	               	<c:forEach items="${videosList}" var="video">
						<div class="grid">
							<h3> ${video.name}</h3>
							<a href="video?name=${video.name}"><img src="img/g1 copy.png" title= "${video.name}" /></a>
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