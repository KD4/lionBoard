
(function($) {
    $loginUserId = $("#loginUser").data('userid');
    $currentPostId = $("#currentPost").data('postid');

    $(".comment-reply").click(function(){
        var cmtId = $(this).data('cmtid');
        if($loginUserId == null){
            alert("로그인을 해주세요.");
            window.location.replace("/login");
        }else{
            $(".replyId-"+cmtId).toggleClass("hide");
        }
    });

    $(".comment-delete").click(function () {
        var cmtId = $(this).data('cmtid');
        var data = {
            statusCode:'D'
        }
        $.ajax({
            url: '/comments/'+cmtId+"/status?statusCode=D",
            type: 'put',
            dataType: 'text',
            success: function (data) {
                if(data==="success"){
                    window.location.replace("/posts/"+$currentPostId);
                }else{
                    alert(data);
                }
            },
            error: function(data) {

            }
        });
    });

    $(".edit-btn").click(function(){
        var urlToEdit = "/view/editPost/"+$currentPostId;
        window.location.replace(urlToEdit);
    });

    $("#comment-from").submit(function(){
        var comment = {
            userId:$loginUserId,
            postId:$currentPostId,
            depth:0,
            contents:$("#comment-contents").val()
        };


        $.ajax({
            url: '/posts/'+$currentPostId+"/comments",
            type: 'post',
            data: comment,
            dataType: 'text',
            success: function (data) {
                if(data==="success"){
                    window.location.replace("/posts/"+$currentPostId);
                }else{
                    alert(data);
                }
            },
            error: function(data) {

            }
        });

        return false;
    });

    $(".reply-comment-form").submit(function(){
        var $parentDepth = $(this).data('depth');
        var depth = $parentDepth + 1;
        var parentNum = $(this).data('parentnum');
        var contents = $("textarea[name=contents]",this).val();
        var comment = {
            userId:$loginUserId,
            postId:$currentPostId,
            depth:depth,
            cmtNum:parentNum,
            contents:contents
        };

        $.ajax({
            url: '/posts/'+$currentPostId+"/comments",
            type: 'post',
            data: comment,
            dataType: 'text',
            success: function (data) {
                if(data==="success"){
                    window.location.replace("/posts/"+$currentPostId);
                }else{
                    alert(data);
                }
            },
            error: function(data) {

            }
        });

        return false;
    });

})(jQuery);