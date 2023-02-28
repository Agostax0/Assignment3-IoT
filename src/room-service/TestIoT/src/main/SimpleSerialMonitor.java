package main;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 */
public class SimpleSerialMonitor  {

	public static void main(String[] args) throws Exception {
		String comPortName = "COM9";
		SerialMonitor monitor = new SerialMonitor();
		
		monitor.start(comPortName);
		
		JFrame frame = new JFrame();
		
		frame.setSize(new Dimension(400,400));
		
		JPanel panel = new JPanel();
		
		JButton btn = new JButton("BRUH");
		
		btn.addActionListener(al ->{
			monitor.write("B,A{1}[111]".getBytes());
		});
		
		panel.add(btn);
		
		frame.add(panel);
		
		frame.setVisible(true);
		
	}
}