package org.wpwl.typesetting.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.wpwl.typesetting.util.ClassUtil;

public class ClientFrame extends JFrame {
	private static final long serialVersionUID = -4950181632250663022L;
	private static ArrayList<String> picClazzList = new ArrayList<String>();
	private static ArrayList<String> pdfClazzList = new ArrayList<String>();
	private static Properties prop = new Properties();
	private static String picPkgName = "org.wpwl.typesetting.pic";
	private static String pdfPkgName = "org.wpwl.typesetting.pdf";

	static {
		initClass(picPkgName,picClazzList);
		initClass(pdfPkgName,pdfClazzList);
		loadProp();
	}

	private static void initClass(String picPkgName, ArrayList<String> clazzList) {
		List<Class<?>> classes = ClassUtil.getClasses(picPkgName);
		List<String> list = new ArrayList<String>();
		for (Class<?> clazz : classes) {
			String name = clazz.getName();
			int index = name.lastIndexOf(".");
			if (picPkgName.equals(name.substring(0, index))) {
				list.add(name.substring(index + 1));
			}
		}
		clazzList.addAll(list);
	}

	private static void loadProp() {
		InputStream is = ClientFrame.class.getClassLoader().getResourceAsStream("arguments.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JComboBox<String> comboBox1;
	private JComboBox<String> comboBox2;
	private JTextField txtUsage;
	private JTextField txtArgsValue;
	private JTextArea txtResult;
	private Clipboard clipboard;

	public ClientFrame() throws HeadlessException {
		super();
		clipboard=getToolkit().getSystemClipboard();
		initComponent();
	}

	private void initComponent() {
		setTitle("SerialTypesetting");
		Container contailer = this.getContentPane();
//		contailer.setLayout(null);
		JPanel outer = new JPanel();
//		outer.setBounds(10, 10, 565, 120);
		outer.setBorder(BorderFactory.createTitledBorder("Configure Parameters:"));

		// 实例化一个布局类的对象
		FlowLayout flow = new FlowLayout();
		// 设置窗体的布局方式为流式布局
		outer.setLayout(flow);

		// 实例化一个JLabel标签类的对象
		txtUsage = new JTextField ("");
		txtUsage.setPreferredSize(new Dimension(510, 40));
		txtUsage.setFont(new java.awt.Font("微软雅黑", 0, 16));
		txtUsage.setEditable(false);
		txtUsage.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				txtUsage.selectAll();
				clipboard.setContents(new StringSelection(txtUsage.getText()), null);
			}
		});
		// 将jlaName对象添加到容器JFrame对象上
		outer.add(txtUsage);

		// 实例化一个JLabel对象
		JLabel lblArgs = new JLabel("Arguments:");
		// 将jlaName2对象添加到容器JFrame对象上
		outer.add(lblArgs);

		// 实例化一个JTextField对象
		txtArgsValue = new JTextField("");
		txtArgsValue.setPreferredSize(new Dimension(445, 40));
		txtArgsValue.setFont(new java.awt.Font("微软雅黑", 0, 16));
		// 将jteName对象添加到容器JFrame对象上
		outer.add(txtArgsValue);

		initPicComponent(outer);
		initPdfComponent(outer);
		txtResult = new JTextArea("");
//		txtResult.setEditable(false);
		txtResult.setFont(new java.awt.Font("微软雅黑", 0, 16));
		txtResult.setPreferredSize(new Dimension(510, 140));
		outer.add(txtResult);
		
