package edu.uiowa.icts.util;

import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.RemoteIssue;

import edu.uiowa.icts.jira.TicketService;
import edu.uiowa.icts.jira.tags.ticket.Ticket;

public class JiraIssue {

	public static final String BUG = "1";
	public static final String NEW_FUNCTIONALITY = "2";
	public static final String TASK = "3";
	public static final String IMPROVEMENT = "4";
	public static final String END_USER_ISSUE = "6";
	public static final String REQUEST_A_CONSULT = "7";
	public static final String REQUEST_INFORMATION = "8";
	public static final String REPORT_A_PROBLEM = "9";
	public static final String MILESTONE = "10";
	public static final String PROJECT = "11";
	public static final String EPIC = "12";
	public static final String STORY = "13";
	
	public static RemoteIssue createRemoteIssue(String projectKey, 
									String ticketType, 
									String projectAssignee, 
									String summary,
									String environment,
									String description,
									String issueFullName,
									String userName) throws RemoteException{
		Ticket ticket = new Ticket();
    	ticket.setProject(projectKey);
    	ticket.setTicketType(ticketType);
    	ticket.setAssignee(projectAssignee);
    	ticket.setSummary(summary);
    	ticket.setEnvironment(environment);
    	ticket.setDescription(description);
    	ticket.setFullName(issueFullName);
    	ticket.setUserName(userName);
    	TicketService ticketService = new TicketService();
		RemoteIssue remoteIssue = ticketService.createIssue(ticket);
		return remoteIssue;
	}
}