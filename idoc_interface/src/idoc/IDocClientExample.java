package idoc;
// package com.sap.conn.idoc.examples;

import com.sap.conn.jco.*;
import com.sap.conn.idoc.jco.*;
import com.sap.conn.idoc.*;
// import java.io.*;

public class IDocClientExample {

	public static void main(String[] args) {

		try {
			// see provided configuration file SHF.jcoDestination
			JCoDestination destination = JCoDestinationManager.getDestination("SHF");
			IDocRepository iDocRepository = JCoIDoc.getIDocRepository(destination);
			String tid = destination.createTID();
			IDocFactory iDocFactory = JCoIDoc.getIDocFactory();

			// a) create new idoc type    SAP WE30 세팅 
			IDocDocument doc = iDocFactory.createIDocDocument(iDocRepository, "ZYY_BOMIDOC");
			
            // Set Sender Info   SAP WE20 세팅 
			doc.setMessageType("ZYY_MESS_TYPE");
			// doc.setSenderPort("SAPIDOC");
			doc.setSenderPartnerType("LS");
			doc.setSenderPartnerNumber("ZYYJAVA");

			// Set Segment    SAP WE31 세팅 
			IDocSegment segment = doc.getRootSegment();
			segment = segment.addChild("ZYY_BOMHEAD");
			segment.setValue("STLTY", "1");
			segment.setValue("STLNR", "JGZ");
			segment.setValue("STLAL", "01");
			segment.setValue("STKOZ", "001");
			segment.setValue("LKENZ", "X");

			segment = segment.addChild("ZYY_BOMITEM");
			segment.setValue("STLTY", "1");
			segment.setValue("STLNR", "JGZ");
			segment.setValue("STLKN", "20220517");
			segment.setValue("STPOZ", "20220517");
			
			segment = segment.addSibling();
			segment.setValue("STLTY", "2");
			segment.setValue("STLNR", "JGZJGZ");
			segment.setValue("STLKN", "20220517");
			segment.setValue("STPOZ", "20220517");
			JCoIDoc.send(doc, IDocFactory.IDOC_VERSION_DEFAULT, destination, tid);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print("program end");
	}
}
