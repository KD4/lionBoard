(function($) {
    $("#post-form").submit(function(ev){
        var postInfo = {
            title:$("input[name=title]").val(),
            contents:CKEDITOR.instances.contents.getData(),
            depth:0,
            userId:$("input[name=userId]").val(),
            existFiles:'F'
        };


        $.ajax({
            url: '/posts',
            type: 'post',
            data: postInfo,
            dataType: 'text',
            success: function (postId) {
                window.location.replace("/posts/"+postId);
            }
        });

        return false;
    });
})(jQuery);