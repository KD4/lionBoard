(function($) {
    $("#edit-form").submit(function(){
        var postId =$("input[name=postId]").val();
        var postInfo = {
            title:$("input[name=title]").val(),
            contents:CKEDITOR.instances.contents.getData(),
            postId:postId,
            userId:$("input[name=userId]").val(),
            existFiles:'F'
        };


        $.ajax({
            url: '/posts/'+postId,
            type: 'put',
            data: JSON.stringify(postInfo),
            contentType:"application/json; charset=UTF-8",
            dataType: 'text',
            success: function (postId) {
                window.location.replace("/posts/"+postId);
            }
        });

        return false;
    });
})(jQuery);