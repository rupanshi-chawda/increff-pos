function getUserUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/admin/user";
}

function resetForm() {
  var element = document.getElementById("user-form");
  element.reset();
}

//BUTTON ACTIONS
function addUser(event) {
  //Set the values to update
  var $form = $("#user-form");
  var json = toJson($form);
  var url = getUserUrl();

  $.ajax({
    url: url,
    type: "POST",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      getUserList();
      toastr.success("User Added Successfully", "Success : ");
      resetForm();
    },
    error: handleAjaxError,
  });

  return false;
}

function getUserList() {
  var url = getUserUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayUserList(data);
    },
    error: handleAjaxError,
  });
}

var deleteid = 0;
function displayDeleteUser(id) {
  deleteid = id;
  $("#delete-user-modal").modal("toggle");
  var $head = $("#delete-user-modal").find("h5");
    $head.empty();
    var span = "Delete User - " + id;
    $head.append(span);
}

function deleteUser(event) {
  var url = getUserUrl() + "/" + deleteid;
  $("#delete-user-modal").modal("toggle");

  $.ajax({
    url: url,
    type: "DELETE",
    success: function (data) {
      getUserList();
      toastr.success("User Deleted Successfully", "Success : ");
    },
    error: handleAjaxError,
  });
}

//UI DISPLAY METHODS

function displayUserList(data){

    var $head = $("#total-rows").find("span");
    $head.empty();
    var span = "Total Rows : " + data.length;
    $head.append(span);

	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		if(e.role == "supervisor") {
		    continue;
		}
		else {
	        var buttonHtml = '<button onclick="displayDeleteUser(' + e.id + ')" class="btn table__button-group" id="delete-button" title="Delete User"><i class="fa-solid fa-trash" style="color:#DC3545"></i></button>'
    		var row = '<tr>'
    		+ '<td>' + e.id + '</td>'
    		+ '<td>' + e.email + '</td>'
    		+ '<td>' + e.role + '</td>'
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';
            $tbody.append(row);
            if (e.role == "supervisor") {
                  $("#delete-button").prop("disabled", true);
            }
		}
	}
}

function displayAddUser(data) {
  $("#add-user-modal").modal("toggle");
}

function checkform() {
  var f = document.forms["user-form"].elements;
  var cansubmit = true;
  for (var i = 0; i < f.length; i++) {
    if (f[i].value.length == 0) cansubmit = false;
  }
  document.getElementById("add-user").disabled = !cansubmit;
}

function resetButtons(event){
    resetForm();
    checkform();
}

function activateNav(){
    // Get the current URL path
    var currentPath = window.location.pathname;

    // Loop through each navigation link
    $('.nav-link').each(function() {
      // Get the link's href attribute
      var linkHref = $(this).attr('href');

      // If the link's href attribute matches the current URL path
      if (currentPath === linkHref) {
        // Add the "active" class to the link's parent list item
        $(this).parent().addClass('active');
      }
    });
}

//INITIALIZATION CODE
function init() {
  $("#add-user").click(addUser);
  $("#delete-user").click(deleteUser);
  $("#add-modal").click(displayAddUser);
}

$(document).ready(init);
$(document).ready(getUserList);
$(document).ready(activateNav);