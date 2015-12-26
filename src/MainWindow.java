import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;


public class MainWindow extends JFrame
{

	private JPanel contentPane;
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
					MainWindow frame = new MainWindow();
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
	public MainWindow()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new MigLayout("", "[86px][87px][][][][][][][][]", "[23px]"));
		
		tb_filename = new JTextField();
		tb_filename.setHorizontalAlignment(SwingConstants.LEFT);
		tb_filename.setEditable(false);
		buttonPanel.add(tb_filename, "cell 0 0 8 1,alignx center,aligny center");
		tb_filename.setColumns(40);
		
		JButton btn_chooseFile = new JButton("Choose File");
		btn_chooseFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser fc = new JFileChooser("C:\\Sample Images");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				//fc.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Pictures"));
				
				int validFile = fc.showOpenDialog(contentPane);
				if (validFile == JFileChooser.APPROVE_OPTION)
				{
					File file = fc.getSelectedFile();
					tb_filename.setText(file.getName());
				}
			}
		});
		buttonPanel.add(btn_chooseFile, "cell 9 0,alignx center,aligny center");
		
	}

}
