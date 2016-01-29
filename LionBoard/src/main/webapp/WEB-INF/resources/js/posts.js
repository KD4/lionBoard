
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
    $(".comment-like").click(function(){
        var cmtId = $(this).data('cmtid');
        var beforeCount = $(this).text().substring(4).trim();
        var $aTag = $(this);

        //이미 좋아요를 눌렀으면 wasClicked 클래스가 적용되어 있음.
        if($aTag.hasClass("wasClicked")){
            //좋아요늘 다시 누르면 좋아요 취소~
            $.ajax({
                url: '/comments/'+cmtId+"/likes?action=sub",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) - 1;
                        $aTag.text("Like "+afterCount);
                        $aTag.removeClass("wasClicked");
                    }else{
                        alert(data);
                    }
                    return false;
                },
                error: function(data) {

                }
            });
        }else{
            //좋아요 + 1 요청
            $.ajax({
                url: '/comments/'+cmtId+"/likes?action=add",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) + 1;
                        $aTag.text("Like "+afterCount);
                        $aTag.addClass("wasClicked");
                    }else{
                        alert(data);
                    }
                    return false;
                },
                error: function(data) {

                }
            });
        }
    });
    $(".comment-hate").click(function(){
        var cmtId = $(this).data('cmtid');
        var beforeCount = $(this).text().substring(4).trim();
        var $aTag = $(this);

        //이미 싫어요 눌렀으면 was clicked 클래스가 적용되어 있음.
        if($aTag.hasClass("wasClicked")){
            //싫어요를 다시 누르면 싫어요 취소!
            $.ajax({
                url: '/comments/'+cmtId+"/hates?action=sub",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) - 1;
                        $aTag.text("Hate "+afterCount);
                        $aTag.removeClass("wasClicked");
                    }else{
                        alert(data);
                    }
                    return false;
                },
                error: function(data) {

                }
            });
        }else{
            //싫어요 + 1 요청
            $.ajax({
                url: '/comments/'+cmtId+"/hates?action=add",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) + 1;
                        $aTag.text("Hate "+afterCount);
                        $aTag.addClass("wasClicked");
                    }else{
                        alert(data);
                    }
                    return false;
                },
                error: function(data) {

                }
            });
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