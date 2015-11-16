var jqAjax;

$(document).ready(function() {
    jqAjax = $.ajax;
    retrieveVideos('/api/videos');
});