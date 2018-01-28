/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.util;

/**
 * <b>SimpleId</b> provides an id based on a base name and an integer index.
 */
public class SimpleId {

    /**
     * The base name of the id.
     */
    private String rootName;

    /**
     * The starting index.
     */
    private int index;

    /**
     * Construct simpleId.
     *
     * @param rootName root name.
     * @param index    beginning index.
     */
    public SimpleId(final String rootName, final int index) {
        this.rootName = rootName;
        this.index = index;
    }

    /**
     * Returns a simple identifier.
     *
     * @return a unique identification
     */
    public String getId() {
        String id = rootName + "_" + index++;
        return id;
    }

    /**
     * Returns what the simple identifier would be if a hypothetical construct
     * requiring such an id were constructed and put into the network.
     *
     * @return a hypothetical unique identification
     */
    public String getHypotheticalId() {
        int ind = index;
        String id = rootName + "_" + ind;
        return id;
    }

}
