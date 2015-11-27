$(document).ready(function() {
    var playlistSource = $("#activityTemplate").html();
    var template = Handlebars.compile(playlistSource);
    $.ajax({
        url: '/api/activities',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#activityDiv').append(template(data));
        },
        error: function() {
            console.log('Error retrieving activity stream');
        }
    });
});