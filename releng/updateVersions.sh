#! /bin/bash -xe

###############################################################################
# Copyright (c) 2025, 2025 IILS mbH and others
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License 2.0 which is available at
# https://www.eclipse.org/legal/epl-2.0/.
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Hannes Wellmann (IILS mbH) - initial API and implementation
###############################################################################

if [[ -z "${NEW_VERSION}" ]]; then
	echo "ERROR: Required parameter NEW_VERSION not defined"
	exit 1
fi
mvn org.eclipse.tycho:tycho-versions-plugin:set-version "-DnewVersion=${NEW_VERSION}-SNAPSHOT"

# Update Provided Capability MANIFEST.MF files
for file in bundles/*/META-INF/MANIFEST.MF; do
	sed --in-place --expression="s|;version=\"[[:digit:]]\+\.[[:digit:]]\+\.[[:digit:]]\+\";|;version=\"${NEW_VERSION}\";|g" $file
done
