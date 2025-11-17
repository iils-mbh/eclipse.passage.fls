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
 * @since 2.1
 */
public final class FeatureId extends StringNamedData {

	public FeatureId(String value) {
		super(value);
	}

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