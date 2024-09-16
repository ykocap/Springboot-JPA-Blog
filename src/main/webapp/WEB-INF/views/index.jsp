<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="layout/header.jsp"%>

<div class="container">
	<c:forEach var="board" items="${boards.content}">
		<div class="card m-2">
			<div class="card-body">
				<h4 class="card-title">${board.title}</h4>
				<a href="/board/${board.id}" class="btn btn-primary">상세보기</a>
			</div>
		</div>
	</c:forEach>
	<!--justify-content-center 가운데 정렬 -->
	<ul class="pagination justify-content-center">
		<c:choose>
			<c:when test="">
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<li class="page-item ${boards.first? 'disabled' :''}"><a class="page-link" href="?page=${boards.number - 1}">Previous</a></li>
		<li class="page-item ${boards.last? 'disabled' :''}"><a class="page-link" href="?page=${boards.number + 1}">Next</a></li>
	</ul>
</div>

<%@ include file="layout/footer.jsp"%>


