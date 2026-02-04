/*******************************************************************************
 * Copyright (c) 2020, 2026 ArSysOp
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

import java.util.function.Function;

import org.eclipse.passage.lic.api.FeatureIdentifier;
import org.eclipse.passage.lic.base.NamedData;
import org.eclipse.passage.lic.base.StringNamedData;

/**
 * Supplies an {@code identifier} of a feature under licensing, which is
 * expected to be stored in a variety of sources under a special key.
 * 
 * @see NamedData
 * 
 * @since 4.0
 */
public final class FeatureId extends StringNamedData {

	public FeatureId(FeatureIdentifier feature) {
		super(feature.identifier());
	}

	public FeatureId(Function<String, String> retriever) {
		super(retriever);
	}

	@Override
	public String key() {
		return "licensing.feature.identifier"; //$NON-NLS-1$
	}
}
