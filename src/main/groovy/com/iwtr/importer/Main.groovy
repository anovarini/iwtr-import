/**
 * Copyright 2012 Alessandro Novarini
 *
 * This file is part of the iwtr project.
 *
 * Iwtr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.iwtr.importer

def cli = new CliBuilder(usage: 'iwtr-import [options] <project-location ...>', header: 'Options:')
cli._(longOpt: 'help', 'Print this help')
cli._(longOpt: 'repository', args: 1, argName: 'location', 'Use a custom repository location (default ${user.home}/.iwtr)')
cli._(longOpt: 'clean-repository', 'Clean the repository before starting the import')


def options = cli.parse(args)

if (options.arguments()) {
    Repository repository = setupRepository customRepositoryLocationOrDefault(options), options.'clean-repository' as boolean
    doImport options.arguments(), repository
} else {
    cli.usage()
}

private String customRepositoryLocationOrDefault(OptionAccessor options) {
    String homeDir = System.getProperty("user.home");
    options.repository ?: "$homeDir/.iwtr"
}

private void doImport(final def locations, final Repository repository) {
    try {
        tryImport locations, repository
    } finally {
        repository.close()
    }
}

private void tryImport(final def projectLocations, final Repository repository) {
    repository.init()
    projectLocations.each {repository.importProject new File(it as String) }
}

private Repository setupRepository(String repositoryLocation, boolean cleanRepoRequested) {
    new Repository(prepareRepositoryLocation(repositoryLocation, cleanRepoRequested))
}

private File prepareRepositoryLocation(String repositoryLocation, boolean cleanRepoRequested) {
    final File location = new File(repositoryLocation)
    if (!location.exists()) {
        location.mkdir()
    } else {
        deleteRepositoryContentIfRequested(location, cleanRepoRequested)
    }
    location
}

private void deleteRepositoryContentIfRequested(File location, boolean cleanRepoRequested) {
    if (cleanRepoRequested) {
        deleteRepositoryContent(location)
    }
}

private void deleteRepositoryContent(File location) {
    def scan
    scan = {
        File aFile = it
        aFile.eachDir scan
        aFile.eachFile { it.delete() }
        if (!aFile.equals(location)) aFile.delete()
    }
    scan location
}
