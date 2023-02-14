var wholeInventory = []

function getInventoryUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/inventory";
}
const inventoryList = new Map();

function resetForm() {
  var element = document.getElementById("inventory-form");
  element.reset();
}

function arrayToJson() {
    let json = [];
    for(i in wholeInventory) {
        let data = {};
        data["barcode"]=JSON.parse(wholeInventory[i]).barcode;
        data["quantity"]=JSON.parse(wholeInventory[i]).quantity;
        json.push(data);
    }
    return JSON.stringify(json);
}

function isJson(str) {
  try {
    JSON.parse(str);
  } catch (e) {
    return false;
  }
  return true;
}

//BUTTON ACTIONS
function addInventory(event) {
  //Set the values to update
  var $form = $("#inventory-form");
  var json = toJson($form);

  var qty = JSON.parse(json).quantity;

  if(isNaN(qty) || isNaN(parseFloat(qty))) {
      toastr.error("Quantity must be number", "Error : ");
      return;
  }
  if(parseFloat(qty) > 2147483647) {
      toastr.error("Quantity is greater than Maximum value allowed", "Error : ");
      return;
  }

  var url = getInventoryUrl();
  wholeInventory.push(json);
  var barcodeInv = $("#inventory-form input[name=barcode]").val();
  console.log(barcodeInv);
//  console.log(url);
//  console.log(json);
	var jsonObj = arrayToJson();
	console.log(wholeInventory);
    console.log(url);

  $.ajax({
    url: url,
    type: "POST",
    data: jsonObj,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      getInventoryList();
      resetForm();
      wholeInventory=[];
      if (inventoryList.has(barcodeInv)) {
        toastr.success(
          "Inventory available, Updated it successfully",
          "Success : "
        );
      } else {
        toastr.success("Inventory Added Successfully", "Success : ");
      }
    },
    error: function (response) {
        console.log(response);
        if(response.status == 403) {
            toastr.error("Error: 403 unauthorized");
        }
        else {
            var resp = JSON.parse(response.responseText);
                if (isJson(resp.message) == true) {
                   var jsonObj = JSON.parse(resp.message);
                   console.log(jsonObj);
                   toastr.error(jsonObj[0].message, "Error : ");
                } else {
                   handleAjaxError(response);
                }
            }
        wholeInventory=[];
    }
  });

  return false;
}

function updateInventory(event) {
  //Get the ID
  var barcode = $("#inventory-edit-form input[name=barcode]").val();
  var url = getInventoryUrl() + "/" + barcode;

  //Set the values to update
  var $form = $("#inventory-edit-form");
  var json = toJson($form);
  console.log(json);

  var qty = JSON.parse(json).quantity;

  if(isNaN(qty) || isNaN(parseFloat(qty))) {
      toastr.error("Quantity must be number", "Error : ");
      return;
  }
  if(parseFloat(qty) > 2147483647) {
      toastr.error("Quantity is greater than Maximum value allowed", "Error : ");
      return;
  }

  $.ajax({
    url: url,
    type: "PUT",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      $("#edit-inventory-modal").modal("toggle");
      getInventoryList();
      toastr.success("Inventory Updated Successfully", "Success : ");
    },
    error: handleAjaxError,
  });

  return false;
}

function getInventoryList() {
  var url = getInventoryUrl();

  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      console.log(data);
      displayInventoryList(data);
    },
    error: handleAjaxError,
  });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
  var file = $("#inventoryFile")[0].files[0];
  console.log(file);
  readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
  fileData = results.data;
    var filelen = fileData.length;
    	if(filelen > 5000) {
    	    toastr.error("file length exceeds 5000, Not Allowed");
    	}
    	else {
            var headers = ["barcode","quantity"];
      	    if(Object.keys(fileData[0]).length != headers.length) {
                toastr.error("Number of columns in File do not match. Please check the file and try again");
                return;
            }
            for(var i in headers) {
                if(!fileData[0].hasOwnProperty(headers[i])) {
                    toastr.error('File columns Names do not match. Please check the file and try again');
                    return;
                }
            }
    	    uploadRows();
    	}
}

