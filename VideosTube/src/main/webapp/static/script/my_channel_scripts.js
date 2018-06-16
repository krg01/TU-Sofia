window.onload = function(){
	showMyVideos();
	hideEditButton();
}
function viewEditButton() {
	var img = document.getElementById ('edit-but') ;
	img.style.visibility = 'visible';
}
function hideEditButton() {
	var img = document.getElementById ('edit-but') ;
	img.style.visibility = 'hidden';
}

var span = document.getElementsByClassName("close")[0];
var modal = document.getElementById('myModal');

function displayModal() {	
	modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Create Playlist Dialog
var playlistSpan = document.getElementsByClassName("playlist-dialog-close")[0];
var playlistModal = document.getElementById('create-playlist-modal');

function openDialog() {

	playlistModal.style.display = "block";
}

playlistSpan.onclick = function() {
	playlistModal.style.display = "none";
}

function createPlaylist() {
	hideAll();
	document.getElementById("my-playlists").style.display = "block";

	var playlistName = document.getElementById("playlist-name").value;
	playlistModal.style.display = "none";
	$.post("createPlaylist", {name: playlistName}, function(result){
		document.getElementById("my-playlists").innerHTML = result;
    });
}
function showLikedVideos() {
	hideAll();
	document.getElementById("liked-videos-div").style.display = "block";
	$.get("likeds", {}, function(result){
		document.getElementById("liked-videos-div").innerHTML = result;
    });
}
function showPlaylists() {
	hideAll();
	document.getElementById("my-playlists").style.display = "block";
	$.get("myPlaylists", {}, function(result){
		document.getElementById("my-playlists").innerHTML = result;
    });
}

function showMyVideos() {
	hideAll();
	document.getElementById("liked-videos-div").style.display = "block";
	$.get("myVideos", {}, function(result){
		document.getElementById("liked-videos-div").innerHTML = result;
    });
}

function swohSubscriptions() {
	hideAll();
	document.getElementById("subscriptionsDiv").style.display = "block";

	$.get("abonatedChannals", {}, function(result){
		document.getElementById("subscriptionsDiv").innerHTML = result;
    });
}

function hideAll() {
	document.getElementById("subscriptionsDiv").style.display = "none";
	document.getElementById("my-playlists").style.display = "none";
	document.getElementById("videoBox").style.display = "none";
	document.getElementById("liked-videos-div").style.display = "none";
}

function Validate(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z _\n\r. 0-9]+/g, '');
}

function showError() {
	document.getElementById("subscribeError").style.visibility = "visible" ;
}
function hideError() {
	document.getElementById("subscribeError").style.visibility = "hidden" ;
}

function search() {
	hideAll();
	document.getElementById("liked-videos-div").style.display = "block";
	var searchField = document.getElementById("search-field").value;
	var searchType = document.getElementById("search-drop-down").value;
	
	$.get(
			"doSearch", 
			{ search: searchField, 
				type: searchType
			},
			function(result){		
				document.getElementById('liked-videos-div').innerHTML = result;
		    });
}

function printMessage() {
	document.getElementById("subsErrMsg").style.visibility = "visible" ;
    setTimeout(hideErrMsg, 3000);
}

function hideErrMsg(){
	document.getElementById("subsErrMsg").style.visibility = "hidden" ;
	return;
}
