function setupVideoFrames() {
    // Find all the YouTube video embedded on a page
    var videos = document.getElementsByClassName("youtube");

    for (var i = 0; i< videos.length; i++) {
        var youtube = videos[i];

        // Based on the YouTube ID, we can easily find the thumbnail image
        var img = document.createElement("img");
        var videoUrl = "http://i.ytimg.com/vi/" + youtube.id + "/mqdefault.jpg";
        img.setAttribute("src", videoUrl);
        img.setAttribute("class", "thumb");
        img.setAttribute("width", "100%");

        // Overlay the Play icon to make it look like a video player
        var circle = document.createElement("div");
        circle.setAttribute("class","circle");

        youtube.appendChild(img);
        youtube.appendChild(circle);

        // Attach an onclick event to the YouTube Thumbnail
        youtube.onclick = function() {

            // Create an iFrame with autoplay set to true
            var iframe = document.createElement("iframe");
            iframe.setAttribute("src",
                "https://www.youtube.com/embed/" + this.id
                + "?autoplay=1&autohide=1&border=0&wmode=opaque&enablejsapi=1");
            iframe.setAttribute("allowfullscreen", "true");

            // Replace the YouTube thumbnail with YouTube HTML5 Player
            this.parentNode.replaceChild(iframe, this);
        };
    }
}

function retrieveVideos(ajaxSource) {
    var playlistSource = $("#videosTemplate").html();
    var template = Handlebars.compile(playlistSource);
    jqAjax({
        url: ajaxSource,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#videosDiv').append(template(data));
            setupVideoFrames();
        },
        error: function() {
            console.log('Error retrieving videos');
        }
    });
}