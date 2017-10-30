/*
 * Part of Simbrain--a java-based neural network kit Copyright (C) 2005,2007 The
 * Authors. See http://www.simbrain.net/credits This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You
 * should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 59 Temple Place
 * - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.simbrain.util.widgets;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A widget that contains an apply button, for cases when it should be possible
 * to immediately apply changes in an editing panel (as opposed to waiting to
 * press ok in the parent dialog). Implements {@link EditablePanel}, since the
 * apply can fail.
 *
 * @author Zoë Tosi
 */
@SuppressWarnings("serial")
public class ApplyPanel extends JPanel implements EditablePanel {

    /**
     * The committable panel which is being wrapped around, and to which changes
     * will be committed when the apply button is pressed.
     */
    private final EditablePanel mainPanel;

    /** The button used to apply changes. */
    private final JButton applyButton = new JButton("Apply");

    /** Layout manager for the panel. */
    private final GridBagLayout layoutManager = new GridBagLayout();

    /** Constraints. */
    private final GridBagConstraints gbc = new GridBagConstraints();

    {
        setLayout(layoutManager);
    }

    /**
     * A factory method to create an apply panel.
     *
     * @param mainPanel the panel to wrap
     * @return the wrapped apply panel
     */
    public static ApplyPanel createApplyPanel(EditablePanel mainPanel) {
        final ApplyPanel ap = new ApplyPanel(mainPanel);
        ap.applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ap.commitChanges();
            }
        });
        return ap;
    }

    /**
     * Constructs the apply panel. Requires an editable panel to wrap around.
     *
     * @param mainPanel the editable panel to which changes may be applied
     */
    private ApplyPanel(EditablePanel mainPanel) {
        this.mainPanel = mainPanel;
        masterLayout();
    }

    /**
     * Lay out the panel. It is assumed that the editable main panel has
     * already been laid out.
     */
    protected void masterLayout() {
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        int jbHeight = applyButton.getPreferredSize().height;
        int jbWidth = applyButton.getPreferredSize().width;
        int mpHeight = mainPanel.getPanel().getPreferredSize().height;
        int mpWidth = mainPanel.getPanel().getPreferredSize().width;

        gbc.gridheight = (int) Math.ceil((double) mpHeight / jbHeight);
        gbc.gridwidth = (int) Math.ceil((double) mpWidth / jbWidth);

        this.add(mainPanel.getPanel(), gbc);

        gbc.gridy += gbc.gridheight;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(Box.createVerticalGlue(), gbc);

        gbc.gridx += gbc.gridwidth - 1;
        gbc.gridy += 1;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        applyButton.setVisible(true);

        this.add(applyButton, gbc);
    }

    /**
     * {@inheritDoc} Specifically: commits changes to the committable panel
     * {@link #mainPanel}.
     */
    @Override
    public boolean commitChanges() {
        return mainPanel.commitChanges();
    }

    /**
     * Allows outside parties to add other listeners to the apply panel's apply
     * button.
     * @param l
     */
    public void addActionListener(ActionListener l) {
        applyButton.addActionListener(l);
    }

    @Override
    public JPanel getPanel() {
        return (JPanel) mainPanel;
    }

    @Override
    public void fillFieldValues() {
    }

    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
    	applyButton.setEnabled(enabled);
    }
    
}
