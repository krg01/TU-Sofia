<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<c:if test="${sessionScope.user == null}" >
		<c:redirect url="login"/>
	</c:if>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Upload Video</title>
	
	<link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
	<link href="css/style.css" rel="stylesheet" type="text/css"  media="all" />
	<link href='http://fonts.googleapis.com/css?family=Ropa+Sans' rel='stylesheet' type='text/css'>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>	
	<script src="script/upload_page_scripts.js"></script>
	
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
	<div class="left-content" id="left-content">
		<div id="results"></div>
		<div class="panel-body" id="panel-body">
		
		<h4 style="font-size: x-large;">Select video file from your computer</h4>
		<form id="js-upload-form" enctype="multipart/form-data" method="POST">
			<div class="form-inline">
				<div class="form-group">
					<input type="file" name="video" id="js-upload-files" onchange="fileSize()" 
						pattern=".{3,}" required title="3 characters minimum" 
						accept="video/mp4" value="select file">
				</div>
										
				<div class="video-upload-name">
				    <label for="comment" id="video-name-label" class="video-name-label">Video name:</label>
					<input class="form-control" rows="5" id="comment" maxlength="30" minlength="3" id="videoName" pattern=".{1,}" required title="1 characters minimum"
				      		onkeyup="checkName(this)" onblur="validateName(this)" name="videoName" placeholder="Type here..."></input>
					<h1 id="videonameAllowedMsg" class="videonameAllowedMsg">text</h1>
					
				</div>
				
				<div class="video-category-drop" style="display: none;">
					<label>Category:</label>
					<select name="category">
						<option>Autos and Vehicles</option>
						<option>Comedy</option>
						<option>Education</option>
						<option>Film & Animation</option>
						<option>Gaming</option>
						<option>Howto & Style</option>
						<option>Music</option>
						<option>News & Politics</option>
						<option>Nonprofits & Activism</option>
						<option>People & Blogs</option>
						<option>Pets & Animals</option>
						<option>Science & Technology</option>
						<option>Sports</option>
						<option>Travel & Events</option>
					</select>
				</div><br>
				<div class="video-upload-description">
				      <label for="comment">Description:</label>
				      <textarea class="form-control" rows="5" id="comment" name="description" onkeyup ="ValidateText(this)"
				      		maxlength="220" placeholder="Type here..."></textarea>
  					</div>						
				
				<button type="submit" name="submit" class="btn btn-sm btn-primary"							
					id="js-upload-submit">Upload files</button>
			</div>
		</form>
		<br>
		
		
		
		<h1 style="color:red;">${status}</h1>
		
		
		<!-- Drop Zone
		<h4>Or drag and drop files below</h4>
		<div class="upload-drop-zone" id="drop-zone">Just drag and
			drop files here</div>

		<!-- Progress Bar 
		<div class="progress">
			<div class="progress-bar" id="progress-bar" role="progressbar"
				aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
				style="width: 60%;">
				<span class="sr-only"></span>
			</div>

			<div class="progress-bar" id="progress-bar-butt" role="progressbar"
				aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
				style="width: 60%;">
				<span class="sr-only"></span>
			</div>
		</div>
		-->
		
		</div>
	</div>            
</div>
</body>
</html>