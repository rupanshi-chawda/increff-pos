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

function isNumber(str) {
  return /^\d+$/.test(str);
}

//BUTTON ACTIONS
function addInventory(event) {
  //Set the values to update
  var $form = $("#inventory-form");
  var json = toJson($form);

  var qty = JSON.parse(json).quantity;

  if(qty.includes("-") || qty.includes("+") || qty.includes("*") || qty.includes("/") || qty.includes(".") || !isNumber(qty)) {
      toastr.warning("Quantity must be whole number", "Error : ");
      return;
  }
  if(parseFloat(qty) > 2147483647) {
      toastr.warning("Quantity is greater than Maximum value allowed", "Error : ");
      return;
  }

  var url = getInventoryUrl();
  wholeInventory.push(json);
  var barcodeInv = $("#inventory-form input[name=barcode]").val();
//  console.log(url);
//  console.log(json);
	var jsonObj = arrayToJson();

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

      displayAddInventory();
    },
    error: function (response) {
        if(response.status == 403) {
            toastr.error("Error: 403 unauthorized");
        }
        else {
            var resp = JSON.parse(response.responseText);
                if (isJson(resp.message) == true) {
                   var jsonObj = JSON.parse(resp.message);
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

  var qty = JSON.parse(json).quantity;

  if(qty.includes("-") || qty.includes("+") || qty.includes("*") || qty.includes("/") || qty.includes(".")) {
      toastr.warning("Quantity must be whole number", "Error : ");
      return;
  }
  if(parseFloat(qty) > 2147483647) {
      toastr.warning("Quantity is greater than Maximum value allowed", "Error : ");
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
  if(file.name.split('.').pop() != "tsv"){
      toastr.warning("File should be TSV");
      return;
  }
  readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
  fileData = results.data;
    var filelen = fileData.length;
    	if(filelen == 0) {
            toastr.warning("File is empty, upload not allowed");
        }
        else if(filelen > 5000) {
    	    toastr.warning("File length exceeds 5000, upload not allowed");
    	}
    	else {
            var headers = ["barcode","quantity"];
      	    if(Object.keys(fileData[0]).length != headers.length) {
                toastr.warning("Number of columns in File do not match. Please check the file and try again");
                return;
            }
            for(var i in headers) {
                if(!fileData[0].hasOwnProperty(headers[i])) {
                    toastr.warning('File column names do not match. Please check the file and try again');
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

  var json = JSON.stringify(fileData);
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
		    if(isJson(resp.message) == true) {
                var jsonObj = JSON.parse(resp.message);
                errorData = jsonObj;
                processCount = fileData.length;
                $("#download-errors").prop('disabled', false);
                resetForm();
                toastr.error("There are errors in file, please download errors", "Error : ");
            }
            else {
                $("#process-data").prop("disabled", false);
                handleAjaxError(response);
            }
		}
    }
  });
}

function downloadErrors() {
  writeFileData(errorData);
}

//UI DISPLAY METHODS
function displayInventoryList(data){

    var $head = $("#total-rows").find("span");
    $head.empty();
    var span = "Total Rows : " + data.length;
    $head.append(span);

	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
//		console.log(e.barcode);
//		console.log(typeof e.barcode);
        inventoryList.set(e.barcode, e.quantity);
		var buttonHtml = '<button onclick="displayEditInventory(\'' + e.barcode + '\')" class="btn table__button-group" title="Edit Inventory"><i class="fa-solid fa-pencil" style="color:#007BFF"></i></button>'
        var row = '<tr>'
        + '<td>' + e.id + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditInventory(barcode) {
  var url = getInventoryUrl() + "/" + barcode;
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
}

function updateFileName() {
  var $file = $("#inventoryFile");
  var fileName = $file.val();
  $("#inventoryFileName").html(fileName);
  activateUpload();
}

function displayUploadData() {
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
  $("#add-inventory").click(addInventory);
  $("#update-inventory").click(updateInventory);
  $("#refresh-data").click(getInventoryList);
  $("#upload-data").click(displayUploadData);
  $("#process-data").click(processData);
  $("#download-errors").click(downloadErrors);
  $("#inventoryFile").on("change", updateFileName);
  $("#download-csv").click(downloadCsv);
  $("#add-modal").click(displayAddInventory);
}

$(document).ready(init);
$(document).ready(getInventoryList);
$(document).ready(activateNav);