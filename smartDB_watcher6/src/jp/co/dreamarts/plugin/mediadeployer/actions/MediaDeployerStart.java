package jp.co.dreamarts.plugin.mediadeployer.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.dreamarts.plugin.mediadeployer.util.ConsolePanel;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class MediaDeployerStart implements IWorkbenchWindowActionDelegate {
    public static int getPid() {
        return Pid;
    }

    public static void setPid(int pid) {
        Pid = pid;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static int Pid;

    // in Mac os ,start grunt
    class MacRunTime extends Thread {
        String filePath;

        public MacRunTime(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {
                if (startProcess == null) {
                    startProcess = Runtime.getRuntime().exec(
                            File.separator.toString() + filePath + "start.sh");
                    ProcessUtil.getPid((getStartProcess()));
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            try {
                                Runtime.getRuntime().exec("kill " + Pid + "\n");
                            } catch (IOException e) {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
                                ;
                            }
                        }
                    }));
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer configure has been completed");
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer directly start...");
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            startProcess.getInputStream()));
                    String log = "";
                    boolean logFlag = false;
                    while ((log = in.readLine()) != null) {
                        if (log.indexOf("Waiting") != -1) {
                            logFlag = true;
                            continue;
                        }
                        if (logFlag) {
                            if (log.indexOf("[39m") != -1) {
                                log = log.substring(log.indexOf("[39m") + "[39m".length(), log.length());
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            } else {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            }
                        }
                    }
                }
                if (startProcess != null) {
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[WARN] "
                            + "Media Deployer has been started");
                }
            } catch (IOException e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }
        }
    }

    //create new thread to install grunt-sdbwatcher in mac os
    class MacSdbwatcherInstaller extends Thread {
        String filePath, oldPath, newPath;

        public MacSdbwatcherInstaller(String filePath, String oldPath, String newPath) {
            this.filePath = filePath;
            this.oldPath = oldPath;
            this.newPath = newPath;
        }

        @Override
        public void run() {
            macInstall(filePath, oldPath, newPath);

            try {
                if (startProcess == null) {
                    startProcess = Runtime.getRuntime().exec(
                            File.separator.toString() + filePath + "start.sh");
                    ProcessUtil.getPid((getStartProcess()));
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            try {
                                Runtime.getRuntime().exec("kill " + Pid + "\n");
                            } catch (IOException e) {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
                                ;
                            }
                        }
                    }));
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer configuration is complete");
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + "Media Deployer start...");
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            startProcess.getInputStream()));
                    String log = "";
                    boolean logFlag = false;
                    while ((log = in.readLine()) != null) {
                        if (log.indexOf("Waiting") != -1) {
                            logFlag = true;
                            continue;
                        }
                        if (logFlag) {
                            if (log.indexOf("[39m") != -1) {
                                log = log.substring(log.indexOf("[39m") + "[39m".length(), log.length());
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            } else {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            }
                        }
                    }
                }
                if (startProcess != null) {
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[WARN] "
                            + "Media Deployer has been started");
                }
            } catch (IOException e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }

        }
    }

    // in windows os ,start grunt
    class WindowsRunTime extends Thread {
        String filePath;

        public WindowsRunTime(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {
                if (startProcess == null) {
                    startProcess = Runtime.getRuntime().exec(filePath + "start.bat");
                    ProcessUtil.getPid((getStartProcess()));
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            try {
                                Runtime.getRuntime().exec("cmd /c " + "taskkill /T /F /PID " + Pid + "\n");
                            } catch (IOException e) {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
                                ;
                            }
                        }
                    }));

                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer configure has been completed");
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer directly start...");
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            startProcess.getInputStream()));
                    String log = "";
                    boolean logFlag = false;
                    while ((log = in.readLine()) != null) {
                        if (log.indexOf("Waiting") != -1) {
                            logFlag = true;
                            continue;
                        }
                        if (logFlag) {
                            if (log.indexOf("[39m") != -1) {
                                log = log.substring(log.indexOf("[39m") + "[39m".length(), log.length());
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            } else {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            }
                        }
                    }
                }
                if (startProcess != null) {
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[WARN] "
                            + "Media Deployer has been started");
                }
            } catch (IOException e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }
        }
    }

    //create new thread to install grunt-sdbwatcher in windows os
    class WinSdbwatcherInstaller extends Thread {
        String filePath, oldPath, newPath;

        public WinSdbwatcherInstaller(String filePath, String oldPath, String newPath) {
            this.filePath = filePath;
            this.oldPath = oldPath;
            this.newPath = newPath;
        }

        @Override
        public void run() {
            winInstall(filePath, oldPath, newPath);
            try {
                if (startProcess == null) {
                    startProcess = Runtime.getRuntime().exec(filePath + "start.bat");
                    ProcessUtil.getPid((getStartProcess()));
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            try {
                                Runtime.getRuntime().exec("cmd /c " + "taskkill /T /F /PID " + Pid + "\n");
                            } catch (IOException e) {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
                                ;
                            }
                        }
                    }));
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer configuration is complete");
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + "Media Deployer start...");
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            startProcess.getInputStream()));
                    String log = "";
                    boolean logFlag = false;
                    while ((log = in.readLine()) != null) {
                        if (log.indexOf("Waiting") != -1) {
                            logFlag = true;
                            continue;
                        }
                        if (logFlag) {
                            if (log.indexOf("[39m") != -1) {
                                log = log.substring(log.indexOf("[39m") + "[39m".length(), log.length());
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            } else {
                                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] " + log);
                            }
                        }
                    }
                }
                if (startProcess != null) {
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[WARN] "
                            + "Media Deployer has been started");
                }

            } catch (IOException e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }

        }
    }

    static Process startProcess = null;

    public static void setStartProcess(Process startProcess) {
        MediaDeployerStart.startProcess = startProcess;
    }

    public static Process getStartProcess() {
        if (startProcess == null) {
            return null;
        } else {
            return startProcess;
        }
    }

    /**
     * the function for copying file
     * 
     * @param oldPath
     * @param newPath
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fos.write(buffer, 0, byteread);
                }
                inStream.close();
                fos.close();
            }
        } catch (Exception e) {
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + "Copy single file operation error");
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
        }
    }

    /**
     * 
     * @param filePath
     * @param workspacePath2
     * @param file
     * @param fileBat
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     *  in Windows operating system create path.json、start.bat file
     */
    private void creatJsonBatFile(String filePath, String workspacePath2, File file, File fileBat)
            throws IOException, FileNotFoundException, UnsupportedEncodingException {
        // create path.json file
        file.createNewFile();
        String command = "";
        command = "{\r\n \"watchPath\":\"" + workspacePath2 + "\"," + "\r\n\"destPath\":\"" + ".."
                + File.separator.toString() + File.separator + workspacePath2 + File.separator.toString()
                + File.separator + "HIBIKI_v1_0" + File.separator + File.separator + "webapp"
                + File.separator + File.separator + "\"" + "\r\n}";
        createFile(file, command);
        // location is partition of the workspace
        fileBat.createNewFile();
        String location = filePath.substring(0, filePath.indexOf(":") + 1);
        command = location + "\r\n" + "cd " + filePath + "\r\n" + "grunt" + "\r\n";
        createFile(fileBat, command);
    }

    /**
     * 
     * @param filePathJs
     * @param workspacePath
     * @param filePathJs
     * @param fileSH
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     *             in Mac OS create path.json、start.sh file
     */
    private void creatJsonShFile(String filePath, String workspacePath, File filePathJs, File fileSH,
            File sdbWatcherConfig, File npminstall) throws IOException, FileNotFoundException,
            UnsupportedEncodingException {
        // create path.json file
        String command = "";
        command = "{\n \"watchPath\":\"" + ".." + File.separator.toString() + workspacePath + "\","
                + "\n\"destPath\":\"" + ".." + File.separator.toString() + workspacePath
                + File.separator.toString() + "HIBIKI_v1_0" + File.separator + "webapp" + File.separator
                + "\"" + "\n}";
        createFile(filePathJs, command);
        // create start.sh file
        command = "cd " + filePath + "\n" + "grunt" + "\n";
        createFile(fileSH, command);
        //create sdbwatcherConfig.sh file
        command = "cd " + filePath + "\n" + "npm install grunt-sdbwatcher" + "\n";
        createFile(sdbWatcherConfig, command);
        //create npminstall.sh file
        command = "cd " + filePath + "\n" + "npm install" + "\n";
        createFile(npminstall, command);
        filePathJs.setReadable(true);
        npminstall.setExecutable(true);
        sdbWatcherConfig.setExecutable(true);
        fileSH.setExecutable(true);
    }

    private void createFile(File file, String command) throws IOException, FileNotFoundException,
            UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        FileOutputStream fos;
        file.createNewFile();
        fos = new FileOutputStream(file);
        sb.append(command);
        fos.write(sb.toString().getBytes("utf-8"));
        fos.flush();
        fos.close();
        sb.delete(0, sb.length());
    }

    private void macInstall(String filePath, String oldPath, String newPath) {
        try {
            Process process = Runtime.getRuntime().exec(filePath + "sdbwatcherConfig.sh");
            process.waitFor();
            copyFile(oldPath + "package.json", newPath + "package.json");
            copyFile(oldPath + "Gruntfile.js", newPath + "Gruntfile.js");
        } catch (IOException | InterruptedException e) {
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] "
                    + "grunt-sdbwatcher Installation failed ");
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
        }
        // execute "npm install"in shell
        try {
            Process process = Runtime.getRuntime().exec(filePath + "npminstall.sh");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
        }
    }

    @Override
    public void run(IAction actionConfiguration) {
        // Gets absolute path of the current workspace
        String workspacePath = Platform.getInstanceLocation().getURL().getPath();
        /*
         * if Operating system is windows,then Perform the following
         */
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WIN") != -1) {

            /*
             * The absolute path according to the path delimiter separated, only
             * the workspace folder name remained
             */
            String[] stringTemps = workspacePath.split("\\\\");
            String[] stringTemp2s = stringTemps[stringTemps.length - 1].split("/");
            String filePath = "";
            /*
             * splicing path for path.json, grunt.bat file, in fact it is the
             * workspace folder path
             */
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
            String workspaceName = stringTemp2s[stringTemp2s.length - 1];
            // read path.json
            File file = new File(filePath + "path.json");
            File fileBat = new File(filePath + "start.bat");
            // write path.json
            try {
                if (!file.exists()) {
                    creatJsonBatFile(filePath, workspaceName, file, fileBat);
                } else {
                    file.delete();
                    file = new File(filePath + File.separator + "path.json");
                    creatJsonBatFile(filePath, workspaceName, file, fileBat);
                }
            } catch (IOException e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }
            /*
             * Detecting grunt-sdbwatcher whether or not installed ,if not
             * ,installe grunt-sdbwatcher copy package.json、Gruntfile.js to
             * workspace execute package.json
             */

            // copy package.json、Gruntfile.js to workspace
            String oldPath = filePath + "node_modules" + File.separator.toString() + "grunt-sdbwatcher"
                    + File.separator.toString();
            String newPath = filePath + File.separator.toString();
            File pluginFlag = new File(oldPath + "tasks" + File.separator + "sdbwatcher.js");
            //Detecting grunt-sdbwatcher whether or not installed ,if not ,install grunt-sdbwatcher
            if (pluginFlag.exists()) {
                copyFile(oldPath + "package.json", newPath + "package.json");
                copyFile(oldPath + "Gruntfile.js", newPath + "Gruntfile.js");
                new WindowsRunTime(filePath).start();
            } else {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                        + "Media Deployer is configuring , this will take serveal seconds,please wait...");
                new WinSdbwatcherInstaller(filePath, oldPath, newPath).start();
            }
        }
        /*
         * if Operating system is Mac OS X,then Perform the following
         */
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("MAC") != -1) {
            /*
             * The absolute path according to the path delimiter separated, only
             * the workspace folder name remained
             */
            String[] stringTemps = workspacePath.split("\\\\");
            String[] stringTemp2s = stringTemps[stringTemps.length - 1].split("/");
            String filePath = File.separator.toString();
            String workspaceName = "";
            // splicing path for path.json, grunt.bat file, in fact it is the
            // workspace folder path
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
            // get workspace folder's name
            workspaceName = stringTemp2s[stringTemp2s.length - 1];
            // read path.json
            File file = new File(filePath + "path.json");
            File fileSh = new File(filePath + "start.sh");
            File sdbwatcherConfig = new File(filePath + "sdbwatcherConfig.sh");
            File npminstall = new File(filePath + "npminstall.sh");
            // write path.json
            try {
                if (!file.exists()) {
                    creatJsonShFile(filePath, workspaceName, file, fileSh, sdbwatcherConfig, npminstall);
                } else {
                    file.delete();
                    file = new File(filePath + File.separator + "path.json");
                    creatJsonShFile(filePath, workspaceName, file, fileSh, sdbwatcherConfig, npminstall);
                }
            } catch (IOException e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }
            /*
             * Detecting grunt-sdbwatcher whether or not installed ,if not
             * ,installe grunt-sdbwatcher copy package.json、Gruntfile.js to
             * workspace execute package.json
             */

            // copy package.json、Gruntfile.js to workspace
            String oldPath = filePath + "node_modules" + File.separator.toString() + "grunt-sdbwatcher"
                    + File.separator.toString();
            String newPath = filePath + File.separator.toString();
            File pluginFlag = new File(oldPath + "tasks" + File.separator + "sdbwatcher.js");
            // Detecting grunt-sdbwatcher whether or not installed ,if
            // not,installe grunt-sdbwatcher copy package.json、Gruntfile.js to
            if (pluginFlag.exists()) {
                copyFile(oldPath + "package.json", newPath + "package.json");
                copyFile(oldPath + "Gruntfile.js", newPath + "Gruntfile.js");
                new MacRunTime(filePath).start();
            } else {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                        + "Media Deployer is configuring , this will take serveal seconds ,please wait...");
                new MacSdbwatcherInstaller(filePath, oldPath, newPath).start();
            }
        }
    }

    private void winInstall(String filePath, String oldPath, String newPath) {
        // for windows operating system
        String location = filePath.substring(0, filePath.indexOf(":") + 1);
        String command = "cmd /c  " + location + " && cd " + filePath + " && npm install grunt-sdbwatcher";
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            copyFile(oldPath + "package.json", newPath + "package.json");
            copyFile(oldPath + "Gruntfile.js", newPath + "Gruntfile.js");
        } catch (IOException | InterruptedException e) {
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] "
                    + "grunt-sdbwatcher Installation failed");
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
        }
        // execute "npm install" in cmd
        try {
            String command2 = "cmd /c " + location + " &&cd " + filePath + " && npm install ";
            Process process = Runtime.getRuntime().exec(command2);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(IWorkbenchWindow arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {
        // TODO Auto-generated method stub

    }
}

class ProcessUtil {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static interface Kernel32 extends Library {
        public static Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

        public int GetProcessId(Long hProcess);
    }

    public static int getPid(Process process) {
        Field filed;
        if (com.sun.jna.Platform.isWindows()) {
            try {
                filed = process.getClass().getDeclaredField("handle");
                filed.setAccessible(true);
                int pid = Kernel32.INSTANCE.GetProcessId((Long) filed.get(process));
                MediaDeployerStart.setPid(pid);
                return pid;
            } catch (Exception e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }
        } else if (com.sun.jna.Platform.isMac()) {
            try {
                filed = process.getClass().getDeclaredField("pid");
                filed.setAccessible(true);
                int pid = (Integer) filed.get(process);
                int gruntPid = pid + 1;
                MediaDeployerStart.setPid(gruntPid);
                return pid;
            } catch (Exception e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }
        }
        return 0;
    }
}
