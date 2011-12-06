/**
 * Copyright (c) 2009-2011, NetBout.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the NetBout.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.netbout.spi.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.netbout.spi.Identity;
import com.ymock.util.Logger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * Restful session.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class RestSession {

    /**
     * Home URI.
     */
    private final transient URI home;

    /**
     * Home URI.
     */
    private final transient Client client;

    /**
     * Public ctor.
     * @param uri Home URI
     */
    public RestSession(final URI uri) {
        this.home = uri;
        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
            .put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);
        this.client = Client.create(config);
    }

    /**
     * Get identity in the session.
     * @param user The user to authenticate
     * @param identity Name of the identity
     * @param secret The secret word to use
     * @return The identity to work with
     */
    public Identity authenticate(final URI user, final String iname,
        final String secret) {
        final WebResource resource = this.client.resource(this.home);
        resource.type(MediaType.APPLICATION_XML);
        resource.cookie(new Cookie("netbout", this.fetch(user, iname, secret)));
        return new RestIdentity(new JerseyRestClient(resource));
    }

    /**
     * Fetch auth code.
     * @param user The user to authenticate
     * @param identity Name of the identity
     * @param secret The secret word to use
     * @return The URL
     */
    public String fetch(final URI user, final String identity,
        final String secret) {
        return this.client.resource(this.home)
            .path("/auth")
            .queryParam("user", user.toString())
            .queryParam("identity", identity)
            .queryParam("secret", secret)
            .get(ClientResponse.class)
            .getHeaders()
            .getFirst("Netbout-auth");
    }

}
