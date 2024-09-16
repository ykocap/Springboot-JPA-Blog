let index = {
	init: function() {
		$("#btn-save").on("click", () => { //function (){}를 안쓰는 이유는 this를 바인딩 하기 위해서임, function 을쓰면window요소가 this인식함
			this.save();
		});
		$("#btn-update").on("click", () => {
			this.update();
		});
	},

	save: function() {
		console.log("user save called");
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		console.log(data);

		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(res) {
			if (res.status == 500) {
				alert("회원가입이 실패했습니다.");
			} else {
				alert("회원가입이 완료됬습니다.");
				location.href = "/";
			}

		}).fail(function(error) {
			alert(JSON.stringify(error));

		});
	},

	update: function() {
		console.log("user update called");

		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		console.log(data);

		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(res) {
			alert("회원수정이 완료됬습니다.");
			console.log(res);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));

		});
	}

}

index.init();