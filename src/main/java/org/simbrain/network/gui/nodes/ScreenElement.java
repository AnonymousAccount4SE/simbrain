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
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventFilter;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.util.PBounds;
import org.simbrain.network.NetworkModel;
import org.simbrain.network.gui.NetworkPanel;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

/**
 * <b>ScreenElement</b> extends a Piccolo node with property change, tool tip,
 * and property dialog, and support. Screen elements are automatically support
 * the primary user interactions in the network panel.
 */
public abstract class ScreenElement extends PPath.Float {

    /**
     * Network panel.
     */
    private NetworkPanel networkPanel;

    /**
     * Is this element a member of a view group?.
     */
    private boolean isGrouped = false;

    /**
     * Create a new abstract screen element with the specified network panel.
     *
     * @param networkPanel network panel for this screen element
     */
    protected ScreenElement(final NetworkPanel networkPanel) {
        super();
        setNetworkPanel(networkPanel);
        init();
    }

    /**
     * Initialize this <code>ScreenElement</code>.
     */
    private void init() {

        if (hasContextMenu()) {
            addInputEventListener(new ContextMenuEventHandler());
        }

        if (hasPropertyDialog()) {
            addInputEventListener(new PropertyDialogEventHandler());
        }

        if (hasToolTipText()) {
            addInputEventListener(new ToolTipTextUpdater(networkPanel) {

                /** @see ToolTipTextUpdater */
                protected String getToolTipText() {
                    return ScreenElement.this.getToolTipText();
                }
            });
        }

        // Basic event handler for single clicks. Only register regular clicks
        // (not right clicks).
        addInputEventListener(new PBasicInputEventHandler() {
            /** @see PBasicInputEventHandler */
            public void mousePressed(final PInputEvent event) {
                // System.out.println("Mouse Pressed: " + event);
                if (!isRightClick(event)) {
                    singleClickEvent();
                }
            }

            /** @see PBasicInputEventHandler */
            public void mouseClicked(final PInputEvent event) {
                // System.out.println("Mouse Clicked: " + event);
                if (!isRightClick(event)) {
                    singleClickEvent();
                }
            }

        });
    }

    /**
     * Helper method to abstract between genuine right clicks on control-down
     * events (which are often treated as right clicks when there is no right
     * click button).
     *
     * @param event the input event
     * @return whether it is a "right click" or not
     */
    private boolean isRightClick(final PInputEvent event) {

        if (event.isRightMouseButton()) {
            return true;
        } else if (event.isControlDown()) {
            return true;
        }
        return false;
    }

    /**
     * Return <code>true</code> if this screen element is selectable.
     * <p>
     * Being selectable requires that this screen element is pickable as far as
     * the Piccolo API is concerned, so if this method returns <code>true</code>
     * , be sure that this class also returns <code>true</code> for its
     * <code>getPickable()</code> method.
     * </p>
     *
     * @return true if this screen element is selectable
     * @see org.piccolo2d.PNode#getPickable
     * @see org.piccolo2d.PNode#setPickable
     */
    public abstract boolean isSelectable();

    /**
     * Return <code>true</code> if this screen element should show a selection
     * handle.
     * <p>
     * Showing a selection handle requires that this screen element is pickable
     * as far as the Piccolo API is concerned, so if this method returns
     * <code>true</code>, be sure that this class also returns <code>true</code>
     * for its <code>getPickable()</code> method.
     * </p>
     * <p>
     * Showing a selection handle also requires that this screen element is
     * selectable, so if this method returns <code>true</code>, be sure that
     * this class also returns <code>true</code> for its
     * <code>isSelectable()</code> method.
     * </p>
     *
     * @return true if this screen element should show a selection handle
     * @see org.piccolo2d.PNode#getPickable
     * @see org.piccolo2d.PNode#setPickable
     * @see #isSelectable
     */
    public abstract boolean showNodeHandle();

    /**
     * Return <code>true</code> if this screen element is draggable.
     * <p>
     * Being draggable requires that this screen element is pickable as far as
     * the Piccolo API is concerned, so if this method returns <code>true</code>
     * , be sure that this class also returns <code>true</code> for its
     * <code>getPickable()</code> method.
     * </p>
     * <p>
     * Being draggable also requires that this screen element is selectable, so
     * if this method returns <code>true</code>, be sure that this class also
     * returns <code>true</code> for its <code>isSelectable()</code> method.
     * </p>
     *
     * @return true if this screen element is draggable
     * @see org.piccolo2d.PNode#getPickable
     * @see org.piccolo2d.PNode#setPickable
     * @see #isSelectable
     */
    public abstract boolean isDraggable();

    /**
     * Return <code>true</code> if this screen element has tool tip text. If
     * this screen element does not have tool tip text, a tool tip event handler
     * will not be registered.
     *
     * @return true if this screen element has tool tip text
     * @see #getToolTipText
     */
    protected abstract boolean hasToolTipText();

