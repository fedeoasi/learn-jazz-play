var dataTable;
$(document).ready(function() {
  dataTable = $('#aebersoldTable').dataTable({
    //"sDom": 'T<"clear">lfrtip',
    "bProcessing": true,
    "bStateSave": true,
    "bSort" : true,
    "sAjaxSource": 'api/aebersold',
    "sAjaxDataProp": 'songs',
    "aoColumns": [
        { "mData": "title", "sTitle": "Title" },
        { "mData": "songType", "sTitle": "Type" },
        { "mData": "key", "sTitle": "Key", "sWidth": "120px" },
        { "mData": "tempo", "sTitle": "Tempo", "sWidth": "120px", "defaultContent": "-" },
        { "mData": "chorusNum", "sTitle": "Chorus Number" }
    ]
  });
});