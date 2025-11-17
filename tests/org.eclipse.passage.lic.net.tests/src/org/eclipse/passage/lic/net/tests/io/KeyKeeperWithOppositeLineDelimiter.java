/*******************************************************************************
 * Copyright (c) 2022 IILS mbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IILS mbH (Hannes Wellmann) - initial API and implementation
 *******************************************************************************/
package org.eclipse.passage.lic.net.tests.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

import org.eclipse.passage.lic.api.LicensingException;
import org.eclipse.passage.lic.api.io.KeyKeeper;
import org.eclipse.passage.lic.base.io.FileKeyKeeper;

final class KeyKeeperWithOppositeLineDelimiter extends TestKeyKeeper {

	@Override
	public KeyKeeper get() throws LicensingException, IOException {
		Path file = SafePayloadTest.folder.resolve("key.pub"); //$NON-NLS-1$
		Files.write(file, content(), StandardOpenOption.CREATE_NEW);
		return new FileKeyKeeper(file);
	}

	private byte[] content() throws IOException {
		try (var lines = Files.lines(publicKey())) {
			return lines.collect(Collectors.joining(oppositeLineSeparator())) //
					.getBytes(StandardCharsets.UTF_8);
		}
	}

	private String oppositeLineSeparator() {
		return "\r\n".equals(System.lineSeparator()) //$NON-NLS-1$
				? "\n" //$NON-NLS-1$
				: "\r\n"; //$NON-NLS-1$
	}
}
