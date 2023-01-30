
function getBrandUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/brand";
}

function resetForm() {
    var element = document.getElementById("brand-form");
    element.reset()
}

//BUTTON ACTIONS
function addBrand(event){
   //Set the values to update
   var $form = $("#brand-form");
   var json = toJson($form);
   var url = getBrandUrl();
    console.log(url);
   $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             getBrandList();
             toastr.success("Brand Added Successfully", "Success : ");
             resetForm();
      },
      error: handleAjaxError
   });

   return false;
}

function updateBrand(event){
   $('#edit-brand-modal').modal('toggle');
   //Get the ID
   var id = $("#brand-edit-form input[name=id]").val();
   var url = getBrandUrl() + "/" + id;

   //Set the values to update
   var $form = $("#brand-edit-form");
   var json = toJson($form);

   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             getBrandList();
             toastr.success("Brand Updated Successfully", "Success : ");
      },
      error: handleAjaxError
   });

   return false;
}


function getBrandList(){
   var url = getBrandUrl();
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayBrandList(data);
      },
      error: handleAjaxError
   });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
   var file = $('#brandFile')[0].files[0];
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
    $("#process-data").prop('disabled', true);
   //If everything processed then return
   if(processCount==fileData.length){
        //toastr.success("Rows uploaded Successfully", "Success : ");
        return;
   }
    if(errorData.length > 0){
            $("#download-errors").prop('disabled', false);
    }
   //Process next row
   var row = fileData[processCount];
   processCount++;

   var json = JSON.stringify(row);
   var url = getBrandUrl();

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

function displayBrandList(data){
   var $tbody = $('#Brand-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      var buttonHtml = '<button onclick="displayEditBrand(' + e.id + ')" class="btn table__button-group"><i class="fa-solid fa-pencil" style="color:#00295F"></i></button>'
      var row = '<tr>'
      + '<td>' + e.brand + '</td>'
      + '<td>'  + e.category + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
        $tbody.append(row);
   }
}

function displayEditBrand(id){
   var url = getBrandUrl() + "/" + id;
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayBrand(data);
      },
      error: handleAjaxError
   });
}

function resetUploadDialog(){
   //Reset file name
   var $file = $('#brandFile');
   $file.val('');
   $('#brandFileName').html("Choose File");
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
   var $file = $('#brandFile');
   var fileName = $file.val();
   $('#brandFileName').html(fileName);
}

function displayUploadData(){
    console.log("hello");
   resetUploadDialog();
   $('#upload-brand-modal').modal('toggle');
    $("#download-errors").prop('disabled', true);
    $("#process-data").prop('disabled', true);
}

function activateUpload() {
    $("#process-data").prop('disabled', false);
}

function downloadCsv(){
    window.location.href = getBrandUrl() + "/exportcsv";
}

function displayBrand(data){
   $("#brand-edit-form input[name=brand]").val(data.brand);
   $("#brand-edit-form input[name=category]").val(data.category);
   $("#brand-edit-form input[name=id]").val(data.id);
   $('#edit-brand-modal').modal('toggle');
   document.getElementById('update-brand').disabled = true;
}
function checkform() {
    var f = document.forms["brand-form"].elements;
    var cansubmit = true;
    for (var i = 0; i < f.length; i++) {
        if (f[i].value.length == 0)
            cansubmit = false;
    }
    document.getElementById('add-brand').disabled = !cansubmit;
}

function displayAddBrand(data){
   $('#add-brand-modal').modal('toggle');
}

function enableUpdate(){
    document.getElementById('update-brand').disabled = false;
}

//INITIALIZATION CODE
function init(){
   $('#add-brand').click(addBrand);
   $('#update-brand').click(updateBrand);
   $('#refresh-data').click(getBrandList);
   $('#upload-data').click(displayUploadData);
   $('#process-data').click(processData);
   $('#download-errors').click(downloadErrors);
   $('#brandFile').on('change', updateFileName);
   $('#download-csv').click(downloadCsv);
   $('#brandFile').click(activateUpload);
   $('#add-modal').click(displayAddBrand);
}

$(document).ready(init);
$(document).ready(getBrandList);
