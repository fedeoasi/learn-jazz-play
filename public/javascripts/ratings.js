$(document).ready(function() {
    var playlistSource = $("#ratingsTemplate").html();
    var template = Handlebars.compile(playlistSource);
    $.ajax({
        url: '/api/ratings/favoriteTitles',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#ratingsDiv').append(template(data));
            $.each($('.rateit'), function(i, item) {
                $(item).rateit();
            });
        },
        error: function() {
            console.log('Error retrieving ratings');
        }
    });
});
