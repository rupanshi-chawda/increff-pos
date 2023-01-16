var wholeOrder = []

function getOrderItemUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/cart";
}

function getOrderItemByIdUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/cartitems";
}

function getOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/order";
}

function resetForm() {
    var element = document.getElementById("order-item-form");
    element.reset()
}

function getProductUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/product";
}

//BUTTON ACTIONS
// --------------------------------------------------------------------------------

function placeOrder(event){
    var url = getOrderItemUrl();

       var jsonObj = arrayToJson();
       console.log(jsonObj);
       $.ajax({
           url: url,
           type: 'POST',
           data: jsonObj,
           headers: {
          	 'Content-Type': 'application/json'
           },
           success: function(response) {
               $('#add-order-item-modal').modal('toggle');
               getOrderList();
               toastr.success("Order Placed Successfully", "Success : ");
               wholeOrder = []
           },
           error: handleAjaxError
       });

       return false;
}

function displayOrderItemList(data){
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();

	for(var i in wholeOrder)
	{
        var e = wholeOrder[i];
        var row = '<tr>'
            + '<td>' + JSON.parse(wholeOrder[i]).barcode + '</td>'
            + '<td>' + JSON.parse(wholeOrder[i]).quantity + '</td>'
            + '<td>' + JSON.parse(wholeOrder[i]).sellingPrice + '</td>'
            + '</tr>';

        $tbody.append(row);
    }
}

function checkOrderItemExist() {
    for(i in wholeOrder) {
        var barcode = JSON.parse(wholeOrder[i]).barcode;
        console.log(barcode);
        var temp_barcode = $("#order-item-form input[name=barcode]").val();
        console.log(temp_barcode);
        if(temp_barcode == barcode) {
            console.log("Exist");
            return true;
        }
    }
    return false;
}

function changeQuantity(item)
{
    var barcode = item[0];
    console.log(barcode);

    var quantity = parseInt(item[1]);
    console.log(quantity);
    console.log(wholeOrder);

    for(i in wholeOrder) {
        let data = {}
        var temp_barcode = JSON.parse(wholeOrder[i]).barcode;
        if(temp_barcode == barcode)
        {
            var prev_quantity = parseInt(JSON.parse(wholeOrder[i]).quantity);
            var new_quantity = prev_quantity + quantity;
            console.log(new_quantity);
            var str = new_quantity.toString();
            console.log(str);

            data["barcode"] = JSON.parse(wholeOrder[i]).barcode;
            data["quantity"] = str;
            data["sellingPrice"] = JSON.parse(wholeOrder[i]).sellingPrice;
            console.log(data);

            var new_data = JSON.stringify(data);
            wholeOrder[i] = new_data;
        }
    }

    console.log(wholeOrder);
}

function getOrderList(){
   var url = getOrderUrl();
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayOrderList(data);
      },
      error: handleAjaxError
   });
}

function checkSellingPrice(item) {
    for(i in wholeOrder) {
        var barcode = JSON.parse(wholeOrder[i]).barcode;
        console.log(barcode);
        var sp = parseInt(JSON.parse(wholeOrder[i]).sellingPrice);
        console.log(sp);

        var temp_barcode = item[0];
        var temp_sp = item[2];
        console.log(temp_barcode);
        console.log(temp_sp)

        if(temp_barcode == barcode && temp_sp == sp) {
                return true;
        }
    }
    return false;
}

var barcodeList = []
function getBarcode(data) {
    for (i in data) {
        var b = data[i].barcode;
        barcodeList.push(b);
        console.log(barcodeList);
    }
}

function getProductList() {
    var url = getProductUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            barcodeList = []
            getBarcode(data);
        },
        error: handleAjaxError
     });
}

function checkBarcode(data) {
    for (i in barcodeList) {
        if (barcodeList[i] == data) {
            return true;
        }
    }
    return false;
}

function addOrderItem(event){
   //Set the values to update
   var $form = $("#order-item-form");
   var json = toJson($form);
   var jsonObj = $.parseJSON(json);

   var barcode1 = $("#order-item-form input[name=barcode]").val();
   var qty = $("#order-item-form input[name=quantity]").val();
   var sp =  $("#order-item-form input[name=sellingPrice]").val();

   if(sp < 1) {
        toastr.error("Price cannot be negative or zero", "Error : ");
   } else if(qty < 1) {
        toastr.error("Quantity cannot be negative or zero", "Error : ");
   } else if(checkOrderItemExist())
   {
        console.log("inside order item");
        let item = []

        var barcode = $("#order-item-form input[name=barcode]").val();
        var quantity = $("#order-item-form input[name=quantity]").val();
        var sp = $("#order-item-form input[name=sellingPrice]").val();

        item.push(barcode);
        item.push(quantity);
        item.push(sp)
        if (checkSellingPrice(item) == false) {
            toastr.error("Selling Price cannot be different", "Error : ");
        } else {
            changeQuantity(item);
        }
   }
   else {
        if (checkBarcode(barcode1) == false) {
            console.log("above");
            toastr.error("Barcode does not exists", "Error : ");
            console.log("below");
        } else {
            wholeOrder.push(json)
        }
   }
   resetForm();
   displayOrderItemList(wholeOrder)
}

function displayCart() {
    $('#add-order-item-modal').modal('toggle');
    // table should be empty
    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();
}

function getOrderItemList() {
    var jsonObj = $.parseJSON('[' + wholeOrder + ']');
	console.log(jsonObj);
}

//UI DISPLAY METHODS
// --------------------------------------------------------------------------------

function displayOrderList(data){
   var $tbody = $('#order-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      console.log(e);
      var buttonHtml = '<button onclick="viewOrder(' + e.id + ')" class="btn btn-light"><i class="fa-solid fa-eye" style="color:blue"></i></button>'
      var row = '<tr>'
      + '<td>' + e.id + '</td>'
      + '<td>' + e.time + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
        $tbody.append(row);
   }
}

function getOrderList() {
    var url = getOrderUrl();
    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	        console.log(data);
    	   		displayOrderList(data);
    	   },
    	   error: handleAjaxError
    });
}

function arrayToJson() {
    let json = [];
    for(i in wholeOrder) {
        let data = {};
        data["barcode"] = JSON.parse(wholeOrder[i]).barcode;
        data["quantity"] = JSON.parse(wholeOrder[i]).quantity;
        data["sellingPrice"] = JSON.parse(wholeOrder[i]).sellingPrice;
        json.push(data);
    }
    return JSON.stringify(json);
}

function viewOrder(id)
{
     $('#view-order-item-modal').modal('toggle');
     var url = getOrderItemByIdUrl() + "/" + id;
       $.ajax({
          url: url,
          type: 'GET',
          success: function(data) {
                console.log(data);
                 displayOrderItemViewList(data);
          },
          error: handleAjaxError
       });
 }

 function displayOrderItemViewList(data){

 	var $tbody = $('#view-order-item-table').find('tbody');
 	$tbody.empty();
    console.log(data);
 	for(var i in data)
 	{
         var e = data[i];
         console.log(e);
         var row = '<tr>'
         + '<td>' + e.barcode + '</td>'
         + '<td>' + e.quantity + '</td>'
         + '<td>' + e.sellingPrice + '</td>'
         + '</tr>';
         $tbody.append(row);
     }
 }

//INITIALIZATION CODE
// --------------------------------------------------------------------------------

function init(){
    $('#add-order').click(displayCart);
   	$('#add-order-item').click(addOrderItem);
   	$('#place-order').click(placeOrder);
   	$('#refresh-data').click(getOrderList);
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getOrderItemList);
$(document).ready(getProductList);
