var brandData = {};
var wholeProduct = []

function getBrandUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/brand";
}

function getProductUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/product";
}

function getBrandOption() {
  selectElement = document.querySelector("#inputBrand");
  output = selectElement.options[selectElement.selectedIndex].value;
  return output;
}

function resetForm() {
  var element = document.getElementById("product-form");
  element.reset();
}

function arrayToJson() {
    let json = [];
    for(i in wholeProduct) {
        let data = {};
        data["barcode"]=JSON.parse(wholeProduct[i]).barcode;
        data["brand"]=JSON.parse(wholeProduct[i]).brand;
        data["category"]=JSON.parse(wholeProduct[i]).category;
        data["name"]=JSON.parse(wholeProduct[i]).name;
        data["mrp"]=JSON.parse(wholeProduct[i]).mrp;
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
function addProduct(event) {
  //Set the values to update
  var $form = $("#product-form");
  var json = toJson($form);

  var mrp = JSON.parse(json).mrp;

  if(isNaN(mrp) || isNaN(parseFloat(mrp))) {
      toastr.warning("MRP must be number", "Error : ");
      return;
  }
  if(parseFloat(mrp) > 2147483647) {
      toastr.warning("MRP is greater than Maximum value allowed", "Error : ");
      return;
  }

      var url = getProductUrl();
      wholeProduct.push(json);
      var jsonObj = arrayToJson();
      $.ajax({
        url: url,
        type: "POST",
        data: jsonObj,
        headers: {
          "Content-Type": "application/json",
        },
        success: function (response) {
          getProductList();
          toastr.success("Product Added Successfully", "Success : ");
          resetForm();
          wholeProduct = [];
          displayAddProduct();
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
                 wholeProduct=[];
              }
      });

  return false;
}

function updateProduct(event) {
  //Get the ID
  var id = $("#product-edit-form input[name=id]").val();
  var url = getProductUrl() + "/" + editProduct;

  //Set the values to update
  var $form = $("#product-edit-form");
  var json = toJson($form);

  var mrp = JSON.parse(json).mrp;

  if(isNaN(mrp) || isNaN(parseFloat(mrp))) {
      toastr.warning("MRP must be a number", "Error : ");
      return;
  }
  if(parseFloat(mrp) > 2147483647) {
      toastr.warning("MRP is greater than Maximum value allowed", "Error : ");
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
      $("#edit-product-modal").modal("toggle");
      getProductList();
      toastr.success("Product Updated Successfully", "Success : ");
    },
    error: handleAjaxError,
  });

  return false;
}

function getProductList() {
  var url = getProductUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayProductList(data);
    },
    error: handleAjaxError,
  });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
  var file = $("#productFile")[0].files[0];
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
  	        var headers = ["barcode", "brand", "category", "name", "mrp"];
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
  //If everything processed then return
  if (processCount == fileData.length) {
    //toastr.success("Rows uploaded Successfully", "Success : ");
    return;
  }

  var json = JSON.stringify(fileData);
  var url = getProductUrl();

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
      getProductList();
      toastr.success("Products Uploaded Successfully", "Success : ");
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

function displayProductList(data){

    var $head = $("#total-rows").find("span");
    $head.empty();
    var span = "Total Rows : " + data.length;
    $head.append(span);

   var $tbody = $('#Product-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      var buttonHtml = '<button onclick="displayEditProduct(' + e.id + ')" class="btn table__button-group" title="Edit Product"><i class="fa-solid fa-pencil" style="color:#007BFF"></i></button>'
      var row = '<tr>'
      + '<td>' + e.barcode + '</td>'
      + '<td>' + e.brand + '</td>'
      + '<td>' + e.category + '</td>'
      + '<td>'  + e.name + '</td>'
      + '<td>'  + parseFloat(e.mrp).toFixed(2) + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
        $tbody.append(row);
   }
}

var editProduct = null;
function displayEditProduct(id) {
  var url = getProductUrl() + "/" + id;
  editProduct = id;
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayProduct(data);
    },
    error: handleAjaxError,
  });
}

function resetUploadDialog() {
  //Reset file name
  var $file = $("#productFile");
  $file.val("");
  $("#productFileName").html("Choose File");
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
  var $file = $("#productFile");
  var fileName = $file.val();
  $("#productFileName").html(fileName);
  activateUpload();
}

function displayUploadData() {
  resetUploadDialog();
  $("#upload-product-modal").modal("toggle");
  $("#download-errors").prop("disabled", true);
  $("#process-data").prop("disabled", true);
}

function activateUpload() {
  $("#process-data").prop("disabled", false);
}

function displayProduct(data) {
  $("#product-edit-form input[name=barcode]").val(data.barcode);
  $("#product-edit-form input[name=name]").val(data.name);
  $("#product-edit-form input[name=mrp]").val(data.mrp);
  $("#edit-product-modal").modal("toggle");

  var $head = $("#edit-product-modal").find("h5");
  $head.empty();
  var span = "Edit Product - " + data.barcode;
  $head.append(span);

  document.getElementById("update-product").disabled = true;
}

function checkform() {
  var f = document.forms["product-form"].elements;
  var cansubmit = true;
  for (var i = 0; i < f.length; i++) {
    if (f[i].value.length == 0) cansubmit = false;
  }
  document.getElementById("add-product").disabled = !cansubmit;
}
function getBrandList() {
  var url = getBrandUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayBrandOptions(data);
    },
    error: handleAjaxError,
  });
}

function displayBrandOptions(data) {
  for (var i in data) {
    var a = data[i].brand;
    var b = data[i].category;
    if (!brandData.hasOwnProperty(a)) Object.assign(brandData, { [a]: [] });
    brandData[a].push(b);
  }
  var $elB = $("#inputBrand");
  $elB.empty();
  $elB.append(
    `<option value="none" selected disabled hidden>select brand</option>`
  );

  $.each(brandData, function (key, value) {
    $elB.append($("<option></option>").attr("value", key).text(key));
  });

  displayCategoryOptions();
}

function displayCategoryOptions() {
  var $elC = $("#inputCategory");

  $elC.empty();
  $elC.append(
    `<option value="none" selected disabled hidden>select category</option>`
  );
  var a = getBrandOption();
  var len = brandData[a].length;
  for (var i = 0; i < len; i++) {
    $elC.append(
      $("<option></option>")
        .attr("value", brandData[a][i])
        .text(brandData[a][i])
    );
  }
}

function displayAddProduct(data) {
  $("#add-product-modal").modal("toggle");
}

function enableUpdate() {
  document.getElementById("update-product").disabled = false;
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
  $("#add-product").click(addProduct);
  $("#update-product").click(updateProduct);
  $("#refresh-data").click(getProductList);
  $("#upload-data").click(displayUploadData);
  $("#process-data").click(processData);
  $("#download-errors").click(downloadErrors);
  $("#productFile").on("change", updateFileName);
  $("#inputBrand").change(displayCategoryOptions);
  $("#add-modal").click(displayAddProduct);
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandList);
$(document).ready(activateNav);