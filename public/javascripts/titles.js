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
        { "mData": "id", "sTitle": "Id" },
        { "mData": "title", "sTitle": "Title" },
        { "mData": "year", "sTitle": "Year" },
        { "mData": "ranking", "sTitle": "Rank" }
    ]
  });
  setTableHover(dataTable);
});

function setTableHover(dataTable) {
    dataTable.find('tbody').on('click', 'td', function() {
      if(dataTable.fnGetData().length > 0 && !$(this).hasClass('actions')) {
          var id = $('td', this.parentElement).eq(0).text();
          window.location = '/titles/' + id;
      }
    });
}