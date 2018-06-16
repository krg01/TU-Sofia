function checkPass()
{
    //Store the password field objects into variables ...
    var pass1 = document.getElementById('pass');
    var pass2 = document.getElementById('confirm');
    //Store the Confimation Message Object ...
    var message = document.getElementById('confirmMessage');
    //Set the colors we will be using ...
    var goodColor = "#66cc66";
    var badColor = "#ff6666";
	
    if(pass1.value == pass2.value){		

        pass2.style.backgroundColor = goodColor;
        message.style.color = goodColor;
        message.innerHTML = "Passwords Match"
    }else{

        pass2.style.backgroundColor = "#ff6666";
        message.style.color = badColor;
        message.innerHTML = "Passwords Do Not Match!"
    }
} 

// validates text only
function Validate(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z_\n\r.0-9]+/g, '');
    check();
}

function check() {
    var text = document.getElementById('username').value;
    var val = document.getElementById('username');
    var statusUsername = document.getElementById('usernameAllowedMsg');
	if(text.length > 0){
		$.get("isUsernameAllowed", {username : text}, function(result){
				if(result){
					val.style.backgroundColor = "#ffffff";
					statusUsername.innerHTML = "";
					statusUsername.style.visibility='hidden';

				}else{
					statusUsername.style.color = "#ff6666";
					statusUsername.innerHTML = "username already exists";
					statusUsername.style.visibility='visible';
				}
	    });
	}else{
		statusUsername.style.visibility='hidden';
	}
}

function CheckLength(){
	var val = document.getElementById('username');
	var statusUsername = document.getElementById('errLast');
	if(val.value.length < 4 || val.value.length > 12){
		val.style.backgroundColor = "#ff6666";
		statusUsername.style.color = "#ff6666";
		statusUsername.innerHTML = "Username shoud be between 4 and 12 symbols."
	}else{
		
		
		val.style.backgroundColor = "#ffffff";
		statusUsername.innerHTML = ""
	}
	check();
}
// validate email
function email_validate(email)
{
	var regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;

    if(regMail.test(email) == false)
    {
    document.getElementById("status").innerHTML    = "<span class='warning' style=color:red;>Email address is not valid yet.</span>";
    }
    else
    {
    document.getElementById("status").innerHTML	= "<span class='valid' style=color:#66cc66;>Thanks, you have entered a valid Email address!</span>";	
    }
}

//validate password length
function PasswordLength(){
	 var pass = document.getElementById('pass');
	 var msg = document.getElementById('confirmMessage');

	if(pass.value.length < 6 || pass.value.length > 20){
		pass.style.backgroundColor = "#ff6666";
		msg.style.color = "#ff6666";
		msg.innerHTML = "Incorrect Password Length! Should be between 6 and 20 symbols."
	}else{
		pass.style.backgroundColor = "#ffffff";
		msg.innerHTML = "";
	}
}



