var brandData = {};

function getBrandUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}

function getProductUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/product";
}

function getBrandOption() {
    selectElement = document.querySelector('#inputBrand');
    output = selectElement.options[selectElement.selectedIndex].value;
    return output;
}

//BUTTON ACTIONS
function addProduct(event){
   //Set the values to update
   var $form = $("#product-form");
   var json = toJson($form);
   var url = getProductUrl();
   $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             getProductList();
      },
      error: handleAjaxError
   });

   return false;
}

function updateProduct(event){
   $('#edit-product-modal').modal('toggle');
   //Get the ID
   var id = $("#product-edit-form input[name=id]").val();
   var url = getProductUrl() + "/" + editProduct;

   //Set the values to update
   var $form = $("#product-edit-form");
   var json = toJson($form);

   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             getProductList();
      },
      error: handleAjaxError
   });

   return false;
}


function getProductList(){
   var url = getProductUrl();
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayProductList(data);
      },
      error: handleAjaxError
   });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
   var file = $('#productFile')[0].files[0];
   console.log(file);
   readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
   fileData = results.data;
   uploadRows();
}

function uploadRows(){
   //Update progress
   updateUploadDialog();
   //If everything processed then return
   if(processCount==fileData.length){
      return;
   }

   //Process next row
   var row = fileData[processCount];
   processCount++;

   var json = JSON.stringify(row);
   var url = getProductUrl();

   //Make ajax call
   $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             uploadRows();
      },
      error: function(response){
             row.error=response.responseText
             errorData.push(row);
             uploadRows();
      }
   });

}

function downloadErrors(){
   writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayProductList(data){
   var $tbody = $('#Product-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      //var buttonHtml = ' <button onclick="displayEditProduct(' + e.id + ')">edit</button>'
      var buttonHtml = '<button onclick="displayEditProduct(' + e.id + ')" class="btn btn-light"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="blue" class="bi bi-pencil-square" viewBox="0 0 16 16"><path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"></path><path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"></path></svg></button>'
      var row = '<tr>'
      + '<td>' + e.barcode + '</td>'
      + '<td>'  + e.name + '</td>'
      + '<td>'  + e.mrp + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
        $tbody.append(row);
   }
}

var editProduct=null;
function displayEditProduct(id){
   var url = getProductUrl() + "/" + id;
   editProduct=id;
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayProduct(data);
      },
      error: handleAjaxError
   });
}

function resetUploadDialog(){
   //Reset file name
   var $file = $('#productFile');
   $file.val('');
   $('#productFileName').html("Choose File");
   //Reset various counts
   processCount = 0;
   fileData = [];
   errorData = [];
   //Update counts
   updateUploadDialog();
}

function updateUploadDialog(){
   $('#rowCount').html("" + fileData.length);
   $('#processCount').html("" + processCount);
   $('#errorCount').html("" + errorData.length);
}

function updateFileName(){
   var $file = $('#productFile');
   var fileName = $file.val();
   $('#productFileName').html(fileName);
}

function displayUploadData(){
    console.log("hello");
   resetUploadDialog();
   $('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
   $("#product-edit-form input[name=barcode]").val(data.barcode);
   $("#product-edit-form input[name=name]").val(data.name);
   $("#product-edit-form input[name=mrp]").val(data.mrp);
   $('#edit-product-modal').modal('toggle');
}

function getBrandList()
{
    var url = getBrandUrl();
    $.ajax({
          url: url,
          type: 'GET',
          success: function(data) {
                displayBrandOptions(data);
          },
          error: handleAjaxError
    });
}

function displayBrandOptions(data)
{
    console.log(data);
    for(var i in data)
        {
            var a = data[i].brand;
            var b = data[i].category;
            if(!brandData.hasOwnProperty(a))
                Object.assign(brandData, {[a]:[]});
            brandData[a].push(b);
        }
    console.log(brandData);
    var $elB = $("#inputBrand");

    $elB.empty();
    $.each(brandData, function(key,value) {
        $elB.append($("<option></option>")
        .attr("value", key).text(key));
        });
    displayCategoryOptions();
}

function displayCategoryOptions()
{
    var $elC = $("#inputCategory");
    $elC.empty();

    var a = getBrandOption();
    console.log(brandData[a]);
    console.log(brandData[a].length);
    var len = brandData[a].length;
    for(var i=0; i<len; i++)
    {
        $elC.append($("<option></option>")
        .attr("value", brandData[a][i]).text(brandData[a][i]));

    }
}


//INITIALIZATION CODE
function init(){
   $('#add-product').click(addProduct);
   $('#update-product').click(updateProduct);
   $('#refresh-data').click(getProductList);
   $('#upload-data').click(displayUploadData);
   $('#process-data').click(processData);
   $('#download-errors').click(downloadErrors);
   $('#productFile').on('change', updateFileName)
   $('#inputBrand').change(displayCategoryOptions);
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandList);