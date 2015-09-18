package org.brewman.hateos.web.filter.gzip;

import javax.servlet.ServletException;

public class GzipResponseHeadersNotModifiableException extends ServletException {

    /**
     * 
     */
    private static final long serialVersionUID = 847538597491269280L;

    public GzipResponseHeadersNotModifiableException(String message) {
        super(message);
    }
}
