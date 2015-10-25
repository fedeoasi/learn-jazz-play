$(document).ready(function() {
    $('#randomSongButton').click(function() {
        $.ajax({
            url: '/api/titles/random',
            dataType: 'json',
            success: function(data) {
                var link = $('<a>' + data.title + '</a>');
                link.attr('href', '/titles/' + data.id)
                $('#randomSongSpan').html(link);
            },
            error: function(data) {
                $('#randomSongSpan').html('Error loading a random title');
            }
        });
    });
});