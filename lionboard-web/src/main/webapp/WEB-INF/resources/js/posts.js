
(function($) {
    $loginUserId = $("#loginUser").data('userid');
    $currentPostId = $("#currentPost").data('postid');


    $("#edit-post").click(function(){
        var urlForEdit = "/view/editPost/"+$currentPostId;
        window.location.replace(urlForEdit);
    });
    $('#delete-post').click(function(){
        if (confirm("정말 삭제하시겠습니까??") == true){    //확인
            var urlForDelete = "/posts/"+$currentPostId;
            $.ajax({
                url: urlForDelete,
                type: 'delete',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        alert("삭제되었습니다.");
                        //삭제 후 메인 페이지로 이동.
                        window.location.replace("/index");
                    }else {
                        alert(data);
                    }
                },
                error: function(data) {
                    alert(data);
                }
            });
        }else{   //취소
            return;
        }
    });
    $('#reply-post').click(function(){
        var urlToEdit = "/view/replyPost/"+$currentPostId;
        window.location.replace(urlToEdit);
    });

    $(".post-like").click(function(){
        var postId = $(this).data('postid');
        var beforeCount = $(this).text().trim().substring(3).trim();
        var $aTag = $(this);
        console.log(beforeCount);
        //이미 좋아요를 눌렀으면 wasPushed 클래스가 적용되어 있음.
        if($aTag.hasClass("active")){
            //좋아요늘 다시 누르면 좋아요 취소~
            $.ajax({
                url: '/posts/'+postId+"/likes?action=sub",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) - 1;
                        $aTag.text("좋아요 "+afterCount);
                        $aTag.removeClass("active");
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
                url: '/posts/'+postId+"/likes?action=add",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) + 1;
                        $aTag.text("좋아요 "+afterCount);
                        $aTag.addClass("active");
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

    $(".post-hate").click(function(){
        var postId = $(this).data('postid');
        var beforeCount = $(this).text().trim().substring(3).trim();
        var $aTag = $(this);

        //이미 좋아요를 눌렀으면 wasPushed 클래스가 적용되어 있음.
        if($aTag.hasClass("active")){
            //좋아요늘 다시 누르면 좋아요 취소~
            $.ajax({
                url: '/posts/'+postId+"/hates?action=sub",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) - 1;
                        $aTag.text("싫어요 "+afterCount);
                        $aTag.removeClass("active");
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
                url: '/posts/'+postId+"/hates?action=add",
                type: 'put',
                dataType: 'text',
                success: function (data) {
                    if(data==="success"){
                        var afterCount = Number(beforeCount) + 1;
                        $aTag.text("싫어요 "+afterCount);
                        $aTag.addClass("active");
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
        var cmtInfo = {
            cmtId : cmtId,
            cmtStatus:'D'
        };
        $.ajax({
            url: '/comments/'+cmtId+"/status",
            type: 'put',
            data: JSON.stringify(cmtInfo),
            contentType:"application/json; charset=UTF-8",
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
        var depth = Number($(this).data('depth'))+1;
        var cmtNum = Number($(this).data('parentnum'))-1;
        var contents = $("textarea[name=contents]",this).val();
        var comment = {
            userId:$loginUserId,
            postId:$currentPostId,
            depth:depth,
            cmtNum:cmtNum,
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


    $(".go2Parent").click(function(){

        window.location.replace("/posts/"+$currentPostId+"/parent");

    });

    <!-- 게시글 신고하기 기능에 대한 스크립트 -->
    $("#report-post-form").submit(function(){

        var formData = new FormData();
        formData.append("postId",$currentPostId);
        formData.append("reporterId",$loginUserId);
        //첨부된 파일이 있을때만 formdata객체에 파일 속성을 생성함.
        formData.append("reason", $("#postReason").val());
        $.ajax({
            url: '/posts/'+$currentPostId+"/reports",
            type: 'post',
            data: formData,
            cache: false,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request,
            dataType:'text',
            success:function(responsedData){
                if(responsedData=="success"){
                    alert("이 게시물을 신고하였습니다.");
                    $('#reportPostModal').modal('toggle');
                }else{
                    alert(responsedData);
                }
            },
            error:function(responsedData){
                alert(responsedData);
            }
        });

        return false;
    });

    <!-- 댓글 신고하기 기능에 대한 스크립트 -->
    $(".comment-report").click(function(){
        $("#report-cmtId").val($(this).data('cmtid'));
        $("#reportCmtModal").modal('toggle');
    });

    $("#report-cmt-form").submit(function(){

        var formData = new FormData();
        formData.append("cmtId",$("input[name=cmtId]").val());
        formData.append("reporterId",$loginUserId);
        //첨부된 파일이 있을때만 formdata객체에 파일 속성을 생성함.
        formData.append("reason", $("#cmtReason").val());
        $.ajax({
            url: '/comments/'+$("input[name=cmtId]").val()+"/reports",
            type: 'post',
            data: formData,
            cache: false,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request,
            dataType:'text',
            success:function(responsedData){
                if(responsedData=="success"){
                    alert("해당 댓글을 신고하였습니다.");
                    $('#reportCmtModal').modal('toggle');
                }else{
                    alert(responsedData);
                }
            },
            error:function(responsedData){
                alert(responsedData);
            }
        });

        return false;
    });

})(jQuery);