		contailer.add(outer);
	}

	private void initPdfComponent(JPanel outer) {
		JLabel lblP2PClass = new JLabel("Pic2Pdf Class:");
		lblP2PClass.setPreferredSize(new Dimension(120, 30));
		outer.add(lblP2PClass);
		comboBox2 = new JComboBox<String>();
		comboBox2.setPreferredSize(new Dimension(200, 30));
		for (int i = 0; i < pdfClazzList.size(); i++) {
			comboBox2.addItem(pdfClazzList.get(i));
		}
		outer.add(comboBox2);
		
		comboBox2.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// 如果选中了一个
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 这里写你的任务 ，比如取到现在的值
					String item = (String) comboBox2.getSelectedItem();
					txtUsage.setText(prop.getProperty(item));
					txtArgsValue.setText(prop.getProperty(item));
				}
			}
		});

		
		JButton btnRun2 = new JButton("   R u n   ");
		// jbuName.setBorder(BorderFactory.createRaisedBevelBorder());
		btnRun2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String args = txtArgsValue.getText();
				if("".equals(args)){
					txtResult.setText("参数不能为空");
					return;
				}
				int index = comboBox2.getSelectedIndex();
				String picClass = pdfClazzList.get(index);
//				String args = prop.getProperty(picClass);
				
				try {
					Class<?> clazz = Class.forName(pdfPkgName+"."+picClass);
					Method method = clazz.getMethod("main", String[].class);
					method.invoke(clazz.newInstance(), (Object)args.trim().split("\\s")); 
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e1) {
					txtResult.setText(e1.getMessage());
					e1.printStackTrace();
				} 
			}
		});
		// 将jButton对象添加到容器JFrame对象上
		outer.add(btnRun2);
		JButton btnReset2 = new JButton("   R e s e t   ");
		btnReset2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtArgsValue.setText("");
				txtResult.setText("");
				txtUsage.setText("");
			}
		});
		// 将jButton对象添加到容器JFrame对象上
		outer.add(btnReset2);
	}

	private void initPicComponent(JPanel outer) {
		JLabel lblM2PClass = new JLabel("Code2Pic Class:");
		lblM2PClass.setPreferredSize(new Dimension(120, 30));
		outer.add(lblM2PClass);
		comboBox1 = new JComboBox<String>();
		comboBox1.setPreferredSize(new Dimension(200, 30));
		for (int i = 0; i < picClazzList.size(); i++) {
			comboBox1.addItem(picClazzList.get(i));
		}
		outer.add(comboBox1);
		
		comboBox1.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// 如果选中了一个
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 这里写你的任务 ，比如取到现在的值
					String item = (String) comboBox1.getSelectedItem();
					txtUsage.setText(prop.getProperty(item));
					txtArgsValue.setText(prop.getProperty(item));
				}
			}
		});
		
		JButton btnRun = new JButton("   R u n   ");
		// jbuName.setBorder(BorderFactory.createRaisedBevelBorder());
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String args = txtArgsValue.getText();
				if("".equals(args)){
					txtResult.setText("参数不能为空");
					return;
				}
				int index = comboBox1.getSelectedIndex();
				String picClass = picClazzList.get(index);
//				String args = prop.getProperty(picClass);
				
				try {
					Class<?> clazz = Class.forName(picPkgName+"."+picClass);
					Method method = clazz.getMethod("main", String[].class);
					method.invoke(clazz.newInstance(), (Object)args.trim().split("\\s")); 
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e1) {
					txtResult.setText(e1.getMessage());
					e1.printStackTrace();
				} 
			}
		});
		// 将jButton对象添加到容器JFrame对象上
		outer.add(btnRun);
		JButton btnReset = new JButton("   R e s e t   ");
		btnReset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtArgsValue.setText("");
				txtResult.setText("");
				txtUsage.setText("");
			}
		});
		// 将jButton对象添加到容器JFrame对象上
		outer.add(btnReset);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientFrame window = new ClientFrame();
				URL iconUrl = window.getClass().getClassLoader().getResource("icon.jpg");
				window.setResizable(false);
				window.setAlwaysOnTop(true);
				Image icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
				icon.flush();
				window.setIconImage(icon);
				window.setSize(600, 400);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// The following code ensure the window shown on the center of screen.
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				window.setLocation((dim.width - window.getWidth()) / 2, (dim.height - window.getHeight()) / 2);
				window.setVisible(true);
			}
		});
	}
}
