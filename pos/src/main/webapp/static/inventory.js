
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}
const inventoryList = new Map();

function resetForm() {
    var element = document.getElementById("inventory-form");
    element.reset()
}
//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl();

	var barcodeInv = $("#inventory-form input[name=barcode]").val();
    console.log(barcodeInv)
    console.log(url);
    console.log(json)
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	    console.log(response);
	   		getInventoryList();
	   		if(inventoryList.has(barcodeInv)){
	   		    toastr.success("Inventory available, Updated it successfully", "Success : ");
	   		} else {
                toastr.success("Inventory Added Successfully", "Success : ");
	   		}
	   		resetForm()
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var barcode = $("#inventory-edit-form input[name=barcode]").val();
	var url = getInventoryUrl() + "/" + barcode;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
    console.log(json)
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
             toastr.success("Inventory Updated Successfully", "Success : ");
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList(){
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	    console.log(data)
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
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
	    toastr.success("Rows uploaded Successfully", "Success : ");
		return;
	}
    if(errorData.length > 0){
            $("#download-errors").prop('disabled', false);
    }

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getInventoryUrl();

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

function displayEditInventory(barcode){
	var url = getInventoryUrl() + "/" + barcode;
	console.log(url);
	console.log(barcode);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData(){
    console.log("hello");
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
    $("#download-errors").prop('disabled', true);
    $("#process-data").prop('disabled', true);
}

function activateUpload() {
    $("#process-data").prop('disabled', false);
}

function downloadCsv(){
    window.location.href = getInventoryUrl() + "/exportcsv";
    console.log(getInventoryUrl() + "/exportcsv")
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$('#edit-inventory-modal').modal('toggle');
    document.getElementById('update-inventory').disabled = true;
}

function checkform() {
    var f = document.forms["inventory-form"].elements;
    var cansubmit = true;
    for (var i = 0; i < f.length; i++) {
        if (f[i].value.length == 0)
            cansubmit = false;
    }
    document.getElementById('add-inventory').disabled = !cansubmit;
}

function displayAddInventory(data){
   $('#add-inventory-modal').modal('toggle');
}

function enableUpdate(){
    document.getElementById('update-inventory').disabled = false;
}

//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
    $('#download-csv').click(downloadCsv);
    $('#inventoryFile').click(activateUpload);
    $('#add-modal').click(displayAddInventory);
}

$(document).ready(init);
$(document).ready(getInventoryList);