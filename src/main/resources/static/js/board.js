let index = {
	init: function() {
		$("#btn-save").on("click", () => {
			this.save();
		});
		$("#btn-update").on("click", () => {
			this.update();
		});
		$("#btn-delete").on("click", () => {
			this.deleteById();
		});
		$("#btn-reply-save").on("click", () => {
			this.replySave();
		});
	},

	save: function() {
		console.log("user save called");
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};

		console.log(data);

		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(res) {
			alert("글쓰기가 완료됬습니다.");
			console.log(res);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));

		});
	},

	update: function() {
		console.log("user update called");

		let id =  $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};

		console.log(data);

		$.ajax({
			type: "PUT",
			url: "/api/board/" + id,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(res) {
			alert("글쓰기 수정이 완료됬습니다.");
			console.log(res);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));

		});
	},

	deleteById: function() {
		console.log("replySave delete called");
		let id =  $("#id").text();

		$.ajax({
			type: "DELETE",
			url: "/api/board/" + id,
		}).done(function(res) {
			alert("글쓰기 삭제 완료됬습니다.");
			console.log(res);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));

		});
	},

	replySave: function() {
		console.log("board replySave called");
		let data = {
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val()
		};

		let boardId = $("#boardId").val();
		
		console.log(data);

		$.ajax({
			type: "POST",
			url: `/api/board/${boardId}/reply`, // scipt변수를 사용할 경우 백틱``사용(''아님)
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(res) {
			alert("댓글쓰기가 완료됬습니다.");
			console.log(res);
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));

		});
	},
	

	replyDelete: function(boardId, replyId) {
		console.log("board replyDelete called");
		
		$.ajax({
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json"
		}).done(function(res) {
			alert("댓글삭제가 완료됬습니다.");
			console.log(res);
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));

		});
	},
}

index.init();