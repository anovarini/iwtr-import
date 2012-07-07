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

def cli = new CliBuilder(usage: 'iwtr-import <project location> ...')

def options = cli.parse(args)


if (options.arguments()) {
    startImport(options)
}
else {
    cli.usage()
}

private void startImport(OptionAccessor options) {
    File repositoryLocation = defaultRepositoryLocation()
    def repository = new Repository(repositoryLocation)

    try {
        startImport(repository, options)
    }
    finally {
        repository.close()
    }
}

private void startImport(Repository repository, OptionAccessor options) {
    repository.init()

    options.arguments().each {
        repository.importFrom(new File(it))
    }
}

private File defaultRepositoryLocation() {
    String homeDir = System.getProperty("user.home")
    def defaultRepositoryLocation = new File("$homeDir/.iwtr")
    if (!defaultRepositoryLocation.exists()) {
        defaultRepositoryLocation.mkdir()
    }
    defaultRepositoryLocation
}
