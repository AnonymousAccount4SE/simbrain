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
package org.simbrain.world.odorworld.dialogs;

import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.StandardDialog;
import org.simbrain.world.odorworld.OdorWorldPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <b>DialogOdorWorld</b> is used to set the enivronment's parameters, in
 * particular, the way stimuli are constructed to be sent the network, and the
 * way outputs from the network are expressed in the world.
 */
public class OdorWorldDialog extends StandardDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    /**
     * The inital value for world width field's number of columns.
     */
    private final int initialWorldWidth = 5;

    /**
     * The indent for the color chooser.
     */
    private final int colorChooserIndent = 200;

    /**
     * The world for which properties are to be set.
     */
    private OdorWorldPanel worldPanel;

    /**
     * The pane that holds the choices to set.
     */
    private LabelledItemPanel mainPanel = new LabelledItemPanel();

    /**
     * The text field containing the width of the world.
     */
    private JTextField worldWidth = new JTextField();

    /**
     * The text field containing the height of the world.
     */
    private JTextField worldHeight = new JTextField();

    /**
     * The checkbox representing whether or not moving objects initiates an
     * update of the world.
     */
    private JCheckBox initiateMovement = new JCheckBox();

    /**
     * The checkbox representing whether or not objects inhibit the movement of
     * creatures ("clipping").
     */
    private JCheckBox inhibitMovement = new JCheckBox();

    /**
     * The checkbox representing whether or not local boundaries are used
     * ("wrap-around").
     */
    private JCheckBox useLocalBounds = new JCheckBox();

    /**
     * The checkbox representing whether or not the network is updated while
     * dragging an entity.
     */
    private JCheckBox updateDrag = new JCheckBox();

    /**
     * The Button that brings up a color chooser dialog for the world's
     * background.
     */
    private JButton colorChoice = new JButton("Set");

    /**
     * The color returned from the color chooser.
     */
    private Color theColor;

    /**
     * Constructor initializes a settings dialog for the odorworld from which it
     * was called.
     *
     * @param wp the odorworld calling the constructor
     */
    public OdorWorldDialog(final OdorWorldPanel wp) {
        worldPanel = wp;
        init();
    }

    /**
     * This method initialises the components on the panel.
     */
    private void init() {
        setTitle("World Dialog");

        fillFieldValues();

        worldWidth.setColumns(initialWorldWidth);

        mainPanel.addItem("World Width", worldWidth);
        mainPanel.addItem("World Height", worldHeight);
        mainPanel.addItem("Moving objects initiates creature movement", initiateMovement);
        mainPanel.addItem("Objects block movement", inhibitMovement);
        mainPanel.addItem("Enable boundaries (if not, agents wrap around)", useLocalBounds);
        mainPanel.addItem("Update network while dragging objects", updateDrag);
        mainPanel.addItem("Set Background Color", colorChoice);

        setContentPane(mainPanel);

        colorChoice.addActionListener(this);
        updateDrag.addActionListener(this);
    }

    /**
     * Populate fields with current data.
     */
    public void fillFieldValues() {
        // worldWidth.setText(Integer.toString(worldPanel.getWorld().getWorldWidth()));
        // worldHeight.setText(Integer.toString(worldPanel.getWorld().getWorldHeight()));
        // updateDrag.setSelected(theWorld.getUpdateWhileDragging());
        // useLocalBounds.setSelected(theWorld.getUseLocalBounds());

        if (!updateDrag.isSelected()) {
            initiateMovement.setSelected(false);
            initiateMovement.setEnabled(false);
        } else {
            // initiateMovement.setSelected((theWorld.getObjectDraggingInitiatesMovement()));
        }

        // inhibitMovement.setSelected(theWorld.getObjectInhibitsMovement());
        theColor = new Color(worldPanel.getBackgroundColor());
    }

    /**
     * Set world values based on fields.
     */
    public void setValues() {
        // worldPanel.getWorld().setWorldWidth(Integer.parseInt(worldWidth.getText()));
        // worldPanel.getWorld().setWorldHeight(Integer.parseInt(worldHeight.getText()));
        worldPanel.setBackgroundColor(theColor.getRGB());
        // worldPanel.getParentFrame().setMaxSize();
    }

    /**
     * Performs an appropriate action on the given event.
     *
     * @param e the parameter activating this method
     */
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource().equals(updateDrag)) {
            JCheckBox test = (JCheckBox) e.getSource();

            if (!test.isSelected()) {
                initiateMovement.setSelected(false);
                initiateMovement.setEnabled(false);
                repaint();
            } else if (test.isSelected()) {
                // initiateMovement.setSelected((theWorld.getObjectDraggingInitiatesMovement()));
                initiateMovement.setEnabled(true);
                repaint();
            }
        } else if (e.getSource().equals(colorChoice)) {
            theColor = getColor();
        }
    }

    /**
     * Show the color pallette and get a color.
     *
     * @return selected color
     */
    public Color getColor() {
        JColorChooser colorChooser = new JColorChooser();
        Color theColor = JColorChooser.showDialog(this, "Choose Color", Color.BLACK);
        colorChooser.setLocation(colorChooserIndent, colorChooserIndent); // Set
        // location
        // of
        // color
        // chooser

        return theColor;
    }
}
