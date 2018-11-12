var data1 = [
  ['Gemuese', 'Preis[Euro]', 'Menge[kg]'],
  ['Tomaten', '1', '1'],
  ['Karotten', '0.5', '1'],
  ['Aepfel', '2', '4'],
  ['Kartoffeln', '3', '9'],
];
var dataAsString;
var data;

function buildTable(order) {
  try {
    var old_table = document.getElementById('gemueseTable');
    old_table.parentNode.removeChild(old_table);
  } catch (err) {
    console.log("No table existing yet");
  }
  dataAsString = Android.getDataArrayAsString();
  console.log("DatenString from Android App: " + dataAsString);
  data  = dataAsString.split('***');
  data = data.map((value) => value.split('^*'))
  console.log("Debug");
  console.log(data1.length);
  console.log(data.length);
  var table = '<table id = "gemueseTable">';
  for (var i = 0; i < data.length; i++) {
    var row = data[i];
    if (i == 0) {
      table += '<thead>';
      table += '<tr>';
    } else {
      table += '<tr>';
    }

    for (var j = 0; j < row.length; j++) {
      if (i == 0) {
        if (order == '') {
          table += '<th id = "' + row[j] + ' "' + 'onclick=sort_2d_asc(data,' + j + ')>';
        } else if (order == 'asc') {
          table += '<th id = "' + row[j] + ' "' + 'onclick=sort_2d_desc(data,' + j + ')>';
        } else if (order == 'desc') {
          table += '<th id = "' + row[j] + ' "' + 'onclick=sort_2d_asc(data,' + j + ')>';
        }

        table += row[j];
        if (order == '') {
          table += "&#9660";
          table += "&#9650";
        } else if (order == 'asc') {
          table += "&#9650";
        } else if (order == 'desc') {
          table += "&#9660";
        }
        table += '</th>';
      } else {
        table += '<td contenteditable=true onBlur=saveChanges()>';
        table += row[j];
        table += '</td>';
      }
    }

    if (i == 0) {
      table += '</tr>';
      table += '</thead>';
      table += '<tbody>';
    } else {
      table += '</tr>';
    }
  }
  table += '</tbody>';
  table += '</table>';
  document.body.innerHTML += table;
}


function search() {
  // Declare variables
  var input, filter, table, tr, td, i;
  input = document.getElementById("search");
  filter = input.value.toUpperCase();
  table = document.getElementById("gemueseTable");
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    tds = tr[i].getElementsByTagName("td");
    for (j = 0; j < tds.length; j++) {
      if (tds[j]) {
        if (tds[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
          break;
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }
}

function saveChanges() {
  console.log("Starting 'saveChanges'");
  var rows = document.getElementsByTagName("tr");
  var tds_count = rows[1].getElementsByTagName("td").length;
  var new_data = [rows.length];

  //Iterate through rows
  for (i = 0; i < rows.length; i++) {
    var row_elements = [];
    /*No need to save Ths -> Cannot change them
    if (i == 0) {
      var ths = rows[i].getElementsByTagName("th");
      for (j = 0; j < ths.length; j++) {
        data[i][j] = ths[j].innerText;
        row_elements[j] = ths[j].innerText;
      }

    } else { */
    var tds = rows[i].getElementsByTagName("td");
    for (j = 0; j < tds.length; j++) {
      data[i][j] = tds[j].innerText;
      row_elements[j] = tds[j].innerText;
      //}
    }
    new_data[i] = row_elements;
  }
  console.log("return Array");
  console.log(new_data);
  console.log("Global Data Array");
  console.log(data);
  console.log("Ending 'saveChanges'");
  return new_data;
}



function sort_2d_desc(twoDArray, column) {
  //Need to create a array without the first array (header_elements)
  var headerArray = twoDArray[0];
  var arrayToSort = [];
  for (var i = 1; i < twoDArray.length; i++) {
    arrayToSort[i - 1] = twoDArray[i];
  }

  if (column >= arrayToSort.length) return;

  for (var i = 0; i < arrayToSort.length - 1; i++) {
    for (var j = (i + 1); j < arrayToSort.length; j++) {
      // Falls das aktuelle Element kleiner ist -> tauschen
      //gehen der Reihe nach durch und tauschen an jede stelle das jetzige kleinste Element
      //First check if the 2 Values of the compared rows may be numbers
      var test = parseFloat(arrayToSort[j][column]);
      console.log("Debug--------------");
      console.log("" + test);
      if ((arrayToSort[j][column] > arrayToSort[i][column]) && isNaN(test)) {
        var arrHelp = arrayToSort[i];
        arrayToSort[i] = arrayToSort[j];
        arrayToSort[j] = arrHelp;
      } else if (parseFloat(arrayToSort[j][column]) > parseFloat(arrayToSort[i][column]) && !isNaN(parseFloat(arrayToSort[i][column]))) {
        var arrHelp = arrayToSort[i];
        arrayToSort[i] = arrayToSort[j];
        arrayToSort[j] = arrHelp;
      }
    }
  }
  data = arrayToSort;
  data.unshift(headerArray);
  buildTable(data, 'desc');
  return data;
}


function sort_2d_asc(twoDArray, column) {
  //Need to create a array without the first array (header_elements)
  var headerArray = twoDArray[0];
  var arrayToSort = [];
  for (var i = 1; i < twoDArray.length; i++) {
    arrayToSort[i - 1] = twoDArray[i];
  }

  if (column >= arrayToSort.length) return;

  for (var i = 0; i < arrayToSort.length - 1; i++) {
    for (var j = (i + 1); j < arrayToSort.length; j++) {
      // Falls das aktuelle Element kleiner ist -> tauschen
      //gehen der Reihe nach durch und tauschen an jede stelle das jetzige kleinste Element
      //First check if the 2 Values of the compared rows may be numbers
      var test = parseFloat(arrayToSort[j][column]);
      console.log("Debug--------------");
      console.log("" + test);
      if ((arrayToSort[j][column] < arrayToSort[i][column]) && isNaN(test)) {
        var arrHelp = arrayToSort[i];
        arrayToSort[i] = arrayToSort[j];
        arrayToSort[j] = arrHelp;
      } else if (parseFloat(arrayToSort[j][column]) < parseFloat(arrayToSort[i][column]) && !isNaN(parseFloat(arrayToSort[i][column]))) {
        var arrHelp = arrayToSort[i];
        arrayToSort[i] = arrayToSort[j];
        arrayToSort[j] = arrHelp;
      }
    }
  }
  data = arrayToSort;
  data.unshift(headerArray);
  buildTable(data, 'asc');
  return data;
}
