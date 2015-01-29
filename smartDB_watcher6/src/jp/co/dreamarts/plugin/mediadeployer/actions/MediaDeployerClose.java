package jp.co.dreamarts.plugin.mediadeployer.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.dreamarts.plugin.mediadeployer.util.ConsolePanel;

public class MediaDeployerClose implements IWorkbenchWindowActionDelegate {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(IAction arg0) {
        shutdownListener();
    }

    public void shutdownListener() {
        int processPid;
        if (MediaDeployerStart.getStartProcess() != null) {
            processPid = ProcessUtil.getPid((MediaDeployerStart.getStartProcess()));
            try {
                if (com.sun.jna.Platform.isWindows()) {
                    Runtime.getRuntime().exec("cmd /c " + "taskkill /T /F /PID " + processPid + "\n");
                    MediaDeployerStart.setStartProcess(null);
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer has been closed");
                } else if (com.sun.jna.Platform.isMac()) {
                    processPid += 1;
                    Runtime.getRuntime().exec("kill " + processPid + "\n");
                    MediaDeployerStart.setStartProcess(null);
                    ConsolePanel.getConsole(sdf.format(new Date()) + "[INFO] "
                            + "Media Deployer has been closed");
                }
            } catch (IOException e) {
                ConsolePanel.getConsole(sdf.format(new Date()) + "[ERROR] " + e.getMessage());
            }
        } else {
            ConsolePanel.getConsole(sdf.format(new Date()) + "[WARN] " + "Media Deployer is not running");
        }
    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {
    }

    @Override
    public void dispose() {

    }

    @Override
    public void init(IWorkbenchWindow arg0) {

    }
}
