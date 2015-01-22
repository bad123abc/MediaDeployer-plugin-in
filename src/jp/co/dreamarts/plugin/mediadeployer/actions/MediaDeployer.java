package jp.co.dreamarts.plugin.mediadeployer.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class MediaDeployer implements IObjectActionDelegate {
	@Override 
	public void run(IAction actionConfiguration) {
		// 获取当前工作空间绝对路径
		String workspacePath = Platform.getInstanceLocation().getURL()
				.getPath();
		// 将绝对路径按照路径分隔符进行分隔，只取出工作空间文件夹名称
		String[] stringTemps = workspacePath.split("\\\\");
		String[] stringTemp2s = stringTemps[stringTemps.length - 1].split("/");
		String filePath = "";
		// 拼接path.json、grunt.bat文件的存放路径，为工作空间文件夹路径
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
		// 取出工作空间的相对路径workspacePath2
		String workspacePath2 = stringTemp2s[stringTemp2s.length - 1];
		// 读取path.json文件
		File file = new File(filePath + File.separator + "path.json");
		System.out.println(filePath);
		File fileBat=new File(filePath+File.separator+"start.bat");
		// 将工作空间路径写入path.json
		try {
			if (!file.exists()) {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file, true);
				StringBuffer sb = new StringBuffer();
				// windows版本
				sb.append("{\r\n \"watchPath\":\"" + workspacePath2 + "\",");
				sb.append("\r\n\"destPath\":\""+".."+File.separator.toString() + File.separator+ workspacePath2
						+ File.separator.toString() + File.separator
						+ "HIBIKI_v1_0" + File.separator + File.separator
						+ "webapp" + File.separator + File.separator + "\"");
				sb.append("\r\n}");
				out.write(sb.toString().getBytes("utf-8"));
				out.flush();
				out.close();
				fileBat.createNewFile();
				FileOutputStream out2 = new FileOutputStream(fileBat, true);
				StringBuffer sb2 = new StringBuffer();
				// windows版本
				//location为工作空间盘符
				String location = filePath.substring(0, filePath.indexOf(":") + 2);
				sb2.append("cd "+location+"\r\n"+"cd "+filePath+"\r\n"+"grunt");
				
				sb2.append("\r\n");
				out2.write(sb2.toString().getBytes("utf-8"));
				out2.flush();
				out2.close();
			} else {
				file.delete();
				file = new File(filePath + File.separator + "path.json");
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file, true);
				StringBuffer sb = new StringBuffer();
				sb.append("{\r\n \"watchPath\":\"" + workspacePath2 + "\",");
				sb.append("\r\n\"destPath\":\"" +".."+File.separator.toString() + File.separator+ workspacePath2
						+ File.separator.toString() + File.separator
						+ "HIBIKI_v1_0" + File.separator + File.separator
						+ "webapp" + File.separator + File.separator + "\"");
				sb.append("\r\n}");
				out.write(sb.toString().getBytes("utf-8"));
				out.flush();
				out.close();
				fileBat.createNewFile();
				FileOutputStream out2 = new FileOutputStream(fileBat, true);
				StringBuffer sb2 = new StringBuffer();
				// windows版本
				//location为工作空间盘符
				String location = filePath.substring(0, filePath.indexOf(":") + 2);
				sb2.append("cd "+location+"\r\n"+"cd "+filePath+"\r\n"+"grunt");
				
				sb2.append("\r\n");
				out2.write(sb2.toString().getBytes("utf-8"));
				out2.flush();
				out2.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 检测是否已安装过插件 下载安装grunt-sdbwatcher 将package.json、Gruntfile.jscopy至项目路径
		 * 执行命令install 将package.json、Gruntfile.jscopy至项目路径
		 ***/

		// 将package.json、Gruntfile.jscopy至项目路径
		int bytesum = 0;
		int byteread = 0;
		String oldPath = filePath + "node_modules" + File.separator.toString()
				+ "grunt-sdbwatcher" + File.separator.toString();
		File oldfile = new File(oldPath);
		String newPath = filePath + File.separator.toString();
		File pluginFlag = new File(oldPath + "package.json");
		// 检测是否已安装过插件 下载安装grunt-sdbwatcher
		if (pluginFlag.exists()) {
			copyFile(oldPath + "package.json", newPath + "package.json");
			copyFile(oldPath + "Gruntfile.js", newPath + "Gruntfile.js");
		} else {
			// windows版本
			String location = filePath.substring(0, filePath.indexOf(":") + 1);
			String command = "cmd /c  " + location + " && cd " + filePath
					+ " && npm install grunt-sdbwatcher";
			try {
				Process process = Runtime.getRuntime().exec(command);
				process.waitFor();
				copyFile(oldPath + "package.json", newPath + "package.json");
				copyFile(oldPath + "Gruntfile.js", newPath + "Gruntfile.js");
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("sdbwatcher安装失败");
				e.printStackTrace();
			}
			// 执行npm install
			try {
				// windows版本
				String command2 = "cmd /c  cd " + filePath + " && npm install ";

				Process process = Runtime.getRuntime().exec(command2);
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("配置成功");
	}

	/**
	 * copy文件函数
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fos = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum+"copy");
					fos.write(buffer, 0, byteread);
				}
				inStream.close();
				fos.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	@Override public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub

	}

}
