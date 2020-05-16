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
package org.simbrain.network.gui.dialogs;

import org.simbrain.network.core.Network;
import org.simbrain.network.gui.EditMode;
import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.nodes.NeuronNode;
import org.simbrain.network.gui.nodes.NodeHandle;
import org.simbrain.network.gui.nodes.SynapseNode;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.Utils;
import org.simbrain.util.piccolo.SelectionMarquee;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <b>NetworkPropertiesPanel</b> is a panel for setting the properties of the
 * neural network, mainly the GUI. If the user presses ok, values become default
 * values. Restore defaults restores to original values. When canceling out the
 * values prior to making any changes are restored.
 */
public class NetworkPropertiesPanel extends JPanel {

    /**
     * Network panel.
     */
    protected NetworkPanel networkPanel;

    /**
     * Background.
     */
    private static final String BACKGROUND = "Background";

    /**
     * Line.
     */
    private static final String LINE = "Line";

    /**
     * Hot node.
     */
    private static final String HOTNODE = "Hot node";

    /**
     * Cool node.
     */
    private static final String COOLNODE = "Cool node";

    /**
     * Excitatory weight.
     */
    private static final String EXCITATORY = "Excitatory weight";

    /**
     * Inhibitory weight.
     */
    private static final String INHIBITORY = "Inhibitory weight";

    /**
     * Lasso.
     */
    private static final String LASSO = "Lasso";

    /**
     * Selection.
     */
    private static final String SELECTION = "Selection";

    /**
     * Spike.
     */
    private static final String SPIKE = "Spike";

    /**
     * Zero weight.
     */
    private static final String ZERO = "Zero weight";

    /**
     * List of items for combo box.
     */
    private String[] objectColorList = {BACKGROUND, HOTNODE, COOLNODE, EXCITATORY, INHIBITORY, SPIKE, ZERO};

    /**
     * Color panel displays current color of item selected in combo box.
     */
    private JPanel colorPanel = new JPanel();

    /**
     * Change color combo box.
     */
    private JComboBox cbChangeColor = new JComboBox(objectColorList);

    /**
     * Change color of the item selected in combo box.
     */
    private JButton changeColorButton = new JButton("Set");

    /**
     * Color indicator.
     */
    private JPanel colorIndicator = new JPanel();

    /**
     * Maximum size of weight slider.
     */
    private JSlider weightSizeMaxSlider = new JSlider(JSlider.HORIZONTAL, 5, 50, 10);

    /**
     * Minimum size of weight slider.
     */
    private JSlider weightSizeMinSlider = new JSlider(JSlider.HORIZONTAL, 5, 50, 10);

    /**
     * Threshold above which subnetworks or groups with that many synapses stop
     * displaying them.
     */
    private JTextField tfSynapseVisibilityThreshold = new JTextField();

    /**
     * Nudge amount text field.
     */
    private JTextField nudgeAmountField = new JTextField();

    /**
     * Network time step text field.
     */
    private JTextField timeStepField = new JTextField();

    /**
     * A field for updating how often the GUI redraws the network...
     */
    private JTextField iterUpdateField = new JTextField();

    /**
     * Wand radius.
     */
    private JTextField wandRadiusField = new JTextField();

    /**
     * Show time check box.
     */
    private JCheckBox showTimeBox = new JCheckBox();

    /**
     * This method is the default constructor.
     *
     * @param np reference to <code>NetworkPanel</code>.
     */
    public NetworkPropertiesPanel(final NetworkPanel np) {
        networkPanel = np;
        init();
    }

