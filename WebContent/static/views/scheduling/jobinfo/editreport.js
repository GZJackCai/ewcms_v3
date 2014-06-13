var JobReportEdit = function(){};

JobReportEdit.prototype.init = function(){
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
	
	var monthsAllChecked = true;
	var weekDaysAllChecked = true;
	$("input[name='months']").each(function() {
		if (!$(this).attr('checked')) {
			monthsAllChecked = false;
		}
	});
	if (monthsAllChecked) {
		$('#monthsAll').attr('checked', true);
	} else {
		$('#monthsAll').attr('checked', false);
	}
	$("input[name='weekDays']").each(function() {
		if (!$(this).attr('checked')) {
			weekDaysAllChecked = false;
		}
	});
	if (weekDaysAllChecked) {
		$('#weekDaysAll').attr('checked', true);
	} else {
		$('#weekDaysAll').attr('checked', false);
	}
	$('#monthsAll').click(function() {
		if ($('#monthsAll').attr('checked') == 'checked') {
			$("input[name='months']").attr('checked', true);
		} else {
			$("input[name='months']").attr('checked', false);
		}
	});
	$('#weekDaysAll').click(function() {
		$("input[name='days']").get(1).checked = true;
		if ($('#weekDaysAll').attr('checked') == 'checked') {
			$("input[name='weekDays']").attr('checked', true);
		} else {
			$("input[name='weekDays']").attr('checked', false);
		}
	});
	$("input[name='weekDays']").click(function() {
		$("input[name='days']").get(1).checked = true;
		var weekChecked = true;
		$("input[name='weekDays']").each(function() {
			if (!$(this).attr('checked')) {
				weekChecked = false;
			}
		});
		if (weekChecked) {
			$('#weekDaysAll').attr('checked', true);
		} else {
			$('#weekDaysAll').attr('checked', false);
		}
	});
	$("input[name='months']").click(function() {
		var monthsChecked = true;
		$("input[name='months']").each(function() {
			if (!$(this).attr('checked')) {
				monthsChecked = false;
			}
		});
		if (monthsChecked) {
			$('#monthsAll').attr('checked', true);
		} else {
			$('#monthsAll').attr('checked', false);
		}
	});
	$('#monthDays').click(function() {
		$("input[name='days']").get(2).checked = true;
	});
	$('#occurrenceCount').click(function() {
		$("input[name='occur']").get(2).checked = true;
	});
	$("input[name='days']").click(function() {
		if ($("input[name='days']:checked").val() == 3) {
			$('#monthDays').focus();
		}
	});
	$("input[name='occur']").click(function() {
		var occurId = $("input[name='occur']:checked").val();
		if (occurId == 3) {
			$('#occurrenceCount').focus();
		} else if (occurId == 2) {
			$("input[name='endDateSimple']").focus();
		}
	});
	$("input[name='mode']").click(function() {
		var modeId = $("input[name='mode']:checked").val();
		if (typeof modeId == 'undefined') {
			modeId = 1;
			$("input[name='mode']").get(modeId).checked = true;
			$("input[name='occur']").get(0).checked = true;
		}
		if (modeId == 0) {
			$('#trSimplicity').hide();
			$('#trComplexity').hide();
		} else if (modeId == 1) {
			$('#trSimplicity').show();
			$('#trComplexity').hide();
		} else {
			$('#trSimplicity').hide();
			$('#trComplexity').show();
		}
	});
	var modeId = $("input[name='mode']:checked").val();
	if (typeof modeId == 'undefined' || $('#jobId').val() == "") {
		modeId = 1;
		$("input[name='mode']").get(modeId).checked = true;
		$("input[name='occur']").get(0).checked = true;
	}
	if (modeId == 0) {
		$('#trSimplicity').hide();
		$('#trComplexity').hide();
	} else if (modeId == 1) {
		$('#trSimplicity').show();
		$('#trComplexity').hide();
	} else {
		$('#trSimplicity').hide();
		$('#trComplexity').show();
	}
	var occurId = $("input[name='occur']:checked").val();
	if (typeof occurId == 'undefined' || $('#jobId').val() == "") {
		$("input[name='occur']").get(0).checked = true;
	}
	var daysId = $("input[name='days']:checked").val();
	if (typeof daysId == 'undefined' || $('#jobId').val() == "") {
		$("input[name='days']").get(0).checked = true;
	}
};

JobReportEdit.prototype.checkBoxValue = function(name){
	var isNumber = function(str){
		var patrn=/^\d*$/;    
		if(patrn.test(str)) {
			return true;
		}else{
			return false;  
		}   
	};
	
	var strValue = '';
	var list = document.getElementsByName(name);
	for (var i = 0; i < list.length; i++){
		if (list[i].type == 'checkbox'){
			if (list[i].checked == true) {
				listValue = list[i].value;
				if(strValue != '')strValue += ',';
				if (isNumber(listValue)){
					strValue += listValue;
				}
				else{
					strValue += "'" + listValue + "'";
				}
			}
		}
	}
	document.all[name].value = strValue;
};