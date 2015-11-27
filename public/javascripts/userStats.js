$(document).ready(function() {
    var playlistSource = $("#userStatsTemplate").html();
    var template = Handlebars.compile(playlistSource);
    $.ajax({
        url: '/api/stats',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#userStatsDiv').append(template(data));
        },
        error: function() {
            console.log('Error retrieving user statistics');
        }
    });
});