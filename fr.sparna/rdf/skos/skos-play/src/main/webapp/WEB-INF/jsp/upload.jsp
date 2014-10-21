<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" 	prefix="fmt" 	%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 	prefix="c" 		%>

<!-- setup the locale for the messages based on the language in the session -->
<fmt:setLocale value="${sessionScope['fr.sparna.rdf.skosplay.SessionData'].userLocale.language}"/>
<fmt:setBundle basename="fr.sparna.rdf.skosplay.i18n.Bundle"/>

<c:set var="data" value="${requestScope['fr.sparna.rdf.skosplay.UploadFormData']}" />
<c:set var="applicationData" value="${applicationScope.applicationData}" />

<html>
	<head>
		<title>SKOS Play ! - Visualize SKOS Thesaurus</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" />
		<link href="bootstrap-fileupload/bootstrap-fileupload.min.css" rel="stylesheet" />
		<link href="css/skos-play.css" rel="stylesheet" />
		<script src="js/jquery.min.js"></script>
		<script src="bootstrap/js/bootstrap.min.js"></script>
		<script src="bootstrap-fileupload/bootstrap-fileupload.min.js"></script>

		<script type="text/javascript">
	
			function enabledInput(selected) {
				document.getElementById('source-' + selected).checked = true;
				document.getElementById('url').disabled = selected != 'url';
				document.getElementById('example').disabled = selected != 'example';
				document.getElementById('file').disabled = selected != 'file';
				// document.getElementById('endpoint').disabled = selected != 'endpoint';
			}	
			
	    </script>


	</head>
	<body>
		<div class="container">
			<%-- see http://stackoverflow.com/questions/19150683/passing-parameters-to-another-jsp-file-in-jspinclude --%>
			<jsp:include page="header.jsp">
				<jsp:param name="active" value="upload"/>
			</jsp:include>
			
			<div class="messages">
				<c:if test="${data.errorMessage != null}">
					<div class="alert alert-error fade in">
						<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
						<h4><fmt:message key="error" /></h4>
						${data.errorMessage}
					</div>
				</c:if>
			</div>			
			<form id="upload_form" action="upload" method="post" enctype="multipart/form-data" class="form-horizontal">
			<fieldset>
				<legend><fmt:message key="upload.form.legend" /></legend>
				
				<div class="control-group">
					<label class="control-label">
							<input
								type="radio"
								name="source"
								id="source-example"
								value="example"
								onchange="enabledInput('example')"
								checked="checked" />
							<fmt:message key="upload.form.providedExample" />
					</label>
					<div class="controls">
						<select
							class="span4"
							name="example"
							id="example"
							onchange="enabledInput('example')">
							
							<c:forEach items="${applicationData.exampleDatas}" var="entry">
								<option value="${entry.key}">
									<c:set value="upload.form.providedExample.${entry.key}" var="messageKey"/>
									<c:set value="???${pageScope.messageKey}???" var="unknownValue"/>
									<fmt:message key="${pageScope.messageKey}" var="exampleDataName"/>
	
									<c:choose>
										<c:when test="${pageScope.exampleDataName == pageScope.unknownValue}">${entry.key}</c:when>
										<c:otherwise>${pageScope.exampleDataName}</c:otherwise>
									</c:choose>
								</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">
							<input
								type="radio"
								name="source"
								id="source-file"
								value="file"
								onchange="enabledInput('file')" />
							<fmt:message key="upload.form.localFile" />
					</label>
					<div class="controls">
						<div class="fileupload fileupload-new" data-provides="fileupload">
							<div class="input-append">
								<div class="uneditable-input span4">
									<i class="icon-file fileupload-exists"></i> <span class="fileupload-preview"></span>
								</div>
								<span class="btn btn-file">
									<span class="fileupload-new"><fmt:message key="upload.form.localFile.select" /></span>
									<span class="fileupload-exists"><fmt:message key="upload.form.localFile.change" /></span>
									<input type="file" name="file" id="file" onchange="enabledInput('file')" />
								</span>
								<a href="#" class="btn fileupload-exists" data-dismiss="fileupload"><fmt:message key="upload.form.localFile.remove" /></a>
							</div>
						</div>
						<span class="help-block"><i><fmt:message key="upload.form.localFile.help" /></i></span>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">
						<input
							type="radio"
							name="source"
							id="source-url"
							value="url"
							onchange="enabledInput('url')" />
						<fmt:message key="upload.form.remoteUrl" />
					</label>
					<div class="controls">
						<input
							type="text"
							id="url"
							name="url"
							value=""
							placeholder="http://..."
							onkeypress="enabledInput('url');" />
						<span class="help-block"><i><fmt:message key="upload.form.remoteUrl.help" /></i></span>
					</div>
				</div>
				
				<!--
				<div class="control-group">
					<label class="control-label">
						<input
							type="radio"
							name="source"
							id="source-endpoint"
							value="endpoint"
							onchange="enabledInput('endpoint')" />
						<fmt:message key="upload.form.endpoint" />
					</label>
					<div class="controls">
						<input
							type="text"
							id="endpoint"
							name="endpoint"
							value=""
							onkeypress="enabledInput('endpoint');" />
						<span class="help-block"><i><fmt:message key="upload.form.endpoint.help" /></i></span>
					</div>
				</div>
				 -->
			</fieldset>

			
			<div class="accordion" id="myAccordion">
				<div class="accordion-group">
	   				<div class="accordion-heading">
	   					<a class="accordion-toggle" data-toggle="collapse" data-parent="#myAccordion" href="#collapse1"><h4><fmt:message key="upload.form.advanced.legend" /></h4></a>
	   				</div>
	   				<div id="collapse1" class="accordion-body collapse"><div class="accordion-inner">
	
						<div class="control-group">
							<label class="control-label">
								<fmt:message key="upload.form.rdfs" />
							</label>
							<div class="controls">
								<input
									type="checkbox"
									id="rdfs-inference"
									value="rdfs-inference"
									name="rdfs-inference" />
								<span class="help-block"><i><fmt:message key="upload.form.rdfs.help" /></i></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">
								<fmt:message key="upload.form.owl2skos" />
							</label>
							<div class="controls">
								<input
									type="checkbox"
									id="owl2skos"
									value="owl2skos"
									name="owl2skos" />
								<span class="help-block"><i><fmt:message key="upload.form.owl2skos.help" /></i></span>
							</div>
						</div>
						
	   				</div></div>
	   			</div><!-- end accordion-group : Advanced options -->
   			</div>
			
			<div class="form-actions">
				<button type="submit" id="submit-button" class="btn btn-large btn-primary"><fmt:message key="next" /></button>
				<img src="images/ajax-loader.gif" id="loading" hidden="hidden" />
			</div>
			
			</form>
      	</div>
      	<jsp:include page="footer.jsp" />
      	<script>
	      	$(document).ready(function() {
				// disable submit button on click		
			    $('#upload_form').submit(function() {
			    	$('#submit-button').attr('disabled', true);
			        $('#loading').show();
			    });
				
		      	$(window).unload(function() {
		      		$('#loading').hide();
				    $('#submit-button').attr('disabled', false);
		      	});
	      	});
	      	

		</script>
	</body>
</html>