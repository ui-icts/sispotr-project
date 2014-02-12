<%@ include file="/WEB-INF/include.jsp"%>
<h1>FAQ</h1>
<div class="box">
<div id="faqAccordion">

 ${siteContentMap['help.faq']}
   
</div>
</div>
    <script>
  $(function() {
    $( "#faqAccordion" ).accordion({
      autoHeight: false,
      navigation: true
    });
  });

  </script>