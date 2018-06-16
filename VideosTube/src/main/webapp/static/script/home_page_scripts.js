
function showLikedVideos() {
	document.getElementById('videosList').style.display = "none";
	$.get("likeds", {}, function(result){
		document.getElementById("liked-videos-div").innerHTML = result;
    });
}
function showAbonatedChannals() {
	document.getElementById('videosList').style.display = "none";
	$.get("abonatedChannals", {}, function(result){
		document.getElementById("liked-videos-div").innerHTML = result;
    });
}
function showMyPlaylists() {
	document.getElementById('videosList').style.display = "none";
	$.get("myPlaylists", {}, function(result){
		document.getElementById("liked-videos-div").innerHTML = result;
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
				document.getElementById('videosList').innerHTML = result;
		    });
}
function validate(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z 0-9]+/g, '');	
}