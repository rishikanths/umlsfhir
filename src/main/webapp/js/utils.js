
function setFocus() {
	$("#termSearch").focus();
}

function setMessage(ele, message) {
	$("#" + ele).append("<p style='padding:10px;background-color: #f6f6f6;'><b>"+ message + "</b></p>");
}

function displayError(error) {
	$("#error").empty();
	$("#error").append("<p>" + error + "</p>");
	$("#error").css('display', 'block');
}

function displayMessage(msg) {
	$("#message").empty();
	$("#message").append("<p>" + msg + "</p>");
	$("#message").css('display', 'block');
}

function formatName(name){
	return name.replace(/_/g," ");
}
function reFormatName(name){
	return name.replace(/ /g,"_");
}

