var wholeOrder = [];
//todo: fix order js
function getOrderItemUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/order/cart";
}

function getOrderItemByIdUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/order/cartitems";
}

function getOrderUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/order";
}

function resetForm() {
  var element = document.getElementById("order-item-form");
  element.reset();
}

function getInventoryUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/inventory";
}

function getInvoiceUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  console.log(baseUrl);
  return baseUrl + "/api/order/invoice";
}

//BUTTON ACTIONS
// --------------------------------------------------------------------------------

function placeOrder(event) {
  var url = getOrderItemUrl();
  let len = wholeOrder.length;
  console.log(len);
  if (len == 0) {
    toastr.error("Cart empty! Order cannot be placed.", "Error : ", {
      closeButton: true,
      timeOut: "0",
      extendedTimeOut: "0",
    });
  } else {
    var jsonObj = arrayToJson();
    console.log(jsonObj);
    $.ajax({
      url: url,
      type: "POST",
      data: jsonObj,
      headers: {
        "Content-Type": "application/json",
      },
      success: function (response) {
        $("#add-order-item-modal").modal("toggle");
        getOrderList();
        toastr.success("Order Placed Successfully", "Success : ");
        wholeOrder = [];
      },
      error: handleAjaxError,
    });
  }
  return false;
}

function displayOrderItemList(data) {
  document.getElementById("place-order").disabled = false;
  var $tbody = $("#order-item-table").find("tbody");
  $tbody.empty();

	for(var i in wholeOrder)
	{
        var e = wholeOrder[i];
        //var buttonHtml = '<button onclick="displayEditOrderItem(' + i + ')" class="btn table__button-group"><i class="fa-solid fa-pencil" style="color:#00295F"></i></button>'
        var buttonHtml = '<button onclick="deleteOrderItem(' + i + ')" class="btn table__button-group"><i class="fa-solid fa-trash" style="color:#00295F"></i></button>'
        var row = '<tr>'
            + '<td>' + JSON.parse(e).barcode + '</td>'
            + '<td>' + JSON.parse(e).quantity + '</td>'
            + '<td>'  + parseFloat(JSON.parse(e).sellingPrice).toFixed(2) + '</td>'
            + '<td>' + buttonHtml + '</td>'
            + '</tr>';

    $tbody.append(row);
  }
}

function checkOrderItemExist() {
  for (i in wholeOrder) {
    var barcode = JSON.parse(wholeOrder[i]).barcode;
    console.log(barcode);
    var temp_barcode = $("#order-item-form input[name=barcode]").val();
    console.log(temp_barcode);
    if (temp_barcode == barcode) {
      console.log("Exist");
      return true;
    }
  }
  return false;
}

function changeQuantity(item) {
  var barcode = item[0];
  console.log(barcode);

  var quantity = parseInt(item[1]);
  console.log(quantity);
  console.log(wholeOrder);

  for (i in wholeOrder) {
    let data = {};
    var temp_barcode = JSON.parse(wholeOrder[i]).barcode;
    if (temp_barcode == barcode) {
      var prev_quantity = parseInt(JSON.parse(wholeOrder[i]).quantity);
      var new_quantity = prev_quantity + quantity;
      if (new_quantity > barcodeList.get(barcode)) {
        toastr.error("Quantity not available in the inventory");
        return;
      }
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

function getOrderList() {
  var url = getOrderUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayOrderList(data);
    },
    error: handleAjaxError,
  });
}

function checkSellingPrice(vars) {
  var barcode = vars[0];
  var sp = vars[2];
  for (i in wholeOrder) {
    var temp_barcode = JSON.parse(wholeOrder[i]).barcode;
    if (temp_barcode == barcode) {
      var cur_sp = parseInt(JSON.parse(wholeOrder[i]).sellingPrice);
      if (cur_sp == sp) {
        return true;
      }
    }
  }
  return false;
}

const barcodeList = new Map();
var inv_qty = null;
var inv_barcode = null;

function getBarcode(data) {
  for (i in data) {
    let vars = [];
    vars.push(data[i].id);
    vars.push(data[i].barcode);
    getQtyFromInventory(vars);
  }
}

