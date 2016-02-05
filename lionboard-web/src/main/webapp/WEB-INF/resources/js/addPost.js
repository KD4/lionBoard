(function($) {
    $("#post-form").submit(function(){

        var formData = new FormData();
        formData.append("userId",$("input[name=userId]").val());
        formData.append("title",$("input[name=title]").val());
        formData.append("contents",CKEDITOR.instances.contents.getData());
        formData.append("depth","0");

        //첨부된 파일이 있을때만 formdata객체에 파일 속성을 생성함.
        if($("input[name=uploadFile]")[0].files[0] != null){
            formData.append("uploadFile", $("input[name=uploadFile]")[0].files[0]);
        }
        console.log(formData);
        $.ajax({
            url: '/posts',
            type: 'post',
            data: formData,
            cache: false,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request,
            dataType:'text',
            success:function(responsedData){

                window.location.replace("/posts/"+responsedData);

            }
        });

        return false;
    });
})(jQuery);