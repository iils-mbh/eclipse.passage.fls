/*******************************************************************************
 * Copyright (c) 2021 ArSysOp
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
package org.eclipse.passage.lic.internal.net;

import java.util.Optional;

import org.eclipse.passage.lic.equinox.EquinoxFrameworkAware;
import org.eclipse.passage.lic.internal.net.api.FrameworkConstructor;
import org.eclipse.passage.lic.internal.net.api.handle.NetRequest;
import org.eclipse.passage.lic.internal.net.handle.ProductUserRequest;

@SuppressWarnings("restriction")
public final class NetFrameworkAware<R extends NetRequest> extends EquinoxFrameworkAware<FrameworkConstructor> {

	public NetFrameworkAware(ProductUserRequest<R> request) {
		super(FrameworkConstructor.class, ctor -> Optional.of(ctor.forRequest(request)));
	}

}
