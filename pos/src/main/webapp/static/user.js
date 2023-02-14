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
	console.log('Printing user data');
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="displayDeleteUser(' + e.id + ')" class="btn table__button-group" id="delete-button"><i class="fa-solid fa-trash" style="color:#00295F"></i></button>'
		var row = '<tr>'
		+ '<td> <i class="fa-solid fa-circle-user" style="color:#00295F"></i> </td>'
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

//INITIALIZATION CODE
function init() {
  $("#add-user").click(addUser);
  $("#delete-user").click(deleteUser);
  $("#add-modal").click(displayAddUser);
}

$(document).ready(init);
$(document).ready(getUserList);
