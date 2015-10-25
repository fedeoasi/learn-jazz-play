var dataTable;
$(document).ready(function() {
  dataTable = $('#titleTable').dataTable({
    //"sDom": 'T<"clear">lfrtip',
    "bProcessing": true,
    "bStateSave": true,
    "bSort" : true,
    "sAjaxSource": '/api/titles',
    "sAjaxDataProp": 'titles',
    "aoColumns": [
        { "mData": "title", "sTitle": "Title" },
        { "mData": "year", "sTitle": "Year" },
        { "mData": "ranking", "sTitle": "Rank" }
    ]
  });
});