<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include.jsp"  %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="siSPOTR" />
<meta name="keywords" content="siSPOTR" />
<link rel="icon" href="<c:url value="/resources/images/favicon_1.ico" />" type="image/x-icon" >



<title>${pageTitle} <c:if test="${not empty pageTitle}">:</c:if>siSPOTR</title>




    <link href="<c:url value="/"/>resources/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
    </style>
    <link href="<c:url value="/"/>resources/css/bootstrap-responsive.css" rel="stylesheet">
    
<!--jquery main ( required for many below) -->
<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.7.2.min.js" />"></script>

		
<!--		jquery-ui custom theme-->
		<link type="text/css" href="<c:url value="/resources/jquery-ui/css/smoothness/jquery-ui-1.8.5.custom.css" />" rel="stylesheet" />	
<!--		<script type="text/javascript" src="<c:url value="/resources/jquery-ui/js/jquery-1.4.2.min.js" />"></script>-->
		<script type="text/javascript" src="<c:url value="/resources/jquery-ui/js/jquery-ui-1.8.5.custom.min.js" />"></script>
		
		
	
<!--		required for datatables-->
		<style type="text/css" title="currentStyle">
            @import "<c:url value="/resources/jquery-plugins/datatables/css/demo_page.css" />";
            @import "<c:url value="/resources/jquery-plugins/datatables/css/demo_table.css" />";
            @import "<c:url value="/resources/jquery-plugins/datatables/css/demo_table_jui.css" />";

            @import "<c:url value="/resources/jquery-plugins/datatables/css/TableTools.css" />";
            @import "<c:url value="/resources/jquery-plugins/datatables/css/ColVis.css" />";
            @import "<c:url value="/resources/jquery-plugins/datatables/css/customizedDataTables.css" />";       /* customize the DataTables look */
        </style>
        
        
<!--        required for datetime picker-->
		<link rel="stylesheet" media="all" type="text/css" href="<c:url value='/resources/jquery-plugins/datetimepicker/jquery-ui-timepicker-addon.min.css' />" />
		<script type="text/javascript" src="<c:url value='/resources/jquery-plugins/datetimepicker/jquery-ui-timepicker-addon.min.js' />"></script>
        
<!--        <script type="text/javascript" language="javascript" src="<c:url value="/resources/jquery-plugins/datatables/js/jquery.js" />"></script>-->
        
        <script type="text/javascript" language="javascript" src="<c:url value="/resources/jquery-plugins/datatables/js/jquery.dataTables.js" />"></script>
        <script type="text/javascript" charset="utf-8" src="<c:url value="/resources/jquery-plugins/datatables/js/TableTools.js" />"></script>
        <script type="text/javascript" charset="utf-8" src="<c:url value="/resources/jquery-plugins/datatables/js/ColVis.js" />"></script>
        <!-- customize how DataTables functions -->
        <script type="text/javascript" charset="utf-8" src="<c:url value="/resources/jquery-plugins/datatables/js/customizedDataTables.js" />"></script>

		


<!--css and js for sprinner plugin -->
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/spinner/ui.spinner.css" />" />
<script type="text/javascript" src="<c:url value="/resources/jquery-plugins/spinner/ui.spinner.js" />" ></script>
 
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/layout.css" />" />
 
<script type="text/javascript">

 // Tabs
 $(function(){
	 $( "#toptabs" ).tabs();
 $('#toptabs').tabs('select', '${param.tab}');
 //hover states on the static widgets
 $('#dialog_link, ul#icons li').hover(
 	function() { $(this).addClass('ui-state-hover'); }, 
 	function() { $(this).removeClass('ui-state-hover'); }
 );

 });
 

 $(function() {
 	$("a", ".button").button();
		});

	function toggleMenu() {
		if (menuEnabled) {

			$("#menuContainer").hide();

			$("#bodyContainer").attr('class', 'span11');
			
			
// 			alert("table width:"+$(".display").width());
// 			if($(".display").width()>500)
// 				{
// 					$(".dataTables_wrapper").width(($(".display").width()*1+40));
// 				}
		//	$(".dataTables_wrapper").width("1200px");
			$("#menuButton").html('<i title="hide menu" class="icon-chevron-right"></i> Show Menu');
			menuEnabled = false;
		} else {
			
			$("#menuContainer").show('slow', function() {
				// Animation complete.
			});
			$("#bodyContainer").attr('class', 'span10');
			//$("#tabsContainer").width('1000px');
			$("#menuButton").html('<i title="show menu" class="icon-chevron-left"></i> Hide Menu');
			menuEnabled = true;
		}
	}
	
</script>
