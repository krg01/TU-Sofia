function subscribe(uploader, user) {

	if (uploader == user) {
		showErrMsg();
		return;
	}
	var user = document.getElementById('user');
	if (user == null) {
		var msg = document.getElementById('confirmM');
		msg.style.color = "#ff6666";
		msg.innerHTML = "Login for subscribe.";
	} else {
		$.post("subscribe", {
			channel : uploader
		}).done(

		function(data) {
			document.getElementById('sub').innerHTML = data;
		});
	}
}
function showErrMsg() {
	document.getElementById("subsErrMsg").style.visibility = "visible";
	setTimeout(hideErrMsg, 3000);
}
function hideErrMsg() {
	document.getElementById("subsErrMsg").style.visibility = "hidden";
	return;
}
function chechPlaylist(size) {
	if (size == 0) {
		document.getElementById("popular").style.visibility = "hidden";
	}
}

function showLikedVideos() {
	$.get("likeds", {}, function(result) {
		document.getElementById("searchResults").innerHTML = result;
		document.getElementById('newVideo').style.display = "none";
	});
}
function showAbonatedChannals() {
	$.get("abonatedChannals", {}, function(result) {
		document.getElementById("searchResults").innerHTML = result;
		document.getElementById('newVideo').style.display = "none";
	});
}
function showMyPlaylists() {
	alert("sadasda")
	$.get("myPlaylists", {}, function(result) {
		document.getElementById("searchResults").innerHTML = result;
		document.getElementById('newVideo').style.display = "none";

	});
}
function search() {
	var searchField = document.getElementById("search-field").value;
	var searchType = document.getElementById("search-drop-down").value;
	$.get("doSearch", {
		search : searchField,
		type : searchType
	}, function(result) {
		document.getElementById('searchResults').innerHTML = result;
		document.getElementById('newVideo').style.display = "none";
	});
}

function likeComment(index, id) {
	var user = document.getElementById('user');
	if (user == null) {
		var msg = document.getElementById('confirmMes' + index);
		msg.style.color = "#ff6666";
		msg.innerHTML = "Login for like comment";
	} else {
		$
				.post("comment/like", {
					commentId : id,
					videoName : document.getElementById("videoName").innerHTML
				})
				.done(

						function(data) {
							var pathArray = document.getElementById(id).src
									.split('/');
							var src = ((pathArray[pathArray.length - 1] === 'likeBlue.png') ? 'img/likeGreen.png'
									: 'img/likeBlue.png');
							document.getElementById(id).src = src;
							document.getElementById(index).innerHTML = data;
						});
	}
}
function writeComment() {
	var user = document.getElementById('user');
	if (user == null) {
		var msg = document.getElementById('confirmMessage');
		msg.style.color = "#ff6666";
		msg.innerHTML = "Login for comment video";
	}

	else {
		if (document.getElementById("commentText").value == null
				|| document.getElementById("commentText").value == '') {
			return;
		}
		if (document.getElementById("commentText").value != null) {
			$.post("writeComment", {
				commentText : document.getElementById("commentText").value,
				videoName : document.getElementById("videoName").innerHTML
			},

			function(result) {
				showDiv();
				document.getElementById("comment21").innerHTML = result;

			});
		}
	}
}
function showDiv() {
	$.get('comments', {
		videoName : document.getElementById('videoName').innerHTML
	}, function(result) {
		document.getElementById("comment21").innerHTML = result;
	});
}
function validateText(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z!?.,@ 0-9]+/g, '');	
}
function likeVideo() {
	
	var user =  document.getElementById('user');


		if(user== null){
			 var msg = document.getElementById('confirmMessage');
			 msg.style.color = "#ff6666";
			msg.innerHTML = "Login for vote video";
	    }
		else{
			$.get(
					"video/like", 
					{ videoName: document.getElementById("videoName").innerHTML
					},
					
					function(result){
						document.getElementById("likes").innerHTML =result.likes,
						document.getElementById("dislikes").innerHTML = result.dislikes;
						var pathArray = document.getElementById( 'likeImg' ).src.split( '/' );
						var disLikeArray = document.getElementById( 'disLikeImg' ).src.split( '/' );
				         var src = ((pathArray[pathArray.length-1] === 'likeBlue.png')
				            ? 'img/likeGreen.png'
				            : 'img/likeBlue.png');
				         if(disLikeArray[disLikeArray.length-1] == 'dislikeRed.png'){
				        	 document.getElementById( 'disLikeImg' ).src='img/dislikeBlue.png';
				         }
						        
				         document.getElementById( 'likeImg' ).src=src;
						
				    });
		}
	
}
function dislikeVideo() {
	var user =  document.getElementById('user');
	if(user == null){
		var msg = document.getElementById('confirmMessage');
		 msg.style.color = "#ff6666";
		msg.innerHTML = "Login for vote video";
    }
	else{$.get(
			"video/dislike", 
			{ videoName: document.getElementById("videoName").innerHTML
			},
			
			function(result){
				
				var pathArray = document.getElementById( 'disLikeImg' ).src.split( '/' );
				var likeArray = document.getElementById( 'likeImg' ).src.split( '/' );
				         var src = ((pathArray[pathArray.length-1] === 'dislikeRed.png')
				            ? 'img/dislikeBlue.png'
				            : 'img/dislikeRed.png');
				         document.getElementById( 'disLikeImg' ).src=src;
				         if(likeArray[likeArray.length-1] == 'likeGreen.png'){
				        	 document.getElementById( 'likeImg' ).src='img/likeBlue.png';
				         }        
				
				document.getElementById("likes").innerHTML =result.likes,
				document.getElementById("dislikes").innerHTML = result.dislikes;
    });}
	
}