package easyMahout.GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import easyMahout.GUI.classification.ClassificationJPanel;
import easyMahout.GUI.clustering.ClusterJPanel;
import easyMahout.GUI.recommender.RecommenderJPanel;
import easyMahout.recommender.RecommenderXMLPreferences;
import easyMahout.utils.Constants;

import javax.swing.JSeparator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private RecommenderJPanel recommenderTab;

	private ClusterJPanel clusterTab;

	private ClassificationJPanel classificationTab;

	private JScrollPane logScrollPane;

	private static JTextPane logTextPane;

	private static StringBuilder textBuilder;

	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(MainGUI.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			// Set System(Windows, Mac, linux) Look and fell
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		super();
		setResizable(false);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		BasicConfigurator.configure();
		PropertyConfigurator.configure("src/easyMahout/log4j.properties");

		this.setTitle("easyMahout " + Constants.EasyMahout.VERSION);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/easyMahout/GUI/images/mahoutIcon45.png")));
		this.setBounds(100, 100, 740, 690);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);

		textBuilder = new StringBuilder();

		createMenuBar();

		logScrollPane = new JScrollPane();
		logScrollPane.setBounds(2, 463, 730, 176);
		this.getContentPane().add(logScrollPane);

		// Result log textField
		logTextPane = new JTextPane();
		logScrollPane.setViewportView(logTextPane);
		logTextPane.setBackground(Color.WHITE);
		logTextPane.setBounds(42, 501, 735, 131);
		logTextPane.setEditable(false);
		logTextPane.setContentType("text/html");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(2, 0, 732, 463);
		this.getContentPane().add(tabbedPane);

		recommenderTab = new RecommenderJPanel();
		tabbedPane.addTab("Recommendation", null, recommenderTab, null);

		classificationTab = new ClassificationJPanel();
		tabbedPane.addTab("Classification", null, classificationTab, null);

		clusterTab = new ClusterJPanel();
		tabbedPane.addTab("Clustering", null, clusterTab, null);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				onClose();
			}
		});

		// RecommenderXMLPreferences.saveXMLFile();
		// RecommenderXMLPreferences.loadXMLFile();

	}

	public static void writeResult(String text, String type) {
		// TODO: poner la hora a los logs, scroll o popup para ver los
		// resultados comodamente.
		if (type.toLowerCase().equals(Constants.Log.ERROR)) {
			textBuilder.append("<font color=red>ERROR: ").append(text).append("</font><br>");
		} else if (type.toLowerCase().equals(Constants.Log.WARNING)) {
			textBuilder.append("<font color=yellow>WARNING: ").append(text).append("</font><br>");
		} else if (type.toLowerCase().equals(Constants.Log.RESULT)) {
			textBuilder.append("<font color=black>RESULT: ").append(text).append("</font><br>");
		} else if (type.toLowerCase().equals(Constants.Log.INFO)) {
			textBuilder.append("<font color=green>INFO: ").append(text).append("</font><br>");
		}

		logTextPane.setText(textBuilder.toString());

	}

	private void onClose() {
		if (recommenderTab.isConfigurationModified()) {
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"The actual configuration is not saved, would yo like to save it?",
					"Recommender preferences",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) {
				// modified configuration
				if (recommenderTab.getActiveConfigutation() == null) {
					// crear fichero (jfilechooser)
					// salvar config en fichero
					// RecommenderXMLPreferences.saveXMLFile(recommenderTab.getActiveConfigutation());
				} else {
					// salvar config en fichero
					RecommenderXMLPreferences.saveXMLFile(recommenderTab.getActiveConfigutation());
				}
			} else if (dialogResult == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mnItemExit = new JMenuItem("Exit");
		mnItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClose();
			}
		});
		mnFile.add(mnItemExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mnItemAbout = new JMenuItem("About EasyMahout");
		mnItemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				AboutUsPopupDialogBox dialogBox = new AboutUsPopupDialogBox();
			}
		});

		JMenuItem mntmHelpContents = new JMenuItem("Help Contents");
		mntmHelpContents.setIcon(new ImageIcon(MainGUI.class.getResource("/easyMahout/GUI/images/helpIcon32.png")));
		mnHelp.add(mntmHelpContents);

		JSeparator separator_1 = new JSeparator();
		mnHelp.add(separator_1);
		mnHelp.add(mnItemAbout);

	}
}
