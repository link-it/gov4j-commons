package org.gov4j.commons.api.interceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;

@SuppressWarnings("rawtypes")
public class DumpReceivedMessageToFileSystemInterceptor extends AbstractPhaseInterceptor {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(DumpReceivedMessageToFileSystemInterceptor.class);
	
	public DumpReceivedMessageToFileSystemInterceptor() {
		super(Phase.RECEIVE);
	}

	@Override
	public void handleMessage(Message message) {
		String msgAsString = message.toString();
		String logMsg = String.format("RECEIVE message %s", msgAsString);
		log.info(logMsg);
		message.put(Message.ENCODING, "UTF-8");
		InputStream paramInputStream = message.getContent(InputStream.class);

		if(paramInputStream!=null) {
			File f = null;
			try {
				f = File.createTempFile("ReceivedMessage", ".dump");
			}catch(Exception e) {
				log.error(e.getMessage(),e);
				return;
			}
			try (FileOutputStream fos = new FileOutputStream(f);){
				byte[] buffer = new byte[256];
				while (true) {
					int bytesRead = paramInputStream.read(buffer);
					if (bytesRead == -1) break;
					fos.write(buffer, 0, bytesRead);
				}
				fos.flush();
				paramInputStream.close();
			}catch(Exception e) {
				log.error(e.getMessage(),e);
				return;
			}
	
			logMsg = String.format("Serialized in [%s]",f.getAbsolutePath());
			log.info(logMsg);
			try {
				message.setContent(InputStream.class, new FileInputStream(f));
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}

	}

}
