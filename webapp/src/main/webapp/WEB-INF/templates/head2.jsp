<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include.jsp"  %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="Nebula" />
<meta name="keywords" content="Nebula" />
<link rel="shortcut icon" href="<c:url value="/resources/images/icts_favicon.ico" />" type="image/x-icon" >



<title>${pageTitle} <c:if test="${not empty pageTitle}">:</c:if>siSPOTR</title>

	<!--required for fluid960-->
  	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/fluid960gs/css/reset.css" />" media="screen" />
   	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/fluid960gs/css/text.css" />" media="screen" />
   	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/fluid960gs/css/grid.css" />" media="screen" />
   	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/fluid960gs/css/layout.css" />" media="screen" />
   	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/fluid960gs/css/nav.css" />" media="screen" />
	<!--<script type="text/javascript" src="<c:url value="/resources/themes/fluid960gs/js/mootools-1.2.1-core.js" />"></script>-->
	<!--<script type="text/javascript" src="<c:url value="/resources/themes/fluid960gs/js/mootools-1.2-more.js" />"></script>-->
	<!--<script type="text/javascript" src="<c:url value="/resources/themes/fluid960gs/js/mootools-fluid16-autoselect.js" />"></script>--> 
  	<!--[if IE 6]><link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/fluid960gs/css/ie6.css" />" media="screen" /><![endif]-->
	<!--[if IE 7]><link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/fluid960gs/css/ie.css" />" media="screen" /><![endif]-->

	<!--required for rich text editor-->
	<script type="text/javascript" src="<c:url value="/resources/tiny_mce/tiny_mce.js" />" ></script>

	<!--jquery main ( required for many below) -->
	<script src="<c:url value="/resources/jquery/jquery.js" />"></script>
	
	<!--jquery-ui custom theme-->
	<link type="text/css" href="<c:url value="/resources/jquery-ui/css/smoothness/jquery-ui-1.8.5.custom.css" />" rel="stylesheet" />	
	<!--<script type="text/javascript" src="<c:url value="/resources/jquery-ui/js/jquery-1.4.2.min.js" />"></script>-->
	<script type="text/javascript" src="<c:url value="/resources/jquery-ui/js/jquery-ui-1.8.5.custom.min.js" />"></script>
		
	<!--required for datatables-->
	<style type="text/css" title="currentStyle">
    	@import "<c:url value="/resources/jquery-plugins/datatables/css/demo_page.css" />";
        @import "<c:url value="/resources/jquery-plugins/datatables/css/demo_table.css" />";
        @import "<c:url value="/resources/jquery-plugins/datatables/css/demo_table_jui.css" />";
        @import "<c:url value="/resources/jquery-plugins/datatables/css/TableTools.css" />";
        @import "<c:url value="/resources/jquery-plugins/datatables/css/ColVis.css" />";
        @import "<c:url value="/resources/jquery-plugins/datatables/css/customizedDataTables.css" />";       /* customize the DataTables look */
  	</style>
            
	<!--required for datetime picker-->
	<link rel="stylesheet" media="all" type="text/css" href="<c:url value='/resources/jquery-plugins/datetimepicker/jquery-ui-timepicker-addon.min.css' />" />
	<script type="text/javascript" src="<c:url value='/resources/jquery-plugins/datetimepicker/jquery-ui-timepicker-addon.min.js' />"></script>
	<!--<script type="text/javascript" language="javascript" src="<c:url value="/resources/jquery-plugins/datatables/js/jquery.js" />"></script>-->    
    <script type="text/javascript" language="javascript" src="<c:url value="/resources/jquery-plugins/datatables/js/jquery.dataTables.js" />"></script>
    <script type="text/javascript" charset="utf-8" src="<c:url value="/resources/jquery-plugins/datatables/js/TableTools.js" />"></script>
    <script type="text/javascript" charset="utf-8" src="<c:url value="/resources/jquery-plugins/datatables/js/ColVis.js" />"></script>
    
    <!-- customize how DataTables functions -->
    <script type="text/javascript" charset="utf-8" src="<c:url value="/resources/jquery-plugins/datatables/js/customizedDataTables.js" />"></script>

	<!--required for breadcrumbs-->
	<script type="text/javascript" src="<c:url value="/resources/jquery-plugins/breadcrumb/jquery-easing.1.3.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jquery-plugins/breadcrumb/jquery-breadcrumb.js" />"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/breadcrumb/BreadCrumb.css" />" />
		
	<!--custom css and js-->
	<script src="<c:url value="/resources/js/utils.js" />"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/layout.css" />" />
	<script src="<c:url value="/resources/js/nebula_jquery_ui_defaults.js" />"></script>
	
	<!-- include the Tooltip Tools --> 
	<script type="text/javascript" src="<c:url value="/resources/jquery-tooltips/jquery.tools.min.js" />"></script>
	<link rel="stylesheet" type="text/css" href="src="<c:url value="/resources/jquery-tooltips/standalone.css"/>" >	
	
 
 	<script type="text/javascript" >
		//Main menu
	 	function initMenu() {
	 	  $('#mainmenu ul').hide();
	 	  $('#mainmenu ul:first').show();
	 	  $('#mainmenu li a').click(
	 	  function() {
	 		  $(this).next().slideToggle('normal');});
	 	  }
	 	  $(document).ready(function() {initMenu();}
	 	);
	
		// Tabs
	 	$(function(){
		 	$("#toptabs").tabs();
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
 	</script>
 
 	 <!--From safeseed safeseed.jsp-->
<!--     <link rel="stylesheet" type="text/css" href="jquery/smoothness/css/smoothness/jquery-ui-1.8.9.css" />-->
     <!--<link rel="stylesheet" type="text/css" href="jquery/supplemental.css" />
     <link rel="stylesheet" type="text/css" href="jquery/fieldset.css" />-->
     <link rel="stylesheet" type="text/css" href="jquery/spinner/ui.spinner.css" />

<!--     <script type="text/javascript" src="jquery/js/jquery-1.4.1.js"></script>-->
<!--     <script type="text/javascript" src="jquery/js/jquery-1.8.0-ui.min.js"></script>-->
     <script type="text/javascript" src="/resources/jquery-plugins/spinner/ui.spinner.js"></script>
<!--     <script type="text/javascript" language="javascript" src="media/js/jquery.dataTables.js"></script>-->
     
