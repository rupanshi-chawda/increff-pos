var wholeBrand = []
function getBrandUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/brand";
}

function resetForm() {
  var element = document.getElementById("brand-form");
  element.reset();
}

function arrayToJson() {
    let json = [];
    for(i in wholeBrand) {
        let data = {};
        data["brand"]=JSON.parse(wholeBrand[i]).brand;
        data["category"]=JSON.parse(wholeBrand[i]).category;
        json.push(data);
    }
    return JSON.stringify(json);
}

//BUTTON ACTIONS
function addBrand(event) {
  //Set the values to update
  var $form = $("#brand-form");
  var json = toJson($form);
  wholeBrand.push(json)
  var url = getBrandUrl();
  var jsonObj = arrayToJson();
  $.ajax({
    url: url,
    type: "POST",
    data: jsonObj,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      wholeBrand=[];
      resetForm();
      getBrandList();
      toastr.success("Brand Added Successfully", "Success : ");
      displayAddBrand();
    },
    error: function (response) {
        if(response.status == 403) {
            toastr.error("Error: 403 unauthorized");
        }
        else {
            var resp = JSON.parse(response.responseText);
            if(isJson(resp.message) == true) {
                var jsonObj = JSON.parse(resp.message);
                toastr.error(jsonObj[0].message, "Error : ");
            }
            else {
                handleAjaxError(response);
            }
        }
        wholeBrand=[];
    }
  });

  return false;
}

function updateBrand(event) {
  //Get the ID
  var id = $("#brand-edit-form input[name=id]").val();
  var url = getBrandUrl() + "/" + id;
  //Set the values to update
  var $form = $("#brand-edit-form");
  var json = toJson($form);

  $.ajax({
    url: url,
    type: "PUT",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      $("#edit-brand-modal").modal("toggle");
      getBrandList();
      toastr.success("Brand Updated Successfully", "Success : ");
    },
    error: handleAjaxError,
  });

  return false;
}

function getBrandList() {
  var url = getBrandUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayBrandList(data);
    },
    error: handleAjaxError,
  });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
  var file = $("#brandFile")[0].files[0];
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
  	    var headers = ["brand", "category"];
  	    if(Object.keys(fileData[0]).length != headers.length) {
            toastr.warning("Number of columns in File do not match");
            return;
        }
        for(var i in headers) {
            if(!fileData[0].hasOwnProperty(headers[i])) {
                toastr.warning('File column names do not match');
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
  if (processCount == fileData.length) {
    return;
  }

  var json = JSON.stringify(fileData);
  var url = getBrandUrl();

  //Make ajax call
  $.ajax({
    url: url,
    type: "POST",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      //uploadRows();
      errorData = response;
      resetForm();
      getBrandList();
      toastr.success("Brands Uploaded Successfully", "Success : ");
    },
    error: function (response) {
        if(response.status == 403){
            toastr.error("403 Forbidden");
        }
        else {
            var resp = JSON.parse(response.responseText);
            if (isJson(resp.message) == true) {
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
    },
  });
}

function downloadErrors() {
  writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){

    var $head = $("#total-rows").find("span");
    $head.empty();
    var span = "Total Rows : " + data.length;
    $head.append(span);

   var $tbody = $('#Brand-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      var buttonHtml = '<button onclick="displayEditBrand(' + e.id + ')" class="btn table__button-group" title="Edit Brand"><i class="fa-solid fa-pencil" style="color:#007BFF"></i></button>'
      var row = '<tr>'
      + '<td>' + e.id + '</td>'
      + '<td>' + e.brand + '</td>'
      + '<td>'  + e.category + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
        $tbody.append(row);
   }
}

function displayEditBrand(id) {
  var url = getBrandUrl() + "/" + id;
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayBrand(data);
    },
    error: handleAjaxError,
  });
}

function resetUploadDialog() {
  //Reset file name
  var $file = $("#brandFile");
  $file.val("");
  $("#brandFileName").html("Choose File");
  //Reset various counts
  processCount = 0;
  fileData = [];
  errorData = [];
  //Update counts
  updateUploadDialog();
}

function updateUploadDialog() {
  $("#rowCount").html("" + fileData.length);
  $("#processCount").html("" + processCount);
  $("#errorCount").html("" + errorData.length);
}

function updateFileName() {
  var $file = $("#brandFile");
  var fileName = $file.val();
  $("#brandFileName").html(fileName);
  activateUpload();
}

function displayUploadData() {
  resetUploadDialog();
  $("#upload-brand-modal").modal("toggle");
  $("#download-errors").prop("disabled", true);
  $("#process-data").prop("disabled", true);
}

function activateUpload() {
  $("#process-data").prop("disabled", false);
}

function downloadCsv() {
  window.location.href = getBrandUrl() + "/exportcsv";
}

function displayBrand(data) {
  $("#brand-edit-form input[name=brand]").val(data.brand);
  $("#brand-edit-form input[name=category]").val(data.category);
  $("#brand-edit-form input[name=id]").val(data.id);
  $("#edit-brand-modal").modal("toggle");

  var $head = $("#edit-brand-modal").find("h5");
  $head.empty();
  var span = "Edit Brand - " + data.id;
  $head.append(span);

  document.getElementById("update-brand").disabled = true;
}

function checkform() {
  var f = document.forms["brand-form"].elements;
  var cansubmit = true;
  for (var i = 0; i < f.length; i++) {
    if (f[i].value.length == 0) cansubmit = false;
  }
  document.getElementById("add-brand").disabled = !cansubmit;
}

function displayAddBrand(data) {
  $("#add-brand-modal").modal("toggle");
}

function enableUpdate() {
  document.getElementById("update-brand").disabled = false;
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
  $("#add-brand").click(addBrand);
  $("#update-brand").click(updateBrand);
  $("#refresh-data").click(getBrandList);
  $("#upload-data").click(displayUploadData);
  $("#process-data").click(processData);
  $("#download-errors").click(downloadErrors);
  $("#brandFile").on("change", updateFileName);
  $("#download-csv").click(downloadCsv);
  $("#add-modal").click(displayAddBrand);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(activateNav);