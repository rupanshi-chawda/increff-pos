//HELPER METHOD
function toJson($form) {
  var serialized = $form.serializeArray();
  var s = "";
  var data = {};
  for (s in serialized) {
    data[serialized[s]["name"]] = serialized[s]["value"];
  }
  var json = JSON.stringify(data);
  return json;
}

function handleAjaxError(response) {
  if (response.status == 403) {
    toastr.warning("Access Forbidden, you cannot upload", "Warning: ");
  } else {
    var response = JSON.parse(response.responseText);

    toastr.error(response.message, "Error : ", {
      closeButton: true,
      timeOut: "0",
      extendedTimeOut: "0",
    });
  }
}

function readFileData(file, callback) {
  var config = {
    header: true,
    delimiter: "\t",
    skipEmptyLines: "greedy",
    complete: function (results) {
      callback(results);
    },
  };
  Papa.parse(file, config);
}

function writeFileData(arr) {
  var config = {
    quoteChar: "",
    escapeChar: "",
    delimiter: "\t",
  };

  var data = Papa.unparse(arr, config);
  var blob = new Blob([data], { type: "text/tsv;charset=utf-8;" });
  var fileUrl = null;

  if (navigator.msSaveBlob) {
    fileUrl = navigator.msSaveBlob(blob, "download.tsv");
  } else {
    fileUrl = window.URL.createObjectURL(blob);
  }
  var tempLink = document.createElement("a");
  tempLink.href = fileUrl;
  tempLink.setAttribute("download", "download.tsv");
  tempLink.click();
}

function hideSupervisorView() {
    var appBanners = document.getElementsByClassName('supervisor');
    for (var i = 0; i < appBanners.length; i++) {
        appBanners[i].style.display = 'none';
    }
}

function init() {
    if($("meta[name=role]").attr("content") == "operator") {
        hideSupervisorView();
    }
}

function isJson(str) {
  try {
    JSON.parse(str);
  } catch (e) {
    return false;
  }
  return true;
}

function isNumber(str) {
  return /^\d+$/.test(str);
}

$(document).ready(init);