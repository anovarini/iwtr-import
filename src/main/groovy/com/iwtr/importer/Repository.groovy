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
import com.tinkerpop.gremlin.groovy.Gremlin

class Repository {

    static {
        Gremlin.load()
    }

    File location
    Neo4jGraph graph

    Repository(File location) {
        this.location = location
    }

    void importFrom(File location) {
        def ivyFile = ~/ivy.xml/

        def scan

        scan = {
            it.eachDir scan
            it.eachFileMatch(ivyFile) {
                BuildFile buildFile = new BuildFile(it)
                buildFile.storeInto(graph)
            }
        }

        scan location
    }

    void init() {
        graph = new Neo4jGraph(location.absolutePath)
    }

    void close() {
        graph.shutdown()
    }
}
