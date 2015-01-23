package jp.co.dreamarts.plugin.mediadeployer.actions;

import java.awt.Desktop;
import java.io.*;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ServiceStarter implements IObjectActionDelegate {

	@Override public void run(IAction arg0) {
		// 获取当前工作空间绝对路径
		String workspacePath = Platform.getInstanceLocation().getURL()
				.getPath();
		// 将绝对路径按照路径分隔符进行分隔，只取出工作空间文件夹名称
		String[] stringTemps = workspacePath.split("\\\\");
		String[] stringTemp2s = stringTemps[stringTemps.length - 1].split("/");
		String filePath = "";
		// 拼接path.json文件的存放路径，为工作空间文件夹的上层目录
		for (int i = 0; i < stringTemps.length - 2; i++) {
			if (stringTemps[i] != null) {
				filePath += stringTemps[i];
				filePath = filePath + File.separator;
			}
		}
		for (int i = 0; i < stringTemp2s.length; i++) {
			if (stringTemp2s[i].length() != 0) {
				filePath += stringTemp2s[i];
				filePath = filePath + File.separator;
			}
		}
		// System.out.println(filePath);
//windows下处理操作
		
			try {
				// windows版本
				String location = filePath.substring(0,
						filePath.indexOf(":") + 1);
				// String command = "cmd /k  " + location + " && cd " + filePath
				// + " && grunt";
				// System.out.println(command);
				// Process process=Runtime.getRuntime().exec(command);
				if (System.getProperties().getProperty("os.name").toUpperCase()
						.indexOf("WIN") != -1) {
				Desktop.getDesktop().open(new File(filePath + "/start.bat"));
				}
				if (System.getProperties().getProperty("os.name").toUpperCase()
						.indexOf("MAC") != -1) {
					Desktop.getDesktop().open(new File(filePath + "/start.sh"));
				}
				System.out.println("插件已启动");
				// int exitValue=process.waitFor();
				// System.out.println(exitValue);
				// process.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Mac下处理操作

	

	@Override public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub

	}

}
