package idoc;
import com.sap.conn.idoc.*;

// import java.io.*;
//import java.text.SimpleDateFormat;

import com.sap.conn.jco.server.*;
import com.sap.conn.idoc.jco.*;


public class IDocServerExample
{
    public static void main(String[] a)
    {   
        try
        {
        	
        	JCoIDocServer server = JCoIDoc.getServer("MYSERVER");      // 서버정보 가져옴 
        	server.setIDocHandlerFactory(new MyIDocHandlerFactory());  // 들어오는 Request를 처리하는 Handler
        	server.setTIDHandler(new MyTidHandler());                  // transaction ID Handler
        	
        	MyThrowableListener listener = new MyThrowableListener();   // Error
        	server.addServerErrorListener(listener);
            server.addServerExceptionListener(listener);
            server.setConnectionCount(1);
            server.start();  //서버 시작 
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
     }
    
    static class MyIDocHandler implements JCoIDocHandler
    {   
    	//Request 처리 
        public void handleRequest(JCoServerContext serverCtx, IDocDocumentList idocList)
        {
        	
                IDocDocument doc = idocList.first(); 

                System.out.println("IDocType: " + doc.getIDocType());         // SAP WE30 세팅 
                System.out.println("MessageType: " + doc.getMessageType());   // SAP WE81,WE82,WE20 세팅 
                System.out.println("NumSegments: " + doc.getNumSegments());   // Segments수량 

                IDocSegment segmentRoot = doc.getRootSegment();


                IDocSegment slist[];
                slist = segmentRoot.getChildren("ZYY_BOMHEAD");  // SAP WE31 세팅 
                IDocSegment parentSeg = slist[0];
                
//                System.out.println(slist.length);
                System.out.println("--------Header-----------");
                System.out.println("Segment Type: " + parentSeg.getType()); // SAP WE31 세팅 


                
                try {
					System.out.println("STLTY: " + parentSeg.getString("STLTY"));
					System.out.println("STLNR: " + parentSeg.getString("STLNR"));
					System.out.println("STLAL: " + parentSeg.getString("STLAL"));
					System.out.println("STLAL: " + parentSeg.getString("STKOZ"));
					System.out.println("STLAL: " + parentSeg.getString("LKENZ"));
//					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//					String strDate1 = sdf1.format(parentSeg.getDate("BEDAT"));
//					System.out.println("strDate1:" + strDate1);
				} catch (IDocFieldNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                System.out.println("--------Item-----------");
                IDocSegment slist_item[];
                slist_item = parentSeg.getChildren("ZYY_BOMITEM"); // SAP WE31 세팅 
                System.out.println(slist_item.length);
                for(int i=0;i<slist_item.length;i++) {
                	System.out.println("Segment Type: " + slist_item[i].getType()); // SAP WE31 세팅 
                    try {
    					System.out.println("STLTY: " + slist_item[i].getString("STLTY"));
    					System.out.println("STLNR: " + slist_item[i].getString("STLNR"));
    					System.out.println("STLKN: " + slist_item[i].getString("STLKN"));
    					System.out.println("STPOZ: " + slist_item[i].getString("STPOZ"));
    				} catch (IDocFieldNotFoundException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} 
                }
        }
    }
    
    static class MyIDocHandlerFactory implements JCoIDocHandlerFactory
    {
    	private JCoIDocHandler handler = new MyIDocHandler();
    	public JCoIDocHandler getIDocHandler(JCoIDocServerContext serverCtx)
    	{
    		return handler;
    	}
    }
    
    static class MyThrowableListener implements JCoServerErrorListener, JCoServerExceptionListener
    {
        
        public void serverErrorOccurred(JCoServer server, String connectionId, JCoServerContextInfo ctx, Error error)
        {
            System.out.println(">>> Error occured on " + server.getProgramID() + " connection " + connectionId);
            error.printStackTrace();
        }
        public void serverExceptionOccurred(JCoServer server, String connectionId, JCoServerContextInfo ctx, Exception error)
        {
            System.out.println(">>> Error occured on " + server.getProgramID() + " connection " + connectionId);
            error.printStackTrace();
        }
    }
    
    static class MyTidHandler implements JCoServerTIDHandler
    {
    	public boolean checkTID(JCoServerContext serverCtx, String tid)
    	{
    		System.out.println("checkTID called for TID="+tid);
    		return true;
    	}
    
    	public void confirmTID(JCoServerContext serverCtx, String tid)
    	{
    		System.out.println("confirmTID called for TID="+tid);
    	}
   
    	public void commit(JCoServerContext serverCtx, String tid)
    	{
    		System.out.println("commit called for TID="+tid);
    	}
    
    	public void rollback(JCoServerContext serverCtx, String tid)
    	{
    		System.out.print("rollback called for TID="+tid);
    	}
    }
}
