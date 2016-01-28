
(function($) {
    $(".comment-reply").click(function(){
        var num = $(this).data('num');
        $(".replyNum"+num).toggleClass("hide");
    });

    $(".edit-btn").click(function(){
        var postId = $(this).data('postid');
        var urlToEdit = "/editPost/"+postId;
        window.location.replace(urlToEdit);
    });

})(jQuery);