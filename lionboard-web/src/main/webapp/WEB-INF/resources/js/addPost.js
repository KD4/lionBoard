(function ($) {
    $("#post-form").submit(function () {

        var postTitle = $("input[name=title]").val();
        var authorId = $("input[name=userId]").val();

        if (postTitle.trim().length < 1) {
            alert("글 제목을 입력해주세요.");
            return false;
        }

        var formData = new FormData();
        formData.append("userId", authorId);
        formData.append("title", postTitle);
        formData.append("contents", $('#summernote').summernote('code'));
        formData.append("depth", "0");

        //첨부된 파일이 있을때만 formdata객체에 파일 속성을 생성함.
        if ($("input[name=uploadFile]")[0].files[0] != null) {
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
            dataType: 'text',
            success: function (responsedData) {

                window.location.replace("/posts/" + responsedData);

            }
        });

        return false;
    });


})(jQuery);

$(document).ready(function () {
    $('#summernote').summernote({
        height: 400,                 // set editor height
        minHeight: null,             // set minimum height of editor
        maxHeight: null,             // set maximum height of editor
        focus: true                  // set focus to editable area after initializing summernote
    });
});