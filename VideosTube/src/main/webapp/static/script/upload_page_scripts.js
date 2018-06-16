
function showLikedVideos() {
	document.getElementById("panel-body").style.display = "none";
	$.get("likeds", {}, function(result){
		document.getElementById("results").innerHTML = result;
    });
}
function showAbonatedChannals() {
	document.getElementById("panel-body").style.display = "none";
	$.get("abonatedChannals", {}, function(result){
		document.getElementById("results").innerHTML = result;
    });
}
function showMyPlaylists() {
	document.getElementById("panel-body").style.display = "none";
	$.get("myPlaylists", {}, function(result){
		document.getElementById("results").innerHTML = result;
    });
}

function search() {
	var searchField = document.getElementById("search-field").value;
	var searchType = document.getElementById("search-drop-down").value;
	document.getElementById("panel-body").style.display = "none";
	
	$.get(
			"doSearch", 
			{ search: searchField, 
				type: searchType
			},
			function(result){		
				document.getElementById('results').innerHTML = result;
		    });
}

function validateName(input){

	var regex = new RegExp("[^a-zA-Z_.\s \n\r0-9]+");
	var status = document.getElementById("video-name-label")
	var txt = document.getElementById('comment').value;

	if(txt.length < 3){
		input.style.backgroundColor = "#ff6666";
		status.innerText = "Video name should be at least 3 symbols";
		status.style.color = "#ff6666";
		return;
	}
	
	if(regex.test(input.value)){
		input.style.backgroundColor = "#ff6666";
		status.innerText = " Invalid video name";
		status.style.color = "#ff6666";
		
	}else{
		input.style.backgroundColor = "#66cc66";
		status.innerText = "Video name";
		status.style.color = "black";
	}
	checkName();
}

function checkName(txt) {
	
    txt.value = txt.value.replace(/[^a-zA-Z 0-9]+/g, '');
	var input = document.getElementById('comment').value;
    var status = document.getElementById('videonameAllowedMsg');
	if(input.length > 0){
		$.get("isVideoNameAllowed", {videoName : input}, function(result){
				if(result){
					status.style.color = "#66cc66";
					status.innerHTML = "";
					status.style.visibility = "hidden" ;

				}else{
					status.style.color = "#ff6666";
					status.innerHTML = "Name already exists.";
					status.style.visibility = "visible";
				}
	    });
	}else{
		statusUsername.style.visibility='hidden';
	}
}

function validate(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z 0-9]+/g, '');
}


//window.onload = function() {
//	var dropbox = document.getElementById("drop-zone");
//	dropbox.addEventListener("dragenter", noop, false);
//	dropbox.addEventListener("dragexit", noop, false);
//	dropbox.addEventListener("dragover", noop, false);
//	dropbox.addEventListener("drop", dropUpload, false);
//}
//
//function noop(event) {
//	event.stopPropagation();
//	event.preventDefault();
//}
//
//function dropUpload(event) {
//	noop(event);
//	var files = event.dataTransfer.files;
//
//	for (var i = 0; i < files.length; i++) {
//		upload(files[i]);
//	}
//}

function upload(file) {
	//document.getElementById("status").innerHTML = "Uploading " + file.name;
	var formData = new FormData();
	formData.append("video", file);

	var xhr = new XMLHttpRequest();
	xhr.upload.addEventListener("progress", uploadProgress, false);
	xhr.addEventListener("load", uploadComplete, false);
	xhr.open("POST", "UploadVideoServlet", true); // If async=false, then you'll miss progress bar support.
	xhr.send(formData);
}

function uploadProgress(event) {
	// Note: doesn't work with async=false.
	var progress = Math.round(event.loaded / event.total * 100);
	document.getElementById("progress-bar").innerHTML = "Progress "
			+ progress + "%";
}

function uploadComplete(event) {
	document.getElementById("progress-bar").innerHTML = event.target.responseText;
}

function uploadByButton(file) {
	var formData = new FormData();
	formData.append("video", file);

	var xhr = new XMLHttpRequest();
	xhr.open("POST", "UploadVideoServlet", true); // If async=false, then you'll miss progress bar support.
	xhr.send(formData);
	document.getElementById("progress-bar-butt").innerHTML = "Complate"
}
function ValidateText(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z_.\s \n\r0-9!?@,()&]+/g, '');
}

function fileSize(){
	if(document.getElementById('js-upload-files').files[0].size/1024/1024>500){
		document.getElementById("js-upload-submit").disabled = true;
	}
	else{
		document.getElementById("js-upload-submit").disabled = false;
	}

}