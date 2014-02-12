<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include.jsp"  %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description" content="Nebula" />
<meta name="keywords" content="Nebula" />
<link rel="shortcut icon" href="<c:url value="/resources/images/icts_favicon.ico" />" type="image/x-icon" >


<title>${pageTitle} <c:if test="${not empty pageTitle}">:</c:if>SafeSeed</title>
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
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/datatables/css/demo_page.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/datatables/css/demo_table.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/datatables/css/demo_table_jui.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/datatables/css/TableTools.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/datatables/css/ColVis.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-plugins/datatables/css/customizedDataTables.css" />" />
            
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


 		<!--From safeseed results.jsp-->
        <style type="text/css" title="currentStyle">
            @import "<c:url value="/media/css/demo_page.css" />";
            @import "<c:url value="/media/css/demo_table.css" />";
            @import "<c:url value="/media/css/demo_table_jui.css" />";
            .dataTables_paginate {float: right; text-align: right; padding:2px; font-size: 11px;}
            table.display thead {font-size: 11px;}
            table.display tbody {font-size: 11px;}
            table.display tfoot {font-size: 11px;}
            div.dataTables_scroll { clear: both; }
            <%-- override --%>
            #dt_example #container {
                width: 100%;
                margin: 30px auto;
                padding: 0;
            }
        </style>
      
        <link rel="stylesheet" type="text/css" href="<c:url value="/jquery/smoothness/css/smoothness/jquery-ui-1.8.9.css" />" />
        <!--<link rel="stylesheet" type="text/css" href="<c:url value="/jquery/supplemental.css" />" />-->
        <link rel="stylesheet" type="text/css" href="<c:url value="/jquery/spinner/ui.spinner.css" />" />
        <style type="text/css">
            #example {background-color: white; padding: 10px}
            #example input {width: 100px}

            #example table.grid {
                border-collapse: separate;
                border-spacing: 0px;
            }
            #example table.grid td {background-color: #ccc; padding: 3px 5px; text-align: left}
        </style>
        <script type="text/javascript" src="<c:url value="/jquery/js/jquery-1.4.1.js" />"></script>
        <script type="text/javascript" src="<c:url value="/jquery/js/jquery-1.8.0-ui.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/jquery/spinner/ui.spinner.js" />"></script>
        <script type="text/javascript" language="javascript" src="<c:url value="/media/js/jquery.dataTables.js" />"></script>
        <script type="text/javascript" charset="utf-8">
        	jQuery().ready(function($) {
                $(function() {
                    var oTable = $('#example').dataTable();
                    if ( oTable.length > 0 ) {
                        oTable.fnAdjustColumnSizing();
                    }
                    var oTable = $('#oneOff').dataTable();
                    if ( oTable.length > 0 ) {
                        oTable.fnAdjustColumnSizing();
                    }
                });
            });

            /* Get the rows which are currently selected */
            function fnGetSelected( oTableLocal )
            {
                var aReturn = new Array();
                var aTrs = oTableLocal.fnGetNodes();

                for ( var i=0 ; i<aTrs.length ; i++ )
                {
                    if ( $(aTrs[i]).hasClass('row_selected') )
                    {
                        aReturn.push( aTrs[i] );
                    }
                }
                return aReturn;
            }
            function setDataTable(){
                // the show MUST go first in order for the columns to correctly render
                $('#example').show();
                /**
                 * This is used to allow only single row selection
                 */
                // Add a click handler to the rows - this could be used as a callback
                /*$("#example tbody").click(function(event) {
                    $(oTable.fnSettings().aoData).each(function (){
                        $(this.nTr).removeClass('row_selected');
                    });
                    $(event.target.parentNode).addClass('row_selected');
                });

                // Add a click handler for the delete row
                $('#delete').click( function() {
                    var anSelected = fnGetSelected( oTable );
                    oTable.fnDeleteRow( anSelected[0] );
                } );

                // Init the table var oTable = $('#example').dataTable)()
                oTable =*/

                /**
                 * This is used to allow for multiple row selection
                 */
                // Add a click handler to the rows - this could be used as a callback
                $('#example tr').click( function() {
                    if ( $(this).hasClass('row_selected') )
                        $(this).removeClass('row_selected');
                    else
                        $(this).addClass('row_selected');
                } );

                // Init the table var oTable = $('#example').dataTable)()
                var oTable =
                    // the data table element
                $('#example').dataTable({
                	"bRetrieve": true,
                    "bProcessing": true,                                        // show the processing message
                    //"sAjaxSource": 'media/example_data/json_source.txt',        // get a data tile
                    "sScrollY": "300px",                                            // Y scroll
                    "sScrollX": "100%",                                         // X scroll
                    // this MUST be turned off or it will misalign the columns (only happens with many overflow cols)
                    //"sScrollXInner": "110%",                                    // inner X scroll
                    "bScrollCollapse": true,
                    //"sScrollYInner": "110%",                                    // inner Y scorll
                    //"aaSorting": [[ 0, "asc" ]],                                // sort the first col asc on initialization
                    "bJQueryUI": true,                                          // use the JQueryUI
                    "bPaginate": false,
                    //"sPaginationType": "full_numbers" ,                         // pagination style
                    //"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]      // length menu
                    //"sDom": '<"H"TCfr>t<"F"ip>',                                // saving information plug-in
                    //"sDom": 'C<"bJUI">lfrtip',                                  // show/hide columns plug-in
                    //"oColVis": {"activate": "mouseover"},
                    //"bFilter": true,

                    "fnInitComplete": function(){
                        this.fnAdjustColumnSizing();
                        this.fnDraw();
                    }
                });
                alert('here');
                $('#loadingData').hide();
            };
            
            // create a new type definition to sort "start - end" positions
            jQuery.fn.dataTableExt.aTypes.push(
           		function ( sData )
           		{
           			var sValidChars = "0123456789-";
           			var Char;
           			
           			/* Check the numeric part */
           			for (i=0; i<sData.length; i++ ) 
           			{ 
           				Char = sData.charAt(i); 
           				if (sValidChars.indexOf(Char) == -1) 
           				{
           					return null;
           				}
           			}
           			// don't want to overwrite any number commands
           			if(sData.indexOf("-") > 0){
           				return 'startend';
           			}
           			return null;
           		}
           	);

            // sort based on 'sortend' type: ###-###, only sort of first set of ###
            jQuery.fn.dataTableExt.oSort['startend-asc']  = function(x,y) {
            	var xs = parseInt(x.substring(0, x.indexOf("-",0)));
            	var ys = parseInt(y.substring(0, y.indexOf("-",0)));
            	//alert(xs + ', ' + ys + ' ' + ((xs < ys) ? -1 : ((xs > ys) ?  1 : 0)));
				return ((xs < ys) ? -1 : ((xs > ys) ?  1 : 0));
			};
			jQuery.fn.dataTableExt.oSort['startend-desc'] = function(x,y) {
				var xs = parseInt(x.substring(0, x.indexOf("-",0)));
            	var ys = parseInt(y.substring(0, y.indexOf("-",0)));
				return ((xs < ys) ?  1 : ((xs > ys) ? -1 : 0));
			};
        </script>
        