    /**
     * Return a <code>String</code> to use as tool tip text for this screen
     * element. Return <code>null</code> if this screen element does not have
     * tool tip text or to temporarily prevent the tool tip from displaying.
     *
     * @return a <code>String</code> to use as tool tip text for this screen
     * element
     * @see #hasToolTipText
     */
    protected abstract String getToolTipText();

    /**
     * Return <code>true</code> if this screen element has a context menu. If
     * this screen element does not have a context menu, a context menu event
     * handler will not be registered.
     *
     * @return true if this screen element has a context menu.
     * @see #getContextMenu
     */
    protected abstract boolean hasContextMenu();

    /**
     * Return a context menu specific to this screen element. Return
     * <code>null</code> if this screen element does not have a context menu.
     *
     * @return a context menu specific to this screen element
     * @see #hasContextMenu
     */
    protected abstract JPopupMenu getContextMenu();

    /**
     * Return <code>true</code> if this screen element has a property dialog. If
     * this screen element does not have a property dialog, a property dialog
     * event handler will not be registered.
     *
     * @return true if this screen element has a property dialog
     * @see #getPropertyDialog
     */
    protected abstract boolean hasPropertyDialog();

    /**
     * Return a property dialog for this screen element. Return
     * <code>null</code> if this screen element does not have a property dialog.
     *
     * @return a property dialog for this screen element
     * @see #hasPropertyDialog
     */
    protected abstract JDialog getPropertyDialog();

    /**
     * Reset colors when default colors have been changed in
     * <code>NetworkPreferences</code>.
     */
    public abstract void resetColors();

    /**
     * Return the network panel for this screen element.
     *
     * @return the network panel for this screen element
     */
    public final NetworkPanel getNetworkPanel() {
        return networkPanel;
    }

    /**
     * Set the network panel for this screen element to
     * <code>networkPanel</code>.
     * <p>
     * <p>
     * This is a bound property.
     * </p>
     *
     * @param networkPanel network panel for this screen element
     */
    public final void setNetworkPanel(final NetworkPanel networkPanel) {

        NetworkPanel oldNetworkPanel = this.networkPanel;
        this.networkPanel = networkPanel;
        firePropertyChange(-1, "networkPanel", oldNetworkPanel, this.networkPanel);
    }

    /**
     * Screen element-specific context menu event handler.
     */
    private class ContextMenuEventHandler extends PBasicInputEventHandler {

        /**
         * Show the context menu.
         *
         * @param event event
         */
        private void showContextMenu(final PInputEvent event) {

            event.setHandled(true);
            JPopupMenu contextMenu = getContextMenu();
            Point2D canvasPosition = event.getCanvasPosition();
            // TODO
            //networkPanel.getPlacementManager().setLastClickedPosition(canvasPosition);
            contextMenu.show(networkPanel.getCanvas(), (int) canvasPosition.getX(), (int) canvasPosition.getY());
        }

        @Override
        public void mousePressed(final PInputEvent event) {

            if (event.isPopupTrigger()) {
                showContextMenu(event);
            }
        }

        @Override
        public void mouseReleased(final PInputEvent event) {

            if (event.isPopupTrigger()) {
                showContextMenu(event);
            }
        }
    }

    /**
     * Called when element is single clicked on. Override to provide custom
     * behaviors in that case.
     */
    protected void singleClickEvent() {
    }

    /**
     * Property dialog event handler.
     */
    private class PropertyDialogEventHandler extends PBasicInputEventHandler {

        /**
         * Create a new property dialog event handler.
         */
        public PropertyDialogEventHandler() {
            super();
            setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
        }

        @Override
        public void mouseClicked(final PInputEvent event) {

            if (event.getClickCount() == 2) {
                event.setHandled(true);
                SwingUtilities.invokeLater(new Runnable() {
                    /** @see Runnable */
                    public void run() {
                        JDialog propertyDialog = ScreenElement.this.getPropertyDialog();
                        propertyDialog.pack();
                        propertyDialog.setLocationRelativeTo(null);
                        propertyDialog.setVisible(true);
                    }
                });
            }
        }
    }

    public boolean isGrouped() {
        return isGrouped;
    }

    public void setGrouped(boolean isGrouped) {
        this.isGrouped = isGrouped;
    }

    /**
     * Returns a reference to the the top level PNode of this Screen Element.
     * Usually the ScreenElement is the top level PNode, but in some cases
     * e.g. an interaction box, it's not.  Override in those cases.
     */
    public PNode getNode() {
        return this;
    }

    /**
     * Returns a reference to the model object this node represents.
     */
    public abstract NetworkModel getModel();

    /**
     * Override if selection events should select something besides this PNode.
     */
    public ScreenElement getSelectionTarget() {
        return this;
    }

    public boolean isIntersecting(PBounds bound) {
        return getGlobalBounds().intersects(bound);
    }
}
