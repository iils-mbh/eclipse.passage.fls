/*******************************************************************************
 * Copyright (c) 2020, 2022 ArSysOp
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
package org.eclipse.passage.lbc.internal.jetty;

import org.eclipse.passage.lbc.internal.base.FlotingRequestHandled;
import org.eclipse.passage.lbc.internal.base.api.FloatingState;
import org.eclipse.passage.lbc.internal.base.api.FloatingStateFromGear;
import org.eclipse.passage.lbc.jetty.FlsCommandScope;
import org.eclipse.passage.lic.internal.jetty.JettyHandler;
import org.eclipse.passage.lic.internal.jetty.interaction.LicensedJettyActivator;
import org.eclipse.passage.lic.internal.net.connect.Storage;
import org.osgi.framework.BundleContext;

@SuppressWarnings("restriction")
public final class FlsJettyActivator extends LicensedJettyActivator {

	private final Storage storage;

	public FlsJettyActivator() {
		this.storage = new Storage();
	}

	@Override
	protected String name() {
		return new FlsCommandScope().id();
	}

	@Override
	protected JettyHandler handler() {
		FloatingState state = new FloatingStateFromGear(storage.get()).get();
		return new JettyHandler(request -> new FlotingRequestHandled(new StatedRequest(request, state)).get());
	}

	@Override
	protected void configureLogging() {
		// should be done by end-product framework
	}

	@Override
	protected void registerCustomCommands(BundleContext context) {
		new FlsCommands().register(context, name(), storage);
	}

}
