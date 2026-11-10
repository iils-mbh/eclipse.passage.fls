/*******************************************************************************
 * Copyright (c) 2021, 2022 ArSysOp
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.lic.internal.jetty;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConfiguration.Customizer;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.passage.lic.internal.jetty.i18n.Messages;
import org.eclipse.passage.lic.internal.net.connect.BindAddress;
import org.eclipse.passage.lic.internal.net.connect.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JettyServer {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Supplier<JettyHandler> handler;
	private final List<HttpConfiguration.Customizer> customizers;
	private Optional<Server> server = Optional.empty();

	public JettyServer(Supplier<JettyHandler> handler) {
		this(handler, List.of());
	}

	public JettyServer(Supplier<JettyHandler> handler, List<HttpConfiguration.Customizer> customizers) {
		Objects.requireNonNull(handler, "JettyServer::handler"); //$NON-NLS-1$
		this.handler = handler;
		this.customizers = customizers;
	}

	public void launch(BindAddress listen, Port port) throws JettyException {
		try {
			InetSocketAddress address = InetSocketAddress.createUnresolved(listen.get().get(), port.get().get());
			server = Optional.of(new Server(address));
			server.get().setHandler(handler.get());
			applyHttpConfigurationsCustomizers(server.get(), customizers);
			server.get().start();
			log.info(String.format(Messages.started, address));
		} catch (Exception e) {
			logAndRethrow(e, Messages.error_onstart);
		}
	}

	private static void applyHttpConfigurationsCustomizers(Server server, List<Customizer> customizers) {
		Set<HttpConfiguration> configurations = Collections.newSetFromMap(new IdentityHashMap<>());
		for (Connector connector : server.getConnectors()) {
			if (connector instanceof AbstractConnector abstractConnector) {
				for (ConnectionFactory factory : abstractConnector.getConnectionFactories()) {
					HttpConfiguration httpConfiguration = switch (factory) {
					case HttpConnectionFactory httpFactory -> httpFactory.getHttpConfiguration();
					default -> null;
					};
					if (httpConfiguration != null && configurations.add(httpConfiguration)) {
						customizers.forEach(httpConfiguration::addCustomizer);
					}
				}
			}
		}
	}

	public void terminate() throws JettyException {
		try {
			if (server.isPresent()) {
				server.get().stop();
			}
			log.info(String.format(Messages.stopped));
		} catch (Exception e) {
			logAndRethrow(e, Messages.error_onstop);
		}
	}

	public String state() throws JettyException {
		try {
			if (!server.isPresent()) {
				return "NOT LAUNCHED"; //$NON-NLS-1$
			}
			return server.get().getState();
		} catch (Exception e) {
			throw new JettyException(Messages.error_onstate, e);
		}
	}

	private void logAndRethrow(Exception e, String template) throws JettyException {
		String message = String.format(template, e.getClass(), e.getMessage());
		log.error(message, e);
		throw new JettyException(message, e);
	}

}
