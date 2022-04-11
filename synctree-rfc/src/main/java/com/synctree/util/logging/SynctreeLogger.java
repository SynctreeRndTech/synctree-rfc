package com.synctree.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SynctreeLogger {
	private Logger logger;

	public SynctreeLogger(String name) {
		this.logger = Logger.getLogger(name);
	}

	public void info(String msg) {
		this.logger.info(msg);
	}

	public void debug(String msg) {
		this.logger.log(Level.FINE, msg);
	}

	public void error(String msg) {
		this.logger.log(Level.SEVERE, msg);
	}

	public void warn(String msg) {
		this.logger.warning(msg);
	}
}
