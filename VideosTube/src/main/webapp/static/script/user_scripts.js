
function showMyVideos(username) {	
	$.get("userVideos", {username : username}, function(result){
		document.getElementById("resultList").innerHTML = result;
    });
}

function search() {
	var searchField = document.getElementById("search-field").value;
	var searchType = document.getElementById("search-drop-down").value;
	$.get(
			"doSearch", 
			{ search: searchField, 
				type: searchType
			},
			function(result){		
				document.getElementById('twPc-div').style.display = "none";
				document.getElementById('my_channel_content').style.display = "none";
				document.getElementById('searchResults').innerHTML = result;
		    });
}

function showPlaylists(user) {
	$.get("userPlaylists", {username : user}, function(result){
		document.getElementById("resultList").innerHTML = result;
    });
}

function unsubscribe(name, user) {
	
	if(user == null){
		var msg = document.getElementById('confirmM');
		 msg.style.color = "#ff6666";
		msg.innerHTML = "Login for subscribe comment";
    }
	else{
		$.post(
			"subscribe", 
			{ channel: name
			}).done(
					
			function(data){
				document.getElementById('subs').innerHTML = data;
		    });}
}
