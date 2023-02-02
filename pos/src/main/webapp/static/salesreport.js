var brandData = {};

function getSalesReportUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/salesreport";
}

function getBrandUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/brand";
}

function resetForm() {
  var element = document.getElementById("sales-report-form");
  element.reset();
}

function getFilteredList(event) {
  var $form = $("#sales-report-form");
  var json = toJson($form);
  console.log(json);
  var url = getSalesReportUrl() + "/filter";
  $.ajax({
    url: url,
    type: "POST",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      displaySalesList(response);
      downloadCsv();
    },
    error: handleAjaxError,
  });

  return false;
}

function displaySalesList(data) {
    var $tbody = $('#Sales-report-table').find('tbody');
    $tbody.empty();
    for (var i in data) {
        var e = data[i];
        var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + parseFloat(e.revenue).toFixed(2) + '</td>'
		+ '</tr>';
        $tbody.append(row);
    }
}

function getSalesList() {
  var url = getSalesReportUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displaySalesList(data);
    },
    error: handleAjaxError,
  });
}
function getBrandOption() {
  selectElement = document.querySelector("#inputBrand");
  output = selectElement.options[selectElement.selectedIndex].value;
  return output;
}

function displayCategoryOptions() {
  var $elC = $("#inputCategory");

  $elC.empty();
  $elC.append(`<option value="all" selected >All</option>`);
  var a = getBrandOption();
  for (var i in brandData[a]) {
    $elC.append(
      $("<option></option>")
        .attr("value", brandData[a][i])
        .text(brandData[a][i])
    );
  }
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

  $elB.append(`<option value="all" selected >All</option>`);

  $.each(brandData, function (key, value) {
    $elB.append($("<option></option>").attr("value", key).text(key));
  });

  displayCategoryOptions();
}
const prod = new Set();
function populate(data) {
  var $elC = $("#inputCategory");
  for (i in data) {
    var a = "all";
    var b = data[i].category;
    if (!brandData.hasOwnProperty(a)) Object.assign(brandData, { [a]: [] });
    brandData[a].push(b);
    prod.add(data[i].category);
  }
  $elC.empty();
  $elC.append(`<option value="all" selected >All</option>`);

  for (const item of prod) {
    $elC.append($("<option></option>").attr("value", item).text(item));
  }
}

function initlists() {
  var url = getBrandUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      populate(data);
    },
    error: handleAjaxError,
  });
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

function downloadCsv() {
  window.location.href = getSalesReportUrl() + "/exportcsv";
  console.log(getSalesReportUrl() + "/exportcsv");
  resetForm();
}

function init() {
  $("#apply-filter").click(getFilteredList);
  $("#inputBrand").on("change", displayCategoryOptions);
  //$('#download-csv').click(downloadCsv);
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
  $("#inputSD").attr("value", today);
}

function disableDate() {
  var sd = $("#sales-report-form input[name=startDate]").val();
  $("#inputED").attr("min", sd);
}

$(document).ready(init);
$(document).ready(getSalesList);
$(document).ready(getBrandList);
$(document).ready(initlists);
$(document).ready(autoFillDate);
