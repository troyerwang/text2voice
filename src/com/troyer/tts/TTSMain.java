package com.troyer.tts;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;

/**
 * 利用google_tts接口,实现将文本转换为语音的程序
 * 
 * @author Troy
 * @Email troy_1988@126.com
 */
public class TTSMain extends JFrame {

	private static final long serialVersionUID = 1914286717004357562L;

	// 获取屏幕宽度和高度
	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;

	static {
		SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth();
		SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize()
				.getHeight();
	}

	// 定义可以转换的语言种类，其实google_tts提供了几乎世界上的所有语言，这里我们只选了其中的几个，如果要增加，只需要在这里添加就可以了
	private String[][] destl = { { "zh-CN", "中文" }, { "en", "英文" },
			{ "de", "德语" }, { "ja", "日语" }, { "fr", "法语" } };

	// 定义界面元素
	private JPanel panel = new JPanel();
	private JMenuBar menuBar;
	private JMenu menu1;
	private JMenu menu2;
	private JMenuItem menuitem1;
	private JMenuItem menuitem2;
	private JMenuItem menuitem3;
	private JButton[] desbtns = new JButton[destl.length];
	private JTextArea sourceText;
	private JButton start;
	private JLabel label;

	// 定义当前转换语种
	public String tl = "";

	public TTSMain() {

		setTitle("google_tts Client v1.0");
		setIconImage(TTSUtils.getImage("/resources/images/tts_48.png"));
		setBounds((SCREEN_WIDTH - 500) / 2, (SCREEN_HEIGHT - 320) / 2, 500, 320);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setContentPane(panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		setResizable(false);
		initFrame();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initFrame() {

		// 定义菜单栏
		menuBar = new JMenuBar();
		menu1 = new JMenu("文件");
		menuitem1 = new JMenuItem("打开文件");
		menuitem2 = new JMenuItem("退出");
		menuitem3 = new JMenuItem("关于我");
		menu2 = new JMenu("关于");

		menuBar.add(menu1);
		menuBar.add(menu2);
		menu1.add(menuitem1);
		menu1.addSeparator();
		menu1.add(menuitem2);
		menu2.add(menuitem3);

		setJMenuBar(menuBar);

		// 给打开文件添加事件
		menuitem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						return null;
					}

					// 只能打开扩展名为.txt的文件
					@Override
					public boolean accept(File f) {
						return f.getName().endsWith("txt") || f.isDirectory();
					}
				});

				int i = fc.showOpenDialog(panel);
				if (i == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						List<String> words = FileUtils.readLines(file);
						String str = "";
						for (int j = 0; j < words.size(); j++) {
							if (j == words.size() - 1) {
								str += words.get(j);
							} else {
								str += words.get(j) + "\n";
							}
						}
						sourceText.setText(str);

					} catch (IOException e1) {
						System.out.println("读取文件失败！");
					}
				}
			}
		});

		// 给退出添加事件
		menuitem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		menuitem3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(TTSMain.this,
						"Author： Troy \nEmail： Troy_1988@126.com");
			}
		});
		
		// 定义可以转换的语种按钮，并添加事件响应，点击按钮设定响应的语种为当前要转换的语种
		for (int i = 0; i < destl.length; i++) {
			desbtns[i] = new JButton();
			desbtns[i].setText(destl[i][0]);
			desbtns[i].setToolTipText(destl[i][1]);
			desbtns[i].setBackground(new Color(238, 238, 238));
			desbtns[i].setForeground(Color.BLACK);
			panel.add(desbtns[i]);

			desbtns[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < destl.length; i++) {
						if (desbtns[i] == e.getSource()) {
							desbtns[i].setBackground(Color.BLUE);
							desbtns[i].setForeground(Color.white);
						} else {
							desbtns[i].setBackground(new Color(238, 238, 238));
							desbtns[i].setForeground(Color.BLACK);
						}
					}
					tl = e.getActionCommand();
				}
			});
		}

		// 添加一个文本域
		sourceText = new JTextArea();
		sourceText.setBackground(Color.white);
		sourceText.setRows(10);
		sourceText.setColumns(40);
		// 为文本域添加滚动条
		JScrollPane scroll = new JScrollPane(sourceText);
		// 设定超出文本域才显示滚动条
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scroll);

		// 添加开始转换按钮
		start = new JButton();
		start.setSize(10, 20);
		start.setText("开始转换");
		start.setToolTipText("将文本转换为语音");
		start.setBackground(new Color(238, 238, 238));
		start.setForeground(Color.BLACK);
		panel.add(start);

		// 添加一些帮助信息
		label = new JLabel();
		label.setText("转换的文件保存在D:/tts_result中");
		label.setForeground(Color.BLUE);
		panel.add(label);

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!tl.equals("") && sourceText.getText().trim().length() != 0) {
					String str[] = sourceText.getText().split("\n|\t");
					for (String words : str) {
						if (words.trim().length() != 0) {
							TTSUtils.saveMp3File(words, "d:/tts_result", tl);
						}
					}
					JOptionPane.showMessageDialog(TTSMain.this,
							"转换成功！请到D:/tts_result中查看转换结果。");
				} else {
					JOptionPane.showMessageDialog(TTSMain.this,
							"请选择要转换的语音语种以及要转换的文本！");
				}
			}
		});

	}

	public static void main(String[] args) {

		new TTSMain();
	}

}