    /**
     * This method initializes the components on the panel.
     */
    private void init() {

        // Initial setup
        Box mainVertical = Box.createVerticalBox();
        fillFieldValues();

        // Set up sliders
        weightSizeMaxSlider.setMajorTickSpacing(25);
        weightSizeMaxSlider.setPaintTicks(true);
        weightSizeMaxSlider.setPaintLabels(true);
        weightSizeMinSlider.setMajorTickSpacing(25);
        weightSizeMinSlider.setPaintTicks(true);
        weightSizeMinSlider.setPaintLabels(true);

        // Add Action Listeners
        addActionListeners();

        // Set up color pane
        colorPanel.add(cbChangeColor);
        colorIndicator.setSize(20, 20);
        colorPanel.add(colorIndicator);
        colorPanel.add(changeColorButton);
        setIndicatorColor();

        // Set up graphics panel
        LabelledItemPanel guiPanel = new LabelledItemPanel();
        guiPanel.addItem("Color:", colorPanel);
        guiPanel.addItem("Weight size max", weightSizeMaxSlider);
        guiPanel.addItem("Weight size min", weightSizeMinSlider);
        mainVertical.add(guiPanel);

        // Separator
        mainVertical.add(new JSeparator(JSeparator.HORIZONTAL));

        // Other properties
        LabelledItemPanel miscPanel = new LabelledItemPanel();
        miscPanel.addItem("Network time step", timeStepField);
        miscPanel.addItem("GUI update frequency", iterUpdateField);
        miscPanel.addItem("Synapse visibility threshold", tfSynapseVisibilityThreshold);
        nudgeAmountField.setColumns(3);
        miscPanel.addItem("Nudge Amount", nudgeAmountField);
        miscPanel.addItem("Wand radius", wandRadiusField);

        // TODO: tooltips for all this
        mainVertical.add(miscPanel);

        // Add the main panel
        add(mainVertical);

    }

