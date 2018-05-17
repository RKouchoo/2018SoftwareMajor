package com.rkouchoo.mm.actions;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.commons.io.FilenameUtils;

import com.rkocuhoo.mm.interfaces.ActionInterface;
import com.rkouchoo.mm.file.FileCommenter;
import com.rkouchoo.mm.util.FileHash;

public class CommentAction implements ActionInterface {

	private ActionManager manager;
	private String comment;
	private FileCommenter commenter;
	
	
	public CommentAction(ActionManager man) {
		this.manager = man;
		commenter = new FileCommenter(manager.getMessenger());
	}
	
	@Override
	public void run() {
		
		comment = "";
		
		// create the popup window, resuing some older assets
		manager.getMediaManager().commentPanel = new JPanel(new BorderLayout(3, 3));
		manager.getMediaManager().newTypeFile = new JRadioButton("File", true);
		manager.getMediaManager().name = new JTextField(15);

		manager.getMediaManager().commentPanel.add(new JLabel("Comment"), BorderLayout.WEST);
		manager.getMediaManager().commentPanel.add(manager.getMediaManager().name);
		
		File dir = manager.getMediaManager().currentFile;
		
		if (dir == null) {
			manager.getMessenger().showErrorMessage("Unhandled directory exception!" , "Commentig error");
			return;
		}
			
		int result = JOptionPane.showConfirmDialog(
				manager.getMediaManager().uiPanel, 
				manager.getMediaManager().commentPanel, 
				"Comment on: " + FilenameUtils.getName(manager.getMediaManager().currentFile.getPath()) + (manager.getMediaManager().currentFile.isDirectory() ? " (dir)" : ""), // check if its a directory
				JOptionPane.OK_CANCEL_OPTION
				);

		if (result == JOptionPane.OK_OPTION) {
			
			this.comment = manager.getMediaManager().name.getText(); // get the text from the box
			manager.getMediaManager().name.setText(""); // clear the text
						
			String key = "";
			
			try {
				key = FileHash.getFileHash(dir);
			} catch (Throwable e) {
				manager.getMessenger().showThrowable(e);
			}
			
			//System.out.println(dir);
			//for (File f : manager.getMediaManager().currentDirectoryFiles) {
				//System.out.println(f.toString());
			//}
			
			//System.out.println(key);
			//System.out.println(comment);
			
			commenter.doComment(dir, manager.getMediaManager().currentDirectoryFiles, key, comment);		
		}
	}
}
