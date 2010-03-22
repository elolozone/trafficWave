<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    %>
    <%@ page import="com.elolozone.trafficstore.UserStat" %>
    <%@ page import="com.elolozone.trafficstore.ToStore" %>
    <%@ page import="com.elolozone.constants.IConstants" %>
     <%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Graphique session utilisateur</title>
<%

String idSessionStr = request.getParameter("idSession"); 
String idUser = request.getParameter("idUser");  


int idSession =0;

if ( idUser == null) 
{
	
	out.println("idUser obligatoire");
	return;
} 


if (idSessionStr != null) 
{
	
	Integer idSessionInteger = Integer.decode(idSessionStr); 
	idSession  = idSessionInteger.intValue();
} 
else idSession=-1;



List<UserStat> userStats ;


 userStats = ToStore.listUserStatAsc(idSession, idUser); 




String x="";
String y1=""; 
String y2="";
String y3="";
String y4="";
String y5="";
String y6="" ;

Integer i=0;
Double lastx = new Double(0);
double maxYa = 0;
double maxYb = 0;
Double v = new Double(0);
for (Iterator iter = userStats.iterator(); iter.hasNext() && i<IConstants.NBRE_POINT_TOGRAPH ;) 
{ i++;
	UserStat userStat = (UserStat) iter.next();

	 	 lastx = ToStore.roundDown(Double.valueOf(
				userStat.getLastLocationDate().getTime()-userStats.get(0).getLastLocationDate().getTime()
		)/1000,0);
	 
			x=x+String.valueOf(lastx);
						
			v= userStat.getSpeed()*3.6d;
			y1=y1+String.valueOf(v.intValue());
			if (maxYa<v) maxYa = v;
			
			v=userStat.getAvgSpeed() *3.6d;
			y2=y2+String.valueOf(v.intValue());
			if (maxYa<v) maxYa = v;
			
			
			
			v=userStat.getMaxSpeed() *3.6d;
			y3=y3+String.valueOf(v.intValue());
			if (maxYa<v) maxYa = v;
			
			v=userStat.getRatio()*100d;
			y4=y4+String.valueOf(v.intValue());
			if (maxYb<v) maxYb = v;
			
			v=userStat.getRatioPond()*100d;
			y5=y5+String.valueOf(v.intValue());
			if (maxYb<v) maxYb = v;
			//y6=y6+String.valueOf(userStat.getRatio() ) ;
			//y7=y7+String.valueOf(userStat.getRatioPond());
			
			
			if (userStat.getInTraffic ()) y6=y6+"25"; else y6=y6+"0";
			
			
			if (iter.hasNext() && i<IConstants.NBRE_POINT_TOGRAPH ) 
			{
			x=x+ ",";
			y1=y1+ ",";
			y2=y2+ ",";
			y3=y3+ ",";
			y4=y4+ ","; 
			y5=y5+ ",";
			y6=y6+ ",";
			}			
}

out.println("<img src=\"http://chart.apis.google.com/chart?chds=0,"+String.valueOf(maxYa)+"&chxr=0,0,"+lastx.toString()+"|1,0,150&chxt=x,y&chdl=userSpeed|avrSpeedStreet&chco=FF0000,00FF00&chs=600x500&cht=lxy&chd=t:");
out.println("-1|"+y1+"|-1|"+y2);
out.println ("\" alt=\"Vitesses\" />");

out.println("<img src=\"http://chart.apis.google.com/chart?chds=0,"+String.valueOf(maxYa)+"&chxr=0,0,"+lastx.toString()+"|1,0,150&chxt=x,y&chdl=avrSpeedStreet|maxSpeedStreet&chco=00FF00,0000FF&chs=600x500&cht=lxy&chd=t:");
out.println("-1|"+y2+"|-1|"+y3);
out.println ("\" alt=\"Vitesses\" />");


out.println("<img src=\"http://chart.apis.google.com/chart?chds=0,"+String.valueOf(maxYb)+"&chxr=0,0,"+lastx.toString()+"|1,0,100&chxt=x,y&chdl=Ratio|RatioPond&chco=00FF00,FF0000,0000FF&chs=600x500&cht=lxy&chd=t:");
out.println("-1|"+y4+"|-1|"+y5);
out.println ("\" alt=\"Ratios\" />");

out.println("<img src=\"http://chart.apis.google.com/chart?chds=0,"+String.valueOf(maxYb)+"&chxr=0,0,"+lastx.toString()+"|1,0,100&chxt=x,y&chdl=Traffic|RatioPond|userSpeed|avgStreet&chco=0000FF,00FF00,FF0000,0FF0FF&chs=600x500&cht=lxy&chd=t:");
out.println("-1|"+y6+"|-1|"+y5+"|-1|"+y1+"|-1|"+y2);
out.println ("\" alt=\"Traffic\" />");


%>
</head>
<body>

</body>
</html>