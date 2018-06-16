function addPlaylist(listName){
	var user =  document.getElementById('user');
	if(user == null){
		var msg = document.getElementById('confirmM');
		 msg.style.color = "#ff6666";
		msg.innerHTML = "Login for subscribe comment";
    }
	else{
		$.post(
				"addPlaylist", 
				{ playlist: listName,
					videoName: document.getElementById("videoName").innerHTML
				
				},
				
				function(result){
					var msg = document.getElementById('addToList');
					 msg.style.color = "#ff6666";
					msg.innerHTML = result;
			    });
	
	}	
}
function startNextVideo(id){
	 $.get("nextVideo", {}, function(result){
		  document.getElementById("newVideo").innerHTML = result;
		    });
}
function nextVideo(video){
	 $.get("videoNew", {name : video}, function(result){
//		 document.getElementById('newVideo').style.display = "none";
		 document.getElementById("newVideo").innerHTML = result;
//	 document.getElementById("videoNewa").innerHTML = resu1lt;
		    });
}
