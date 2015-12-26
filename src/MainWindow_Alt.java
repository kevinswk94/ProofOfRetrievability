import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainWindow_Alt extends JFrame
{

	private JPanel panel_contentPane;
	private JTextField tb_filename;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow_Alt frame = new MainWindow_Alt();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow_Alt()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		panel_contentPane = new JPanel();
		panel_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(panel_contentPane);

		JPanel panel_buttonPanel = new JPanel();
		panel_contentPane.add(panel_buttonPanel, BorderLayout.NORTH);
		panel_buttonPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("286px:grow"),
				ColumnSpec.decode("40px"), ColumnSpec.decode("93px"), }, new RowSpec[] { RowSpec.decode("23px"), }));

		tb_filename = new JTextField();
		tb_filename.setEditable(false);
		panel_buttonPanel.add(tb_filename, "1, 1, fill, fill");
		tb_filename.setColumns(1);

		JButton btn_chooseFile = new JButton("Choose File");
		btn_chooseFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser fc = new JFileChooser("C:\\Sample Images");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				//fc.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Pictures"));
				
				int validFile = fc.showOpenDialog(panel_contentPane);
				if (validFile == JFileChooser.APPROVE_OPTION)
				{
					File file = fc.getSelectedFile();
					tb_filename.setText(file.getName());
				}
			}
		});
		panel_buttonPanel.add(btn_chooseFile, "3, 1, fill, fill");
		
		JPanel panel_splitPanel = new JPanel();
		panel_contentPane.add(panel_splitPanel, BorderLayout.CENTER);
		
		JButton btnSplit = new JButton("Split");
		panel_splitPanel.add(btnSplit);
	}

}
