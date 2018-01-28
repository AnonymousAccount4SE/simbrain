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
package org.simbrain.world.odorworld.resources;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * <b>OdorWorldResourceManager</b> provides resources (stored in the same
 * directory) to the rest of the program.
 */
public class OdorWorldResourceManager {

    /**
     * Retrieve an ImageIcon based on its file name.
     *
     * @param name name of the image file to retrieve
     * @return the ImageIcon which can be used with Swing components, etc
     */
    public static ImageIcon getImageIcon(final String name) {
        ImageIcon imageIcon;
        URL url;

        url = OdorWorldResourceManager.class.getResource(name);
        imageIcon = new ImageIcon(url);

        return imageIcon;
    }

    /**
     * Retrieve an Image based on its file name.
     *
     * @param name name of the image file to retrieve
     * @return the Image which can be used with Swing components, etc
     */
    public static Image getImage(final String name) {
        URL url;

        url = OdorWorldResourceManager.class.getResource(name);

        java.awt.Toolkit toolKit = java.awt.Toolkit.getDefaultToolkit();

        return toolKit.getImage(url);
    }
}
