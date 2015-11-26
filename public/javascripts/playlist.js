$(document).ready(function() {
    var playlistSource = $("#playlistTemplate").html();
    var template = Handlebars.compile(playlistSource);
    $.ajax({
        url: '/api/playlists/new',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#playlistDiv').append(template(data));
        },
        error: function() {
            console.log('Error retrieving playlist');
        }
    });
});