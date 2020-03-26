
var lat="";
var long="";
var ip="";
var modal = document.getElementById("myModal");
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];

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
			  if(data.status==150)
              {
				  document.getElementById("nameattendee").innerHTML ="Welcome "+data.username;
				  document.getElementById("employeeattlog").innerHTML ='<button class="button" onclick="listofemp()">GET EMPLOYEE ATTENDANCE LOG</button>';
				  
				  checklog();
				  getLocation() ;
				 
              }
			  
			  if(data.status==200)
				  {
				  document.getElementById("nameattendee").innerHTML ="Welcome "+data.username;
				  checklog();
				  getLocation() ;
				  }
			  if(data.status!=200 && data.status!=150)
			  {
				  pageRedirect("index.html");
			  }
              
			 }
		 
		 });
    }
    else{
      
    }
	
   });


function listofemp()
{
	var datalog=' <thead><tr><th> <h2>Employee details</h2></th><th><button class="button" onclick=pageRedirect("attendance.html")>GO TO YOUR ATTENDANCE PAGE</button></th> </thead><thead> <tr> <th>Name </th><th>Email Id</th><th>RFID</th><th>Download Attendance Logs</th></tr></thead><tbody id="logattendance">';
	 $.ajax({
		 type: "GET",
		 contentType : "application/json",
		 url : "/listofEmployee/",
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function (data)
			 {
			     if(data!=null)
	             {
			    	 for(var i = 0; i < data.length; i++) 
				        {
			    		 datalog+='<tr><td><a onclick="getemplog('+data[i].id+')">'+data[i].name+'</a></td> <td>'+data[i].email+'</td> <td>'+data[i].rfid+'</td><td><a onclick="getfilename('+data[i].id+')">Download</a></td></tr>';
				       }
			    	 datalog+=" </tbody></table>";
			    	 document.getElementById("tableid").innerHTML =datalog;
				 }
			 }
		 
		 });
	
	 
	
}
function getfilename(userid)
{
	$('#downloadbu').html('<button class="downloadbutton" onclick="getfillogename('+userid+')">Download</button>');
	  modal.style.display = "block";
}
span.onclick = function() {
	  modal.style.display = "none";
	}

	   $(function() {
	     var vvv='<div id="reportrange" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc; width: 100%"><i class="fa fa-calendar"></i>&nbsp;  <span id="datevalue"></span> <i class="fa fa-caret-down"></i></div>'
	   
	$('#cin').html(vvv);
	    var start = moment().subtract(29, 'days');
	    var end = moment();

	    function cb(start, end) {
	    
	        $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
	    }

	    $('#reportrange').daterangepicker({
	        startDate: start,
	        endDate: end,
	        ranges: {
	           'Today': [moment(), moment()],
	           'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	           'Last 7 Days': [moment().subtract(6, 'days'), moment()],
	           'Last 30 Days': [moment().subtract(29, 'days'), moment()],
	           'This Month': [moment().startOf('month'), moment().endOf('month')],
	           'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
	        }
	    }, cb);

	    cb(start, end);

	});

	// When the user clicks anywhere outside of the modal, close it
	window.onclick = function(event) {
	  if (event.target == modal) {
	    modal.style.display = "none";
	  }
	}

	
function getfillogename(userid)
{
	
	
	var daterange=document.getElementById("datevalue").innerHTML;
	 $.ajax({
		 type: "GET",
		 contentType : "application/json",
		 url : "/getfilename/"+userid+"/"+daterange,
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function (data)
			 {
			     if(data!=null)
	             {
			    	 download(data);	
				 }
			 }
		 
		 });	
}

function download(filename)
{       
document.getElementById('my_iframe').src = "/downloadFile/"+filename;  
}
function getemplog(userid)
{
	var datalog=' <thead><tr><th> <h2>Employee Logs </h2></th><th><button class="button" onclick=pageRedirect("attendance.html")>GO TO YOUR ATTENDANCE PAGE</button></th> </thead><thead> <tr> <th>Name </th><th>entry Time Id</th><th>Exit Time</th><th>Log Date</th></tr></thead><tbody id="logattendance">';
	 $.ajax({
		 type: "GET",
		 contentType : "application/json",
		 url : "/getattendaceemplog/"+userid,
		 headers: {"Authorization": localStorage.getItem("atttoken")},
		 success: function (data)
			 {
			     if(data!=null)
	             {
			    	 for(var i = 0; i < data.length; i++) 
				        {
			    		 datalog+='<tr><td>'+data[i].name+'</a></td> <td>'+data[i].entryTime+'</td> <td>'+data[i].exitTime+'</td><td>'+data[i].logdate+'</td></tr>';
				       }
			    	 datalog+=" </tbody></table>";
			    	 document.getElementById("tableid").innerHTML =datalog;
				 }
			 }
		 
		 });
}
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
	var isok=true;
	if(ip=='')  
		{
		isok=false;
		alert("Your Ip is not detected,Please change the Configuration for this app.");
		}
	if(lat=='' || long=='')
		{
		isok=false;
		alert("Your Location  is not detected,Please change the Configuration for this app.");
		}
		if(isok)
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
	
}

function markout()
{
	var isok=true;
	if(ip=='')  
		{
		isok=false;
		alert("Your Ip is not detected,Please change the Configuration for this app.");
		}
	if(lat=='' || long=='')
		{
		isok=false;
		alert("Your Location  is not detected,Please change the Configuration for this app.");
		}
		if(isok)
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
