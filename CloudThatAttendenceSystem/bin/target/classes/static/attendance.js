
var lat="";
var long="";
var ip="";

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
		 success: function (data)
			 {
			  if(data.status==200)
              {
				  document.getElementById("nameattendee").innerHTML ="Welcome "+data.username;
				  checklog();
				  getLocation() ;
				 
              }
              else
              {
            	  pageRedirect("index.html");
              }
			 }
		 
		 });
    }
    else{
      
    }
	
   });



function getLocation() {
	  if (navigator.geolocation) {
	    navigator.geolocation.watchPosition(showPosition);
	  } else { 
	    alert("Location is not supported by browser");
	  }
	   
	}
	    
	function showPosition(position) {
	   lat= position.coords.latitude;
	   long= position.coords.longitude;
	   
	}

function checklog()
{
	  getLocation() ;
	  attlog();
	 $.ajax({
		 type: "GET",
		 contentType : "application/json",
		 url : "/checklog/",
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function (data)
			 {
			    	
				 if(data.status==200)
	             {
					 document.getElementById("markbutton").innerHTML ="<button onclick='markin()' class='button'>Mark In</button>";
				 }
			 else
				 {
				 document.getElementById("markbutton").innerHTML ="<button onclick='markout()' class='button'>Mark out</button>";
				 }
			 }
		 
		 });
	
}


function markin()
{
	 $.ajax({
		 type: "POST",
		 contentType : "application/json",
		 url : "/markin/"+ip+"/"+lat+"/"+long,
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function (data)
			 {
			    	
				 if(data.status==200)
	             {
					pageRedirect("attendance.html");
	             }
			 else
				 {
				 alert(data.message+"  Your Ip :"+ip+"  Your Lat :"+lat+" Your Long : "+long);
				 }
			 }
		 
		 });
}

function markout()
{
	 $.ajax({
		 type: "POST",
		 contentType : "application/json",
		 url : "/markout/"+ip+"/"+lat+"/"+long,
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function (data)
			 {
			    	
				 if(data.status==200)
	             {
					pageRedirect("attendance.html");
	             }
			 else
				 {
				  alert(data.message+"  Your Ip :"+ip+"  Your Lat :"+lat+" Your Long : "+long);
				 }
			 }
		 
		 });
}

function attlog()
{
	
	 var alltenlog='';
	 $.ajax({
		 type: "GET",
		 contentType : "application/json",
		 url : "/getattendacelog/",
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function (data)
			 {
			    	
				 if(data.length>0)
	             {
					 for(var i = 0; i < data.length; i++) 
				        {
						 if(data[i].exitTime!=null && data[i].exitTime!=undefined && data[i].exitTime!='')
						 alltenlog+=' <tr><td>'+data[i].entryTime+'</td> <td>'+data[i].exitTime+'</td> <td>'+data[i].logdate+'</td></tr>';
						 else
							 alltenlog+=' <tr><td>'+data[i].entryTime+'</td> <td>'+"---------"+'</td> <td>'+data[i].logdate+'</td></tr>';
				        }
					 document.getElementById("logattendance").innerHTML =alltenlog;
	             }
				
			 }
		
		 });
	 
}


function logout()
{
   localStorage.removeItem('atttoken');
   pageRedirect('index.html');
}
