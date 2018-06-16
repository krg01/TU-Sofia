<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="css/add_playList.css" rel="stylesheet" type="text/css"
	media="all" />
<link href='http://fonts.googleapis.com/css?family=Ropa+Sans'
	rel='stylesheet' type='text/css'>

<link href="http://vjs.zencdn.net/5.11.7/video-js.css" rel="stylesheet">

<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="script/register_form.js"></script>
<script src="script/add_playlist.js"></script>
<script src="script/video_page_scripts.js"></script>

</head>
<body>
	<div id="commentsDiv" class="comments-container">
		<h1>Video comments</h1>

		<ul id="comments-list" class="comments-list">

			<li><c:if test="${sessionScope.user!=null}">
					<div id="writeCommentLogin" class="comment-main-level">
						<c:if test="${sessionScope.user!=null}">
							<div class="comment-avatar">
								<a href="myChannel"><img
									src="myChannel/${sessionScope.user.getUsername()}" alt=""></a>
							</div>
						</c:if>
						<div class="comment-box">
							<div class="comment-head">
								<h6 class="comment-name">
									<a href="myChannel">You</a>
								</h6>
							</div>
							<form action="javascript:writeComment()">
								<div class="comment-content">
									<input id="commentText" type="text" style="heigh: 10px;" onkeyup="validateText(this)"
										placeholder="Write comment..." maxlength="90"></input>
									<button type="submit">Add</button>
								</div>
							</form>
						</div>
					</div>
				</c:if></li>


			<c:forEach items="${comments}" var="com" varStatus="loop">
				<li>



					<div class="comment-main-level">
						<!-- Avatar -->
						<div class="comment-avatar">
							<a href="userProfile?name=${com.user}"><img
								src="myChannel/${com.user}" alt=""></a>
						</div>
						<!-- Contenedor del Comentario -->
						<div class="comment-box">
							<div id="commentId" style="display: none;">
								<c:out value="${com.id }"></c:out>
							</div>
							<div class="comment-head">
								<c:if test="${com.user==uploader}">
									<h6 class="comment-name by-author">
										<c:out value="${com.user}" />
									</h6>
								</c:if>
								<c:if test="${com.user!=uploader}">
									<h6 class="comment-name ">
										<c:out value="${com.user}" />
									</h6>
								</c:if>
								<span><c:out value="${com.date}" /> <a href="login"
									id="confirmMes${loop.index}" class="confirmMessage"> </a></span>
								<div>
									<i id="${loop.index}" class="fa fa-reply"><c:out
											value="${com.likes}" /></i>
								</div>
								<i class="fa fa-heart"> <c:if
										test="${sessionScope.user!=null}">
										<c:if
											test="${sessionScope.user.isLikeComment(videoName,com.id)}">
											<c:set var="commentLike" value="img/likeGreen.png" />
										</c:if>
										<c:if
											test="${!sessionScope.user.isLikeComment(videoName,com.id)}">
											<c:set var="commentLike" value="img/likeBlue.png" />
										</c:if>
									</c:if> <c:if test="${sessionScope.user==null}">
										<c:set var="commentLike" value="img/likeBlue.png" />
									</c:if> <img id="${com.id}"
									onclick="likeComment(${loop.index},${com.id})"
									src="${commentLike}" /></i>
							</div>
							<div class="comment-content">
								<c:out value="${com.text}" />
							</div>
						</div>
					</div>
				</li>
			</c:forEach>

		</ul>
	</div>
</body>
</html>