function getQtyFromInventory(vars) {
  var url = getInventoryUrl() + "/" + vars[0];
  var temp_qty;
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      barcodeList.set(vars[1], data.quantity);
    },
    error: handleAjaxError,
  });

  return temp_qty;
}

function getProductList() {
  var url = getInventoryUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      barcode_qty.clear();
      getBarcode(data);
    },
    error: handleAjaxError,
  });
}

function checkBarcode(data) {
  console.log(data);
  console.log(barcode_qty[data]);
  console.log(barcode_qty.has(data));
  if (barcode_qty.has(data)) {
    return true;
  }
  return false;
}
var inv_qty = null;
var inv_barcode = null;
var mrp = null;

var check = 1;

function getInventory(barcode) {
  var url = getInventoryUrl() + "/" + barcode;
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      inv_barcode = data.barcode;
      inv_qty = data.qty;

      barcodeList.set(data.barcode, data.qty);
      addItem();
      resetForm();
    },
    error: function (data) {
      toastr.error("Barcode does not exist in the Inventory");
      resetForm();
    },
  });
}

function addItem() {
  var $form = $("#order-item-form");
  var json = toJson($form);
  var jsonObj = $.parseJSON(json);
  var barcode1 = $("#order-item-form input[name=barcode]").val();

  var qty = $("#order-item-form input[name=quantity]").val();

  console.log(check);

  console.log(qty);
  console.log(inv_qty);
  if(isNaN(qty) || isNaN(parseFloat(qty))) {
        toastr.error("Quantity must be a number", "Error : ");
  }
  else if (qty > barcodeList.get(barcode1)) {
    toastr.error("Quantity not present in inventory");
    resetForm();
  }
  else
  {
    var _qty = barcodeList.get(barcode1) - qty;
    var sp = $("#order-item-form input[name=sellingPrice]").val();

        if (sp <= 0) {
            toastr.error("Price cannot be negative or zero");
        } else if (qty <= 0) {
            toastr.error("Quantity cannot be negative or zero");
        }
        if (qty > barcodeList.get(barcode1)) {
            toastr.error("Not enough quantity present in inventory , "+ barcodeList.get(barcode1) + " available");
        } else {
              if (checkOrderItemExist()) {
                console.log("inside check");
                let vars = [];

                var barcode = $("#order-item-form input[name=barcode]").val();
                var qty = $("#order-item-form input[name=quantity]").val();
                var sp = $("#order-item-form input[name=sellingPrice]").val();

                vars.push(barcode);
                vars.push(qty);
                vars.push(sp);
                if (checkSellingPrice(vars) == false) {
                  toastr.error("Selling price cannot be different");
                } else {
                  changeQuantity(vars);
                }
              } else {
                wholeOrder.push(json);
                toastr.success("Item added to Cart", "Success : ");
                resetForm();
              }

          displayOrderItemList(wholeOrder);
        }
  }
}

function addOrderItem(event) {
  check = 1;
  var $form = $("#order-item-form");
  var json = toJson($form);
  var jsonObj = $.parseJSON(json);

  var barcode1 = $("#order-item-form input[name=barcode]").val();
  getInventory(barcode1);
  var qty = $("#order-item-form input[name=quantity]").val();
}

function displayCart() {
  $("#add-order-item-modal").modal("toggle");
  // table should be empty
  var $tbody = $("#order-item-table").find("tbody");
  $tbody.empty();
}

function clearCart() {
  console.log("inside clear cart");
  wholeOrder = [];
  console.log(wholeOrder);
  toastr.info("Cart Cleared", "Info : ");
}

function getOrderItemList() {
  var jsonObj = $.parseJSON("[" + wholeOrder + "]");
  console.log(jsonObj);
}

function deleteOrderItem(id) {
  wholeOrder.splice(id, 1);
  displayOrderItemList(wholeOrder);
}

//UPDATE METHODS
// --------------------------------------------------------------------------------

