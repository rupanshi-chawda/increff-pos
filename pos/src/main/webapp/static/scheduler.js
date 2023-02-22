function getSalesUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/sales";
}

function displaySalesList(data) {

    var $head = $("#total-rows").find("span");
    $head.empty();
    var span = "Total Rows : " + data.length;
    $head.append(span);

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
  var month_today = date.getMonth() + 1;
  var month_before = date.getMonth();
  var year = date.getFullYear();

  if (month_before < 10) month_before = "0" + month_before;
  if (month_today < 10) month_today = "0" + month_today;
  if (day < 10) day = "0" + day;

  var today = year + "-" + month_today + "-" + day;
  var before = year + "-" + month_before + "-" + day;
  $("#inputED").attr("value", today);
  $("#inputSD").attr("value", before);
}

function disableDate() {
  var sd = $("#sales-form input[name=startDate]").val();
  $("#inputED").attr("min", sd);
}

function init() {
  $("#refresh-data").click(refreshData);
  $("#apply-filter").click(getFilteredList);
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

$(document).ready(init);
$(document).ready(getSalesList);
$(document).ready(autoFillDate);
$(document).ready(activateNav);