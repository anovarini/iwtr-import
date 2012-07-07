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

import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph
import com.tinkerpop.blueprints.pgm.Vertex

class BuildFile {

    File buildFile

    BuildFile(File buildFile) {
        this.buildFile = buildFile
    }

    void storeInto(Neo4jGraph graph) {
        def slurpedBuildFile = new XmlSlurper().parse buildFile
        def buildName = slurpedBuildFile.info.@module.text()

        Vertex buildNode = lookup buildName, graph

        def dependencies = slurpedBuildFile.dependencies.dependency.collect { it.@name.text() }
        dependencies.each {
            name ->
            def dependencyNode = lookup name, graph
            connect(buildNode, dependencyNode, graph)
        }
    }

    Vertex lookup(def buildFileName, def graph) {
        def storedModule = graph.V.filter { it.name == buildFileName }

        if (!storedModule.hasNext()) {
            storedModule = graph.addVertex([name: (buildFileName)])
            return storedModule
        }
        storedModule.next()
    }

    void connect(Vertex storedModule, Vertex storedDependency, def graph) {
        def storedLink = storedModule.out.filter {
            it.name == storedDependency.name
        }
        if (!storedLink.hasNext()) {
            graph.addEdge null, storedModule, storedDependency, 'depends_on'
        }
    }
}
