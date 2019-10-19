
var jsonObj = null;

function reset(){
	$("#jsonSection").LoadingOverlay("hide");
	$("#message").css('display', 'none');
	$("#error").css('display', 'none');
	$("#copyContent").removeClass();
    $("#copyMessage").text("");
    $("#copyButton").css("display","none");
    $('#myTree').empty();
}
$(document).ready(function(){
	$('#typeOptions .btn').on('click', function(event) {
		if(document.getElementsByClassName("list-group-item-action active")[0]!=null){
			getConceptMap(document.getElementsByClassName("list-group-item-action active")[0].id,$(this).find('input').val())
		}
	});
})

$("#termSearch").autocomplete(
{
	minLength : 4,
	delay : 200,
	source : function(req, resp) {
		$.ajax({
			url : '/umls-fhir/search?term=' + $('#termSearch').val(),
			success : function(data, status, response) {
				var object = jQuery.parseJSON(data);
				if (object.length == 0) {
					var msg = "No matches found for term - <b>"+ $('#termSearch').val() + "</b>";
					displayMessage(msg);
					setFocus();
				} else {
					var div = $("#searchResults")
					div.empty();
					$.each(object,function(key,value){
						div.append("<a href='#' id='"+value[0]+"'class='list-group-item list-group-item-action'>"+value[1]+ "</a> ");
					});
					$('.list-group-item').click(function(e) {
				        e.preventDefault()
				        $(this).addClass('active').siblings().removeClass('active');
				        $("#conceptInfo .card-body").empty()
				        $("#conceptInfo .card-body").append("<span>ConceptMap for <b>"+$(this).text() +"("+$(this).attr('id')+")</b></span>") ;
				        getConceptMap($(this).attr('id'),$("input[name='options']:checked").val());
				    });
				}
				
			},
			error : function(data, status, response) {
				var error = "Response - " + JSON.stringify(data.responseText)
					+ "\nStatus - " + JSON.stringify(status);
				displayError(error);
				setFocus();
			},
			beforeSend : function() {
				reset()
			}
		});
	}
});

function getConceptMap(cui,type){

	$.ajax({
		url : '/umls-fhir/mapcui?cui='+cui+'&type='+type,
		success : function(data, status, response) {
			$("#jsonSection").LoadingOverlay("hide");
			$("#copyButton").css("display","inline");
			jsonObj = data;
			$('#myTree').jsonViewer(data,{collapsed: true, rootCollapsable:false, withQuotes: true, withLinks: false});
		},
		beforeSend : function() {
			reset();
			$("#jsonSection").LoadingOverlay("show");
		},
		error : function(data, status, response) {
			var error = "Response - " + JSON.stringify(response)+ "\nStatus - " + JSON.stringify(status);
			displayError(error);
			setFocus();
		}
	})
}
document.getElementById("copyButton").addEventListener("click", function() {
	var clipboard = new ClipboardJS('#copyContent .btn', {
	    text: function() {
	        return JSON.stringify(jsonObj)
	    }
	});
    
	clipboard.on('success', function(e) {
	    e.clearSelection();
	    $("#copyContent").addClass ("alert alert-success");
	    $("#copyMessage").text(" Copied to clipboard!");
	});
	
	clipboard.on('error', function(e) {
		$("#copyContent").addClass ("alert alert-danger");
		$("#copyMessage").text(" Didnt copy to clipboard. Try again !");
	});
})