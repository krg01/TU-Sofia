<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
       <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="msg" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://crea0tivecommons.org/licenses/by/3.0/
-->

<!DOCTYPE HTML>
<html>
<head>
<%
	response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0"); 
	response.addHeader("Pragma", "no-cache"); 
	response.addDateHeader ("Expires", 0);
%>
		<title>Videostube Website</title>
	
	<link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
	<link href="css/style.css" rel="stylesheet" type="text/css"  media="all" />	
	<link href='http://fonts.googleapis.com/css?family=Ropa+Sans' rel='stylesheet' type='text/css'>
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
	<link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">

	
</head>
<body>
	<!----start-wrap---->
    <div class="wrap">
       <!----start-Header---->
       <!----start-Logo---->
       <div class="logo">
           <a href="home"><img src="img/logo.png" title="logo" /></a>
       </div>
           <!----End-Logo---->

           <!----start-top-nav---->
       <div class="top-nav" >
           <ul>
               <li><a href="home">Home</a></li>
               <c:if test="${sessionScope.user != null}" >
               <li><a href="myChannel">My Channel</a></li>
               </c:if>
               <c:if test="${sessionScope.user != null}" >
               <li><a href="likedVideos">Liked Videos</a></li>
               <li><a href="myPlaylist">My Playlist</a></li>
               <li><a href="abonatetChannel">Abonated Channel</a></li>
               </c:if> 
           </ul>
       </div>        
       </div>
		<!----End-Header---->
		<div class="clear"> </div>
		
		<div class="clear"> </div>
	
		<!---reg Form start --->
		<div class="container">
			<div class="main-login main-center">
				<form class="form-horizontal" method = "POST"  >
			
					<div class="login-txt" align="center" style="color:#FFFFFF;"> Login <c:out value="${msg}"></c:out></div><br>
			
					<div class="form-group">
						<label for="email" class="cols-sm-2 control-label">Username</label>
						<div class="cols-sm-10">
							<div class="input-group">
								<span class="input-group-addon"></span>
								<input type="text" class="form-control" name="username" maxlength="16" 
									placeholder="Enter your username"/>
							</div>
						</div>
						<div class="status" id="status"></div>
					</div>
			
					<div class="form-group">
						<label for="password" class="cols-sm-2 control-label">Password</label>
						<div class="cols-sm-10">
							<div class="input-group">
								<span class="input-group-addon"></span>
								<input type="password" class="form-control" name="password" id="password" minlength="4" maxlength="16"  
										onblur="PasswordLength()" id="pass" placeholder="Enter your Password"/>
							</div>
						</div>
					</div>
			
					<span id="confirmMessage" class="confirmMessage"></span>
					
					<div class="form-group ">
						<button type="submit" class="btn btn-primary btn-lg btn-block login-button">Login</button>
					</div>
				</form>
			</div>
		</div>
		<script type="text/javascript" src="assets/js/bootstrap.js"></script>
		<script src="web/script/register_form.js"></script>
		<!--- reg Form end -->
		<div class="clear"> </div>

	<!----End-wrap---->
	</body>
</html>

