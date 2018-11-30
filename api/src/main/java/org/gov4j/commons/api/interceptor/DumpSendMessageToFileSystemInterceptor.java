package org.gov4j.commons.api.interceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;

@SuppressWarnings("rawtypes")
public class DumpSendMessageToFileSystemInterceptor extends AbstractPhaseInterceptor {

	static final Logger log = org.slf4j.LoggerFactory.getLogger(DumpSendMessageToFileSystemInterceptor.class);
	
	public DumpSendMessageToFileSystemInterceptor() {
		super(Phase.PRE_STREAM);
	}

	@Override
	public void handleMessage(Message message) {
		String msgAsString = message.toString();
		String logMsg = String.format("SEND message %s", msgAsString);
		log.info(logMsg);
		message.put(Message.ENCODING, "UTF-8");
		OutputStream outputStream = message.getContent(OutputStream.class);
		if(outputStream!=null) {
			final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(outputStream);
	        message.setContent(OutputStream.class, newOut);
	        newOut.registerCallback(new LoggingCallback());
		}	
    }

}
class LoggingCallback implements CachedOutputStreamCallback {
	
    @Override
	public void onFlush(CachedOutputStream cos) {
    }

    @Override
	public void onClose(CachedOutputStream cos) {
       	File f = null;
		try {
			f = File.createTempFile("SendMessage", ".dump");
			FileOutputStream fos =new FileOutputStream(f);
			cos.writeCacheTo(fos);
			fos.flush();
			cos.flush();
			fos.close();
			String logMsg = String.format("Serialized in [%s]",f.getAbsolutePath());
			DumpSendMessageToFileSystemInterceptor.log.info(logMsg);
		}catch(Exception e) {
			DumpSendMessageToFileSystemInterceptor.log.error(e.getMessage(),e);
		}
    }
}
		




