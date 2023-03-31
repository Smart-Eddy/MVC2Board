<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글조회</title>
</head>
<body>

<c:if test="${msg != null}">
	<script type="text/javascript">
		alert("${msg}");
	</script>
</c:if>

<center>
<h2>게시글 조회</h2>
<table width="700" border="1" bordercolor="gray">
	<tr height="40" >
		<td colspan="5" align="right">
		<button onclick="location.href='BoardWriteForm.jsp'">글쓰기</button>
		</td>
	</tr>
	<tr height="40">
		<td width="50" align="center">번호</td>
		<td width="320" align="center">제목</td>
		<td width="100" align="center">작성자</td>
		<td width="150" align="center">작성일</td>
		<td width="80" align="center">조회수</td>
	</tr>
	
	<c:set var="number" value="${number}"/>
	<c:forEach var="bean" items="${v}">
	
	<tr height="40">
		<td width="50" align="left">${number}</td>
		<td width="320" align="left">
			<c:if test="${bean.re_step > 1 }">
			<c:forEach var="j" begin="1" end="${(bean.re_step -1 )* 5}">
			&nbsp;
			</c:forEach>
			</c:if>
			<a href="BoardInfo.do?num=${bean.num}">${bean.subject}</a>
			
		</td>
		<td width="100" align="left">${bean.writer}</td>
		<td width="150" align="left">${bean.reg_date}</td>
		<td width="80" align="left">${bean.readcount}</td>
	</tr>
	
	<c:set var="number" value="${number-1}"/>
	</c:forEach>
</table>
<br>

<!-- paging -->
	<c:if test="${count > 0}">
	<c:set var="pageCount" value="${count / pageSize +(count%pageSize == 0? 0: 1)}"/>
	<c:set var="startPage" value="${1}"/>
	
	<c:if test="${currentPage %10 != 0}">
		<!-- 결과를 정수형으로 리턴 받아야 됨으로 fmt 태그사용 -->
		<fmt:parseNumber var="result" value="${currentPage/10}" integerOnly="true"/>
		<c:set var="startPage" value="${result*10+1}"/>
	</c:if> 
	
	<c:if test="${currentPage %10 == 0}">
		<c:set var="startPage" value="${(result-1)*10+1}"/>
	</c:if> 
	
	<!-- 화면에 보여질 페이지 처리 숫자 처리 -->
	<c:set var="pageBlock" value="${10}"/>
	<c:set var="endPage" value="${startPage+pageBlock-1}"/>
	
	<c:if test="${endPage > pageCount}">
		<c:set var="endPage" value="${pageCount}"/>
	</c:if>
	
	<!-- 이전 링크 -->
	<c:if test="${startPage > 10}">
		<a href='BoardList.do?pageNum=${startPage-10}'>[이전] </a>
	</c:if>
	
	<!-- 페이징처리 -->
	<c:forEach var="i" begin="${startPage}" end="${endPage}">
		<a href='BoardList.do?pageNum=${i}'>[${i}] </a>
	</c:forEach>
	
	<!-- 다음 -->
	<c:if test="${endPage < pageCount}">
		<a href='BoardList.do?pageNum=${startPage+10}'>[다음] </a>
	
	
	</c:if>
	</c:if>
	</center>
	

</body>
</html>