function uploadRows() {
  //Update progress
  updateUploadDialog();
  $("#process-data").prop("disabled", true);
  //If everything processed then return
//  if (processCount == fileData.length) {
//    toastr.success("Rows uploaded Successfully", "Success : ");
//    return;
//  }
//  if (errorData.length > 0) {
//    $("#download-errors").prop("disabled", false);
//  }

  //Process next row
//  var row = fileData[processCount];
//  processCount++;

  var json = JSON.stringify(fileData);
  console.log(json);
  var url = getInventoryUrl();

  //Make ajax call
  $.ajax({
    url: url,
    type: "POST",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      console.log(response);
      errorData = response;
      resetForm();
      getInventoryList();
      toastr.success("Inventory Uploaded Successfully", "Success : ");
    },
    error: function (response) {
        if(response.status == 403){
                toastr.error("403 FOrbidden");
        }
        else {
		    var resp = JSON.parse(response.responseText);
			var jsonObj = JSON.parse(resp.message);
			console.log(jsonObj);
	        errorData = jsonObj;
			processCount = fileData.length;
			console.log(response);
			$("#download-errors").prop('disabled', false);
			resetForm();
            toastr.error("There are errors in file, please Download Errors", "Error : ");
		}
    }
  });
}

function downloadErrors() {
  writeFileData(errorData);
}

//UI DISPLAY METHODS
function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	console.log(data);
	for(var i in data){
		var e = data[i];
//		console.log(e.barcode);
//		console.log(typeof e.barcode);
        inventoryList.set(e.barcode, e.quantity);
		var buttonHtml = '<button onclick="displayEditInventory(\'' + e.barcode + '\')" class="btn table__button-group"><i class="fa-solid fa-pencil" style="color:#00295F"></i></button>'
        var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditInventory(barcode) {
  var url = getInventoryUrl() + "/" + barcode;
  console.log(url);
  console.log(barcode);
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayInventory(data);
    },
    error: handleAjaxError,
  });
}

function resetUploadDialog() {
  //Reset file name
  var $file = $("#inventoryFile");
  $file.val("");
  $("#inventoryFileName").html("Choose File");
  //Reset various counts
  processCount = 0;
  fileData = [];
  errorData = [];
  //Update counts
  updateUploadDialog();
}

function updateUploadDialog() {
  $("#rowCount").html("" + fileData.length);
//  $("#processCount").html("" + processCount);
//  $("#errorCount").html("" + errorData.length);
}

function updateFileName() {
  var $file = $("#inventoryFile");
  var fileName = $file.val();
  $("#inventoryFileName").html(fileName);
  activateUpload();
}

function displayUploadData() {
  console.log("hello");
  resetUploadDialog();
  $("#upload-inventory-modal").modal("toggle");
  $("#download-errors").prop("disabled", true);
  $("#process-data").prop("disabled", true);
}

function activateUpload() {
  $("#process-data").prop("disabled", false);
}

function downloadCsv() {
  window.location.href = getInventoryUrl() + "/exportcsv";
  console.log(getInventoryUrl() + "/exportcsv");
}

function displayInventory(data) {
  $("#inventory-edit-form input[name=barcode]").val(data.barcode);
  $("#inventory-edit-form input[name=quantity]").val(data.quantity);
  $("#edit-inventory-modal").modal("toggle");

  var $head = $("#edit-inventory-modal").find("h5");
  $head.empty();
  var span = "Edit Inventory - " + data.id;
  $head.append(span);

  document.getElementById("update-inventory").disabled = true;
}

function checkform() {
  var f = document.forms["inventory-form"].elements;
  var cansubmit = true;
  for (var i = 0; i < f.length; i++) {
    if (f[i].value.length == 0) cansubmit = false;
  }
  document.getElementById("add-inventory").disabled = !cansubmit;
}

function displayAddInventory(data) {
  $("#add-inventory-modal").modal("toggle");
}

function enableUpdate() {
  document.getElementById("update-inventory").disabled = false;
}


function resetButtons(event){
    resetForm();
    checkform();
}

//INITIALIZATION CODE
function init() {
  $("#add-inventory").click(addInventory);
  $("#update-inventory").click(updateInventory);
  $("#refresh-data").click(getInventoryList);
  $("#upload-data").click(displayUploadData);
  $("#process-data").click(processData);
  $("#download-errors").click(downloadErrors);
  $("#inventoryFile").on("change", updateFileName);
  $("#download-csv").click(downloadCsv);
//  $("#inventoryFile").click(activateUpload);
  $("#add-modal").click(displayAddInventory);
}

$(document).ready(init);
$(document).ready(getInventoryList);