// var editItem=null;
var editOrderItem = null;
function updateOrderItem(event) {
  $("#edit-order-item-modal").modal("toggle");

  var $form = $("#edit-order-item-form");
  var json = toJson($form);
  console.log(json);
  var jsonObj = $.parseJSON(json);

  var barcode = $("#edit-order-item-form input[name=barcode]").val();
  var qty = $("#edit-order-item-form input[name=quantity]").val();
  var sp = $("#edit-order-item-form input[name=sellingPrice]").val();

  if (sp < 1) {
    toastr.error("Price cannot be negative or zero", "Error : ", {
      closeButton: true,
      timeOut: "0",
      extendedTimeOut: "0",
    });
  } else if (qty < 1) {
    toastr.error("Quantity cannot be negative or zero", "Error : ", {
      closeButton: true,
      timeOut: "0",
      extendedTimeOut: "0",
    });
  } else {
    console.log("inside order item");
    let item = [];

    item.push(barcode);
    item.push(qty);
    item.push(sp);

    console.log(item);

    let data = {};
    data["barcode"] = item[0];
    data["quantity"] = item[1];
    data["sellingPrice"] = item[2];

    console.log(data);
    console.log(editOrderItem);
    wholeOrder[editOrderItem] = JSON.stringify(data);
    console.log(wholeOrder);
  }
  //resetForm();
  toastr.success("Item updated Successfully", "Success : ");
  displayOrderItemList(wholeOrder);
}

function displayEditOrderItem(id) {
  editOrderItem = id;
  displayOrderItem(id);
}

//UI DISPLAY METHODS
// --------------------------------------------------------------------------------

function displayOrderList(data){
   var $tbody = $('#order-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      console.log(e);
      var buttonHtml = '<button onclick="viewOrder(' + e.id + ')" class="btn"><i class="fa-solid fa-eye" style="color:#00295F"></i></button>'
      buttonHtml += '<button onclick="printOrder(' + e.id + ')" class="btn" target="_blank"><i class="fa-solid fa-print" style="color:#00295F"></i></button>'
      var row = '<tr>'
      + '<td>' + e.id + '</td>'
      + '<td>' + e.time + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
        $tbody.append(row);
   }
}

function printOrder(id) {
  var req = getInvoiceUrl() + "/" + id;
  window.open(req, "_blank");
}

function getOrderList() {
  var url = getOrderUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      console.log(data);
      displayOrderList(data);
    },
    error: handleAjaxError,
  });
}

function arrayToJson() {
  let json = [];
  for (i in wholeOrder) {
    let data = {};
    data["barcode"] = JSON.parse(wholeOrder[i]).barcode;
    data["quantity"] = JSON.parse(wholeOrder[i]).quantity;
    data["sellingPrice"] = JSON.parse(wholeOrder[i]).sellingPrice;
    json.push(data);
  }
  return JSON.stringify(json);
}

function viewOrder(id) {
  $("#view-order-item-modal").modal("toggle");

  var $head = $("#view-order-item-modal").find("h5");
  $head.empty();
  var span = "Order - " + id;
  $head.append(span);

  var url = getOrderItemByIdUrl() + "/" + id;
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      console.log(data);
      displayOrderItemViewList(data);
    },
    error: handleAjaxError,
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
         //+ '<td>' + e.sellingPrice + '</td>'
         + '<td>'  + parseFloat(e.sellingPrice).toFixed(2) + '</td>'
         + '</tr>';
         $tbody.append(row);
     }
 }

function displayOrderItem(i) {
  let data = {};
  data["barcode"] = JSON.parse(wholeOrder[i]).barcode;
  data["quantity"] = JSON.parse(wholeOrder[i]).quantity;
  data["sellingPrice"] = JSON.parse(wholeOrder[i]).sellingPrice;

  $("#edit-order-item-form input[name=barcode]").val(data.barcode);
  console.log(data.barcode);
  $("#edit-order-item-form input[name=quantity]").val(data.quantity);
  console.log(data.quantity);
  $("#edit-order-item-form input[name=sellingPrice]").val(data.sellingPrice);
  console.log(data.sellingPrice);

  $("#edit-order-item-modal").modal("toggle");
}

//INITIALIZATION CODE
// --------------------------------------------------------------------------------

function init() {
  $("#add-order").click(displayCart);
  $("#add-order-item").click(addOrderItem);
  $("#place-order").click(placeOrder);
  $("#refresh-data").click(getOrderList);
  $("#update-order-item").click(updateOrderItem);
  barcodeList.clear();
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getOrderItemList);
