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
package org.simbrain.network.gui.nodes;

import org.piccolo2d.PNode;
import org.piccolo2d.extras.handles.PHandle;
import org.piccolo2d.extras.util.PNodeLocator;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Selection handle.
 * <p>
 * Usage:
 * <p>
 * <pre>
 * PNode node = ...;
 * SelectionHandle.addSelectionHandleTo(node)
 * </pre>
 * <p>
 * and
 * <p>
 * <pre>
 * PNode node = ...;
 * SelectionHandle.removeSelectionHandleFrom(node)
 * </pre>
 */
public final class SourceHandle extends PHandle {

    /**
     * Extend factor.
     */
    private static final float DEFAULT_EXTEND_FACTOR= 0.2f;

    /**
     * Color of selection boxes.
     */
    private static Color sourceColor = Color.RED;

    /**
     * Create a new selection handle.
     *
     * @param locator locator
     */
    private SourceHandle(final PNodeLocator locator) {

        super(locator);

        reset();
        setPickable(false);

        PNode parentNode = locator.getNode();
        parentNode.addChild(this);

        setPaint(null);
        setStrokePaint(sourceColor);

        // force handle to check its location and size
        updateBounds();
        relocateHandle();
    }

    /**
     * @see PHandle
     */
    public void parentBoundsChanged() {
        updateBounds();
        super.parentBoundsChanged();
    }

    /**
     * Update the bounds of this selection handle based on the size of its
     * parent plus an extension factor.
     */
    private void updateBounds() {
        PNode parentNode = ((PNodeLocator) getLocator()).getNode();
        // Different extension factor depending on whether the node being decorated is a neuron group node or not
        float ef;
        if (parentNode instanceof InteractionBox) {
            // TODO: This is a hack because current interaction boxes are rescaled improperly in neuron group nodes.
            // The problem is that NeuronGroupNode.layoutChildren uses full bounds, which includes the selection box bounds
            ef = .09f;
        } else {
            ef = DEFAULT_EXTEND_FACTOR;
        }

        double x = 0.0d - (parentNode.getBounds().getWidth() * ef);
        double y = 0.0d - (parentNode.getBounds().getHeight() * ef);
        double width = parentNode.getBounds().getWidth() + 2 * (parentNode.getBounds().getWidth() * ef);
        double height = parentNode.getBounds().getHeight() + 2 * (parentNode.getBounds().getHeight() * ef);
        append(new Rectangle2D.Float((float) x, (float) y, (float) width, (float) height), false);    }

    /**
     * Return true if the specified node has a selection handle as a child.
     *
     * @param node node
     * @return true if the specified node has a selection handle as a child
     */
    private static boolean hasSelectionHandle(final PNode node) {

        for (Iterator i = node.getChildrenIterator(); i.hasNext(); ) {
            PNode n = (PNode) i.next();

            if (n instanceof SourceHandle) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a selection handle to the specified node, if one does not exist
     * already.
     *
     * @param node node to add the selection handle to, must not be null
     */
    public static void addSourceHandleTo(final PNode node) {

        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }

        if (hasSelectionHandle(node)) {
            return;
        }

        PNodeLocator nodeLocator = new PNodeLocator(node);
        SourceHandle selectionHandle = new SourceHandle(nodeLocator);
    }

    /**
     * Remove the selection handle(s) from the specified node, if any exist.
     *
     * @param node node to remove the selection handle(s) from, must not be null
     */
    public static void removeSourceHandleFrom(final PNode node) {

        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }

        Collection handlesToRemove = new ArrayList();

        for (Iterator i = node.getChildrenIterator(); i.hasNext(); ) {
            PNode n = (PNode) i.next();

            if (n instanceof SourceHandle) {
                handlesToRemove.add(n);
            }
        }
        node.removeChildren(handlesToRemove);
    }

    /**
     * @return Returns the sourceColor.
     */
    public static Color getSourceColor() {
        return sourceColor;
    }

    /**
     * @param selectionColor The sourceColor to set.
     */
    public static void setSourceColor(final Color selectionColor) {
        SourceHandle.sourceColor = selectionColor;
    }
}