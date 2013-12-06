package easyMahout.GUI.recommender;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.border.TitledBorder;
import javax.swing.JButton;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import easyMahout.GUI.MainGUI;
import easyMahout.utils.Constants;

public class QueriesRecommenderPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JScrollPane scrollPane;

	private JTable table;

	private JButton btnAdd;

	private JButton btnDelete;

	private JButton btnRun;

	private DefaultTableModel tableModel;

	public QueriesRecommenderPanel() {
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Add Queries", TitledBorder.CENTER, TitledBorder.TOP, null,
				null));
		setForeground(Color.BLACK);
		setLayout(null);
		setBounds(228, 11, 480, 408);

		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		scrollPane.setBounds(38, 36, 409, 313);

		table = new JTable();
		table.setRowSelectionAllowed(false);

		table.setModel(new DefaultTableModel(new Object[][] { { false, null, null }, }, new String[] { "Select", "User ID",
				"How many recommendations" }) {

			private static final long serialVersionUID = 1L;

			Class[] columnTypes = new Class[] { Boolean.class, Long.class, Integer.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setMaxWidth(150);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setMaxWidth(1000);

		tableModel = (DefaultTableModel) table.getModel();

		scrollPane.setViewportView(table);
		add(scrollPane);

		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.addRow(new Object[] { false, null, null });
			}
		});
		btnAdd.setBounds(173, 364, 89, 23);
		add(btnAdd);

		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// cuando se ejecuta el recomm con la celda recien modificada, no actualiza el valor
				ArrayList<ArrayList<RecommendedItem>> recommendations = new ArrayList<>();
				try {
					Recommender recomm = RecommenderJPanel.buildRecommender();
					if (recomm != null) {

						int i = 0;						
						while (i < tableModel.getRowCount()) {
							if ((boolean) tableModel.getValueAt(i, 0)) {								
								recommendations.add((ArrayList) recomm.recommend((Long) tableModel.getValueAt(i, 1),
										(Integer) tableModel.getValueAt(i, 2)));								
							} 
							i++;
						}
						
						if (recommendations != null && !recommendations.isEmpty()) {

							Iterator<ArrayList<RecommendedItem>> it = recommendations.iterator();

							while (it.hasNext()) {
								Iterator<RecommendedItem> it2 = it.next().iterator();
								while(it2.hasNext()){
									RecommendedItem item = it2.next();
									System.out.println(item);
									MainGUI.writeResult(item.toString(), Constants.Log.RESULT);
								}								
							}
						}

					} else {
						// TODO sobra??? puede fallar la creacion del recomm si
						// tiene dataModel?
						MainGUI.writeResult("error building the recommender", Constants.Log.ERROR);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					MainGUI.writeResult(e1.toString(), Constants.Log.ERROR);
					e1.printStackTrace();
				}
			}
		});
		btnRun.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRun.setBounds(371, 364, 89, 23);
		add(btnRun);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = 0;				
				while (i < tableModel.getRowCount()) {
					if ((boolean) tableModel.getValueAt(i, 0)) {
						tableModel.removeRow(i);						
					} else {
						i++;
					}
				}
			}
		});
		btnDelete.setBounds(272, 364, 89, 23);
		add(btnDelete);

	}
}
