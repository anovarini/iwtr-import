/*
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

class BuildFile {

    File buildFile
    def buildName
    def dependencies

    BuildFile(File buildFile) {
        this.buildFile = buildFile
    }

    public void analyse() {
        def slurpedBuildFile = new XmlSlurper().parse buildFile
        buildName = slurpedBuildFile.info.@module.text()
        dependencies = slurpedBuildFile.dependencies.dependency.collect { it.@name.text() }
    }

    void storeInto(def repository) {
        def buildNode = repository.lookup buildName

        dependencies.each {
            name ->
            def dependencyNode = repository.lookup name
            repository.connect(buildNode, dependencyNode)
        }
    }
}
