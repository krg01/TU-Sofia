<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Video Page</title>
<link rel="shortcut icon" type="image/x-icon" href="img/pageicon.png" />
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="css/add_playList.css" rel="stylesheet" type="text/css"
	media="all" />
<link href='http://fonts.googleapis.com/css?family=Ropa+Sans'
	rel='stylesheet' type='text/css'>

<link href="http://vjs.zencdn.net/5.11.7/video-js.css" rel="stylesheet">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="script/register_form.js"></script>
<script src="script/add_playlist.js"></script>
<script src="script/video_page_scripts.js"></script>

</head>

<body  onload="chechPlaylist('${playlist.size()}')">

	<c:set var="videoLikes" scope="page" value="${video.getLikes()}"></c:set>
	<c:set var="videoName" scope="page" value="${video.getName()}" />
	<c:set var="username" scope="page"
		value="${sessionScope.user.getUsername()}" />

	<div class="wrap">
		<div class="header">
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
						<input type="text" id="search-field" placeholder="Search videos" pattern=".{1,}" required title="1 characters minimum">
						<input type="submit" value="" onmousedown="search()"
							onsubmit="handle" />
					</form>
				</div>
				<div class="clear"></div>
			</div>
			<div class="buttons">
				<c:if test="${sessionScope.user == null}">
					<button type="button" class="register-but">
						<a href="register" style="color: white;">Register</a>
					</button>
					<button type="button" class="login-but">
						<a href="login">Login</a>
					</button>
				</c:if>
				<c:if test="${sessionScope.user != null}">
					<button type="button" class="register-but">
						<a href="login" style="color: white;">Log out</a>
					</button>
					<button type="button" class="login-but">
						<a href="myChannel"><div id="user">
								<c:out value="${username}"></c:out>
							</div></a>
					</button>
				</c:if>
				<button type="button" class="upload-but">
					<a href="upload">Upload</a>
				</button>
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
				<div id="searchResults"></div>
			</div>

			<div class="inner-page" id="newVideo">

				<c:set var="uploader" scope="page" value="${video.getUploader()}" />
				<c:set var="description" scope="page"
					value="${video.getDescription()}" />

				<div class="title">
					<h3 id="videoName">
						<c:out value="${video.getName()}"></c:out>
					</h3>
					<ul>
						<c:set var="channelName" scope="page"
							value="${video.getUploader()}" />
						<li><h4>By:</h4></li>
						<li><a href="userProfile?name=${uploader}"> <c:out
									value="${uploader}"></c:out>
						</a></li>
						<c:if test="${sessionScope.user==null }">
							<li><img onclick="subscribe('${video.getUploader()}')"
								src="img/sub.png" title="Unsubscribe" />
								<button onclick="subscribe()" type="button">
									<div id="sub">Subscribe</div>
								</button></li>
							<a href="login" id="confirmM" class="confirmMessage"> </a>
						</c:if>
						<c:if test="${sessionScope.user != null}">
							<c:if test="${sessionScope.user.isSubscribeChannel(channelName)}">
								<li><img
									onclick="subscribe('${video.getUploader()}', '${sessionScope.user.getUsername()}')"
									src="img/sub.png" title="Unsubscribe" />
									<button
										onclick="subscribe('${video.getUploader()}', '${sessionScope.user.getUsername()}')"
										type="button">
										<div id="sub">Unsubscribe</div>
									</button></li>
								<a href="login" id="confirmM" class="confirmMessage"> </a>
								<span id="subsErrMsg" class="subsErrMsg">Can't subscribe
									for yourself.</span>
							</c:if>
							<c:if
								test="${!sessionScope.user.isSubscribeChannel(channelName)}">
								<li><img src="img/sub.png" title="subscribe" />
									<button
										onclick="subscribe('${video.getUploader()}', '${sessionScope.user.getUsername()}')"
										type="button">
										<div id="sub">Subscribe</div>
									</button></li>
								<a href="login" id="confirmM" class="confirmMessage"> </a>
								<span id="subsErrMsg" class="subsErrMsg">Can't subscribe
									for yourself.</span>
							</c:if>
						</c:if>
						<li>
							<div id="list1" class="dropdown-check-list" tabindex="100" >
								<span class="anchor">Add to playlist</span>
								<ul class="items">
									<c:if test="${sessionScope.user!=null}">
										<c:forEach items="${sessionScope.user.getPlayLists()}"
											var="list">
											<li><input id="${list.name}" type="checkbox"
									<c:if test="${sessionScope.user.isVideoInList(list.name,requestScope.video.getName())}">checked</c:if>
												onchange="addPlaylist(this.id)" />
											<c:out value="${list.name}" /></li>
										</c:forEach>
										<li><a href="login" id="addToList" class="confirmMessage">
										</a></li>
									</c:if>
								</ul>
							</div> 
						</li>
					</ul>

					<c:set var="videoDislikes" scope="page"
						value="${video.getDislikes()}"></c:set>
					<c:set var="views" scope="page" value="${video.getView()}"></c:set>
					<c:set var="videoDate" scope="page"
						value="${video.getDate().toString() }"></c:set>
					<c:set var="video" scope="page"
						value="${requestScope.video.getName()}" />
					<video id="my-video" class="video-js" controls preload="auto"
						width="640" height="264"> <source
						src="video/${video}" type='video/mp4'></video>
				</div>


				<div class="viwes">
					<div class="view-links">
						<ul>
							<li><h4>Like video:</h4></li>
							
							<li>
									
									<c:set var="image" value="img/likeBlue.png" /> 
									<c:if test="${requestScope.video.isUserLikeVideo(sessionScope.user.getUsername())}">
										<c:set var="image" value="img/likeGreen.png" /> 
									</c:if>
									<c:if test="${!requestScope.video.isUserLikeVideo(sessionScope.user.getUsername())}">
									</c:if>
									<img id="likeImg" src="${image}"  onclick="likeVideo()">
									<div id="likes">
									
										<c:out value="${videoLikes}" />
									</div>
							</li>
							<li>
									<c:if
										test="${!requestScope.video.isUserDislikeVideo(sessionScope.user.getUsername())}">
										<c:set var="image" value="img/dislikeBlue.png" />
									</c:if>
									<c:if test="${requestScope.video.isUserDislikeVideo(sessionScope.user.getUsername())}">
										<c:set var="image" value="img/dislikeRed.png" />
									</c:if>
								<img id="disLikeImg" src="${image}"  onclick="dislikeVideo()">
									<div id="dislikes">
										<c:out value="${videoDislikes}" />
									</div>
							</li>
							
						</ul>
						<ul class="comment1">
							<li><a onclick="showDiv()">View comments</a></li>

						</ul>
					</div>
					<div class="views-count">
						<p>
							<span><c:out value="${views }" /> </span> Views
						</p>
					</div>
					<div class="clear"></div>
				</div>
				<div class="clear"></div>
				<div class="video-details">

					<ul>
						<li><a href="login" id="confirmMessage"
							class="confirmMessage"> </a></li>
						<li><p>
								Uploaded on <a href="#"><c:out value="${videoDate}"></c:out></a>
								by <a href="myChannel"><c:out value="${uploader}"></c:out></a>
							</p></li>
						<li><p>Description for video :</p></li>
						<li><span><c:out value="${description }"></c:out></span></li>

						<div id="comment21"></div>
						<li><a href="login" id="confirmMessage"
							class="confirmMessage"> </a></li>
					</ul>

				</div>
				<div class="clear"></div>

				<div class="clear"></div>

				<div class="clear"></div>
			</div>
			<div class="right-content">
				<div id="myDiv" style="overflow: auto; height: 400px; width: 250px;"
					>
					<div class="popular" id="popular">
						<h3>
							<c:out value="${listname}" />
						</h3>
						<p>
							<c:out value="${playlist.size()}" />
						</p>
						<div class="clear"></div>
					</div>
					<c:set var="playlist" value="${playlist}" />
					<c:forEach items="${playlist}" var="videoList" varStatus="loop">
						<div class="grid1">
							<h3>${loop.index}</h3>
							<h3 id="${loop.index}">${videoList.name}</h3>
							<a id="${videoList.name}" onclick="nextVideo(this.id)">
							<img src="videoPoster/${videoList.name}" title="video-name" /></a>
							<div class="time1">
								<span>Views ${videoList.view}</span>
							</div>

							<div class="grid-info">
								<div class="video-watch">
									<a href="#"></a>
								</div>
								<div class="clear"></div>
								<div class="lables">
									<p>
										Uploader<a href="#"><c:out value="${videoList.uploader}" /></a>
									</p>
								</div>
							</div>
						</div>
					</c:forEach>
					<div class="clear"></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	var checkList = document.getElementById('list1');
	checkList.getElementsByClassName('anchor')[0].onclick = function (evt) {
    	if (checkList.classList.contains('visible'))
        	checkList.classList.remove('visible');
    	else
        	checkList.classList.add('visible');
}
</script>
	<!----End-wrap---->
</body>
</html>