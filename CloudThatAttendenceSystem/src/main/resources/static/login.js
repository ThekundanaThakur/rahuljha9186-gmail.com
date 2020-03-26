function login()
{
	    var signinemail=document.getElementById("username").value.trim();
	    var signinpassword=document.getElementById("password").value.trim();
	    var valid=true;
	if(signinemail=='')
	    {
	        valid=false;
	        $("#username").css("border-color", "deeppink");
	    }
	   else
	    $("#username").css("border-color", "");

	    if(signinpassword=='')
	    {
	        valid=false;
	        $("#password").css("border-color", "deeppink");
	    }
	   else
	    $("#password").css("border-color", "");
	    
    if(valid)
    {
       
        $.ajax({
            url : "/validiateuser/"+signinemail+"/"+signinpassword,
            type : "POST",
            contentType : "application/json",
            dataType : "json",
            async : false,
            success : function(data) 
            {
                      if(data.status==200)
                      {
                        localStorage['atttoken'] = data.jwstoken;
                        pageRedirect("attendance.html");
                      }
                      else
                      {
                        $("#resultmsg").text(data.message);
                      }
                    
                 },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    $("#resultmsg").text('Something went wrong.');
            }	
        });	
    }
 
}

function pageRedirect(page) {
	window.location.replace(page);
	
	} 


$(document).ready(function(){

	if(localStorage.getItem("atttoken")!=null && localStorage.getItem("atttoken")!=undefined && localStorage.getItem("atttoken")!='')
	{
	   $.ajax({
		 type: "GET",
		 contentType : "application/json",
		 url : "/checkAuth/",
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function ()
			 {
			   
			  pageRedirect("attendance.html");
			 },
		 
		 });
    }
    else{
      
    }
	
   });

