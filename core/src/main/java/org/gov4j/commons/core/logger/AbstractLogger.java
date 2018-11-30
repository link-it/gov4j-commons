package org.gov4j.commons.core.logger;

import org.slf4j.Logger;

public abstract class AbstractLogger {

	protected Logger log;
	
	public AbstractLogger(Logger log) {
		this.log = log;
	}
	
	protected abstract String appendPrefix(String message);

	
	public void error(String message, Object ...params) {
		this._error(message, null, params);
	}
	public void error(String message, Throwable t) {
		this._error(message, t, null);
	}
	public void error(String message, Throwable t, Object ...params) {
		this._error(message, t, params);
	}
	private void _error(String message, Throwable t, Object [] params) {
		String msgWithPrefix = this.appendPrefix(message);
		if(params!=null && params.length>0) {
			String msg = String.format(msgWithPrefix,params);
			if(t!=null) {
				this.log.error(msg,t);
			}
			else {
				this.log.error(msg);
			}
		}
		else {
			if(t!=null) {
				this.log.error(msgWithPrefix,t);
			}
			else {
				this.log.error(msgWithPrefix);
			}
		}
	}
	
	
	public void warn(String message, Object ...params) {
		this._warn(message, null, params);
	}
	public void warn(String message, Throwable t) {
		this._warn(message, t, null);
	}
	public void warn(String message, Throwable t, Object ...params) {
		this._warn(message, t, params);
	}
	private void _warn(String message, Throwable t, Object [] params) {
		String msgWithPrefix = this.appendPrefix(message);
		if(params!=null && params.length>0) {
			String msg = String.format(msgWithPrefix,params);
			if(t!=null) {
				this.log.warn(msg,t);
			}
			else {
				this.log.warn(msg);
			}
		}
		else {
			if(t!=null) {
				this.log.warn(msgWithPrefix,t);
			}
			else {
				this.log.warn(msgWithPrefix);
			}
		}
	}
	
	
	public void info(String message, Object ...params) {
		this._info(message, null, params);
	}
	public void info(String message, Throwable t) {
		this._info(message, t, null);
	}
	public void info(String message, Throwable t, Object ...params) {
		this._info(message, t, params);
	}
	private void _info(String message, Throwable t, Object [] params) {
		String msgWithPrefix = this.appendPrefix(message);
		if(params!=null && params.length>0) {
			String msg = String.format(msgWithPrefix,params);
			if(t!=null) {
				this.log.info(msg,t);
			}
			else {
				this.log.info(msg);
			}
		}
		else {
			if(t!=null) {
				this.log.info(msgWithPrefix,t);
			}
			else {
				this.log.info(msgWithPrefix);
			}
		}
	}
	

	public void debug(String message, Object ...params) {
		this._debug(message, null, params);
	}
	public void debug(String message, Throwable t) {
		this._debug(message, t, null);
	}
	public void debug(String message, Throwable t, Object ...params) {
		this._debug(message, t, params);
	}
	private void _debug(String message, Throwable t, Object [] params) {
		String msgWithPrefix = this.appendPrefix(message);
		if(params!=null && params.length>0) {
			String msg = String.format(msgWithPrefix,params);
			if(t!=null) {
				this.log.debug(msg,t);
			}
			else {
				this.log.debug(msg);
			}
		}
		else {
			if(t!=null) {
				this.log.debug(msgWithPrefix,t);
			}
			else {
				this.log.debug(msgWithPrefix);
			}
		}
	}
	

	public void trace(String message, Object ...params) {
		this._trace(message, null, params);
	}
	public void trace(String message, Throwable t) {
		this._trace(message, t, null);
	}
	public void trace(String message, Throwable t, Object ...params) {
		this._trace(message, t, params);
	}
	private void _trace(String message, Throwable t, Object [] params) {
		String msgWithPrefix = this.appendPrefix(message);
		if(params!=null && params.length>0) {
			String msg = String.format(msgWithPrefix,params);
			if(t!=null) {
				this.log.trace(msg,t);
			}
			else {
				this.log.trace(msg);
			}
		}
		else {
			if(t!=null) {
				this.log.trace(msgWithPrefix,t);
			}
			else {
				this.log.trace(msgWithPrefix);
			}
		}
	}
	
}
