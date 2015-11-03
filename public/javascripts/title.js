 var jqAjax;

$(document).ready(function() {
    jqAjax = $.ajax;
    hackToGetThePreviewWorking();

    loadTitle();

    bindRating('know', '#knowRateDiv');
    bindRating('like', '#likeRateDiv');

    $("a").linkpreview({
        previewContainer: "#preview-container",
        errorMessage: "BOO!"
    });

    var previewContainer = '#preview-container2';

    $("input").linkpreview({
        previewContainer: previewContainer,
        refreshButton: "#refresh-button",
        previewContainerClass: "row-fluid",
        errorMessage: "We are sorry we couldn't load the preview. We only support youtube URLs.",
        onSuccess: function() {
            var saveButton = $('<button>Save</button>');
            saveButton.attr('id', 'saveVideoButton');
            saveButton.attr('class', 'btn btn-primary');
            saveButton.attr('type', 'button');
            var url = $('#preview-container2 a').attr('href')
            saveButton.click(function() {
                jqAjax({
                    url: '/api/titles/' + titleId + '/videos',
                    data: { 'url': encodeURIComponent(url) },
                    type: 'POST',
                    success: function(data) {
                        console.log('Video successfully saved');
                    },
                    error: function(data) {
                        console.log('Error retrieving rating');
                    }
                });
            });
            $(previewContainer).append(saveButton);
        }
    });

    retrieveVideos();
});

function bindRating(ratingType, divId) {
    var knowRatingUrl = '/api/titles/' + titleId + '/ratings/' + ratingType;
    jqAjax({
        url: knowRatingUrl,
        type: 'GET',
        success: function(data) {
            if(data != 'N/A') {
                $(divId).rateit('value', data);
            }
        },
        error: function(data) {
            console.log('Error retrieving rating');
        }
    });
    $(divId).bind('rated', function(event, value) {
        jqAjax({
            url: knowRatingUrl,
            type: 'POST',
            data: { 'rating': value },
            error: function(data) {
                console.log('Error saving rating');
            }
        });
    });
    $(divId).bind('reset', function() {
        jqAjax({
            url: knowRatingUrl,
            type: 'DELETE',
            error: function() {
                console.log('Error deleting rating');
            }
        });
    });
}

function loadTitle() {
    jqAjax({
        url: '/api/titles/' + titleId,
        dataType: 'json',
        success: function(data) {
            var dl = $('<dl></dl>')//.attr('class', 'dl-horizontal');
            dl.append($('<dt></dt>').html("Title"));
            dl.append($('<dd></dd>').html(data.title));
            dl.append($('<dt></dt>').html("Year"));
            dl.append($('<dd></dd>').html(data.year));
            dl.append($('<dt></dt>').html("Rank"));
            dl.append($('<dd></dd>').html(data.ranking));
            $('#titleDetailDiv').html(dl);
        },
        error: function(data) {
            $('#titleDetailDiv').html('Error loading a random title');
        }
    });
}

function proxifyUrl(url) {
    return "/api/resolveLink?url=" + encodeURIComponent(url);
}

function hackToGetThePreviewWorking() {
    $.ajax = function(settings) {
        if (settings && settings.url) {
            var oldUrl = settings.url;
            settings.url = proxifyUrl(settings.url);

            var settingsCopy = {
                url: oldUrl,
                complete: settings.complete,
                success: settings.success,
                error: settings.error
            };

            settings.complete = function(jqXHR, textStatus) {
                settingsCopy.complete(jqXHR, textStatus);
            };

            settings.success = function(data, textStatus, jqXHR) {
                settingsCopy.success(data, textStatus, jqXHR);
            };

            settings.error = function(jqXHR, textStatus, errorThrown) {
                settingsCopy.error(jqXHR, textStatus, errorThrown);
            };
        }
        jqAjax(settings);
    };
}

function retrieveVideos() {
    jqAjax({
        url: '/api/titles/' + titleId + '/videos',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $.each(data.videos, function(i, item) {
                console.log(item);
                var videoDiv = '<div class="youtube" id="' + item.videoId +  '" style="width:560px; height: 315px;"></div>';
                $('#videosDiv').append(videoDiv);
            });
            setupVideoFrames();
        },
        error: function() {
            console.log('Error retrieving videos');
        }
    });
}