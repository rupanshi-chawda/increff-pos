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


//BUTTON ACTIONS
function addProduct(event) {
  //Set the values to update
  var $form = $("#product-form");
  var json = toJson($form);
  var url = getProductUrl();
  wholeProduct.push(json);
  var jsonObj = arrayToJson();
  console.log(url);
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
    },
    error: function (response) {
             resetForm();
             console.log(response);
             if(response.status == 403) {
                  toastr.error("Error: 403 unauthorized");
             }
             else {

            var resp = JSON.parse(response.responseText);
             //alert(response.message);
                 console.log(resp);
            var jsonObj = JSON.parse(resp.message);
            console.log(jsonObj);
              toastr.error(jsonObj[0].message, "Error : ");
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
  console.log(file);
  readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
  fileData = results.data;
  console.log(fileData);
  var filelen = fileData.length;
  	if(filelen > 5000) {
  	    toastr.error("file length exceeds 5000, Not Allowed");
  	}
  	else {
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
//  if (errorData.length > 0) {
//    $("#download-errors").prop("disabled", false);
//  }
  //Process next row
//  var row = fileData[processCount];
//  processCount++;

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
      console.log(response);
      errorData = response;
      resetForm();
      getProductList();
      toastr.success("Products Uploaded Successfully", "Success : ");
    },
    error: function (response) {
//      row.error = response.responseText;
//      errorData.push(row);
//      uploadRows();
        if(response.status == 403){
            toastr.error("403 Forbidden");
        }
        else {
            var resp = JSON.parse(response.responseText);
            console.log(resp.message);
            console.log(typeof resp.message);
            var jsonObj = JSON.parse(resp.message);
            console.log(jsonObj);
            errorData = jsonObj;
            processCount = fileData.length;
            console.log(response);
            $("#download-errors").prop('disabled', false);
            resetForm();
            toastr.error("There are errors in file, please Download Errors", "Error : ");
        }
    },
  });
}

function downloadErrors() {
  writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayProductList(data){
   var $tbody = $('#Product-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      var buttonHtml = '<button onclick="displayEditProduct(' + e.id + ')" class="btn table__button-group"><i class="fa-solid fa-pencil" style="color:#00295F"></i></button>'
      var row = '<tr>'
      + '<td>' + e.barcode + '</td>'
      + '<td>'  + e.name + '</td>'
      //+ '<td>'  + e.mrp + '</td>'
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
  console.log("hello");
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
  var span = "Edit Product - " + data.id;
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
  console.log(data);
  for (var i in data) {
    var a = data[i].brand;
    var b = data[i].category;
    if (!brandData.hasOwnProperty(a)) Object.assign(brandData, { [a]: [] });
    brandData[a].push(b);
  }
  console.log(brandData);
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
  console.log(brandData[a]);
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
//  $("#productFile").click(activateUpload);
  $("#add-modal").click(displayAddProduct);
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandList);
