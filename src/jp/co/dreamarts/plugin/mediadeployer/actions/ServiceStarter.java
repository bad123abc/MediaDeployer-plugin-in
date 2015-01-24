package jp.co.dreamarts.plugin.mediadeployer.actions;

import java.awt.Desktop;
import java.io.*;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ServiceStarter implements IObjectActionDelegate {

    @Override
    public void run(IAction arg0) {
        // Gets absolute path of the current workspace
        String workspacePath = Platform.getInstanceLocation().getURL().getPath();
        /*
         * The absolute path according to the path delimiter separated, only the
         * workspace folder name remained
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

        /*
         * if Operating system is windows,then Perform the following
         */
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WIN") != -1) {
            new WindowsRunTime(filePath).start();
        }
        /*
         * if Operating system is Mac,then Perform the following
         */
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("MAC") != -1) {
            new MacRunTime(filePath).start();
        }
    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {

    }

    @Override
    public void setActivePart(IAction arg0, IWorkbenchPart arg1) {

    }

    // in windows os ,start grunt
    class WindowsRunTime extends Thread {
        String filePath;

        public WindowsRunTime(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {

            StringBuffer sbError = new StringBuffer();
            StringBuffer sbOut = new StringBuffer();

            Process process;
            try {
                process = Runtime.getRuntime().exec("cmd /k start /b /w " + filePath + "start.bat");
                System.out.println("service start");
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s = "";
                while ((s = in.readLine()) != null) {
                    System.out.println(s);
                }
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    // in Mac os ,start grunt
    class MacRunTime extends Thread {
        String filePath;

        public MacRunTime(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {

            StringBuffer sbError = new StringBuffer();
            StringBuffer sbOut = new StringBuffer();

            Process process;
            try {
                process = Runtime.getRuntime().exec(File.separator.toString() + filePath + "start.sh");

                System.out.println("service start");
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s = "";
                while ((s = in.readLine()) != null) {
                    System.out.println(s);
                }
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    static class GetOutPut extends Thread {
        InputStream is;

        StringBuffer buffer;

        GetOutPut(InputStream is, StringBuffer buffer) {
            this.is = is;
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\n");
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
