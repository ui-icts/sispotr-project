CustomizedTilesView.java

Usage:
- define a default template with the name "mainTemplate" as well as any other templates you want

A view can be referenced in three ways:

1. Use a defined template name. eg "myTemplate"
2. Use the default template and specify only the path to the jsp file eg "path/to/myfile"
3. Define both the template name and the path to the file using a "|" as a delimiter eg "myTemplate|path/to/myfile"

NOTE: that the view will first check if a tiles view is defined

Generated JSPs support: 
If the specified file does not exist in "/WEB-INF/jsp", 
the "/WEB-INF/jsp-generated" will be checked for a file with the same relative path




Paste the following view configuration in the application-context.xml 
----------------------------------------------------------------------------
<bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter" />

   	<bean id="tilesConfigurer"
     class="org.springframework.web.servlet.view.tiles2.TilesConfigurer">
        <property name="definitions">
            <list>
                <value>/WEB-INF/tiles-defs/templates.xml</value>
            </list>
        </property>
    </bean>

    <bean id="tilesViewResolver"  class="org.springframework.web.servlet.view.UrlBasedViewResolver">
        <property name="viewClass" value="edu.uiowa.icts.spring.CustomizedTilesView" />
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>       
    </bean>

    <bean id="urlMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="mappings">
            <props>
                <prop key="/**/*.html">viewController</prop>
            </props>
        </property>
    </bean>
----------------------------------------------------------------------------