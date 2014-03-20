<%@ page session = "true" %>
<%@ page import = "todo.*,
    com.google.gson.*,
    java.text.*, java.sql.Date"
%>
<%
Gson g = new Gson();
response.setContentType("text/json");
String action = (request.getParameter("action")!= null)?request.getParameter("action"):"";

String query = (request.getParameter("q")!= null)?request.getParameter("q"):"";
String sort = (request.getParameter("sort")!= null)?request.getParameter("sort"):"duedate";
boolean desc = (request.getParameter("desc  ")!= null)?Boolean.parseBoolean(request.getParameter("desc")):false;
int limit = Integer.parseInt((request.getParameter("limit")!= null)?request.getParameter("limit"):"10");
int offset = Integer.parseInt((request.getParameter("offset")!= null)?request.getParameter("offset"):"0");

if (action.equals("delete")) {
	int id = Integer.parseInt((request.getParameter("id")!= null)?request.getParameter("id"):"0");
	todo.delete(id);
} else if (action.equals("save")) { 
	todo td = new todo();
	if (request.getParameter("todo") != null) {		
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = (JsonObject)jsonParser.parse(request.getParameter("todo"));
		td.setText(jo.get("text").getAsString());
		td.setPriority(jo.get("Priority").getAsInt());
		String dateStr = jo.get("DueDate").getAsString();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new java.sql.Date(formatter.parse(dateStr).getTime());
		td.setDueDate(date);
		td.save();
	}
} else if (action.equals("getTodo")) { 
	todo td = new todo();
    String idParam = request.getParameter("id");
	int id = Integer.parseInt((idParam!= null && !idParam.equals("undefined") )?request.getParameter("id"):"0");
	td.getTodo(id);
	out.println(g.toJson(td));
} else {
	out.println(g.toJson(todo.getTodos(query, sort, desc, limit, offset)));
}
%>

