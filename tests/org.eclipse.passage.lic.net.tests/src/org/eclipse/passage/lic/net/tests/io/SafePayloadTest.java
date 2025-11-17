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
 *     IILS mbH (Hannes Wellmann) - Harden KeyContent against different line-delimiters
 *******************************************************************************/
package org.eclipse.passage.lic.net.tests.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.eclipse.passage.lic.api.io.KeyKeeper;
import org.eclipse.passage.lic.base.io.MD5Hashes;
import org.eclipse.passage.lic.internal.net.io.SafePayload;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public final class SafePayloadTest {
	@TempDir
	public static Path folder;

	public static List<TestKeyKeeper> keyKeepers() {
		return Arrays.asList(new SimpleKeyKeeper(), new KeyKeeperWithOppositeLineDelimiter());
	}

	@ParameterizedTest
	@MethodSource("keyKeepers")
	public void symmetric(TestKeyKeeper factory) {
		String original = "S0me sophisticäted Str!ng"; //$NON-NLS-1$
		try {
			KeyKeeper keeper = new SimpleKeyKeeper().get();
			byte[] encoded = new SafePayload(keeper, new MD5Hashes()).encode(original.getBytes());
			assertTrue(encoded.length > 0);
			byte[] decoded = new SafePayload(factory.get(), new MD5Hashes()).decode(encoded);
			assertTrue(decoded.length > 0);
			assertEquals(original, new String(decoded));
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Not intended to fail on valid data"); //$NON-NLS-1$
		}
	}
}
