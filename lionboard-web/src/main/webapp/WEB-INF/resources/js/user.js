(function($) {

    $("#edit-user").submit(function(){
        var userId =$("input[name=userId]").val();
        if($("input[name=profileImage]")[0].files[0] != null){
            var formData = new FormData();

            formData.append("profileImage", $("input[name=profileImage]")[0].files[0]);

            $.ajax({
                url: '/users/'+userId+"/profile",
                type: 'post',
                data: formData,
                cache: false,
                processData: false, // Don't process the files
                contentType: false, // Set content type to false as jQuery will tell the server its a query string request,
                dataType:'text',
                success:function(responsedData){
                    if(responsedData=="success"){
                        var userInfo = {
                            name:$("input[name=name]").val(),
                            email:$("input[name=email]").val(),
                        };
                        $.ajax({
                            url: '/users/'+userId,
                            type: 'put',
                            data: JSON.stringify(userInfo),
                            contentType:"application/json; charset=UTF-8",
                            dataType: 'text',
                            success: function (userId) {
                                window.location.replace("/users/"+userId);
                            }
                        });
                    }else{
                        alert(responsedData);
                    }
                }
            });
        }else{
            var userInfo = {
                name:$("input[name=name]").val(),
                email:$("input[name=email]").val(),
            };
            $.ajax({
                url: '/users/'+userId,
                type: 'put',
                data: JSON.stringify(userInfo),
                contentType:"application/json; charset=UTF-8",
                dataType: 'text',
                success: function (userId) {
                    window.location.replace("/users/"+userId);
                }
            });
        }
        return false;

    });

})(jQuery);
