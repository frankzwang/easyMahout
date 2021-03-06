package easyMahout.GUI.classification;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import easyMahout.GUI.MainGUI;
import easyMahout.GUI.recommender.MainRecommenderPanel;
import easyMahout.recommender.RecommenderXMLPreferences;
import easyMahout.utils.Constants;
import easyMahout.utils.DisabledNode;
import easyMahout.utils.DisabledRenderer;
import easyMahout.utils.DynamicTree;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JSeparator;

public class MainClassifierPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final static Logger log = Logger.getLogger(MainClassifierPanel.class);

	private JPanel panelClassifier;

	private JPanel treePanel; 

	private JPanel p1, p2, p3, p4;
	
	private static boolean configurationModified;

	private boolean controlModified;

	private String activeConfigutation;
	
	private static String fileName;

	private static JTree treeMenu;	

	private static DisabledNode nodeRoot;	
	
	private ArrayList<DisabledNode> treeNodes;

	private DisabledNode nodeConfigure;

	private DisabledNode nodeTraining;

	private DisabledNode nodeAlgorithm;

	private DisabledNode nodeEvaluator;

	private DisabledNode nodeSaves;

	private DisabledNode nodeSelected;

	public MainClassifierPanel(){
		
		this.setLayout(null);
		
		panelClassifier = this;
		
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

		p1 = new DataModelClassifierPanel();
		p1.setBounds(238, 11, 481, 382);
		panelClassifier.add(p1);
		p1.setLayout(null);
		p1.setVisible(false);

		p2 = new TrainingDataClassificationPanel();
		p2.setBounds(238, 11, 481, 382);
		panelClassifier.add(p2);
		p2.setLayout(null);
		p2.setVisible(false);

		p3 = new AlgorithmClassifierPanel();
		p3.setBounds(238, 11, 481, 382);
		panelClassifier.add(p3);
		p3.setLayout(null);
		p3.setVisible(false);

		p4 = new EvaluatorClassifierPanel();
		p4.setBounds(238, 11, 481, 382);
		panelClassifier.add(p4);
		p4.setLayout(null);
		p4.setVisible(false);
	}

	public JPanel getTreePanel() {
		return treePanel;
	}

	public void setTreePanel(JPanel treePanel) {
		this.treePanel = treePanel;
	}

	public DisabledNode[] populateTree() {

		String[] categories = { "Root", // 0
				"Configure", // 1
				"Data Model", // 2
				"Training Data", // 3
				"Algorithm", // 4
				"Evaluator", // 5
				"Saves" //6
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
		treeNodes.get(0).add(treeNodes.get(6));
		
		nodeConfigure = treeNodes.get(1);
		nodeTraining = treeNodes.get(3);
		nodeAlgorithm = treeNodes.get(4);
		nodeEvaluator = treeNodes.get(5);
		nodeSaves = treeNodes.get(6);		
		
		ArrayList<DisabledNode> savesNodes = getSavesFiles();
		if (savesNodes != null) {
			treeNodes.addAll(savesNodes);
			for (int i = categories.length; i < treeNodes.size(); i++) {
				nodeSaves.add(treeNodes.get(i));
			}
		}
		
		return treeNodes.toArray(new DisabledNode[treeNodes.size()]);
	}

///////////////////	
//MOUSE CLICK EVENT
///////////////////
	
	void doMouseClicked(MouseEvent me) {
		//Left click
		if (me.getButton() == MouseEvent.BUTTON1) {
			try {				
				nodeSelected = (DisabledNode) treeMenu.getPathForLocation(me.getX(), me.getY()).getLastPathComponent();
				
				if (nodeSelected != null) {
					if (nodeSelected.equals(nodeConfigure)) {
						log.info("configureNode");
					} else if (nodeConfigure.isNodeChild(nodeSelected)) {
						log.info("classifier configure children B1");

						String category = (String) nodeSelected.getUserObject();
						
						if (category.equals("Data Model")) {
							log.info("dataB1");
							p1.setVisible(true);
							p2.setVisible(false);
							p3.setVisible(false);
							p4.setVisible(false);

						} else if (category.equals("Training Data")) {
							log.info("trainingDataB1");
							p1.setVisible(false);
							p2.setVisible(true);
							p3.setVisible(false);
							p4.setVisible(false);

						} else if (category.equals("Algorithm")) {
							log.info("algorithmB1");
							p1.setVisible(false);
							p2.setVisible(false);
							p3.setVisible(true);
							p4.setVisible(false);

						} else if (category.equals("Evaluator")) {
							log.info("evalB1");
							p1.setVisible(false);
							p2.setVisible(false);
							p3.setVisible(false);
							p4.setVisible(true);
						}
					}
				}
			} catch (Exception e1) {
				// The place where we clicked is not a tree node, do nothing.
			}
		}
		//Right click
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
											"The actual Classifier configuration is not saved, would you like to save it?",
											"Save preferences",
											JOptionPane.YES_NO_CANCEL_OPTION);
									if (dialogResult == JOptionPane.YES_OPTION) {
										if (StringUtils.isBlank(activeConfigutation)) {
											// TODO poner carpeta saves por
											// defecto en el chooser
											JFileChooser selectedFile = new JFileChooser();
											int i = selectedFile.showOpenDialog(MainClassifierPanel.this);
											if (i == JFileChooser.APPROVE_OPTION) {
												File prefs = selectedFile.getSelectedFile();
												String absPath = prefs.getAbsolutePath();
												RecommenderXMLPreferences.saveXMLFile(absPath);
												MainGUI.writeResult("Preferences file saved as: " + prefs.getName(), Constants.Log.INFO);
											} else if (i == JFileChooser.ERROR_OPTION) {
												MainGUI.writeResult("Error saving the file", Constants.Log.ERROR);
												log.error("Error saving preferences file");
											}
											// y nuevo
											addPreferencesFile();
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
								treeMenu.expandRow(8);
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
								String filePath = ((DisabledNode) nodeSelected).getPathFile();
								RecommenderXMLPreferences.loadXMLFile(filePath);
								activeConfigutation = filePath;
								configurationModified = false;
								controlModified = true;
								MainGUI.setSaveItemEnabled(true);
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
										treeMenu.expandRow(8);

										if (filePath.equals(activeConfigutation)) {
											activeConfigutation = "";
											MainGUI.setMainTitle(activeConfigutation);
											MainGUI.setSaveItemEnabled(false);
											configurationModified = false;
											controlModified = false;
										}
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
	
	private ArrayList<DisabledNode> getSavesFiles() {

		File dir = new File(Constants.SavesPaths.CLASSIFIER);

		if (dir.exists()) {
			File[] files = dir.listFiles();
			ArrayList<DisabledNode> nodes = new ArrayList<DisabledNode>();
			for (int i = 0; i < files.length; i++) {
				nodes.add(new DisabledNode(RecommenderXMLPreferences.getTagName(files[i].getPath()), files[i].getPath()));
				System.out.println(files[i].getName());

			}
			return nodes;

		} else {
			log.error("Preferences folder (" + Constants.SavesPaths.CLASSIFIER + ") doesnt exist.");
			return null;
		}

	}

	private void addPreferencesFile() {
		fileName = JOptionPane.showInputDialog(null, "Write a new preferences file name?", "Enter a name", JOptionPane.QUESTION_MESSAGE);
	
		if (fileName != null && !fileName.isEmpty()) {
	
			File directory = new File(Constants.SavesPaths.RECOMMENDER);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			String filePath = Constants.SavesPaths.RECOMMENDER + fileName + Constants.SavesPaths.EXTENSION;
	
			activeConfigutation = filePath;
			configurationModified = false;
			controlModified = true;
	
			treeNodes.add(new DisabledNode(fileName, filePath));
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
			treeMenu.expandRow(9);
	
			MainGUI.setMainTitle(activeConfigutation);
			MainGUI.setSaveItemEnabled(true);
			MainGUI.writeResult("Preferences file added: " + fileName, Constants.Log.INFO);
		}
	}
	
	public boolean isConfigurationModified() {
		return configurationModified;
	}

	public boolean isControlModified() {
		return controlModified;
	}

	public static void setConfigurationModified(boolean configurationModified) {
		MainClassifierPanel.configurationModified = configurationModified;
	}

	public String getActiveConfigutation() {
		return activeConfigutation;
	}

	public void setActiveConfigutation(String activeConfigutation) {
		this.activeConfigutation = activeConfigutation;
	}

	public static String getFileName() {
		return fileName;
	}
}
