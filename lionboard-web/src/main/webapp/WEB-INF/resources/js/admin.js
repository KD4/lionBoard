(function($) {

    $('#admin-nav-search').submit(function(){
       var source = $(this).data('source');

        if(source == 'users'){
            searchUsers(source);
        }else if(source == 'posts'){
            searchPosts(source);
        }else if(source == 'comments'){
            searchCmts(source);
        }else if(source == 'posts/reports'){
            searchPostReports(source);
        }else if(source == 'comments/reports'){
            searchCmtReports(source);
        }


        return false;
    });

    $("#admin-table-tbody").on('click','.change-user-status',function(){
        var userId = $(this).data('userid');
        var userName = $(this).data('username');
        var beStatus = $(this).data('status');
        var statusName = $(this).text();
        if(confirm(userName+"님의 상태를 "+statusName+"으로 변경하시겠습니까?")){

            var userInfo = {
                id: userId,
                userStatus : beStatus
            };

            $.ajax({
                url:'/users/'+userId+'/status',
                type:'put',
                data: JSON.stringify(userInfo),
                contentType:"application/json; charset=UTF-8",
                dataType:'text',
                success:function(returnData){
                    if(returnData=='success'){
                        alert("변경되었습니다.");
                        window.location.replace("/admin/users");
                    }else{
                        alert(returnData);
                    }
                },
                error:function(returnData){
                    alert(returnData);
                }
            });

        }else{
            alert("취소하셨습니다.");
        }
    });

    $("#admin-table-tbody").on('click','.change-user-role',function(){
            var userId = $(this).data('userid');
            var userName = $(this).data('username');
            var beRole = $(this).data('roles');
            var roleName = $(this).text();
            if(confirm(userName+"님의 상태를 "+roleName+"으로 변경하시겠습니까?")){

                var userInfo = {
                    id: userId,
                    roles : beRole
                };

                $.ajax({
                    url:'/users/'+userId+'/roles',
                    type:'put',
                    data: JSON.stringify(userInfo),
                    contentType:"application/json; charset=UTF-8",
                    dataType:'text',
                    success:function(returnData){
                        if(returnData=='success'){
                            alert("변경되었습니다.");
                            window.location.replace("/admin/users");
                        }else{
                            alert(returnData);
                        }
                    },
                    error:function(returnData){
                        alert(returnData);
                    }
                });

            }else{
                alert("취소하셨습니다.");
            }

    });


    $("#admin-table-tbody").on('click','.change-post-status',function(){
        var postId = $(this).data('postid');
        var beStatus = $(this).data('status');
        var statusName = $(this).text();
        if(confirm(postId+"글의 상태를 "+statusName+"으로 변경하시겠습니까?")){

            var postInfo = {
                postId: postId,
                postStatus : beStatus
            };

            $.ajax({
                url:'/posts/'+postId+'/status',
                type:'put',
                data: JSON.stringify(postInfo),
                contentType:"application/json; charset=UTF-8",
                dataType:'text',
                success:function(returnData){
                    if(returnData=='success'){
                        alert("변경되었습니다.");
                        window.location.replace("/admin/posts");
                    }else{
                        alert(returnData);
                    }
                },
                error:function(returnData){
                    alert(returnData);
                }
            });

        }else{
            alert("취소하셨습니다.");
        }

    });

    $("#admin-table-tbody").on('click','.change-comment-status',function(){
        var commentId = $(this).data('commentid');
        var beStatus = $(this).data('status');
        var statusName = $(this).text();
        if(confirm(commentId+"글의 상태를 "+statusName+"으로 변경하시겠습니까?")){

            var cmtInfo = {
                cmtId: commentId,
                cmtStatus : beStatus
            };

            $.ajax({
                url:'/comments/'+commentId+'/status',
                type:'put',
                data: JSON.stringify(cmtInfo),
                contentType:"application/json; charset=UTF-8",
                dataType:'text',
                success:function(returnData){
                    if(returnData=='success'){
                        alert("변경되었습니다.");
                        window.location.replace("/admin/comments");
                    }else{
                        alert(returnData);
                    }
                },
                error:function(returnData){
                    alert(returnData);
                }
            });

        }else{
            alert("취소하셨습니다.");
        }

    });

    $("#admin-table-tbody").on('click','.change-post-report-status',function(){
        var reportId = $(this).data('reportid');
        var postId = $(this).data('postid');
        var beStatus = $(this).data('status');
        var statusName = $(this).text();
        if(confirm(postId+"글에 대한 신고 처리 결과를 "+statusName+"으로 변경하시겠습니까? Pass를 하시게되면 신고를 무시하고, Complete를 하시게되면 해당 게시글의 상태가 T상태가 됩니다.")){

            var reportInfo = {
                id: reportId,
                postId : postId,
                processStatus : beStatus
            };

            $.ajax({
                url:'/posts/'+postId+'/reports',
                type:'put',
                data: JSON.stringify(reportInfo ),
                contentType:"application/json; charset=UTF-8",
                dataType:'text',
                success:function(returnData){
                    if(returnData=='success'){
                        alert("변경되었습니다.");
                        window.location.replace("/admin/postReports");
                    }else{
                        alert(returnData);
                    }
                },
                error:function(returnData){
                    alert(returnData);
                }
            });

        }else{
            alert("취소하셨습니다.");
        }

    });

    $("#admin-table-tbody").on('click','.change-cmt-report-status',function(){
        var reportId = $(this).data('reportid');
        var cmtId = $(this).data('cmtid');
        var beStatus = $(this).data('status');
        var statusName = $(this).text();
        if(confirm(cmtId+"댓글에 대한 신고 처리 결과를 "+statusName+"으로 변경하시겠습니까? Pass를 하시게되면 신고를 무시하고, Complete를 하시게되면 해당 게시글의 상태가 T상태가 됩니다.")){

            var cmtInfo = {
                id: reportId,
                cmtId : cmtId,
                processStatus : beStatus
            };

            $.ajax({
                url:'/comments/'+cmtId+'/reports',
                type:'put',
                data: JSON.stringify(cmtInfo),
                contentType:"application/json; charset=UTF-8",
                dataType:'text',
                success:function(returnData){
                    if(returnData=='success'){
                        alert("변경되었습니다.");
                        window.location.replace("/admin/cmtReports");
                    }else{
                        alert(returnData);
                    }
                },
                error:function(returnData){
                    alert(returnData);
                }
            });

        }else{
            alert("취소하셨습니다.");
        }

    });



    function searchUsers(source){
        var targetUrl = '/'+source+'/search?query='+$("input[name=search-keyword]").val();
        $.ajax({
            url: targetUrl,
            type: 'get',
            dataType:'json',
            success:function(data){
                var $tbody = $("#admin-table-tbody");
                $tbody.html('');
                for (var i = 0; i < data.length; i++) {

                    var d = new Date(data[i]['registeredAt']);
                    var day = d.getDate();
                    var month = d.getMonth() + 1;
                    var year = d.getFullYear();
                    if (day < 10) {
                        day = "0" + day;
                    }
                    if (month < 10) {
                        month = "0" + month;
                    }
                    var registeredAt = day + "/" + month + "/" + year;



                    $tr ="<tr>" +
                        "<td>"+data[i]['id']+"</td>" +
                        "<td><a href='/users/"+data[i]['id']+"'>"+data[i]['identity']+" : "+data[i]['name']+"</a></td>"+
                        "<td class='admin-tbody-date'>"+registeredAt+"</td>"+
                        "<td>"+
                        "<div class='dropdown'>"+
                        "<button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-expanded='true'>"+
                        data[i]['userStatus']+
                        "<span class='caret'></span>"+
                        "</button>"+
                        "<ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu1'>"+
                        "<li role='presentation'>"+
                        "<a class='change-user-status' data-userId='"+data[i]['id']+"' data-username='"+data[i]['name']+"' data-status='A' role='menuitem' tabindex='-1' href='#'>Active</a>"+
                        "</li>"+
                        "<li role='presentation'>"+
                        "<a class='change-user-status' data-userId='"+data[i]['id']+"' data-username='"+data[i]['name']+"' data-status='B' role='menuitem' tabindex='-1' href='#'>Block</a>"+
                        "</li>"+
                        "</ul>"+
                        "</div>"+
                        "</td>"+
                        "<td>"+
                        "<div class='dropdown'>"+
                        "<button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu2' data-toggle='dropdown' aria-expanded='true'>"+
                        data[i]['roles']+
                        "<span class='caret'></span>"+
                        "</button>"+
                        "<ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu2'>"+
                        "<li role='presentation'><a class='change-user-role' data-userId='"+data[i]['id']+"' data-username='"+data[i]['name']+"' data-roles='ROLE_USER' role='menuitem' tabindex='-1' href='#'>사용자</a></li>"+
                        "<li role='presentation'><a class='change-user-role' data-userId='"+data[i]['id']+"' data-username='"+data[i]['name']+"' data-roles='ROLE_MANAGER' role='menuitem' tabindex='-1' href='#'>관리자</a></li>"+
                        "<li role='presentation'><a class='change-user-role' data-userId='"+data[i]['id']+"' data-username='"+data[i]['name']+"' data-roles='ROLE_ADMIN' role='menuitem' tabindex='-1' href='#'>마스터</a></li>"+
                        "</ul>"+
                        "</div>"+
                        "</td>"+
                        "</tr>";
                    $tbody.append($tr);

                }
            }
        });
    }

    function searchPosts(source) {
        var targetUrl = '/' + source + '/search?query=' + $("input[name=search-keyword]").val();
        $.ajax({
            url: targetUrl,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                var $tbody = $("#admin-table-tbody");
                $tbody.html('');
                for (var i = 0; i < data.length; i++) {

                    var d = new Date(data[i]['createdAt']);
                    var day = d.getDate();
                    var month = d.getMonth() + 1;
                    var year = d.getFullYear();
                    if (day < 10) {
                        day = "0" + day;
                    }
                    if (month < 10) {
                        month = "0" + month;
                    }
                    var createdAt = day + "/" + month + "/" + year;


                    $tr = "<tr>" +
                        "<td>" + data[i]['postId'] + "</td>" +
                        "<td><a href='/posts/" + data[i]['postId'] + "'>" + data[i]['title'] + "</a></td>" +
                        "<td><a href='/users/" + data[i]['userId'] + "'>" + data[i]['userName'] + "</a></td>" +
                        "<td class='admin-tbody-date'>" + createdAt + "</td>" +
                        "<td>" +
                        "<div class='dropdown'>" +
                        "<button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-expanded='true'>" +
                        data[i]['postStatus'] +
                        "<span class='caret'></span>" +
                        "</button>" +
                        "<ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu1'>" +
                        "<li role='presentation'>" +
                        "<a class='change-post-status' data-postId='" + data[i]['postId'] + "' data-status='S' role='menuitem' tabindex='-1' href='#'>Service</a>" +
                        "</li>" +
                        "<li role='presentation'>" +
                        "<a class='change-post-status' data-postId='" + data[i]['postId'] + "' data-status='T' role='menuitem' tabindex='-1' href='#'>Temp</a>" +
                        "</li>" +
                        "<li role='presentation'>" +
                        "<a class='change-post-status' data-postId='" + data[i]['postId'] + "' data-status='D' role='menuitem' tabindex='-1' href='#'>Delete</a>" +
                        "</li>" +
                        "<li role='presentation'>" +
                        "<a class='change-post-status' data-postId='" + data[i]['postId'] + "' data-status='A' role='menuitem' tabindex='-1' href='#'>Admin Delete</a>" +
                        "</li>" +
                        "</ul>" +
                        "</div>" +
                        "</td>" +
                        "</tr>";
                    $tbody.append($tr);

                }

            }
        });
    }

        function searchCmts(source) {
            var targetUrl = '/' + source + '/search?query=' + $("input[name=search-keyword]").val();
            $.ajax({
                url: targetUrl,
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    var $tbody = $("#admin-table-tbody");
                    $tbody.html('');
                    for (var i = 0; i < data.length; i++) {

                        var d = new Date(data[i]['createdAt']);
                        var day = d.getDate();
                        var month = d.getMonth() + 1;
                        var year = d.getFullYear();
                        if (day < 10) {
                            day = "0" + day;
                        }
                        if (month < 10) {
                            month = "0" + month;
                        }
                        var createdAt = day + "/" + month + "/" + year;


                        $tr = "<tr>" +
                            "<td>" + data[i]['cmtId'] + "</td>" +
                            "<td><a href='/posts/" + data[i]['postId'] + "'>" + data[i]['contents'] + "</a></td>" +
                            "<td><a href='/users/" + data[i]['userId'] + "'>" + data[i]['userName'] + "</a></td>" +
                            "<td class='admin-tbody-date'>" + createdAt + "</td>" +
                            "<td>" +
                            "<div class='dropdown'>" +
                            "<button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-expanded='true'>" +
                            data[i]['cmtStatus'] +
                            "<span class='caret'></span>" +
                            "</button>" +
                            "<ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu1'>" +
                            "<li role='presentation'>" +
                            "<a class='change-comment-status' data-commentId='" + data[i]['cmtId'] + "' data-status='S' role='menuitem' tabindex='-1' href='#'>Service</a>" +
                            "</li>" +
                            "<li role='presentation'>" +
                            "<a class='change-comment-status' data-commentId='" + data[i]['cmtId'] + "' data-status='T' role='menuitem' tabindex='-1' href='#'>Temp</a>" +
                            "</li>" +
                            "<li role='presentation'>" +
                            "<a class='change-comment-status' data-commentId='" + data[i]['cmtId'] + "' data-status='D' role='menuitem' tabindex='-1' href='#'>Delete</a>" +
                            "</li>" +
                            "<li role='presentation'>" +
                            "<a class='change-post-status' data-postId='" + data[i]['postId'] + "' data-status='A' role='menuitem' tabindex='-1' href='#'>Admin Delete</a>" +
                            "</li>" +
                            "</ul>" +
                            "</div>" +
                            "</td>" +
                            "</tr>";
                        $tbody.append($tr);

                    }

                }
            });
        }


    function searchPostReports(source) {
        var targetUrl = '/' + source + '/search?query=' + $("input[name=search-keyword]").val();
        $.ajax({
            url: targetUrl,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                var $tbody = $("#admin-table-tbody");
                $tbody.html('');
                for (var i = 0; i < data.length; i++) {

                    var d = new Date(data[i]['reportedAt']);
                    var day = d.getDate();
                    var month = d.getMonth() + 1;
                    var year = d.getFullYear();
                    if (day < 10) {
                        day = "0" + day;
                    }
                    if (month < 10) {
                        month = "0" + month;
                    }
                    var reportedAt = day + "/" + month + "/" + year;


                    $tr = "<tr>" +
                        "<td><a href='/posts/" + data[i]['postId'] + "'>" + data[i]['postId'] + "</a></td>" +
                        "<td>" + data[i]['reason'] + "</a></td>" +
                        "<td class='admin-tbody-date'>" + reportedAt + "</td>" +
                        "<td><a href='/users/" + data[i]['reporterId'] + "'>" + data[i]['reporterId'] + "</a></td>" +
                        "<td>" +
                        "<div class='dropdown'>" +
                        "<button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-expanded='true'>" +
                        data[i]['processStatus'] +
                        "<span class='caret'></span>" +
                        "</button>" +
                        "<ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu1'>" +
                        "<li role='presentation'>" +
                        "<a class='change-post-report-status' data-reportId="+data[i]['id']+" data-postId='" + data[i]['postId'] + "' data-status='N' role='menuitem' tabindex='-1' href='#'>New</a>" +
                        "</li>" +
                        "<li role='presentation'>" +
                        "<a class='change-post-report-status' data-reportId="+data[i]['id']+" data-postId='" + data[i]['postId'] + "' data-status='P' role='menuitem' tabindex='-1' href='#'>Pass</a>" +
                        "</li>" +
                        "<li role='presentation'>" +
                        "<a class='change-post-report-status' data-reportId="+data[i]['id']+" data-postId='" + data[i]['postId'] + "' data-status='C' role='menuitem' tabindex='-1' href='#'>Complete</a>" +
                        "</li>" +
                        "</ul>" +
                        "</div>" +
                        "</td>" +
                        "</tr>";
                    $tbody.append($tr);

                }

            }
        });
    }

    function searchCmtReports(source) {
        var targetUrl = '/' + source + '/search?query=' + $("input[name=search-keyword]").val();
        $.ajax({
            url: targetUrl,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                var $tbody = $("#admin-table-tbody");
                $tbody.html('');
                for (var i = 0; i < data.length; i++) {

                    var d = new Date(data[i]['reportedAt']);
                    var day = d.getDate();
                    var month = d.getMonth() + 1;
                    var year = d.getFullYear();
                    if (day < 10) {
                        day = "0" + day;
                    }
                    if (month < 10) {
                        month = "0" + month;
                    }
                    var reportedAt = day + "/" + month + "/" + year;


                    $tr = "<tr>" +
                        "<td><a href='/posts/" + data[i]['postId'] + "'>" + data[i]['cmtId'] + "</a></td>" +
                        "<td>" + data[i]['reason'] + "</a></td>" +
                        "<td class='admin-tbody-date'>" + reportedAt + "</td>" +
                        "<td><a href='/users/" + data[i]['reporterId'] + "'>" + data[i]['reporterId'] + "</a></td>" +
                        "<td>" +
                        "<div class='dropdown'>" +
                        "<button class='btn btn-default dropdown-toggle' type='button' id='dropdownMenu1' data-toggle='dropdown' aria-expanded='true'>" +
                        data[i]['processStatus'] +
                        "<span class='caret'></span>" +
                        "</button>" +
                        "<ul class='dropdown-menu' role='menu' aria-labelledby='dropdownMenu1'>" +
                        "<li role='presentation'>" +
                        "<a class='change-cmt-report-status' data-reportId="+data[i]['id']+" data-cmtId='" + data[i]['cmtId'] + "' data-status='N' role='menuitem' tabindex='-1' href='#'>New</a>" +
                        "</li>" +
                        "<li role='presentation'>" +
                        "<a class='change-cmt-report-status' data-reportId="+data[i]['id']+" data-cmtId='" + data[i]['cmtId'] + "' data-status='P' role='menuitem' tabindex='-1' href='#'>Pass</a>" +
                        "</li>" +
                        "<li role='presentation'>" +
                        "<a class='change-cmt-report-status' data-reportId="+data[i]['id']+" data-cmtId='" + data[i]['cmtId'] + "' data-status='C' role='menuitem' tabindex='-1' href='#'>Complete</a>" +
                        "</li>" +
                        "</ul>" +
                        "</div>" +
                        "</td>" +
                        "</tr>";
                    $tbody.append($tr);

                }

            }
        });
    }

})(jQuery);