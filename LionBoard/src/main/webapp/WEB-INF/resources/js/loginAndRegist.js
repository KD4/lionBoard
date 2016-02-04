(function($) {
    "use strict";
	
	// Options for Message
	//----------------------------------------------
  var options = {
	  'btn-loading': '<i class="fa fa-spinner fa-pulse"></i>',
	  'btn-success': '<i class="fa fa-check"></i>',
	  'btn-error': '<i class="fa fa-remove"></i>',
	  'msg-success': 'All Good! Redirecting...',
	  'msg-error': 'Wrong login credentials!',
	  'useAJAX': true,
  };

	// Login Form
	//----------------------------------------------
	// Validation
  //$("#login-form").validate({
  //	rules: {
  //    lg_username: "required",
  //	  lg_password: "required",
  //  },
  //	errorClass: "form-invalid"
  //});

	//Form Submission
  //$("#login-form").submit(function() {
	//  remove_loading($(this));
  //
	//  var account = {
	//	  email:$("input[name=email]").val(),
	//	  password:$("input[name=password]").val()
	//  };
  //
  //
	//  $.ajax({
	//	  url: '/loginProcess',
	//	  type: 'post',
	//	  data: account,
	//	  dataType: 'text',
	//	  success: function (data) {
	//		  console.log(data);
	//		  //if(data == "ok") {
	//			  window.location.replace("/index");
	//		  //}else{
	//			//  alert(data);
	//			//  return false;
	//		  //}
	//	  },
	//	  error: function(data) {
	//		  console.log(data);
	//	  }
	//  });
  //
	//  return false;

  //});
	
	// Register Form
	//----------------------------------------------
	// Validation
  $("#register-form").validate({
  	rules: {
  	  	password: {
  			required: true,
  			minlength: 5
  		},
   		password_confirm: {
  			required: true,
  			minlength: 5,
  			equalTo: "#register-form [name=password]"
  		},
		email: {
			required: true,
			email: true
		}
    },
	errorClass: "form-invalid"
  });

  // Form Submission
  $("#register-form").submit(function(ev) {
	  remove_loading($(this));
	  console.log($("input[name=profileImage]")[0].files[0]);

	  //email 유효성 검사
	  var regex=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
	  if( regex.test($("input[name=email]").val()) === false){
		  alert("email 형식으로 입력해주세요.");
		  return false;
	  }

	  if($("input[name=password]").val() != $("input[name=password_confirm]").val()){
		  alert("패스워드가 일치하지 않습니다.");
		  return false;
	  }



	  var user = {
		  identity:$("input[name=email]").val(),
		  roles:"ROLE_USER",
		  email:$("input[name=email]").val(),
		  password:$("input[name=password]").val(),
		  name:$("input[name=name]").val(),
		  isOAuth:"F",
		  profileUrl:"http://t1.daumcdn.net/osa/tech/i1.daumcdn.jpg"
	  };

	  $.ajax({
		  url: '/users',
		  type: 'post',
		  data: user,
		  dataType: 'json',
		  success: function (responsedData) {
			  //회원 등록이 성공한다면, responsedData JSON 변수 첫번째 값으로 success가 들어가고, 실패하면 에러메시지가 들어감.
			  if(responsedData[0]==="success"){
				  // 프로필 사진을 업로드한다면, 사진 등록 로직을 수행함.
				  if($("input[name=profileImage]")[0].files[0] != null){
					  var formData = new FormData();

					  formData.append("profileImage", $("input[name=profileImage]")[0].files[0]);

					  //회원 등록 이슈가 없어서 등록에 성공한다면, responsedData json 포맷의 2번째 인덱스에 등록된 ID가 반환됨.

					  $.ajax({
						  url: '/users/'+responsedData[1]+'/profile',
						  type: 'post',
						  data: formData,
						  cache: false,
						  processData: false, // Don't process the files
						  contentType: false, // Set content type to false as jQuery will tell the server its a query string request,
						  dataType:'text',
						  success:function(responsedData){
							  if(responsedData==="success"){
								  window.location.replace("/login");
							  }else{
								  alert(responsedData);
							  }
						  }
					  })
				  }else{
				  	window.location.replace("/login");
				  }
			  }else{
				  alert(responsedData);
			  }
		  },
		  error: function(data) {

		  }
	  });


	  return false;

  });


	// Loading
	//----------------------------------------------
  function remove_loading($form)
  {
  	$form.find('[type=submit]').removeClass('error success');
  	$form.find('.login-form-main-message').removeClass('show error success').html('');
  }

  function form_loading($form)
  {
    $form.find('[type=submit]').addClass('clicked').html(options['btn-loading']);
  }
  
  function form_success($form)
  {
	  $form.find('[type=submit]').addClass('success').html(options['btn-success']);
	  $form.find('.login-form-main-message').addClass('show success').html(options['msg-success']);
  }

  function form_failed($form)
  {
  	$form.find('[type=submit]').addClass('error').html(options['btn-error']);
  	$form.find('.login-form-main-message').addClass('show error').html(options['msg-error']);
  }

	// Dummy Submit Form (Remove this)
	//----------------------------------------------
	// This is just a dummy form submission. You should use your AJAX function or remove this function if you are not using AJAX.
  function dummy_submit_form($form)
  {


  	if($form.valid())
  	{
  		form_loading($form);
  		
  		setTimeout(function() {
  			form_success($form);
  		}, 2000);
  	}
  }
	
})(jQuery);

