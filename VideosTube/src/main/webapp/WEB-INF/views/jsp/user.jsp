<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML>
<html>
	<head>
	<c:if test="${sessionScope.user == null}" >
		<c:redirect url="login"/>
	</c:if>
		<title>My Channel</title>
	<link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
	<link href="css/style.css" rel="stylesheet" type="text/css"  media="all" />		
	<link href='http://fonts.googleapis.com/css?family=Ropa+Sans' rel='stylesheet' type='text/css'>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>	
	
</head>
<body onload="showMyVideos('${userChannel.username}')">
	<div class="wrap">
		<div class="logo">
			<a href="home"><img src="img/logo.png" title="logo" /></a>
		</div>
		<div class="searchbar">
			<div class="search-left">
	            <p>Search</p>
				<select class="search-drop-down" id="search-drop-down">
                	<option>Video</option>
                	<option>Play List</option>
                	<option>Channel</option>
              	 </select>
            </div>
            <div class="search-right">
                <form action="javascript:search()">
                    <input type="text" id="search-field" placeholder="Search videos">
                    <input type="submit" value="" />
                </form>
            </div>
            <div class="clear"> </div>
        </div>
		<div class="top-nav" >
			<ul>
				<li><a href="home">Home</a></li>
				<li><a href="myChannel">My Channel</a></li>                    
            </ul>
		</div>
		<div class="clear"> </div>
		<div class="buttons">
			<c:if test="${sessionScope.user == null}" > 
				<button type="button" class="register-but" ><a href="register" style="color:white;" >Register</a></button>
				<button type="button" class="login-but"><a href="login">Login</a></button>
			</c:if>
			<c:if test="${sessionScope.user != null}">
				<button type="button" class="register-but" ><a href="login" style="color:white;" >Log out</a></button>
				<button type="button" class="login-but"><a href="myChannel"><c:out value="${sessionScope.user.getUsername() }"></c:out></a></button>
			</c:if>
			<button type="button" class="upload-but"><a href="upload">Upload</a></button>
		</div> 	
		<div class="twPc-div" id="twPc-div">
			<a class="twPc-bg twPc-block"></a>
			<div>
				<div title="#" href="#" class="twPc-avatarLink">
					<img   src="profilePic/${userChannel.getProfilePic()}" class="twPc-avatarImg">
				</div>
				<div class="twPc-divUser">
					<div class="twPc-divName">
						<a href="#"><c:out value="${userChannel.username}"></c:out></a>
					</div>
				</div><br><br>
				<div class="twPc-divStats">
					<ul class="twPc-Arrange">
						<li class="twPc-ArrangeSizeFit">
							<a href="#" onclick="showMyVideos('${userChannel.username}')">
								<span >Videos</span>
							</a>
						</li>
						<li class="twPc-ArrangeSizeFit">
							<a href="#" onclick="showPlaylists('${userChannel.username}')">
								<span >Playlists</span>
							</a>
						</li>
						<li>
							<a href="#" >
								
								<c:set var="sessionUser" value="${sessionScope.user.getUsername()}"></c:set>
								<c:set var="isSubscribed" value="${userChannel.getMyChannel().checkUserSubscribe(sessionUser)}"></c:set>
																
								<c:choose>
    								<c:when test="${isSubscribed==true }">
							      		<span id="subs"onclick="unsubscribe('${userChannel.username}', '${sessionScope.user}')" >Unsubscribe</span>
							    	</c:when>
							    	<c:otherwise>
							        	<span id="subs"onclick="unsubscribe('${userChannel.username}', '${sessionScope.user}')">Subscribe</span>
							    	</c:otherwise>
								</c:choose>
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="content">	
		<div class="left-content">            
			<div id="searchResults"></div>
		</div>
	</div>
	<div class="my_channel_content" id="my_channel_content">
		<div id="resultList"  class="box"></div>	
	</div>

<script src="script/user_scripts.js"></script>
</body>
</html>

