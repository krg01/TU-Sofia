<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
<!DOCTYPE HTML>
<html>
<head>
    <title>Video Tube </title>
    <link rel="shortcut icon" type="image/x-icon" href=img/pageicon.png" />
    <link href="css/style.css" rel="stylesheet" type="text/css"  media="all" />
    <link href='http://fonts.googleapis.com/css?family=Ropa+Sans' rel='stylesheet' type='text/css'>
    <link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="script/home_page_scripts.js"></script>
</head>
<body>
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
                    <input type="text" id="search-field" placeholder="Search videos" onkeyup="validate(this)"
                    pattern=".{1,}" required title="1 characters minimum">
                    <input type="submit" value=""/>
                </form>
            </div>
            <div class="clear"> </div>
        </div>
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
        <div class="top-nav" >
            <ul>
                <li><a href="home">Home</a></li>
                <c:if test="${sessionScope.user != null}" >
                	<li><a href="myChannel">My Channel</a></li>
                </c:if>
                <c:if test="${sessionScope.user != null}" >
                	<li><a href="#" onclick="showLikedVideos()">Liked Videos</a></li>
                	<li><a href="#" onclick="showMyPlaylists()">My Playlist</a></li>
                	<li><a href="#" onclick="showAbonatedChannals()">Abonated Channels</a></li>
                </c:if>
            </ul>
        </div>
	</div>
	<div class="content">    
		<div class="left-content">            
			<div id="liked-videos-div"></div>
			<div id="videosList"  class="box">		
				<div id="channelsList"></div>
				<div class="grids">
               		<div id="videoBox" >                		             		
	                	<c:set var="videosList" value="${videos}" />  
	                	<h1 style="text-align:center">Top viewed videos</h1>              	
	                	<c:forEach items="${videosList}" var="video">
							<div class="grid">
								<h3> ${video.name}</h3>
								<a href="video?name=${video.name}"><img style="width:274px;height:178px;" src="videoPoster/${video.name}" title= "${video.name}" /></a>
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
					</div>
				</div>
			</div>	
		</div>
	</div>
</body>
</html>