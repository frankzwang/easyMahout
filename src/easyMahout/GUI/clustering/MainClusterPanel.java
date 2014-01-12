package easyMahout.GUI.clustering;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.apache.log4j.Logger;
import easyMahout.utils.DisabledNode;
import easyMahout.utils.DisabledRenderer;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

public class MainClusterPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelCluster;

	private JPanel treePanel;
	
	private static ConfigureClusterPanel configureClusterPanel;

	private static AlgorithmClusterPanel algorithmClusterPanel;

	private static DistanceMeasurePanel distanceClusterPanel;

	private static NumberClusterPanel numberClusterPanel;

	private static NumberIterationsPanel numberIterationsPanel;

	private static DataModelClusterPanel dataModelClusterPanel;

	private static boolean canopy;

	private static JTree treeMenu;

	private final static Logger log = Logger.getLogger(MainClusterPanel.class);

	private JButton btnRun;

	private JButton btnEvaluate;

	public MainClusterPanel() {
		panelCluster = this;

		canopy = true;

		treeMenu = new JTree(populateTree()[0]);
		DisabledRenderer renderer = new DisabledRenderer();
		treeMenu.setCellRenderer(renderer);

		treeMenu.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				doMouseClicked(me);
			}
		});

		treePanel = new JPanel();
		treePanel.setBorder(null);
		treePanel.setBounds(20, 11, 202, 410);

		treeMenu.setBounds(0, 0, 202, 410);
		treeMenu.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Options", TitledBorder.CENTER, TitledBorder.TOP,
				null, null));
		treeMenu.setRootVisible(false);
		treeMenu.setShowsRootHandles(true);
		treeMenu.expandRow(0);
		setLayout(null);

		treePanel.setLayout(null);
		treePanel.add(treeMenu);
		this.add(treePanel);

		// Create different panes
		configureClusterPanel = new ConfigureClusterPanel();
		configureClusterPanel.setBounds(228, 11, 481, 410);
		panelCluster.add(configureClusterPanel);
		configureClusterPanel.setLayout(null);
		configureClusterPanel.setVisible(true);
		
		algorithmClusterPanel = new AlgorithmClusterPanel();
		algorithmClusterPanel.setBounds(228, 11, 481, 410);
		panelCluster.add(algorithmClusterPanel);
		algorithmClusterPanel.setLayout(null);
		algorithmClusterPanel.setVisible(false);

		distanceClusterPanel = new DistanceMeasurePanel();
		distanceClusterPanel.setBounds(228, 11, 481, 410);
		panelCluster.add(distanceClusterPanel);
		distanceClusterPanel.setLayout(null);
		distanceClusterPanel.setVisible(false);

		numberClusterPanel = new NumberClusterPanel();
		numberClusterPanel.setBounds(228, 11, 481, 410);
		panelCluster.add(numberClusterPanel);
		numberClusterPanel.setLayout(null);
		numberClusterPanel.setVisible(false);

		numberIterationsPanel = new NumberIterationsPanel();
		numberIterationsPanel.setBounds(228, 11, 481, 410);
		panelCluster.add(numberIterationsPanel);
		numberIterationsPanel.setLayout(null);
		numberIterationsPanel.setVisible(false);

		dataModelClusterPanel = new DataModelClusterPanel();
		dataModelClusterPanel.setBounds(228, 11, 481, 410);
		panelCluster.add(dataModelClusterPanel);
		dataModelClusterPanel.setLayout(null);
		dataModelClusterPanel.setVisible(false);
	}

	public DisabledNode[] populateTree() {

		String[] strs = { "Root", // 0
				"Configure", // 1
				"Algorithm", // 2
				"Distance Measure", // 3
				"Number of Clusters", // 4
				"Number of Iterations",// 5
				"Data Model"// 6

		};

		DisabledNode[] nodes = new DisabledNode[strs.length];
		for (int i = 0; i < strs.length; i++) {
			nodes[i] = new DisabledNode(strs[i]);
		}
		nodes[0].add(nodes[1]);
		nodes[1].add(nodes[2]);
		nodes[1].add(nodes[3]);
		nodes[1].add(nodes[4]);
		nodes[1].add(nodes[5]);
		nodes[1].add(nodes[6]);
		/*
		 * nodes[1].add(nodes[7]); nodes[0].add(nodes[8]);
		 * nodes[8].add(nodes[9]);
		 */
		// nodeNeighborhood = nodes[5];

		return nodes;
	}

	public void doMouseClicked(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeMenu.getModel().getRoot();
			DefaultMutableTreeNode cluster = (DefaultMutableTreeNode) root.getFirstChild();
			try {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMenu.getPathForLocation(me.getX(), me.getY())
						.getLastPathComponent();
				if (node != null) {
					if (node.equals(root) || node.equals(cluster)) {
						log.info("root or cluster node B1");
						configureClusterPanel.setVisible(true);
						algorithmClusterPanel.setVisible(false);
						distanceClusterPanel.setVisible(false);
						numberClusterPanel.setVisible(false);
						numberIterationsPanel.setVisible(false);
						dataModelClusterPanel.setVisible(false);

					} else if (cluster.isNodeChild(node))  {
						log.info("cluster children B1");
						String category = (String) node.getUserObject();
					
						if (category.equals("Configure")) {
							log.info("configureB1");
							configureClusterPanel.setVisible(true);
							algorithmClusterPanel.setVisible(false);
							distanceClusterPanel.setVisible(false);
							numberClusterPanel.setVisible(false);
							numberIterationsPanel.setVisible(false);
							dataModelClusterPanel.setVisible(false);

						} else if (category.equals("Algorithm")) {
							log.info("algorithmB1");
							configureClusterPanel.setVisible(false);
							algorithmClusterPanel.setVisible(true);
							distanceClusterPanel.setVisible(false);
							numberClusterPanel.setVisible(false);
							numberIterationsPanel.setVisible(false);
							dataModelClusterPanel.setVisible(false);

						} else if (category.equals("Distance Measure")) {
							log.info("distanceB1");
							configureClusterPanel.setVisible(false);
							algorithmClusterPanel.setVisible(false);
							distanceClusterPanel.setVisible(true);
							numberClusterPanel.setVisible(false);
							numberIterationsPanel.setVisible(false);
							dataModelClusterPanel.setVisible(false);

						} else if (category.equals("Number of Clusters")) {
							log.info("numberOfClustersB1");
							configureClusterPanel.setVisible(false);
							algorithmClusterPanel.setVisible(false);
							distanceClusterPanel.setVisible(false);
							numberClusterPanel.setVisible(true);
							numberIterationsPanel.setVisible(false);
							dataModelClusterPanel.setVisible(false);

						} else if (category.equals("Number of Iterations")) {
							log.info("numberOfIterationsB1");
							configureClusterPanel.setVisible(false);
							algorithmClusterPanel.setVisible(false);
							distanceClusterPanel.setVisible(false);
							numberClusterPanel.setVisible(false);
							numberIterationsPanel.setVisible(true);
							dataModelClusterPanel.setVisible(false);

						} else if (category.equals("Data Model")) {
							log.info("numberOfIterationsB1");
							configureClusterPanel.setVisible(false);
							algorithmClusterPanel.setVisible(false);
							distanceClusterPanel.setVisible(false);
							numberClusterPanel.setVisible(false);
							numberIterationsPanel.setVisible(false);
							dataModelClusterPanel.setVisible(true);

						}

					}
				}
			} catch (Exception e1) {

			}

		}
		if (me.getButton() == MouseEvent.BUTTON3) {

			DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeMenu.getModel().getRoot();
			try {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMenu.getPathForLocation(me.getX(), me.getY())
						.getLastPathComponent();
				if (node != null) {
					if (node.equals(root)) {
						log.info("root");

					} else if (root.isNodeChild(node)) {
						log.info("cats");

						JPopupMenu popupMenuRoot = new JPopupMenu();

						JMenuItem _new = new JMenuItem("New");
						_new.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String category = (String) node.getUserObject();
								// TODO: iniciar panel para cada tipo;
								if (category.equals("Algorithm")) {
									log.info("Algorithm");

								}
							}
						});

					}
				}
			} catch (Exception e1) {
			}// try
		}// if

	}// doMouseClicked

}