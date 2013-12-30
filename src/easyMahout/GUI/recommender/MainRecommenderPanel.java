package easyMahout.GUI.recommender;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import easyMahout.GUI.MainGUI;
import easyMahout.recommender.RecommenderXMLPreferences;
import easyMahout.utils.Constants;
import easyMahout.utils.DisabledNode;
import easyMahout.utils.DisabledRenderer;

import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainRecommenderPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelRecommender;

	private JPanel treePanel;

	private static ConfigureRecommenderPanel configPanel;

	private static TypeRecommenderPanel typePanel;

	private static DataModelRecommenderPanel dataModelPanel;

	private static SimilarityRecommenderPanel similarityPanel;

	private static NeighborhoodRecommenderPanel neighborhoodPanel;

	private QueriesRecommenderPanel queriesPanel;

	private EvaluatorRecommenderPanel evaluatorPanel;

	private JobRecommenderPanel jobPanel;

	private static JTree treeMenu;

	private final static Logger log = Logger.getLogger(MainRecommenderPanel.class);

	private static boolean itembased;

	private ArrayList<DisabledNode> treeNodes;

	private static DisabledNode nodeNeighborhood;

	private static DisabledNode nodeEvaluator;

	private static DisabledNode nodeQueries;

	private static DisabledNode nodeSaves;

	private static DisabledNode nodeRoot;

	private static DisabledNode nodeConfigure;

	private static DisabledNode nodeSelected;

	private boolean configurationModified;

	private boolean configurationNew;

	private String activeConfigutation;

	private Document actualDoc;

	private DisabledNode nodeJob;

	public MainRecommenderPanel() {

		this.setLayout(null);

		panelRecommender = this;

		itembased = false;
		configurationModified = false;
		configurationNew = false;
		actualDoc = RecommenderXMLPreferences.createDefaultXMLDoc();

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

		treePanel.setLayout(null);
		treePanel.add(treeMenu);

		nodeRoot = (DisabledNode) treeMenu.getModel().getRoot();

		this.add(treePanel);

		// Create different panes

		configPanel = new ConfigureRecommenderPanel();
		configPanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(configPanel);
		configPanel.setLayout(null);
		configPanel.setVisible(true);

		typePanel = new TypeRecommenderPanel();
		typePanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(typePanel);
		typePanel.setLayout(null);
		typePanel.setVisible(false);

		dataModelPanel = new DataModelRecommenderPanel();
		dataModelPanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(dataModelPanel);
		dataModelPanel.setLayout(null);
		dataModelPanel.setVisible(false);

		similarityPanel = new SimilarityRecommenderPanel();
		similarityPanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(similarityPanel);
		similarityPanel.setLayout(null);
		similarityPanel.setVisible(false);

		neighborhoodPanel = new NeighborhoodRecommenderPanel();
		neighborhoodPanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(neighborhoodPanel);
		neighborhoodPanel.setLayout(null);
		neighborhoodPanel.setVisible(false);

		evaluatorPanel = new EvaluatorRecommenderPanel();
		evaluatorPanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(evaluatorPanel);
		evaluatorPanel.setLayout(null);
		evaluatorPanel.setVisible(false);

		queriesPanel = new QueriesRecommenderPanel();
		queriesPanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(queriesPanel);
		queriesPanel.setLayout(null);
		queriesPanel.setVisible(false);

		jobPanel = new JobRecommenderPanel();
		jobPanel.setBounds(228, 11, 481, 410);
		panelRecommender.add(jobPanel);
		jobPanel.setLayout(null);
		jobPanel.setVisible(false);

	}

	public JPanel getTreePanel() {
		return treePanel;
	}

	public void setTreePanel(JPanel treePanel) {
		this.treePanel = treePanel;
	}

	public static Recommender buildRecommender() {
		if (typePanel.getSelectedType().equals(Constants.RecommType.USERBASED)) {
			DataModel model = dataModelPanel.getDataModel();
			if (model != null) {
				UserSimilarity similarity = similarityPanel.getUserSimilarity(model);
				UserNeighborhood neighborhood = neighborhoodPanel.getNeighborhood(similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			} else {
				log.error("Trying to run a recommender without datamodel loaded");
				MainGUI.writeResult("Trying to run a recommender without a dataModel loaded", Constants.Log.ERROR);
				return null;
			}

		} else if (typePanel.getSelectedType().equals(Constants.RecommType.ITEMBASED)) {
			DataModel model = dataModelPanel.getDataModel();
			if (model != null) {
				ItemSimilarity similarity = similarityPanel.getItemSimilarity(model);
				return new GenericItemBasedRecommender(model, similarity);
			} else {
				log.error("Trying to run a recommender without datamodel loaded");
				MainGUI.writeResult("Trying to run a recommender without a dataModel loaded", Constants.Log.ERROR);
				return null;
			}

		} else
			// mas posibles tipos de recomm
			return null;
	}

	public static String[] buildRecommenderJob() {
		if (typePanel.getSelectedType().equals(Constants.RecommType.ITEMBASED_DISTRIBUTED)) {
			return new String[] { 
					"--input", dataModelPanel.getInputPath().toString(), 
					"--output",	dataModelPanel.getOutputPath().toString(),
					"--similarityClassname", similarityPanel.getDistributedSimilarity(),
					"--maxSimilaritiesPerItem", similarityPanel.getMaxSimilarities(),
					"--maxPrefsPerUser", similarityPanel.getMaxPreferences(),
					"--minPrefsPerUser", similarityPanel.getMinPreferences(),
					"--booleanData", dataModelPanel.getBooleanPrefs(),
					"--threshold", similarityPanel.getThreshold() };
		} else if (typePanel.getSelectedType().equals(Constants.RecommType.ITEMSIMILARITY)) {
			// TODO
			return null;
		} else {
			// if(Constants.RecommType.FACTORIZED_RECOMMENDER)
			// TODO
			return null;
		}

	}

	// private RecommenderBuilder buildRecommender() {
	// if (typePanel.getSelectedType().equals(Constants.RecommType.USERBASED)) {
	// // DataModel model = dataModelPanel.getDataModel();
	//
	// RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
	// public Recommender buildRecommender(DataModel model) throws
	// TasteException {
	// UserSimilarity similarity = similarityPanel.getUserSimilarity(model);
	// UserNeighborhood neighborhood =
	// neighborhoodPanel.getNeighborhood(similarity, model);
	// return new GenericUserBasedRecommender(model, neighborhood, similarity);
	// }
	// };
	//
	// return recommenderBuilder;
	//
	// } else if
	// (typePanel.getSelectedType().equals(Constants.RecommType.USERBASED)) {
	// // DataModel model = dataModelPanel.getDataModel();
	// // final ItemSimilarity similarity = getItemSimilarity(model);
	// // //final UserNeighborhood neighborhood =
	// // neighborhoodPanel.getNeighborhood(similarity, model);
	// //
	// // RecommenderBuilder recommenderBuilder = new RecommenderBuilder()
	// // {
	// // public Recommender buildRecommender(DataModel model) throws
	// // TasteException {
	// // return new GenericItemBasedRecommender(model,similarity);
	// // }
	// // };
	// return null;
	// }// mas posibles tipos de recomm
	// else
	// return null;
	//
	// }

	private DisabledNode[] populateTree() {

		String[] categories = { "Root", // 0
				"Configure", // 1
				"Type", // 2
				"Data Model", // 3
				"Similarity", // 4
				"Neighborhood", // 5
				"Evaluator", // 6
				"Queries", // 7
				"Hadoop Job", // 8
				"Saves", // 9
		};

		treeNodes = new ArrayList<DisabledNode>();
		for (int i = 0; i < categories.length; i++) {
			treeNodes.add(new DisabledNode(categories[i]));
		}

		treeNodes.get(0).add(treeNodes.get(1));
		treeNodes.get(1).add(treeNodes.get(2));
		treeNodes.get(1).add(treeNodes.get(3));
		treeNodes.get(1).add(treeNodes.get(4));
		treeNodes.get(1).add(treeNodes.get(5));
		treeNodes.get(1).add(treeNodes.get(6));
		treeNodes.get(1).add(treeNodes.get(7));
		treeNodes.get(1).add(treeNodes.get(8));
		treeNodes.get(0).add(treeNodes.get(9));

		nodeNeighborhood = treeNodes.get(5);
		nodeQueries = treeNodes.get(7);
		nodeEvaluator = treeNodes.get(6);
		nodeJob = treeNodes.get(8);
		nodeJob.setEnabled(false);
		nodeSaves = treeNodes.get(9);
		nodeConfigure = treeNodes.get(1);

		ArrayList<DisabledNode> savesNodes = getSavesFiles();
		if (savesNodes != null) {
			treeNodes.addAll(savesNodes);
			for (int i = categories.length; i < treeNodes.size(); i++) {
				nodeSaves.add(treeNodes.get(i));
			}
		}

		return treeNodes.toArray(new DisabledNode[treeNodes.size()]);
	}

	// Mouse Events in JTree nodes
	void doMouseClicked(MouseEvent me) {
		// Left button
		if (me.getButton() == MouseEvent.BUTTON1) {
			try {

				nodeSelected = (DisabledNode) treeMenu.getPathForLocation(me.getX(), me.getY()).getLastPathComponent();

				if (nodeSelected != null) {
					if (nodeSelected.equals(nodeConfigure)) {
						log.info("configureNode");
						configPanel.setVisible(true);
						typePanel.setVisible(false);
						dataModelPanel.setVisible(false);
						similarityPanel.setVisible(false);
						neighborhoodPanel.setVisible(false);
						evaluatorPanel.setVisible(false);
						queriesPanel.setVisible(false);
						disableHelpTips();
					} else if (nodeConfigure.isNodeChild(nodeSelected)) {
						log.info("recomender configure  children B1");

						String category = (String) nodeSelected.getUserObject();
						if (category.equals("Type")) {
							log.info("typeB1");
							configPanel.setVisible(false);
							typePanel.setVisible(true);
							dataModelPanel.setVisible(false);
							similarityPanel.setVisible(false);
							neighborhoodPanel.setVisible(false);
							evaluatorPanel.setVisible(false);
							queriesPanel.setVisible(false);
							jobPanel.setVisible(false);
							disableHelpTips();

						} else if (category.equals("Data Model")) {
							log.info("dataB1");
							configPanel.setVisible(false);
							typePanel.setVisible(false);
							dataModelPanel.setVisible(true);
							similarityPanel.setVisible(false);
							neighborhoodPanel.setVisible(false);
							evaluatorPanel.setVisible(false);
							queriesPanel.setVisible(false);
							jobPanel.setVisible(false);
							disableHelpTips();

						} else if (category.equals("Similarity")) {
							log.info("similarityB1");
							configPanel.setVisible(false);
							typePanel.setVisible(false);
							dataModelPanel.setVisible(false);
							similarityPanel.setVisible(true);
							neighborhoodPanel.setVisible(false);
							evaluatorPanel.setVisible(false);
							queriesPanel.setVisible(false);
							jobPanel.setVisible(false);
							disableHelpTips();

						} else if (category.equals("Neighborhood")) {
							log.info(MainGUI.isDistributed());
							if (!MainGUI.isDistributed() && !itembased) {
								log.info("neighB1");
								configPanel.setVisible(false);
								typePanel.setVisible(false);
								dataModelPanel.setVisible(false);
								similarityPanel.setVisible(false);
								neighborhoodPanel.setVisible(true);
								evaluatorPanel.setVisible(false);
								queriesPanel.setVisible(false);
								jobPanel.setVisible(false);
								disableHelpTips();
							}

						} else if (category.equals("Evaluator")) {
							if (!MainGUI.isDistributed()) {
								log.info("evalB1");
								configPanel.setVisible(false);
								typePanel.setVisible(false);
								dataModelPanel.setVisible(false);
								similarityPanel.setVisible(false);
								neighborhoodPanel.setVisible(false);
								evaluatorPanel.setVisible(true);
								queriesPanel.setVisible(false);
								jobPanel.setVisible(false);
								disableHelpTips();
							}

						} else if (category.equals("Queries")) {
							log.info("queriesB1");
							configPanel.setVisible(false);
							typePanel.setVisible(false);
							dataModelPanel.setVisible(false);
							similarityPanel.setVisible(false);
							neighborhoodPanel.setVisible(false);
							evaluatorPanel.setVisible(false);
							queriesPanel.setVisible(true);
							jobPanel.setVisible(false);
							disableHelpTips();
						}

						else if (category.equals("Hadoop Job")) {
							if (MainGUI.isDistributed()) {
								log.info("jobB1");
								configPanel.setVisible(false);
								typePanel.setVisible(false);
								dataModelPanel.setVisible(false);
								similarityPanel.setVisible(false);
								neighborhoodPanel.setVisible(false);
								evaluatorPanel.setVisible(false);
								queriesPanel.setVisible(false);
								jobPanel.setVisible(true);
								disableHelpTips();
							}
						}
					}
				}
			} catch (Exception e1) {
				// The place where we clicked is not a tree node, do nothing.
			}
		}
		// Right button
		if (me.getButton() == MouseEvent.BUTTON3) {
			try {

				nodeSelected = (DisabledNode) treeMenu.getPathForLocation(me.getX(), me.getY()).getLastPathComponent();

				if (nodeSelected != null) {
					if (nodeSelected.equals(nodeSaves)) {
						log.info("node: " + nodeSelected.toString());
						JPopupMenu popupMenuAdd = new JPopupMenu();
						JMenuItem addItem = new JMenuItem("Add");
						addItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (configurationModified) {
									int dialogResult = JOptionPane.showConfirmDialog(null,
											"The actual configuration is not saved, would yo like to save it?",
											"Save preferences",
											JOptionPane.YES_NO_CANCEL_OPTION);
									if (dialogResult == JOptionPane.YES_OPTION) {
										if (activeConfigutation == null) {
											// jfilechooser save as...
											log.debug("modified, default config");
										} else {
											// salvar y nuevo
											RecommenderXMLPreferences.saveXMLFile(activeConfigutation);
											// configurationNew = true;
											addPreferencesFile();
											log.debug("modified, saving added configuration");
										}
									} else if (dialogResult == JOptionPane.NO_OPTION) {
										// nuevo directamente
										// configurationNew = true;
										addPreferencesFile();
										log.debug("modified, no save");
									}
								} else {
									// a�adir directamente
									// configurationNew = true;
									addPreferencesFile();
									log.debug("no modified");
								}
							}
						});

						popupMenuAdd.add(addItem);
						this.add(popupMenuAdd);
						popupMenuAdd.show(me.getComponent(), me.getX(), me.getY());

					} else if (nodeSaves.isNodeChild(nodeSelected)) {
						log.info("node: " + nodeSelected.toString());
						JPopupMenu popupMenuSaves = new JPopupMenu();
						JMenuItem loadItem = new JMenuItem("Load");
						loadItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {

							}
						});

						JMenuItem deleteItem = new JMenuItem("Delete");
						deleteItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String name = (String) nodeSelected.getUserObject();
								String filePath = ((DisabledNode) nodeSelected).getPathFile();
								int dialogButton = JOptionPane.YES_NO_OPTION;
								int dialogResult = JOptionPane.showConfirmDialog(null,
										"Would you like to delete \"" + name + "\"?",
										"Delete File",
										dialogButton);
								if (dialogResult == JOptionPane.YES_OPTION) {
									File fichero = new File(filePath);
									if (fichero.delete()) {

										treeNodes.remove(nodeSelected);
										nodeSaves.remove(nodeSelected);

										DefaultTreeModel model = new DefaultTreeModel(nodeRoot);

										treeMenu.setModel(model);
										treeMenu.expandRow(0);
										treeMenu.expandRow(7);

										MainGUI.writeResult("Preferences file \"" + name + "\" successfully deleted.", Constants.Log.INFO);

									} else {
										MainGUI.writeResult("Preferences file \"" + name + "\" cannot be deleted.", Constants.Log.ERROR);
									}
								}
							}
						});

						popupMenuSaves.add(loadItem);
						popupMenuSaves.add(deleteItem);

						this.add(popupMenuSaves);
						popupMenuSaves.show(me.getComponent(), me.getX(), me.getY());

					}
				}
			} catch (Exception e1) {
				// The place where we clicked is not a tree node, do nothing.
			}
		}
	}

	private void disableHelpTips() {
		typePanel.getHelpTooltip().disable();
		dataModelPanel.getHelpTooltip().disable();
		similarityPanel.getHelpTooltip().disable();
		neighborhoodPanel.getHelpTooltip().disable();
		evaluatorPanel.getHelpTooltip().disable();
		queriesPanel.getHelpTooltip().disable();
		jobPanel.getHelpTooltip().disable();
	}

	public void setDistributed(boolean distributed) {
		nodeNeighborhood.setEnabled(!distributed);
		nodeEvaluator.setEnabled(!distributed);
		nodeQueries.setEnabled(!distributed);
		nodeJob.setEnabled(distributed);
		treeMenu.repaint();
		typePanel.setDistributed(distributed);
		dataModelPanel.setDistributed(distributed);
		similarityPanel.setDistributed(distributed);
		configPanel.setDistributed(distributed);
		this.setConfigPanelEnabled();
	}

	private void setConfigPanelEnabled() {
		configPanel.setVisible(true);
		typePanel.setVisible(false);
		dataModelPanel.setVisible(false);
		similarityPanel.setVisible(false);
		neighborhoodPanel.setVisible(false);
		evaluatorPanel.setVisible(false);
		queriesPanel.setVisible(false);
		jobPanel.setVisible(false);
		disableHelpTips();
	}

	private ArrayList<DisabledNode> getSavesFiles() {

		File dir = new File(Constants.SavesPaths.RECOMMENDER);

		if (dir.exists()) {
			File[] files = dir.listFiles();
			ArrayList<DisabledNode> nodes = new ArrayList<DisabledNode>();
			for (int i = 0; i < files.length; i++) {
				nodes.add(new DisabledNode(RecommenderXMLPreferences.getTagName(files[i].getPath()), files[i].getPath()));
				System.out.println(files[i].getName());

			}
			return nodes;

		} else {
			log.error("Preferences folder (" + Constants.SavesPaths.RECOMMENDER + ") doesnt exist.");
			return null;
		}

	}

	private void addPreferencesFile() {
		String name = JOptionPane.showInputDialog(null, "Write a new preferences file name?", "Enter a name", JOptionPane.QUESTION_MESSAGE);

		if (name != null && !name.isEmpty()) {

			String filePath = Constants.SavesPaths.RECOMMENDER + name + Constants.SavesPaths.EXTENSION;

			activeConfigutation = filePath;

			treeNodes.add(new DisabledNode(name, filePath));
			nodeSaves.add(treeNodes.get(treeNodes.size() - 1));
			DefaultTreeModel model = new DefaultTreeModel(nodeRoot);

			File file = new File(filePath);
			try {
				if (file.createNewFile())
					System.out.println("El fichero se ha creado correctamente");
				else
					System.out.println("No ha podido ser creado el fichero");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			treeMenu.setModel(model);
			treeMenu.expandRow(0);
			treeMenu.expandRow(7);
		}
	}

	public static TypeRecommenderPanel getTypePanel() {
		return typePanel;
	}

	public static SimilarityRecommenderPanel getSimilarityPanel() {
		return similarityPanel;
	}

	public static void setEnableNeighborhood(boolean enabled) {
		nodeNeighborhood.setEnabled(enabled);
		itembased = !enabled;
		treeMenu.repaint();
	}

	public boolean isConfigurationModified() {
		return configurationModified;
	}

	public void setConfigurationModified(boolean configurationModified) {
		this.configurationModified = configurationModified;
	}

	public boolean isConfigurationNew() {
		return configurationNew;
	}

	public void setConfigurationNew(boolean configurationNew) {
		this.configurationNew = configurationNew;
	}

	public String getActiveConfigutation() {
		return activeConfigutation;
	}

	public void setActiveConfigutation(String activeConfigutation) {
		this.activeConfigutation = activeConfigutation;
	}
}