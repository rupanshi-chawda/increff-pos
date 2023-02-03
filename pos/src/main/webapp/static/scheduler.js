function getSalesUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/sales";
}
var last_run = null;
function displaySalesList(data) {
    last_run = data[0].lastRun;
    console.log(last_run);
    $("#last-run input[name=lastRun]").val(last_run);
    var $tbody = $('#Sales-table').find('tbody');
    $tbody.empty();
    for (var i in data) {
        var e = data[i];
        var row = '<tr>'
		+ '<td>' + e.date + '</td>'
		+ '<td>'  + e.invoicedOrderCount + '</td>'
		+ '<td>' + e.invoicedItemsCount + '</td>'
		+ '<td>' + parseFloat(e.totalRevenue).toFixed(2) + '</td>'
		+ '</tr>';
        $tbody.append(row);
    }
}

function getSalesList() {
  var url = getSalesUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displaySalesList(data);
    },
    error: handleAjaxError,
  });
}

function resetForm() {
  var element = document.getElementById("sales-form");
  element.reset();
}

function getFilteredList(event) {
  var $form = $("#sales-form");
  var json = toJson($form);
  console.log(json);
  var url = getSalesUrl() + "/filter";
  $.ajax({
    url: url,
    type: "POST",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      resetForm();
      displaySalesList(response);
      toastr.success("Report Created Successfully", "Success : ");
    },
    error: handleAjaxError,
  });

  document.getElementById("inputED").disabled = true;
  return false;
}

function refreshData() {
  var url = getSalesUrl() + "/scheduler";
  $.ajax({
    url: url,
    type: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    success: function () {
      getSalesList();
    },
    error: handleAjaxError,
  });

  return false;
}

function autoFillDate() {
  var date = new Date();
  var day = date.getDate();
  var month = date.getMonth() + 1;
  var year = date.getFullYear();

  if (month < 10) month = "0" + month;
  if (day < 10) day = "0" + day;

  var today = year + "-" + month + "-" + day;
  $("#inputED").attr("value", today);
  `//`$("#inputSD").attr("value", today);
}

function disableDate() {
  var sd = $("#sales-form input[name=startDate]").val();
  document.getElementById("inputED").disabled = false;
  $("#inputED").attr("min", sd);
}

function init() {
  $("#refresh-data").click(refreshData);
  $("#apply-filter").click(getFilteredList);
}

$(document).ready(init);
$(document).ready(getSalesList);
$(document).ready(autoFillDate);