    /**
     * Set up relevant buttons to respond to actions. These actions are
     * immediately changed in the network panel (to make it easier to fine tune
     * them), and so "canceling" out of the dialog will not change these.
     */
    private void addActionListeners() {
        changeColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateColors();
            }
        });
        weightSizeMaxSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                SynapseNode.setMaxDiameter(j.getValue());
                networkPanel.filterScreenElements(SynapseNode.class).forEach(SynapseNode::updateDiameter);
            }
        });
        weightSizeMinSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                SynapseNode.setMinDiameter(j.getValue());
                networkPanel.filterScreenElements(SynapseNode.class).forEach(SynapseNode::updateDiameter);
            }
        });
        cbChangeColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setIndicatorColor();
            }
        });
    }

    /**
     * Update relevant colors based on combo box. As noted in
     * {@link #addActionListeners}, these changes are immediate and canceling
     * from the dialog will not revert them.
     */
    private void updateColors() {
        Color theColor = showColorChooser();
        if (cbChangeColor.getSelectedItem().toString().equals(BACKGROUND)) {
            if (theColor != null) {
                networkPanel.setBackgroundColor(theColor);
            }
        } else if (cbChangeColor.getSelectedItem().toString().equals(LINE)) {
            if (theColor != null) {
                SynapseNode.setLineColor(theColor);
            }
        } else if (cbChangeColor.getSelectedItem().toString().equals(HOTNODE)) {
            if (theColor != null) {
                NeuronNode.setHotColor(Utils.colorToFloat(theColor));
            }
        } else if (cbChangeColor.getSelectedItem().toString().equals(COOLNODE)) {
            if (theColor != null) {
                NeuronNode.setCoolColor(Utils.colorToFloat(theColor));
            }
        } else if (cbChangeColor.getSelectedItem().toString().equals(EXCITATORY)) {
            if (theColor != null) {
                SynapseNode.setExcitatoryColor(theColor);
            }
        } else if (cbChangeColor.getSelectedItem().toString().equals(INHIBITORY)) {
            if (theColor != null) {
                SynapseNode.setInhibitoryColor(theColor);
            }
        } else if (cbChangeColor.getSelectedItem().toString().equals(SPIKE)) {
            if (theColor != null) {
                NeuronNode.setSpikingColor(theColor);
            }
        } else if (cbChangeColor.getSelectedItem().toString().equals(ZERO)) {
            if (theColor != null) {
                SynapseNode.setZeroWeightColor(theColor);
            }
        }
        //TODO
        //networkPanel.resetColors();
        setIndicatorColor();
    }

    /**
     * Populate fields with current data.
     */
    public void fillFieldValues() {
        weightSizeMaxSlider.setValue(SynapseNode.getMaxDiameter());
        weightSizeMinSlider.setValue(SynapseNode.getMinDiameter());
        showTimeBox.setSelected(networkPanel.getShowTime());
        wandRadiusField.setText(Integer.toString(EditMode.getWandRadius()));
        timeStepField.setText(Double.toString(networkPanel.getNetwork().getTimeStep()));
        iterUpdateField.setText(Integer.toString(networkPanel.getNetwork().getUpdateFreq()));
        nudgeAmountField.setText(Double.toString(networkPanel.getNudgeAmount()));
        tfSynapseVisibilityThreshold.setText(Integer.toString(Network.getSynapseVisibilityThreshold()));
    }

    /**
     * Commits changes when ok is pressed in parent dialog. Canceling will leave
     * these unchanged.
     */
    public void commitChanges() {
        networkPanel.getNetwork().setTimeStep(Double.parseDouble(timeStepField.getText()));
        int upF = Integer.parseInt(iterUpdateField.getText());
        upF = upF < 1 ? 1 : upF;
        networkPanel.getNetwork().setUpdateFreq(upF);
        networkPanel.setNudgeAmount(Double.parseDouble(nudgeAmountField.getText()));
        Network.setSynapseVisibilityThreshold(Integer.parseInt(tfSynapseVisibilityThreshold.getText()));
        EditMode.setWandRadius(Integer.parseInt(wandRadiusField.getText()));
        if (networkPanel.getEditMode().isWand()) {
            networkPanel.getEditMode().resetWandCursor();
            // TODO
            //networkPanel.updateCursor();
        }
        networkPanel.setShowTime(showTimeBox.isSelected());
        networkPanel.repaint();

    }

    /**
     * Show the color palette and get a color.
     *
     * @return selected color
     */
    public Color showColorChooser() {
        JColorChooser colorChooser = new JColorChooser();
        Color theColor = JColorChooser.showDialog(this, "Choose Color", colorIndicator.getBackground());
        colorChooser.setLocation(200, 200); // Set location of color chooser
        return theColor;
    }

    /**
     * Set the color indicator based on the current selection in the combo box.
     */
    public void setIndicatorColor() {
        if (cbChangeColor.getSelectedItem().toString().equals(BACKGROUND)) {
            colorIndicator.setBackground(networkPanel.getBackgroundColor());
        } else if (cbChangeColor.getSelectedItem().toString().equals(LINE)) {
            colorIndicator.setBackground(SynapseNode.getLineColor());
        } else if (cbChangeColor.getSelectedItem().toString().equals(HOTNODE)) {
            colorIndicator.setBackground(Utils.floatToHue(NeuronNode.getHotColor()));
        } else if (cbChangeColor.getSelectedItem().toString().equals(COOLNODE)) {
            colorIndicator.setBackground(Utils.floatToHue(NeuronNode.getCoolColor()));
        } else if (cbChangeColor.getSelectedItem().toString().equals(EXCITATORY)) {
            colorIndicator.setBackground(SynapseNode.getExcitatoryColor());
        } else if (cbChangeColor.getSelectedItem().toString().equals(INHIBITORY)) {
            colorIndicator.setBackground(SynapseNode.getInhibitoryColor());
        } else if (cbChangeColor.getSelectedItem().toString().equals(LASSO)) {
            colorIndicator.setBackground(SelectionMarquee.getMarqueeColor());
        } else if (cbChangeColor.getSelectedItem().toString().equals(SELECTION)) {
            colorIndicator.setBackground(NodeHandle.SELECTION_STYLE.getSelectionColor());
        } else if (cbChangeColor.getSelectedItem().toString().equals(SPIKE)) {
            colorIndicator.setBackground(NeuronNode.getSpikingColor());
        } else if (cbChangeColor.getSelectedItem().toString().equals(ZERO)) {
            colorIndicator.setBackground(SynapseNode.getZeroWeightColor());
        }
    }

}
