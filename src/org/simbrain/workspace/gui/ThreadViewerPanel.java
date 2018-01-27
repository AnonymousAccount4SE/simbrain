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
package org.simbrain.workspace.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.simbrain.util.StandardDialog;
import org.simbrain.workspace.Workspace;
import org.simbrain.workspace.WorkspaceComponent;
import org.simbrain.workspace.updater.ComponentUpdateListener;
import org.simbrain.workspace.updater.WorkspaceUpdaterListener;

/**
 * Display updater and thread information.
 *
 * @author jyoshimi
 *
 */
public class ThreadViewerPanel extends JPanel {

    /** Thread viewer panel. */
    private JPanel threadViewer = new JPanel(new BorderLayout());

    /** List. */
    private JList list = new JList();

    /** List model. */
    private ThreadListModel<ListItem> listModel = new ThreadListModel<ListItem>();

    /** Reference to parent workspace. */
    private Workspace workspace;

    /** Number of update threads. */
    private JTextField updaterNumThreads = new JTextField();

    /**
     * Constructor for viewer panel.
     *
     * @param workspace reference to parent workspace.
     */
    public ThreadViewerPanel(final Workspace workspace) {

        super(new BorderLayout());
        this.workspace = workspace;

        // Set up thread viewer
        updateList();
        JScrollPane scrollPane = new JScrollPane(list);
        threadViewer.add(scrollPane);

        JButton showUpdateManager = new JButton("Update Manager");
        showUpdateManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                StandardDialog dialog = new StandardDialog();
                dialog.setContentPane(new WorkspaceUpdateManagerPanel(
                        workspace, dialog));
                dialog.setModal(true);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        JToolBar toolBar = new JToolBar();
        toolBar.add(showUpdateManager);

        toolBar.addSeparator();
        toolBar.add(new JLabel("Number of Threads: "));
        updaterNumThreads.setMaximumSize(new Dimension(100, 100));
        toolBar.add(updaterNumThreads);
        JButton setThreadsButton = new JButton("Set");
        setThreadsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                workspace.getUpdater().setNumThreads(
                        Integer.parseInt(updaterNumThreads.getText()));
            }

        });
        toolBar.add(setThreadsButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Number of Processors: "
                + Runtime.getRuntime().availableProcessors()));
        updateStats();

        // Add main components to panel
        this.add("North", toolBar);
        this.add("Center", threadViewer);

        // Add updater component listener
        workspace.getUpdater().addComponentListener(
                new ComponentUpdateListener() {
                    public void finishedComponentUpdate(WorkspaceComponent component, int update, int thread) {
                        String text = String.format("Thread %s: Finished updating %s", thread, component.getName());
                        listModel.getElementAt(thread - 1).setText(text);
                        threadViewer.repaint();
                    }

                    public void startingComponentUpdate(WorkspaceComponent component, int update, int thread) {
                        String text = String.format("Thread %s: Started updating %s", thread, component.getName());
                        listModel.getElementAt(thread - 1).setText(text);
                        threadViewer.repaint();
                    }
                });

        // Add updater listener
        workspace.getUpdater().addUpdaterListener(
            new WorkspaceUpdaterListener() {

                public void updatedCouplings(int update) {
                    String text = String.format("Thread %s: Updated couplings", update);
                    listModel.getElementAt(update - 1).setText(text);
                    threadViewer.repaint();
                }

                public void changedUpdateController() {
                    updateStats();
                }

                public void changeNumThreads() {
                    updateList();
                }

                // TODO: Should be some useful graphic thing to do when update begins and ends...
                public void updatingStarted() {
                }

                public void updatingFinished() {
                }

                public void workspaceUpdated() {
                }

            });
    }

    /**
     * Update thread viewer list.
     */
    private void updateList() {
        listModel = new ThreadListModel<ListItem>();
        for (int i = 1; i <= workspace.getUpdater().getNumThreads(); i++) {
            ListItem label = new ListItem("Thread " + i);
            listModel.add(label);
        }
        list.setModel(listModel);
        updaterNumThreads.setText("" + workspace.getUpdater().getNumThreads());
    }

    /**
     * Update various labels and components reflecting update stats.
     */
    private void updateStats() {
        updaterNumThreads.setText("" + workspace.getUpdater().getNumThreads());
    }

    /**
     * Simple holder for list items, to display thread state.
     */
    private class ListItem {

        /** Item text. */
        String text;

        public ListItem(String arg) {
            this.text = arg;
        }

        public String toString() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * Simple List Model.
     *
     * @param <ListItem>
     */
    private class ThreadListModel<ListItem> extends AbstractListModel {

        /* List items. */
        ArrayList<ListItem> list = new ArrayList<ListItem>();

        public void add(ListItem item) {
            list.add(item);
        }

        public ListItem getElementAt(int index) {
            return list.get(index);
        }

        public int getSize() {
            return list.size();
        }

    }

}
