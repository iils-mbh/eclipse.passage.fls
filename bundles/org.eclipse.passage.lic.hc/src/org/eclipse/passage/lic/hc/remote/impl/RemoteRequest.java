/*******************************************************************************
 * Copyright (c) 2020, 2025 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *     Hannes Wellmann (IILS mbh) - Support complete URIs as floating server IP
 *******************************************************************************/
package org.eclipse.passage.lic.hc.remote.impl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.passage.lic.api.LicensedProduct;
import org.eclipse.passage.lic.api.LicensingException;
import org.eclipse.passage.lic.api.io.HashesRegistry;
import org.eclipse.passage.lic.hc.remote.Connection;
import org.eclipse.passage.lic.hc.remote.Request;
import org.eclipse.passage.lic.hc.remote.RequestContext;
import org.eclipse.passage.lic.internal.hc.i18n.AccessMessages;
import org.eclipse.passage.lic.licenses.model.api.FloatingLicenseAccess;
import org.eclipse.passage.lic.licenses.model.api.FloatingServerConnection;
import org.eclipse.passage.lic.licenses.model.meta.LicensesPackage;

/**
 * <p>
 * Supplies all the data we are to tell a licensing server on a request.
 * </p>
 * <ul>
 * use
 * <li>{@code url()}</li> to compose server coordinates and all the request
 * parameters
 * <li>{@code config()} to gain a proper request headers configuring unit</li>
 * </ul>
 * 
 * @since 1.1
 */
public abstract class RemoteRequest<C extends Connection> implements Request<C> {

	protected final LicensedProduct product;
	protected final FloatingLicenseAccess access;
	protected final String hash;

	public RemoteRequest(LicensedProduct product, FloatingLicenseAccess access, HashesRegistry hashes) {
		this.product = product;
		this.access = access;
		this.hash = hashes.get().services().iterator().next().id().toString();
	}

	@Override
	public final URL url() throws LicensingException {
		try {
			FloatingServerConnection server = access.getServer();
			URI uri = new URI(server.getIp());
			if (uri.getScheme() == null && uri.getAuthority() == null && uri.getPath() != null) {
				// Server's IP is a pure host definition and should be used as such
				uri = new URI("http", server.getIp(), null, null); //$NON-NLS-1$
			}
			int port = uri.getPort();
			if (server.eIsSet(LicensesPackage.eINSTANCE.getFloatingServerConnection_Port())) {
				port = server.getPort();
			}
			String query = parameters().query();
			URI fullURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), port, //
				uri.getPath(), query, uri.getFragment());
			return fullURI.toURL();
		} catch (LicensingException | MalformedURLException | URISyntaxException e) {
			throw new LicensingException(AccessMessages.Request_failed_to_compose_url, e);
		}
	}

	@Override
	public final RequestContext context() {
		return new BaseRequestContext(product, hash);
	}

}
