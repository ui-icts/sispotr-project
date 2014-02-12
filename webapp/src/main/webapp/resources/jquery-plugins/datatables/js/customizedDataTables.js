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
function setDataTable(selector,type)

{
	


	if(type == "sortable")
		{
	
		setDataTableSortable(selector);
	
		}
	else
		{
		setDataTableBasic(selector);
		}
		
}
		
function setDataTableBasic(selector)
	{
		
		  // the show MUST go first in order for the columns to correctly render
	    $(selector).show();
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
	    $(selector + ' tr').click( function() {
	        if ( $(this).hasClass('row_selected') )
	            $(this).removeClass('row_selected');
	        else
	            $(this).addClass('row_selected');
	    } );

	    // Init the table var oTable = $('#example').dataTable)()
	    var oTable =

	        // the data table element
	        $(selector).dataTable( {
	            "bProcessing": true,                                        // show the processing message
	            "bPaginate": false,
	            "bSort": false,
	            "bFilter": false,
	                                  // inner X scroll
	            //"aaSorting": [[ 4, "asc" ]],                                // sor the first col asc on initialization
	            "bJQueryUI": true                                      // use the JQueryUI
	         //   "sPaginationType": "full_numbers"                          // pagination style
	            //"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]     // length menu
	        } );
//	    $('#loadingData').hide();
	
	}
		
function setDataTableSortable(selector)
	{
    // the show MUST go first in order for the columns to correctly render
    $(selector).show();
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
    $(selector +' tr').click( function() {
        if ( $(this).hasClass('row_selected') )
            $(this).removeClass('row_selected');
        else
            $(this).addClass('row_selected');
    } );

    // Init the table var oTable = $('#example').dataTable)()
    var oTable =

    // the data table element
    $(selector).dataTable( {
        "bProcessing": true,                                        // show the processing message
        "bDestroy":true,
                                  // inner X scroll
        "aaSorting": [[ 0, "asc" ]],                                // sor the first col asc on initialization
        "bJQueryUI": true,                                          // use the JQueryUI
        "sPaginationType": "full_numbers"                          // pagination style
        //"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]     // length menu
    } );

//    $('#loadingData').hide();
}

