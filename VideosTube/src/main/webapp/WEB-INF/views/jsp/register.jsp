<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="msg" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML>
<html>
<head>
	<title>Register form</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
	<link href="css/style.css" rel="stylesheet" type="text/css"  media="all" />
	<link href='http://fonts.googleapis.com/css?family=Ropa+Sans' rel='stylesheet' type='text/css'>	
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link href='https://fonts.googleapis.com/css?family=Passion+One' rel='stylesheet' type='text/css'>
	<link href='https://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	
</head>
<body>
	<div class="wrap">
		<div class="header">
			<div class="logo">
					<a href="home"><img src="img/logo.png" title="logo" /></a>
			</div>
			<div class="top-nav">
				<ul>
		            <li><a href="home">Home</a></li>
				</ul>
			</div>
			<div class="clear"> </div>
		</div>
		<div class="clear"> </div>
		<div class="clear"> </div>
	</div>
		<div class="container">
			<div class="row main">
				<div class="main-register main-center">
					<sf:form  class="form-horizontal" commandName="user">
						<div class="login-txt" align="center" style="color:#FFFFFF;"> Register <c:out value="${msg}"></c:out></div><br>
						<div class="form-group">
							<label for="name" class="cols-sm-2 control-label">Username</label>
							<div class="cols-sm-10">
								<div class="input-group">
									<span class="input-group-addon"></span>
									<sf:input type="text" path="username" class="form-control"  id="username" maxlength="16"
										onkeyup ="Validate(this)" onblur="CheckLength()" placeholder="Enter your Username" />
										<h1 id="usernameAllowedMsg" class="usernameAllowedMsg"></h1>
								</div>
							</div><br>
							<div id="errLast"></div>
							<label for="email" class="cols-sm-2 control-label">Your Email</label>
							<div class="cols-sm-10">
								<div class="input-group">
									<span class="input-group-addon"></span>
									<sf:input type="email" path="email" class="form-control"  id="email" maxlength="50"
										onchange="email_validate(this.value);"
										placeholder="Enter your Email"/>
								</div>
							</div>
							<div class="status" id="status"></div><br>
							<label for="password" class="cols-sm-2 control-label">Password</label>
							<div class="cols-sm-10">
								<div class="input-group">
									<span class="input-group-addon"></span>
									<sf:input type="password" path="password" class="form-control" id="pass" 
											minlength="4" maxlength="16"  
											 placeholder="Enter your Password"/>
								</div>
							</div><br>
							
							<label for="confirm" class="cols-sm-2 control-label">Confirm Password</label>
							<div class="cols-sm-10">
								<div class="input-group">
									<span class="input-group-addon"></span>
									<input type="password" class="form-control" name="confirm" id="confirm"
										 minlength="4" maxlength="16" placeholder="Enter again to validate"  
										 id="pass2" onkeyup="checkPass(); return false;"
										 placeholder="Confirm your Password"/>
								</div>
							</div>
							 
						</div>

							<span id="confirmMessage" class="confirmMessage"></span>
						
						<div class="form-group ">
							<button type="submit" class="btn btn-primary btn-lg btn-block login-button">Register</button>
						</div>
						<div class="login-register">
				            <a  href="login">Login</a>
				         </div>
					</sf:form>
				</div>
			</div>
		</div>

		<script src="script/register_form.js"></script>
			<div class="clear"> </div>

	</body>
</html>

