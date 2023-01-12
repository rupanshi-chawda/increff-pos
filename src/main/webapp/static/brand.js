
function getBrandUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/brand";
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
   //If everything processed then return
   if(processCount==fileData.length){
      return;
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
      //var buttonHtml = ' <button onclick="displayEditBrand(' + e.id + ')">edit</button>'
      var buttonHtml = '<button onclick="displayEditBrand(' + e.id + ')" class="btn btn-light"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="blue" class="bi bi-pencil-square" viewBox="0 0 16 16"><path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"></path><path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"></path></svg></button>'
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
}

function displayBrand(data){
   $("#brand-edit-form input[name=brand]").val(data.brand);
   $("#brand-edit-form input[name=category]").val(data.category);
   $("#brand-edit-form input[name=id]").val(data.id);
   $('#edit-brand-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
   $('#add-brand').click(addBrand);
   $('#update-brand').click(updateBrand);
   $('#refresh-data').click(getBrandList);
   $('#upload-data').click(displayUploadData);
   $('#process-data').click(processData);
   $('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getBrandList);
